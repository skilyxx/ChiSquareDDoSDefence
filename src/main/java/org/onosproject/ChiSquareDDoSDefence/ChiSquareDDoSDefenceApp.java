/*
 * Copyright 2020-present Open Networking Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.onosproject.ChiSquareDDoSDefence;

import org.onlab.packet.Ethernet;
import org.onosproject.ChiSquareDDoSDefence.fsmDangerLevel.Action;
import org.onosproject.ChiSquareDDoSDefence.fsmDangerLevel.EntranceMachine;
import org.onosproject.ChiSquareDDoSDefence.fsmDangerLevel.EntranceMachineState;
import org.onosproject.core.CoreService;
import org.onosproject.event.AbstractListenerManager;
import org.onosproject.mastership.MastershipEvent;
import org.onosproject.mastership.MastershipListener;
import org.onosproject.net.Device;
import org.onosproject.net.HostId;
import org.onosproject.net.device.DeviceEvent;
import org.onosproject.net.device.DeviceListener;
import org.onosproject.net.device.DeviceService;
import org.onosproject.net.packet.InboundPacket;
import org.onosproject.net.packet.PacketContext;
import org.onosproject.net.packet.PacketProcessor;
import org.onosproject.net.packet.PacketService;
import org.osgi.service.component.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * An App For ChiSquareDDoSDefence
 */
@Component(immediate = true,
        service = {ChiSquareDDoSDefenceService.class})
public class ChiSquareDDoSDefenceApp
        extends AbstractListenerManager<MastershipEvent, MastershipListener>
        implements ChiSquareDDoSDefenceService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    ChiSquareDDoSDefenceApp.ReactivePacketProcessor processor =
            new ChiSquareDDoSDefenceApp.ReactivePacketProcessor();

    // 危险评估状态机, 初始化为低危状态
    EntranceMachine entranceMachine = new EntranceMachine(EntranceMachineState.LOW_RISK);

    // 加速度阈值
    double lowAcceleration = 500.0;        // TODO 需要跟cb联调

    // 低危状态的packet_in计数器
    private int lowCounter = 0;

    // App启动时间, 每个packet_in到达时间
    Long startTime, nowTime;

    // 中高危状态的packet_in计数器
    private int middleAndHighCounter = 0;

    // 中高危数据样本数量
    private static final int MAX_PACKET_NUM = 50;

    // IP地址出现频数统计表
    Hashtable<String, Integer> ipFrequency = new Hashtable<>(MAX_PACKET_NUM);

    // 卡方值阈值
    private static final double LOW_CHI_SQUARE_VALUE = 8.0;

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    protected CoreService coreService;

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    protected PacketService packetService;

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    protected DeviceService deviceService;

    private final DeviceListener deviceListener = new InternalDeviceListener();

    private static class InternalDeviceListener implements DeviceListener {

        @Override
        public void event(DeviceEvent event) {}

        @Override
        public boolean isRelevant(DeviceEvent event) {
            return event.subject().type() == Device.Type.CONTROLLER;
        }
    }

    @Activate
    protected void activate() {
        coreService.registerApplication("org.onosproject.constantine3");
        packetService.addProcessor(processor, PacketProcessor.ADVISOR_MAX + 2);
        deviceService.addListener(deviceListener);
        this.startTime = System.currentTimeMillis();
        log.info("Started");
    }

    @Deactivate
    protected void deactivate() {
        packetService.removeProcessor(processor);
        processor = null;
        log.info("Stopped");
    }

    private class ReactivePacketProcessor implements PacketProcessor {

        @Override
        public void process(PacketContext context) {
            InboundPacket pkt = context.inPacket();
            Ethernet ethPkt = pkt.parsed();
            HostId dstId = HostId.hostId(ethPkt.getDestinationMAC());
            attackDetect(dstId.toString());
        }
    }

    private void attackDetect(String dstIpAddress) {

        Long timeDiff;
        double Acceleration;

        log.info("目前状态: " + entranceMachine.getState());

        // 局域网中ip总数
        int MAX_IP_NUM = deviceService.getAvailableDeviceCount();

        // 低危状态
        if (entranceMachine.getState().equals(EntranceMachineState.LOW_RISK)) {
            // 计数器加1
            lowCounter += 1;

            // 计算packet_in加速度
            nowTime = System.currentTimeMillis();
            timeDiff = nowTime - this.startTime;
            Acceleration = (2.0 * lowCounter) / (timeDiff * timeDiff);
            log.info("加速度是 " + Acceleration);

            // 如果实际加速度比阈值大, 低危状态转中危
            if (Acceleration > lowAcceleration) {
                entranceMachine.execute(Action.EXCEED_SPEED_LIMIT);
            } else {
                entranceMachine.execute(Action.STAY_IN_SPEED_LIMIT);
                return;
            }
        }

        // 中高危状态
        // 以MAX_PACKET_NUM为一组进行检测
        log.info("中高危包计数器: " + middleAndHighCounter);
        if (middleAndHighCounter < MAX_PACKET_NUM) {
            middleAndHighCounter += 1;
            if (!ipFrequency.containsKey(dstIpAddress)) {
                ipFrequency.put(dstIpAddress, 1);
            } else {
                Integer count = ipFrequency.get(dstIpAddress);
                count += 1;
                ipFrequency.remove(dstIpAddress);
                ipFrequency.put(dstIpAddress, count);
            }
        } else {
            middleAndHighCounter = 0;

            // W=1, mac唯一
            if (ipFrequency.size() == 1) {
                log.info("W=1");
                entranceMachine.execute(Action.CAUSE_ONLY_ONE_DST_IP);
            }

            // 计算卡方值
            double chiSquareValue = 0.0;
            Enumeration<String> e = ipFrequency.keys();
            while (e.hasMoreElements()) {
                String key = e.nextElement();
                Integer value = ipFrequency.get(key);
                double idealFrequency = MAX_PACKET_NUM / (double) MAX_IP_NUM;
                chiSquareValue += Math.pow((value - idealFrequency), 2) / idealFrequency;
            }
            log.info("卡方值: " + chiSquareValue);

            // 检查卡方值
            if (chiSquareValue == 0) {
                entranceMachine.execute(Action.CAUSE_CHI_SQUARE_VALUE_0);
            } else if (chiSquareValue > LOW_CHI_SQUARE_VALUE) {
                entranceMachine.execute(Action.CAUSE_CHI_SQUARE_VALUE_OVER_LOW);
            } else {
                entranceMachine.execute(Action.CAUSE_CHI_SQUARE_VALUE_NORMAL);
            }
        }
        // TODO DDoS防御
    }
}

package org.onosproject.ChiSquareDDoSDefence.fsmDangerLevel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class defenseEvent implements Event{

    private static final Logger LOG = LoggerFactory.getLogger(defenseEvent.class);

    @Override
    public void execute() {
        // 防御
        LOG.info("开始防御！");
    }
}

package org.onosproject.ChiSquareDDoSDefence.fsmDangerLevel;

public enum Action {
    STAY_IN_SPEED_LIMIT,                // packet_in包加速度小于加速度阈值
    EXCEED_SPEED_LIMIT,                 // packet_in包加速度大于等于加速度阈值
    CAUSE_ONLY_ONE_DST_MAC,              // W=1, 目标IP唯一
    CAUSE_CHI_SQUARE_VALUE_0,           // 卡方值为0且目标IP不唯一
    CAUSE_CHI_SQUARE_VALUE_OVER_LOW,    // 卡方值大于阈值且目标IP不唯一
    CAUSE_CHI_SQUARE_VALUE_NORMAL,      // 卡方值正常且目标IP不唯一
}

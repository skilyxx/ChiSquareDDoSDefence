package org.onosproject.ChiSquareDDoSDefence.fsmDangerLevel;

public enum EntranceMachineState {
    LOW_RISK,                   // 低危
    MIDDLE_RISK,                // 中危
    SINGLE_TARGET_HIGH_RISK,    // 单目标高危
    MULTIPLE_TARGET_HIGH_RISK1, // 多目标高危1
    MULTIPLE_TARGET_HIGH_RISK2, // 多目标高危2
    CHI_SQUARE_HIGH_RISK1,      // 卡方值检测高危1
    CHI_SQUARE_HIGH_RISK2,      // 卡方值检测高危2
    ATTACKER,                   // 确认遭受到DDoS攻击
}

package org.onosproject.ChiSquareDDoSDefence.fsmDangerLevel;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class EntranceMachine {

    public EntranceMachineState getState() {
        return state;
    }

    public void setState(EntranceMachineState state) {
        this.state = state;
    }

    List<EntranceMachineTransaction> entranceMachineTransactionList = Arrays.asList(

            // 低危转低危
            new EntranceMachineTransaction(
                    EntranceMachineState.LOW_RISK,
                    Action.STAY_IN_SPEED_LIMIT,
                    EntranceMachineState.LOW_RISK,
                    new nullEvent()),

            // 低危转中危
            new EntranceMachineTransaction(
                    EntranceMachineState.LOW_RISK,
                    Action.EXCEED_SPEED_LIMIT,
                    EntranceMachineState.MIDDLE_RISK,
                    new nullEvent()),

            // 中危转单目标高危
            new EntranceMachineTransaction(
                    EntranceMachineState.MIDDLE_RISK,
                    Action.CAUSE_ONLY_ONE_DST_MAC,
                    EntranceMachineState.SINGLE_TARGET_HIGH_RISK,
                    new nullEvent()),

            // 中危转多目标高危1
            new EntranceMachineTransaction(
                    EntranceMachineState.MIDDLE_RISK,
                    Action.CAUSE_CHI_SQUARE_VALUE_0,
                    EntranceMachineState.MULTIPLE_TARGET_HIGH_RISK1,
                    new nullEvent()),

            // 中危转卡方值高危1
            new EntranceMachineTransaction(
                    EntranceMachineState.MIDDLE_RISK,
                    Action.CAUSE_CHI_SQUARE_VALUE_OVER_LOW,
                    EntranceMachineState.CHI_SQUARE_HIGH_RISK1,
                    new nullEvent()),

            // 中危转中危
            new EntranceMachineTransaction(
                    EntranceMachineState.MIDDLE_RISK,
                    Action.CAUSE_CHI_SQUARE_VALUE_NORMAL,
                    EntranceMachineState.MIDDLE_RISK,
                    new nullEvent()),

            // 单目标高危转被攻击
            new EntranceMachineTransaction(
                    EntranceMachineState.SINGLE_TARGET_HIGH_RISK,
                    Action.CAUSE_ONLY_ONE_DST_MAC,
                    EntranceMachineState.ATTACKER,
                    new defenseEvent()),

            // 单目标高危转多目标高危1
            new EntranceMachineTransaction(
                    EntranceMachineState.SINGLE_TARGET_HIGH_RISK,
                    Action.CAUSE_CHI_SQUARE_VALUE_0,
                    EntranceMachineState.MULTIPLE_TARGET_HIGH_RISK1,
                    new nullEvent()),

            // 单目标高危转卡方值高危1
            new EntranceMachineTransaction(
                    EntranceMachineState.SINGLE_TARGET_HIGH_RISK,
                    Action.CAUSE_CHI_SQUARE_VALUE_OVER_LOW,
                    EntranceMachineState.CHI_SQUARE_HIGH_RISK1,
                    new nullEvent()),

            // 单目标高危转中危
            new EntranceMachineTransaction(
                    EntranceMachineState.SINGLE_TARGET_HIGH_RISK,
                    Action.CAUSE_CHI_SQUARE_VALUE_NORMAL,
                    EntranceMachineState.MIDDLE_RISK,
                    new nullEvent()),

            // 多目标高危1转单目标高危
            new EntranceMachineTransaction(
                    EntranceMachineState.MULTIPLE_TARGET_HIGH_RISK1,
                    Action.CAUSE_ONLY_ONE_DST_MAC,
                    EntranceMachineState.SINGLE_TARGET_HIGH_RISK,
                    new nullEvent()),

            // 多目标高危1转多目标高危2
            new EntranceMachineTransaction(
                    EntranceMachineState.MULTIPLE_TARGET_HIGH_RISK1,
                    Action.CAUSE_CHI_SQUARE_VALUE_0,
                    EntranceMachineState.MULTIPLE_TARGET_HIGH_RISK2,
                    new nullEvent()),

            // 多目标高危1转卡方值高危1
            new EntranceMachineTransaction(
                    EntranceMachineState.MULTIPLE_TARGET_HIGH_RISK1,
                    Action.CAUSE_CHI_SQUARE_VALUE_OVER_LOW,
                    EntranceMachineState.CHI_SQUARE_HIGH_RISK1,
                    new nullEvent()),

            // 多目标高危1转中危
            new EntranceMachineTransaction(
                    EntranceMachineState.MULTIPLE_TARGET_HIGH_RISK1,
                    Action.CAUSE_CHI_SQUARE_VALUE_NORMAL,
                    EntranceMachineState.MIDDLE_RISK,
                    new nullEvent()),

            // 卡方值高危1转单目标高危
            new EntranceMachineTransaction(
                    EntranceMachineState.CHI_SQUARE_HIGH_RISK1,
                    Action.CAUSE_ONLY_ONE_DST_MAC,
                    EntranceMachineState.SINGLE_TARGET_HIGH_RISK,
                    new nullEvent()),

            // 卡方值高危1转多目标高危1
            new EntranceMachineTransaction(
                    EntranceMachineState.CHI_SQUARE_HIGH_RISK1,
                    Action.CAUSE_CHI_SQUARE_VALUE_0,
                    EntranceMachineState.MULTIPLE_TARGET_HIGH_RISK1,
                    new nullEvent()),

            // 卡方值高危1转卡方值高危2
            new EntranceMachineTransaction(
                    EntranceMachineState.CHI_SQUARE_HIGH_RISK1,
                    Action.CAUSE_CHI_SQUARE_VALUE_OVER_LOW,
                    EntranceMachineState.CHI_SQUARE_HIGH_RISK2,
                    new nullEvent()),

            // 卡方值高危1转中危
            new EntranceMachineTransaction(
                    EntranceMachineState.CHI_SQUARE_HIGH_RISK1,
                    Action.CAUSE_CHI_SQUARE_VALUE_NORMAL,
                    EntranceMachineState.MIDDLE_RISK,
                    new nullEvent()),

            // 多目标高危2转单目标高危
            new EntranceMachineTransaction(
                    EntranceMachineState.MULTIPLE_TARGET_HIGH_RISK2,
                    Action.CAUSE_ONLY_ONE_DST_MAC,
                    EntranceMachineState.SINGLE_TARGET_HIGH_RISK,
                    new nullEvent()),

            // 多目标高危2转被攻击
            new EntranceMachineTransaction(
                    EntranceMachineState.MULTIPLE_TARGET_HIGH_RISK2,
                    Action.CAUSE_CHI_SQUARE_VALUE_0,
                    EntranceMachineState.ATTACKER,
                    new defenseEvent()),

            // 多目标高危2转卡方值高危1
            new EntranceMachineTransaction(
                    EntranceMachineState.MULTIPLE_TARGET_HIGH_RISK2,
                    Action.CAUSE_CHI_SQUARE_VALUE_OVER_LOW,
                    EntranceMachineState.CHI_SQUARE_HIGH_RISK1,
                    new nullEvent()),

            // 多目标高危2转中危
            new EntranceMachineTransaction(
                    EntranceMachineState.MULTIPLE_TARGET_HIGH_RISK2,
                    Action.CAUSE_CHI_SQUARE_VALUE_NORMAL,
                    EntranceMachineState.MIDDLE_RISK,
                    new nullEvent()),

            // 卡方值高危2转单目标高危
            new EntranceMachineTransaction(
                    EntranceMachineState.CHI_SQUARE_HIGH_RISK2,
                    Action.CAUSE_ONLY_ONE_DST_MAC,
                    EntranceMachineState.SINGLE_TARGET_HIGH_RISK,
                    new nullEvent()),

            // 卡方值高危2转多目标高危1
            new EntranceMachineTransaction(
                    EntranceMachineState.CHI_SQUARE_HIGH_RISK2,
                    Action.CAUSE_CHI_SQUARE_VALUE_0,
                    EntranceMachineState.MULTIPLE_TARGET_HIGH_RISK1,
                    new nullEvent()),

            // 卡方值高危2转被攻击
            new EntranceMachineTransaction(
                    EntranceMachineState.CHI_SQUARE_HIGH_RISK2,
                    Action.CAUSE_CHI_SQUARE_VALUE_OVER_LOW,
                    EntranceMachineState.ATTACKER,
                    new defenseEvent()),

            // 卡方值高危2转中危
            new EntranceMachineTransaction(
                    EntranceMachineState.CHI_SQUARE_HIGH_RISK2,
                    Action.CAUSE_CHI_SQUARE_VALUE_NORMAL,
                    EntranceMachineState.MIDDLE_RISK,
                    new nullEvent())
    );

    private EntranceMachineState state;

    public EntranceMachine(EntranceMachineState state) {
        setState(state);
    }

    public void execute(Action action) {
        Optional<EntranceMachineTransaction> transactionOptional = entranceMachineTransactionList
                .stream()
                .filter(transaction ->
                        transaction.getAction().equals(action) && transaction.getCurrentState().equals(state))
                .findFirst();

        if (transactionOptional.isEmpty()) {
            throw new InvalidActionException();
        }

        EntranceMachineTransaction transaction = transactionOptional.get();
        setState(transaction.getNextState());
        transaction.getEvent().execute();
    }
}


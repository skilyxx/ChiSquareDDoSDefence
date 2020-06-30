package org.onosproject.ChiSquareDDoSDefence.fsmDangerLevel;

public class EntranceMachineTransaction {

    private EntranceMachineState currentState;

    private Action action;

    private EntranceMachineState nextState;

    private Event event;

    public EntranceMachineTransaction(
            EntranceMachineState currentState, Action action,
            EntranceMachineState nextState, Event event) {
        this.currentState = currentState;
        this.action = action;
        this.nextState = nextState;
        this.event = event;
    }

    public EntranceMachineState getCurrentState() {
        return currentState;
    }

    public Action getAction() {
        return action;
    }

    public EntranceMachineState getNextState() {
        return nextState;
    }

    public Event getEvent() {
        return event;
    }

}

package model;

public class State implements Comparable<State> {
    private Object state;
    private State  parentState;
    private Action leadingAction;
    private double f;
    private double g;

    public Object getState() {
        return state;
    }

    public void setState(Object state) {
        this.state = state;
    }

    public State(Object state) {
        this.state = state;
    }

    public double getF() {
        return f;
    }

    public void setF(double f) {
        this.f = f + g;
    }

    public double getG() {
        return g;
    }

    public void setG(double g) {
        this.g = g;
    }

    public State getParentState() {
        return parentState;
    }

    public void setParentState(State parentState) {
        this.parentState = parentState;
    }

    public Action getLeadingAction() {
        return leadingAction;
    }

    public void setLeadingAction(Action leadingAction) {
        this.leadingAction = leadingAction;
    }

    @Override
    public int compareTo(State o) {
        return (int) (f - o.f);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof State))
            return false;

        State other = (State) obj;
        return this.state.equals(other.state);
    }
}

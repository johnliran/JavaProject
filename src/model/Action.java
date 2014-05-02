package model;

public interface Action {
    State doAction(State state);
    String getName();
}

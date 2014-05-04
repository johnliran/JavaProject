package model.algorithms;

public interface Action {

    State doAction(State state);

    String getName();
}

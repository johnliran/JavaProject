package model.algorithms;

/**
 * Action
 */
public interface Action {

    public State doAction(State state);

    public String getName();

    public int getDx();

    public int getDy();
}

package model.algorithms;

import java.util.ArrayList;

/**
 * Searcher
 */
public interface Searcher {

    public ArrayList<Action> search(State start, State goal);

    public int getNumOfEvaluatedStates();
}

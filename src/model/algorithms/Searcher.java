package model.algorithms;

import java.util.ArrayList;

public interface Searcher {

    public ArrayList<Action> search(State start, State goal);

    public int getNumOfEvaluatedStates();
}

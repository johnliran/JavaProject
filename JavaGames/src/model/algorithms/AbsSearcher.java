package model.algorithms;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.PriorityQueue;

/**
 * Abstract Searcher
 */
public abstract class AbsSearcher implements Searcher {

    private int NumOfEvaluatedStates;
    private PriorityQueue<State> openList;
    private HashSet<State> closedList;

    public AbsSearcher() {
        this.NumOfEvaluatedStates = 0;
        this.openList = new PriorityQueue<State>();
        this.closedList = new HashSet<State>();
    }

    public State pollFromOpenList() {
        this.NumOfEvaluatedStates++;
        return this.openList.poll();
    }

    public boolean addToOpenList(State state) {
        return this.openList.add(state);
    }

    public boolean addToClosedList(State state) {
        return this.closedList.add(state);
    }

    public boolean openListIsEmpty() {
        return this.openList.isEmpty();
    }

    public boolean closedListIsEmpty() {
        return this.closedList.isEmpty();
    }

    public boolean openListContains(State state) {
        return this.openList.contains(state);
    }

    public boolean closedListContains(State state) {
        return this.closedList.contains(state);
    }

    /**
     * @return Number of evaluated states
     */
    @Override
    public int getNumOfEvaluatedStates() {
        return this.NumOfEvaluatedStates;
    }

    public abstract ArrayList<Action> search(State start, State goal);
}


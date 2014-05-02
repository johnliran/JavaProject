package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.PriorityQueue;

public class AStar {
    private Domain domain;

    public AStar(Domain domain) {
        this.domain = domain;
    }

    public ArrayList<Action> search(State start, State goal) {
        HashSet<State> closedList = new HashSet<State>();
        PriorityQueue<State> openList   = new PriorityQueue<State>();
        openList.add(start);
        start.setG(0);
        start.setF(domain.g(start, start) + domain.h(start, goal));
        while (!openList.isEmpty()) {
            State q = openList.poll();
            if (q.equals(goal)) {
                return reconstructPath(q);
            }
            closedList.add(q);
            for (Action action : domain.getActions(q)) {
                State qTag = action.doAction(q);
                double tentativeGscore = q.getG() + domain.g(q, qTag);
                if (closedList.contains(qTag) && tentativeGscore >= qTag.getG()) {
                    continue;
                }
                if (!openList.contains(qTag) || tentativeGscore < qTag.getG()) {
                    qTag.setParentState(q);
                    qTag.setLeadingAction(action);
                    qTag.setG(tentativeGscore);
                    qTag.setF(domain.h(qTag, goal));
                    if (!openList.contains(qTag)) {
                        openList.add(qTag);
                    }
                }
            }
        }
        return new ArrayList<Action>();
    }

    private ArrayList<Action> reconstructPath(State current) {
        ArrayList<Action> actions = new ArrayList<Action>();
        while (current.getParentState() != null) {
            actions.add(0, current.getLeadingAction());
            current = current.getParentState();
        }
        return actions;
    }
}

package model.algorithms;

import java.util.ArrayList;

/**
 * A* Algorithm
 */
public class AStar extends AbsSearcher {
    private Domain domain;
    private Distance g;
    private Distance h;

    public AStar(Domain domain, Distance g, Distance h) {
        this.domain = domain;
        this.g = g;
        this.h = g;
    }

    /**
     * @param start Start position
     * @param goal  Goal position
     * @return List of actions carried out in order to reach the goal
     */
    @Override
    public ArrayList<Action> search(State start, State goal) {
        addToOpenList(start);
        start.setG(0);
        start.setF(g.getDistance(start, start) + h.getDistance(start, goal));
        while (!openListIsEmpty()) {
            State q = pollFromOpenList();
            if (q.equals(goal)) {
                return reconstructPath(q);
            }
            addToClosedList(q);
            for (Action action : domain.getActions(q)) {
                State qTag = action.doAction(q);
                double tentativeGscore = q.getG() + g.getDistance(q, qTag);
                if (closedListContains(qTag) && tentativeGscore >= qTag.getG()) {
                    continue;
                }
                if (!openListContains(qTag) || tentativeGscore < qTag.getG()) {
                    qTag.setParentState(q);
                    qTag.setLeadingAction(action);
                    qTag.setG(tentativeGscore);
                    qTag.setF(h.getDistance(qTag, goal));
                    if (!openListContains(qTag)) {
                        addToOpenList(qTag);
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


    public Distance getG() {
        return g;
    }

    public void setG(Distance g) {
        this.g = g;
    }

    public Distance getH() {
        return h;
    }

    public void setH(Distance h) {
        this.h = h;
    }
}

package model;

import java.io.Serializable;

import model.algorithms.Action;
import model.algorithms.State;

import org.eclipse.swt.graphics.Point;

/**
 * Holds X and Y coordinates
 */
public class GameMazeAction implements Action,Serializable {
    private int dx;
    private int dy;

    public GameMazeAction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    @Override
    public int getDx() {
        return dx;
    }

    @Override
    public int getDy() {
        return dy;
    }

    /**
     * @param state Current state (current coordinates)
     * @return New state (new coordinates)
     */
    @Override
    public State doAction(State state) {
        Point point = (Point) state.getState();
        State newState = new State();
        newState.setState(new Point((point.x + getDx()), (point.y + getDy())));
        return newState;
    }

    @Override
    public String getName() {
        return "Action: (" + this.getDx() + "," + this.getDy() + ")";
    }

}


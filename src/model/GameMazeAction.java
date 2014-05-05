package model;

import model.algorithms.Action;
import model.algorithms.State;
import org.eclipse.swt.graphics.Point;

public class GameMazeAction implements Action {
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


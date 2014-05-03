package model;

import model.algorithms.Action;
import model.algorithms.Domain;
import model.algorithms.State;
import org.eclipse.swt.graphics.Point;

import java.util.ArrayList;

public class GameMazeDomain implements Domain {
    private final static int DIAGONAL_MOVEMENT_SCORE = 15;
    private final static int DIRECT_MOVEMENT_SCORE = 10;
    private GameMazeModel maze;

    public GameMazeDomain(GameMazeModel maze) {
        this.maze = maze;
    }

    @Override
    public ArrayList<Action> getActions(State state) {
        Point point = (Point) state.getState();
        ArrayList<Action> actions = new ArrayList<Action>();
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) {
                    continue;
                }
                int x = point.x + dx;
                int y = point.y + dy;
                if (maze.getPointValue(x, y) != -1) {
                    actions.add(new GameMazeAction(dx, dy));
                }
            }
        }
        return actions;
    }

    @Override
    public double g(State from, State to) {
        Point pFrom = (Point) from.getState();
        Point pTo = (Point) to.getState();
        if ((pTo.x - pFrom.x) == 0 || (pTo.y - pFrom.y) == 0) {
            return DIRECT_MOVEMENT_SCORE;
        }
        return DIAGONAL_MOVEMENT_SCORE;
    }

    @Override
    public double h(State state, State goal) {
        Point pFrom = (Point) state.getState();
        Point pTo = (Point) goal.getState();
        return Math.abs(pTo.x - pFrom.x) + Math.abs(pTo.y - pFrom.y);
    }
}


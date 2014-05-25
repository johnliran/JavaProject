package model;

import model.algorithms.Action;
import model.algorithms.Domain;
import model.algorithms.State;
import org.eclipse.swt.graphics.Point;

import java.util.ArrayList;

/**
 * Game Maze Domain
 */
public class GameMazeDomain implements Domain {
    private GameMazeModel maze;

    public GameMazeDomain(GameMazeModel maze) {
        this.maze = maze;
    }
    
   

    /**
     * @param state State
     * @return Array of possible actions from given state
     */
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
}


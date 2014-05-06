package model;

import controller.Constants;
import model.algorithms.Distance;
import model.algorithms.State;
import org.eclipse.swt.graphics.Point;

/**
 *
 */
public class GameMazeDistanceG implements Distance, Constants {

    /**
     * @param from
     * @param to
     * @return
     */
    @Override
    public double getDistance(State from, State to) {
        Point pFrom = (Point) from.getState();
        Point pTo = (Point) to.getState();
        if ((pTo.x - pFrom.x) == 0 || (pTo.y - pFrom.y) == 0) {
            return STRAIGHT_MOVEMENT_SCORE;
        }
        return DIAGONAL_MOVEMENT_SCORE;
    }
}

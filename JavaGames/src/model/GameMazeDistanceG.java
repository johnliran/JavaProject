package model;

import controller.Constants;
import model.algorithms.Distance;
import model.algorithms.State;
import org.eclipse.swt.graphics.Point;

/**
 * Distance G
 */
public class GameMazeDistanceG implements Distance {

    /**
     * @param from Start position
     * @param to   Enf position
     * @return Movement score
     */
    @Override
    public double getDistance(State from, State to) {
        Point pFrom = (Point) from.getState();
        Point pTo = (Point) to.getState();
        if ((pTo.x - pFrom.x) == 0 || (pTo.y - pFrom.y) == 0) {
            return Constants.STRAIGHT_MOVEMENT_SCORE;
        }
        return Constants.DIAGONAL_MOVEMENT_SCORE;
    }
}

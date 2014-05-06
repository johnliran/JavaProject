package model;

import model.algorithms.Distance;
import model.algorithms.State;
import org.eclipse.swt.graphics.Point;

/**
 *
 */
public class GameMazeDistanceH implements Distance {

    /**
     * @param from
     * @param to
     * @return
     */
    @Override
    public double getDistance(State from, State to) {
        Point pFrom = (Point) from.getState();
        Point pTo = (Point) to.getState();
        return Math.abs(pTo.x - pFrom.x) + Math.abs(pTo.y - pFrom.y);
    }
}

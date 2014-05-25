package model;

import model.algorithms.Distance;
import model.algorithms.State;
import org.eclipse.swt.graphics.Point;

/**
 * Heuristic Distance
 */
public class GameMazeDistanceH implements Distance {

    /**
     * @param from Start position
     * @param to   End position
     * @return Distance between Start to End (square root)
     */
    @Override
    public double getDistance(State from, State to) {
        Point pFrom = (Point) from.getState();
        Point pTo = (Point) to.getState();
        return Math.sqrt((((Math.abs(pTo.x - pFrom.x)) * (Math.abs(pTo.x - pFrom.x))) + ((Math.abs(pTo.y - pFrom.y)) * (Math.abs(pTo.y - pFrom.y))) * 10));
    }
}
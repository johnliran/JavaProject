package model.algorithms;

import java.util.ArrayList;

/**
 * Domain
 */
public interface Domain {

    ArrayList<Action> getActions(State state);
}

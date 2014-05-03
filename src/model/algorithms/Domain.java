package model.algorithms;

import java.util.ArrayList;

public interface Domain {

    double g(State from, State to);

    double h(State state, State goal);

    ArrayList<Action> getActions(State state);
}

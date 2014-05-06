package model.algorithms;

import java.util.ArrayList;

public interface Domain {

    ArrayList<Action> getActions(State state);
}

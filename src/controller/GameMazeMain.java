package controller;

import model.GameMazeDomain;
import model.GameMazeModel;
import model.algorithms.AStar;
import model.algorithms.Action;
import view.GameMazeView;

import java.util.ArrayList;

public class GameMazeMain {

    public static void main(String[] args) {
        GameMazeModel m = new GameMazeModel();

        AStar as = new AStar(new GameMazeDomain(m));
        ArrayList<Action> actions = as.search(m.getStartState(), m.getGoalState());
        System.out.println("NumberOfActions:" + actions.size());

        GameMazeView ui = new GameMazeView();
        Presenter p = new Presenter(m, ui);
        m.addObserver(p);
        ui.addObserver(p);
        p.startGame();
    }
}

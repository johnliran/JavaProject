package controller;

import model.GameMazeModel;
import view.GameMazeView;

public class GameMazeMain {

    public static void main(String[] args) {
        GameMazeModel m = new GameMazeModel();
        GameMazeView ui = new GameMazeView();
        Presenter p = new Presenter(m, ui);
        m.addObserver(p);
        ui.addObserver(p);
        p.startGame();
    }
}

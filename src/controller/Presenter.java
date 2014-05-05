package controller;

import model.algorithms.Model;
import view.View;
import view.WindowShell;

import java.util.Observable;
import java.util.Observer;

public class Presenter implements Observer {
    private View ui;
    private Model m;
    private final static int RESET = 1;
    private final static int SAVE = 2;
    private final static int LOAD = 3;
    private final static int UNDO = 4;
    private final static int RIGHT = 10;
    private final static int RIGHT_UP = 11;
    private final static int RIGHT_DOWN = 12;
    private final static int LEFT = 15;
    private final static int LEFT_UP = 16;
    private final static int LEFT_DOWN = 17;
    private final static int UP = 20;
    private final static int DOWN = 21;

    @Override
    public void update(Observable observable, Object notification) {
        if (observable == m) {
            ui.displayScore(((Model) observable).getScore());
            ui.displayData(((Model) observable).getData());
            if (((Model) observable).isGameOver()) {
                ui.gameOver();
            } else if (((Model) observable).isGameWon() && !(ui.isUserNotified())) {
                ui.gameWon();
            }
        }
        if (observable == ui) {
            switch (ui.getUserCommand()) {
                case UP:
                    m.moveUp(false);
                    break;

                case DOWN:
                    m.moveDown(false);
                    break;

                case RIGHT:
                    m.moveRight(false);
                    break;

                case RIGHT_DOWN:
                    m.moveDownRight(false);
                    break;

                case RIGHT_UP:
                    m.moveUpRight(false);
                    break;

                case LEFT:
                    m.moveLeft(false);
                    break;

                case LEFT_DOWN:
                    m.moveDownLeft(false);
                    break;

                case LEFT_UP:
                    m.moveUpLeft(false);
                    break;

                case RESET:
                    startGame();
                    break;

                default:
                    break;
            }
        }
        if (observable instanceof WindowShell) {
            switch (ui.getWindowShell().getUserCommand()) {
                case UNDO:
                    m.restore();
                    break;

                case SAVE:
                    m.saveGame((String) notification);
                    break;

                case LOAD:
                    m.loadGame((String) notification);
                    break;

                case RESET:
                    startGame();
                    break;

                default:
                    break;
            }
        }
    }

    public Presenter(Model m, View ui) {
        this.m = m;
        this.ui = ui;
    }

    public void startGame() {
        m.initialize();
        ui.run();
    }
}
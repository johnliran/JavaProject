package controller;

import model.algorithms.Model;
import view.View;
import view.WindowShell;

import java.util.Observable;
import java.util.Observer;

/**
 *
 */
public class Presenter implements Observer, Constants {
    private View ui;
    private Model m;

    /**
     * @param observable
     * @param notification
     */
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
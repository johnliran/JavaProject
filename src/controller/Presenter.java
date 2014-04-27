package controller;

import view.View;
import model.Model;
import org.eclipse.swt.SWT;
import java.util.Observable;
import java.util.Observer;

public class Presenter implements Observer {
    private View ui;
    private Model m;

    @Override
    public void update(Observable observable, Object notification) {
        if (observable == m) {
            if (((Model) observable).isGameOver()) {
                System.out.println("gameOver");
            } else if (((Model) observable).isGameWon()) {
                System.out.println("gameWon");
            }
            ui.displayScore(((Model) observable).getScore());
            ui.displayData(((Model) observable).getData());
        }
        if (observable == ui) {
            switch (ui.getUserCommand()) {
                case SWT.ARROW_UP: {
                    m.moveUp(false);
                    break;
                }

                case SWT.ARROW_DOWN: {
                    m.moveDown(false);
                    break;
                }

                case SWT.ARROW_RIGHT: {
                    m.moveRight(false);
                    break;
                }

                case SWT.ARROW_LEFT: {
                    m.moveLeft(false);
                    break;
                }
                case 1:
                    startGame();

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
        m.initializeBoard();
    }
}
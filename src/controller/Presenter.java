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
                ui.gameOver();
            } else if (((Model) observable).isGameWon()) {
                ui.gameWon();
            }
            ui.displayScore(((Model) observable).getScore());
            ui.displayData(((Model) observable).getData());
        }
        if (observable == ui) {
            switch (ui.getUserCommand()) {
                case SWT.ARROW_UP: {
                    System.out.println("UP");
                    m.moveUp(false);
                    break;
                }

                case SWT.ARROW_DOWN: {
                    System.out.println("DOWN");
                    m.moveDown(false);
                    break;
                }

                case SWT.ARROW_RIGHT: {
                    System.out.println("RIGHT");
                    m.moveRight(false);
                    break;
                }

                case SWT.ARROW_LEFT: {
                    System.out.println("LEFT");
                    m.moveLeft(false);
                    break;
                }

                // For testing only; RESTORE
                case SWT.SPACE: {
                    System.out.println("SPACE");
                    m.restore();
                    break;
                }

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
    }
}
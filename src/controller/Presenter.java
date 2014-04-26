package controller;

import model.Model;
import org.eclipse.swt.SWT;
import view.View;

import java.util.Observable;
import java.util.Observer;

public class Presenter implements Observer {
    View ui;
    Model m;

    @Override
    public void update(Observable observable, Object object) {
        if (observable instanceof Model) {
            ui.displayScore(((Model) observable).getScore());
            ui.displayData(((Model) observable).getData());
        }
        if (observable instanceof View) {
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


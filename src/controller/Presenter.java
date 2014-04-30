package controller;

import view.View;
import model.Model;
import org.eclipse.swt.SWT;
import java.util.Observable;
import java.util.Observer;

public class Presenter implements Observer {
    private View ui;
    private Model m;
    private final static int RESET 		= 1;
    private final static int SAVE 		= 2;
    private final static int LOAD		= 3;
    
    

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

                // For testing only; RESTORE
                case SWT.SPACE: {
                    m.restore();
                    break;
                }
                
                case RESET: {
                	startGame();
                	break;
                }
                
                case SAVE: {
                	
                	break;
                }
                
                case LOAD: {
                	
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
        ui.run();
    }
}
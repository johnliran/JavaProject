package controller;

import view.View;
import view.WindowShell;
import model.Model;

import org.eclipse.swt.SWT;

import java.util.Observable;
import java.util.Observer;

public class Presenter implements Observer {
    private View ui;
    private Model m;
    private final static int RESET= 1;
    private final static int SAVE = 2;
    private final static int LOAD = 3;
    private final static int UNDO = 4;
    

    @Override
    public void update(Observable observable, Object notification) {
        if (observable == m) {
        	System.out.println("Model");
        	ui.displayScore(((Model) observable).getScore());
            ui.displayData(((Model) observable).getData());

        	if (((Model) observable).isGameOver()) {
                ui.gameOver();
            } else if (((Model) observable).isGameWon() && !(ui.isUserNotified())) {
                ui.gameWon();
            }
            
        }
        if (observable == ui) {
        	System.out.println("UI" + ui.getUserCommand());
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
//                    m.restore();
//                    System.out.println("Saving ...");
//                    m.saveGame("2048.txt");
//                    System.out.println("Loading ...");
//                    m.loadGame("2048.txt");
                    System.out.println("Saving ...");
                    m.saveGame("maze.txt");
                    System.out.println("Loading ...");
                    m.loadGame("maze.txt");
                    break;
                }
                
                case RESET: {
                	System.out.println("im in reset");
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
    	if (observable instanceof WindowShell){
    		switch (ui.getWindowShell().getUserCommand()) {
			case UNDO: {
				m.restore();
				break;
			}
			case SAVE:
				break;
			
			case LOAD:
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
    	System.out.println("im starting game");
        m.initialize();
        ui.run();
    }
}
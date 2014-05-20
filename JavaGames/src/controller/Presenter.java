package controller;

import model.algorithms.Model;
import view.View;
import view.WindowShell;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Observable;
import java.util.Observer;

/**
 *
 */
public class Presenter implements Observer {
    private View ui;
    private Model m;

    /**
     * @param observable   Model and View (The Caller)
     * @param notification Observable's Parameters
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
                case Constants.UP:
                    m.moveUp(false);
                    break;

                case Constants.DOWN:
                    m.moveDown(false);
                    break;

                case Constants.RIGHT:
                    m.moveRight(false);
                    break;

                case Constants.RIGHT_DOWN:
                    m.moveDownRight(false);
                    break;

                case Constants.RIGHT_UP:
                    m.moveUpRight(false);
                    break;

                case Constants.LEFT:
                    m.moveLeft(false);
                    break;

                case Constants.LEFT_DOWN:
                    m.moveDownLeft(false);
                    break;

                case Constants.LEFT_UP:
                    m.moveUpLeft(false);
                    break;

                case Constants.RESET:
					startGame();
                    break;
                    
                default:
                    break;
            }
        }
        if (observable instanceof WindowShell) {
            switch (ui.getWindowShell().getUserCommand()) {
	            case Constants.SOLVE: 
	            	new Thread(new Runnable() {
						public void run() {
							try {
								m.solveGame();
							} catch (RemoteException | CloneNotSupportedException
									| NotBoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}).start();
	                break;
	                
	            case Constants.HINT:
	            	new Thread(new Runnable() {
						public void run() {
							try {
								m.getHint();
							} catch (RemoteException | CloneNotSupportedException
									| NotBoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}).start();
	                break;
            
            	case Constants.UNDO:
                    m.restore();
                    break;

                case Constants.SAVE:
                    m.saveGame((String) notification);
                    break;

                case Constants.LOAD:
                    m.loadGame((String) notification);
                    break;

                case Constants.RESET:
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
        m.initialize();
        ui.run();
    }
}
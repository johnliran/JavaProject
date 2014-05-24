package controller;

import model.algorithms.Model;
import view.View;
import view.WindowShell;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 *
 */
public class Presenter implements Observer {
	private View ui;
	private Model m;
	private ArrayList<Thread> threadsList;

	/**
	 * @param observable
	 *            Model and View (The Caller)
	 * @param notification
	 *            Observable's Parameters
	 */
	@Override
	public void update(Observable observable, Object notification) {
		if (observable == m) {
			ui.displayScore(((Model) observable).getScore());
			ui.displayData(((Model) observable).getData());
			if (((Model) observable).isGameOver()) {
				ui.gameOver();
			} else if (((Model) observable).isGameWon()
					&& !(ui.isUserNotified())) {
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
				try {
					closeAllThreads();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				startGame();
				break;

			default:
				break;
			}
		}
		if (observable instanceof WindowShell) {
			switch (ui.getWindowShell().getUserCommand()) {
			case Constants.SOLVE:
				Runnable solveRun = new Runnable() {
					@Override
					public void run() {
						try {
							int solveDepth = ui.getWindowShell()
									.getSolveDepth();
							// int solveDepth = 7;
							m.solveGame(solveDepth);
						} catch (RemoteException | CloneNotSupportedException
								| InterruptedException | NotBoundException e) {
							ui.getWindowShell().setRmiConnected(false);
							ui.getWindowShell().displayErrorMessage(
									Constants.ERROR_SERVER_GENERAL);
						}
					}
				};
				Thread solveThread = new Thread(solveRun);
				threadsList.add(solveThread);
				solveThread.start();

				break;

			case Constants.HINT:
				Runnable hintRun = new Runnable() {
					@Override
					public void run() {
						try {
							int solveDepth = ui.getWindowShell()
									.getSolveDepth();
							int numOfHints = ui.getWindowShell()
									.getNumOfHints();
							// int solveDepth = 7;
							// int numOfHints = 1;
							m.getHint(numOfHints, solveDepth);
						} catch (RemoteException | CloneNotSupportedException
								| InterruptedException | NotBoundException e) {
							// TODO Auto-generated catch block
							ui.getWindowShell().setRmiConnected(false);
							ui.getWindowShell().displayErrorMessage(
									Constants.ERROR_SERVER_GENERAL);
						}

					}
				};
				Thread hintThread = new Thread(hintRun);
				threadsList.add(hintThread);
				hintThread.start();
				break;

			case Constants.CONNECT:
				try {
					String serverName = ui.getWindowShell().getRemoteServer();
					System.out.println(serverName);
					m.connectRMI(serverName);
					ui.getWindowShell().setRmiConnected(true);
				} catch (RemoteException | NotBoundException e) {
					ui.getWindowShell().displayErrorMessage(Constants.ERROR_COULDNT_CONNECT);
					ui.getWindowShell().setRmiConnected(false);
				}
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
				try {
					closeAllThreads();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				startGame();
				break;

			case Constants.PAUSE:
				try {
					closeAllThreads();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			case Constants.CLOSETHREADS:
				System.out.println("Im in CLOSETHREADS CASE");
				try {
					closeAllThreads();

				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			default:
				break;
			}
		}
	}

	public Presenter(Model m, View ui) {
		this.m = m;
		this.ui = ui;
		threadsList = new ArrayList<Thread>();
	}

	public void closeAllThreads() throws InterruptedException {
		System.out.println(threadsList.size());
		for (Thread thread : threadsList) {
			if (thread.isAlive()) {
				thread.stop();
				System.out.println("i Closed " + thread);
				thread.join();
				System.out.println("After Joining " + thread);
			}
		}
	}

	public void startGame() {
		m.initialize();
		ui.run();
	}
}
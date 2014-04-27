package view;

import java.util.Observable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class Game2048View extends Observable implements View, Runnable {
	WindowShell ws;
	Display display;
	Shell shell;
	Board2048 board;
	int userCommand;

	private void initComponents() {
		ws = new WindowShell(400, 300, "2048");

		display = ws.getDisplay();
		shell = ws.getShell();
		shell.setBackground(new Color(display, 187, 173, 160));

		board = ws.getBoard();
		board.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent arg0) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if ((e.keyCode == SWT.ARROW_DOWN) ||
						(e.keyCode == SWT.ARROW_LEFT) ||
						(e.keyCode == SWT.ARROW_UP) ||
						(e.keyCode == SWT.ARROW_RIGHT)) {
					userCommand = e.keyCode;
					setChanged();
					notifyObservers();
				}
			}
		});
		shell.open();
	}

	@Override
	public void run() {

	}

	@Override
	public void displayData(int[][] data) {

		board.setBoardData(data);
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				board.redraw();
				while (!shell.isDisposed()) {
					if (!display.readAndDispatch()) {
						display.sleep();
					}
				}
				display.dispose();
			}
		});
	}

	@Override
	public int getUserCommand() {
		return userCommand;
	}


	public Game2048View() {
		initComponents();
	}

	@Override
	public void displayScore(int score) {
		ws.setScore(score);
	}
}

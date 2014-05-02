package view;

import java.util.Observable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class Game2048View extends Observable implements View, Runnable {
	private final static int RESET 		= 1;
    private final static int SAVE 		= 2;
    private final static int LOAD		= 3;
    private Game2048Board board;
	private Display display;
	private WindowShell windowShell;
	private Shell shell;
	private int userCommand;
	private boolean userNotified;

	private void initComponents() {
		display = new Display();
	    shell = new Shell(display);
	    Label nullLabel = new Label(shell,SWT.FILL);
	    board = new Game2048Board(shell, SWT.NO_BACKGROUND);
        windowShell = new WindowShell(display, shell,(Board)board); 
		board.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent event) {
			}

			@Override
			public void keyPressed(KeyEvent event) {
					userCommand = event.keyCode;
					setChanged();
					notifyObservers();

			}

            // Commented until we will have a buttons listener

            /* @Override
			public void keyPressed(KeyEvent event) {
				if ((event.keyCode == SWT.ARROW_DOWN) ||
				    (event.keyCode == SWT.ARROW_LEFT) ||
					(event.keyCode == SWT.ARROW_UP)   ||
					(event.keyCode == SWT.ARROW_RIGHT)) {
					userCommand = event.keyCode;
					setChanged();
					notifyObservers();
				}
			}*/
		});
		shell.open();
	}

	@Override
	public void run() {
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	
	}

	@Override
	public void displayData(int[][] data) {
		board.setBoardData(data);
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				board.redraw();
			}
		});
	}

	@Override
	public int getUserCommand() {
		return userCommand;
	}

	public Game2048View() {
		initComponents();
		userNotified = false;
	}

	@Override
	public void displayScore(int score) {
		windowShell.setScore(score);
	}

    @Override
    public void gameWon() {
    		userNotified = true;
    		int style = SWT.ICON_WORKING |SWT.YES | SWT.NO;
    	    MessageBox messageBox = new MessageBox(shell, style);
    	    messageBox.setMessage("You won! Do you want to continue?");
    	    int rc = messageBox.open();

    	    switch (rc) {
    	    	case SWT.YES: {
    	    		setChanged();
					notifyObservers();	    	      
					break;
    	    	}
	    	    case SWT.NO:
	    	    	display.dispose();
	      	      	System.exit(0);
	      	      	break;
    	    }    
    	    }

    @Override
    public void gameOver() {
		int style = SWT.ICON_WORKING |SWT.YES | SWT.NO;
	    MessageBox messageBox = new MessageBox(shell, style);
	    messageBox.setMessage("You loose! Do you want to retry again?");
	    int rc = messageBox.open();

	    switch (rc) {
	    	case SWT.YES: {
	    		userCommand = RESET;
	    		userNotified = false; 
	    		setChanged();
				notifyObservers();	    	      
				break;
	    	}
    	    case SWT.NO: {
    	      display.dispose();
    	      System.exit(0);
    	      break;
    	    }
	    }    

    }
    
    @Override
	public boolean isUserNotified() {
		return userNotified;
	}
}

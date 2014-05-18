package view;

import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import controller.Constants;

/**
 * Maze View
 */
public class GameMazeView extends Observable implements View, Runnable {
    private boolean keyFlag;
    private int lastKeyCode;
    private GameMazeBoard board;
    private Display display;
    private WindowShell windowShell;
    private Shell shell;
    private int userCommand;
    private boolean userNotified;
    private MouseCommand mouseCommand;

    public GameMazeView() {
        initComponents();
        userNotified = false;
    }

    private void initComponents() {
        display = new Display();
        shell = new Shell(display);
        Label nullLabel = new Label(shell, SWT.FILL);
        initMouseCommand();
        board = new GameMazeBoard(shell, SWT.NONE, 15, 15, mouseCommand);

        String title = "Maze";
        int width = 800;
        int height = 600;
        windowShell = new WindowShell(title, width, height, display, shell, board);
        initKeyboardListener();
        shell.open();
    }

    private void initKeyboardListener() {
        board.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(final KeyEvent event) {
                if (keyFlag == false) {
                    keyFlag = true;
                    lastKeyCode = event.keyCode;
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            int current = 0, last = 0;
                            if (keyFlag == false && lastKeyCode != event.keyCode) {
                                current = event.keyCode;
                                last = lastKeyCode;
                            } else {
                                keyFlag = false;
                                userCommand = event.keyCode;
                            }

                            if (current != 0 || last != 0) {
                                if ((current == SWT.ARROW_UP && last == SWT.ARROW_RIGHT) || last == SWT.ARROW_UP && current == SWT.ARROW_RIGHT)
                                    userCommand = Constants.RIGHT_UP;
                                if ((current == SWT.ARROW_UP && last == SWT.ARROW_LEFT) || last == SWT.ARROW_UP && current == SWT.ARROW_LEFT)
                                    userCommand = Constants.LEFT_UP;
                                if ((current == SWT.ARROW_DOWN && last == SWT.ARROW_RIGHT) || last == SWT.ARROW_DOWN && current == SWT.ARROW_RIGHT)
                                    userCommand = Constants.RIGHT_DOWN;
                                if ((current == SWT.ARROW_DOWN && last == SWT.ARROW_LEFT) || last == SWT.ARROW_DOWN && current == SWT.ARROW_LEFT)
                                    userCommand = Constants.LEFT_DOWN;

                            } else {
                                if (event.keyCode == SWT.ARROW_UP)
                                    userCommand = Constants.UP;
                                if (event.keyCode == SWT.ARROW_DOWN)
                                    userCommand = Constants.DOWN;
                                if (event.keyCode == SWT.ARROW_RIGHT)
                                    userCommand = Constants.RIGHT;
                                if (event.keyCode == SWT.ARROW_LEFT)
                                    userCommand = Constants.LEFT;
                            }
                            display.asyncExec(new Runnable() {

                                @Override
                                public void run() {
                                    setChanged();
                                    notifyObservers();
                                }
                            });

                        }
                    }, 100); // End timer.schedule
                } else {
                    keyFlag = false;
                    lastKeyCode = event.keyCode;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
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

    /**
     * @param data  Board data
     */
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

    /**
     * @return User's command
     */
    @Override
    public int getUserCommand() {
        return userCommand;
    }

    /**
     * @param score Game score
     */
    @Override
    public void displayScore(int score) {
        windowShell.setScore(score);
    }

    @Override
    public void gameWon() {
        userNotified = true;
        int style = SWT.ICON_WORKING | SWT.YES | SWT.NO;
        MessageBox messageBox = new MessageBox(shell, style);
        messageBox.setMessage("You won! Do you want to continue?");
        int rc = messageBox.open();
        switch (rc) {
            case SWT.YES:
                userCommand = Constants.RESET;
                userNotified = false;
                setChanged();
                notifyObservers();
                break;

            case SWT.NO:
                display.dispose();
                System.exit(0);
                break;
        }
    }

    @Override
    public void gameOver() {
        int style = SWT.ICON_WORKING | SWT.YES | SWT.NO;
        MessageBox messageBox = new MessageBox(shell, style);
        messageBox.setMessage("You loose! Do you want to retry again?");
        int rc = messageBox.open();
        switch (rc) {
            case SWT.YES:
                userCommand = Constants.RESET;
                userNotified = false;
                setChanged();
                notifyObservers();
                break;

            case SWT.NO:
                display.dispose();
                System.exit(0);
                break;

        }
    }

    /**
     * @return True: A notification message displayed to user
     */
    @Override
    public boolean isUserNotified() {
        return userNotified;
    }

    @Override
    public WindowShell getWindowShell() {
        return windowShell;
    }

	public void initMouseCommand() {
		mouseCommand = new MouseCommand() {
			
			@Override
			public void setMouseCommand(Point to, Point objectBounds) {													
				boolean move = true;

				if (to.x < 0  
				&&	to.y < 0   
				&&	Math.abs(to.x) < objectBounds.x    
				&&	Math.abs(to.y) < objectBounds.y) {
						userCommand = Constants.LEFT_UP;
				} else if (to.x < 0  
						&& Math.abs(to.x) < objectBounds.x  
						&& to.y > objectBounds.y
						&& to.y < (2 * objectBounds.y)) {
							userCommand = Constants.LEFT_DOWN;
				} else if (to.x > objectBounds.x    
						&& to.y < 0
						&& to.x < (2 * objectBounds.x)
						&& Math.abs(to.y) < objectBounds.y) {
							userCommand = Constants.RIGHT_UP;
				} else if (to.x > objectBounds.x
						&& to.y > objectBounds.y
						&& to.x < (2 * objectBounds.x) 
						&& to.y < (2 * objectBounds.y)) {
							userCommand = Constants.RIGHT_DOWN;
				} else if (to.x > 0 
						&& to.y < 0
						&& to.x < objectBounds.x 
						&& Math.abs(to.y) < objectBounds.y ) {
							userCommand = Constants.UP;
				} else if (to.x > 0 
						&& to.x < objectBounds.x
						&& to.y > objectBounds.y
						&& to.y < (2 * objectBounds.y)) {
							userCommand = Constants.DOWN;
				} else if (to.x < 0 
						&& Math.abs(to.x) < objectBounds.x 
						&& to.y > 0 
						&& to.y < objectBounds.y) {
							userCommand = Constants.LEFT;
				} else if (to.x > objectBounds.x  
						&& to.y > 0
						&& to.x < (2 * objectBounds.x)
						&& to.y < objectBounds.y) {
							userCommand = Constants.RIGHT;
				} else {
					move = false;
				}

				if (move) {
					setChanged();
					notifyObservers();
				}
			}
		};
		
	}
}
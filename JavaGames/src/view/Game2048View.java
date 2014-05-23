package view;

import controller.Constants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import java.util.Observable;

/**
 * 2048 View
 */
public class Game2048View extends Observable implements View, Runnable {
    private Game2048Board board;
    private Display display;
    public WindowShell windowShell;
    private Shell shell;
    private int userCommand;
    private boolean userNotified;
    private MouseCommand mouseCommand;

    private void initComponents() {
        display = new Display();
        shell = new Shell(display);
        Label nullLabel = new Label(shell, SWT.FILL);
        initMouseCommand();
        board = new Game2048Board(shell, SWT.NONE, mouseCommand);
        String title = "2048";
        int width = 500;
        int height = 350;
        windowShell = new WindowShell(title, width, height, display, shell, board);
        initKeyboardListener();
        shell.open();
    }

    private void initKeyboardListener() {
        board.addKeyListener(new KeyListener() {
            @Override
            public void keyReleased(KeyEvent event) {
            }

            @Override
            public void keyPressed(KeyEvent event) {
                if (event.keyCode == SWT.ARROW_UP)
                    userCommand = Constants.UP;
                if (event.keyCode == SWT.ARROW_DOWN)
                    userCommand = Constants.DOWN;
                if (event.keyCode == SWT.ARROW_RIGHT)
                    userCommand = Constants.RIGHT;
                if (event.keyCode == SWT.ARROW_LEFT)
                    userCommand = Constants.LEFT;
                if (event.keyCode == SWT.SPACE)
                    userCommand = Constants.SPACE;
                setChanged();
                notifyObservers();
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
     * @param data
     *            Board data
     */
    @Override
    public void displayData(final int[][] data) {
        display.syncExec(new Runnable() {
            @Override
            public void run() {
                board.setBoardData(data);
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

    public Game2048View() {
        initComponents();
        userNotified = false;
    }

    /**
     * @param score
     *            Game score
     */
    @Override
    public void displayScore(final int score) {
        display.syncExec(new Runnable() {
            @Override
            public void run() {
                windowShell.setScore(score);
            }
        });
    }

    @Override
    public void gameWon() {
        display.syncExec(new Runnable() {
            @Override
            public void run() {
                userNotified = true;
                int style = SWT.ICON_WORKING | SWT.YES | SWT.NO;
                MessageBox messageBox = new MessageBox(shell, style);
                messageBox.setMessage("You won! Do you want to continue?");
                int rc = messageBox.open();

                switch (rc) {
                    case SWT.YES:
                        setChanged();
                        notifyObservers();
                        break;
                    case SWT.NO:
                        windowShell.closeAll();
                        display.dispose();
                        System.exit(0);
                        break;
                }

            }
        });
    }

    @Override
    public void gameOver() {
        display.syncExec(new Runnable() {
            @Override
            public void run() {
                int style = SWT.ICON_WORKING | SWT.YES | SWT.NO;
                MessageBox messageBox = new MessageBox(shell, style);
                messageBox.setMessage("You lose! Do you want to retry again?");
                int rc = messageBox.open();

                switch (rc) {
                    case SWT.YES:
                        userCommand = Constants.RESET;
                        userNotified = false;
                        setChanged();
                        notifyObservers();
                        break;
                    case SWT.NO:
                        windowShell.closeAll();
                        display.dispose();
                        System.exit(0);
                        break;

                }
            }
        });
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

                if (to.x > 0 && to.y < 0) {
                    userCommand = Constants.UP;
                } else if (to.x > 0 && to.y > objectBounds.y) {
                    userCommand = Constants.DOWN;
                } else if (to.x < 0 && to.y > 0) {
                    userCommand = Constants.LEFT;
                } else if (to.x > objectBounds.x && to.y > 0) {
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

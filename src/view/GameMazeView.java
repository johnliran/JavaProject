package view;

import controller.Constants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

public class GameMazeView extends Observable implements View, Runnable, Constants {
    private boolean keyFlag;
    private int lastKeyCode;
    private GameMazeBoard board;
    private Display display;
    private WindowShell windowShell;
    private Shell shell;
    private int userCommand;
    private boolean userNotified;

    public GameMazeView() {
        initComponents();
        userNotified = false;
    }

    private void initComponents() {
        display = new Display();
        shell = new Shell(display);
        Label nullLabel = new Label(shell, SWT.FILL);
        board = new GameMazeBoard(shell, SWT.NO_BACKGROUND, 15, 15);
        String title = "Maze";
        int width = 800;
        int height = 600;
        windowShell = new WindowShell(title, width, height, display, shell, (Board) board);
        shell.setBackground(new Color(display, 187, 173, 160));
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
                                    userCommand = RIGHT_UP;
                                if ((current == SWT.ARROW_UP && last == SWT.ARROW_LEFT) || last == SWT.ARROW_UP && current == SWT.ARROW_LEFT)
                                    userCommand = LEFT_UP;
                                if ((current == SWT.ARROW_DOWN && last == SWT.ARROW_RIGHT) || last == SWT.ARROW_DOWN && current == SWT.ARROW_RIGHT)
                                    userCommand = RIGHT_DOWN;
                                if ((current == SWT.ARROW_DOWN && last == SWT.ARROW_LEFT) || last == SWT.ARROW_DOWN && current == SWT.ARROW_LEFT)
                                    userCommand = LEFT_DOWN;

                            } else {
                                if (event.keyCode == SWT.ARROW_UP)
                                    userCommand = UP;
                                if (event.keyCode == SWT.ARROW_DOWN)
                                    userCommand = DOWN;
                                if (event.keyCode == SWT.ARROW_RIGHT)
                                    userCommand = RIGHT;
                                if (event.keyCode == SWT.ARROW_LEFT)
                                    userCommand = LEFT;
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
                userCommand = RESET;
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
                userCommand = RESET;
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
    public boolean isUserNotified() {
        return userNotified;
    }

    @Override
    public WindowShell getWindowShell() {
        return windowShell;
    }
}

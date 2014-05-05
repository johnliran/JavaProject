package view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import java.util.Observable;

public class Game2048View extends Observable implements View, Runnable {
    private final static int RESET = 1;
    private final static int SAVE = 2;
    private final static int LOAD = 3;
    private final static int RIGHT = 10;
    private final static int LEFT = 15;
    private final static int UP = 20;
    private final static int DOWN = 21;
    private Game2048Board board;
    private Display display;
    public WindowShell windowShell;
    private Shell shell;
    private int userCommand;
    private boolean userNotified;

    private void initComponents() {
        display = new Display();
        shell = new Shell(display);
        Label nullLabel = new Label(shell, SWT.FILL);
        board = new Game2048Board(shell, SWT.NO_BACKGROUND);

        String title = "2048";
        int width = 400;
        int height = 300;
        windowShell = new WindowShell(title, width, height, display, shell, (Board) board);

        shell.setBackground(new Color(display, 187, 173, 160));
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
                    userCommand = UP;
                if (event.keyCode == SWT.ARROW_DOWN)
                    userCommand = DOWN;
                if (event.keyCode == SWT.ARROW_RIGHT)
                    userCommand = RIGHT;
                if (event.keyCode == SWT.ARROW_LEFT)
                    userCommand = LEFT;
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
        int style = SWT.ICON_WORKING | SWT.YES | SWT.NO;
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
        int style = SWT.ICON_WORKING | SWT.YES | SWT.NO;
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

    @Override
    public WindowShell getWindowShell() {
        return windowShell;
    }
}

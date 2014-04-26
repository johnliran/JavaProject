package view;

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

import java.util.Observable;

public class Game2048View extends Observable implements View, Runnable {
    Display display;
    Shell shell;
    Board board;
    Label score;
    int userCommand;

    private void initComponents() {
        display = new Display();
        shell = new Shell(display);
        shell.setLayout(new GridLayout(2, false));
        shell.setSize(400, 300);
        shell.setText("2048");
        shell.setBackground(new Color(display, 187, 173, 160));

        Button b1 = new Button(shell, SWT.PUSH);
        b1.setText("button 1");
        b1.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));

        //Define the Board Widget and initialize it:
        board = new Board(shell, SWT.NO_BACKGROUND);
        board.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 3));

        Button b2 = new Button(shell, SWT.PUSH);
        b2.setText("button 2");
        b2.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));

        //Set the styled label who contains the game score;
        score = new Label(shell, SWT.BORDER);
        score.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
        score.setForeground(new Color(display, 119, 110, 101));
        Font font = score.getFont();
        score.setFont(new Font(display, font.getFontData()[0].getName(), 24, SWT.BOLD));
        score.setText(0 + "");

        board.setFocus();
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
        this.score.setText(score + "");
    }


}

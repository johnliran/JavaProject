package view;

import java.util.Observable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import controller.Constants;

/**
 * Generic Window Shell
 */
public class WindowShell extends Observable {
    private Label score;
    private int userCommand;

    public WindowShell(String title, int width, int height, Display display, Shell shell, Board board) {
        shell.setText(title);
        shell.setSize(width, height);
        shell.setLayout(new GridLayout(2, false));
        shell.setBackground(new Color(display, 187, 173, 160));
        shell.setBackgroundMode(SWT.INHERIT_DEFAULT);
        ((Composite) board).setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 8));
        initMenuBar(display, shell, board);
        initButtons(display, shell, board);
    }

    private void initMenuBar(Display display, Shell shell, Board board) {
        Menu menuBar = new Menu(shell, SWT.BAR);
        Menu fileMenu = new Menu(menuBar);
        Menu editMenu = new Menu(menuBar);

        MenuItem file = new MenuItem(menuBar, SWT.CASCADE);
        file.setText("File");
        file.setMenu(fileMenu);

        MenuItem editItem = new MenuItem(menuBar, SWT.CASCADE);
        editItem.setText("Edit");
        editItem.setMenu(editMenu);

        MenuItem loadItem = new MenuItem(fileMenu, SWT.PUSH);
        loadItem.setText("Load");

        MenuItem saveItem = new MenuItem(fileMenu, SWT.PUSH);
        saveItem.setText("Save");

        MenuItem exitItem = new MenuItem(fileMenu, SWT.PUSH);
        exitItem.setText("Exit");

        MenuItem hintItem = new MenuItem(editMenu, SWT.PUSH);
        hintItem.setText("Hint");

        MenuItem solveItem = new MenuItem(editMenu, SWT.PUSH);
        solveItem.setText("Solve");

        MenuItem undoItem = new MenuItem(editMenu, SWT.PUSH);
        undoItem.setText("Undo");

        MenuItem resetItem = new MenuItem(editMenu, SWT.PUSH);
        resetItem.setText("Reset");

        shell.setMenuBar(menuBar);

        hintItem.addListener(SWT.Selection, hintListener(shell, board));
        solveItem.addListener(SWT.Selection, solveListener(shell, board));
        undoItem.addListener(SWT.Selection, undoListener(shell, board));
        saveItem.addListener(SWT.Selection, saveListener(shell, board));
        loadItem.addListener(SWT.Selection, loadListener(shell, board));
        resetItem.addListener(SWT.Selection, resetListener(shell, board));
        exitItem.addListener(SWT.Selection, exitListener(display));
    }

    private void initButtons(Display display, Shell shell, Board board) {
        Button hintButton = new Button(shell, SWT.PUSH);
        hintButton.setText("Hint");
        hintButton.setImage(new Image(display, Constants.IMAGE_BUTTON_HINT));
        hintButton.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));

        Button solveButton = new Button(shell, SWT.PUSH);
        solveButton.setText("Solve");
        solveButton.setImage(new Image(display, Constants.IMAGE_BUTTON_SOLVE));
        solveButton.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));

        Button undoButton = new Button(shell, SWT.PUSH);
        undoButton.setText("Undo");
        undoButton.setImage(new Image(display, Constants.IMAGE_BUTTON_UNDO));
        undoButton.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));

        Button resetButton = new Button(shell, SWT.PUSH);
        resetButton.setText("Reset");
        resetButton.setImage(new Image(display, Constants.IMAGE_BUTTON_RESET));
        resetButton.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));

        Button loadButton = new Button(shell, SWT.PUSH);
        loadButton.setText("Load");
        loadButton.setImage(new Image(display, Constants.IMAGE_BUTTON_LOAD));
        loadButton.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));

        Button saveButton = new Button(shell, SWT.PUSH);
        saveButton.setText("Save");
        saveButton.setImage(new Image(display, Constants.IMAGE_BUTTON_SAVE));
        saveButton.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));

        score = new Label(shell, SWT.NONE);
        score.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
        score.setForeground(new Color(display, 119, 110, 101));
        Font font = score.getFont();
        score.setFont(new Font(display, font.getFontData()[0].getName(), Constants.SCORE_FONT_SIZE, SWT.BOLD));
        score.setText(0 + "         ");

        hintButton.addListener(SWT.Selection, hintListener(shell, board));
        solveButton.addListener(SWT.Selection, solveListener(shell, board));
        undoButton.addListener(SWT.Selection, undoListener(shell, board));
        saveButton.addListener(SWT.Selection, saveListener(shell, board));
        loadButton.addListener(SWT.Selection, loadListener(shell, board));
        resetButton.addListener(SWT.Selection, resetListener(shell, board));
    }


    private Listener hintListener(final Shell shell, final Board board) {
        return new Listener() {
            @Override
            public void handleEvent(Event event) {
                userCommand = Constants.HINT;
                setChanged();
                notifyObservers();
                ((Composite) board).forceFocus();
            }
        };
    }


    private Listener solveListener(final Shell shell, final Board board) {
        return new Listener() {
            @Override
            public void handleEvent(Event event) {
                userCommand = Constants.SOLVE;
                setChanged();
                notifyObservers();
                ((Composite) board).forceFocus();
            }
        };
    }

    private Listener undoListener(final Shell shell, final Board board) {
        return new Listener() {
            @Override
            public void handleEvent(Event event) {
                userCommand = Constants.UNDO;
                setChanged();
                notifyObservers();
                ((Composite) board).forceFocus();
            }
        };
    }

    private Listener saveListener(final Shell shell, final Board board) {
        return new Listener() {
            @Override
            public void handleEvent(Event event) {
                FileDialog saveDialog = new FileDialog(shell, SWT.SAVE);
                saveDialog.setFilterExtensions(Constants.EXTENSIONS);
                String saveTo = saveDialog.open();
                if (saveTo != null && saveTo.length() > 0) {
                    userCommand = Constants.SAVE;
                    setChanged();
                    notifyObservers(saveTo);
                    ((Composite) board).forceFocus();
                }
            }
        };
    }

    private Listener loadListener(final Shell shell, final Board board) {
        return new Listener() {
            @Override
            public void handleEvent(Event event) {
                FileDialog loadDialog = new FileDialog(shell, SWT.OPEN);
                loadDialog.setFilterExtensions(Constants.EXTENSIONS);
                String loadFrom = loadDialog.open();
                if (loadFrom != null && loadFrom.length() > 0) {
                    userCommand = Constants.LOAD;
                    setChanged();
                    notifyObservers(loadFrom);
                    ((Composite) board).forceFocus();
                }
            }
        };
    }

    private Listener resetListener(final Shell shell, final Board board) {
        return new Listener() {
            @Override
            public void handleEvent(Event event) {
                userCommand = Constants.RESET;
                setChanged();
                notifyObservers();
                ((Composite) board).forceFocus();
            }
        };
    }

    private Listener exitListener(final Display display) {
        return new Listener() {
            @Override
            public void handleEvent(Event event) {
                display.dispose();
                System.exit(0);
            }
        };
    }

    public int getUserCommand() {
        return userCommand;
    }

    public void setScore(int score) {
        this.score.setText(score + "");
    }
}
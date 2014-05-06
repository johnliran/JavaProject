package view;

import controller.Constants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import java.util.Observable;

/**
 *
 */
public class WindowShell extends Observable implements Constants {
    private Label score;
    private int userCommand;

    public WindowShell(String title, int width, int height, Display display, Shell shell, Board board) {
        shell.setText(title);
        shell.setSize(width, height);
        shell.setLayout(new GridLayout(2, false));
        shell.setBackground(new Color(display, 187, 173, 160));
        shell.setBackgroundMode(SWT.INHERIT_DEFAULT);
        ((Composite) board).setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 6));
        initMenuBar(display, shell);
        initButtons(display, shell);
    }

    private void initMenuBar(Display display, Shell shell) {
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

        MenuItem undoItem = new MenuItem(editMenu, SWT.PUSH);
        undoItem.setText("Undo");

        MenuItem resetItem = new MenuItem(editMenu, SWT.PUSH);
        resetItem.setText("Reset");

        shell.setMenuBar(menuBar);

        undoItem.addListener(SWT.Selection, undoListener(shell));
        saveItem.addListener(SWT.Selection, saveListener(shell));
        loadItem.addListener(SWT.Selection, loadListener(shell));
        resetItem.addListener(SWT.Selection, resetListener(shell));
        exitItem.addListener(SWT.Selection, exitListener(display));
    }

    private void initButtons(Display display, Shell shell) {
        Button undoButton = new Button(shell, SWT.PUSH);
        undoButton.setText("Undo");
        undoButton.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));

        Button resetButton = new Button(shell, SWT.PUSH);
        resetButton.setText("Reset");
        resetButton.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));

        Button loadButton = new Button(shell, SWT.PUSH);
        loadButton.setText("Load");
        loadButton.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));

        Button saveButton = new Button(shell, SWT.PUSH);
        saveButton.setText("Save");
        saveButton.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));

        score = new Label(shell, SWT.NONE);
        score.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
        score.setForeground(new Color(display, 119, 110, 101));
        Font font = score.getFont();
        score.setFont(new Font(display, font.getFontData()[0].getName(), SCORE_FONT_SIZE, SWT.BOLD));
        score.setText(0 + "     ");

        undoButton.addListener(SWT.Selection, undoListener(shell));
        saveButton.addListener(SWT.Selection, saveListener(shell));
        loadButton.addListener(SWT.Selection, loadListener(shell));
        resetButton.addListener(SWT.Selection, resetListener(shell));
    }

    private Listener undoListener(final Shell shell) {
        return new Listener() {
            @Override
            public void handleEvent(Event event) {
                userCommand = UNDO;
                setChanged();
                notifyObservers();
            }
        };
    }

    private Listener saveListener(final Shell shell) {
        return new Listener() {
            @Override
            public void handleEvent(Event event) {
                FileDialog saveDialog = new FileDialog(shell, SWT.SAVE);
                saveDialog.setFilterExtensions(filterExtensions);
                String saveTo = saveDialog.open();
                if (saveTo != null && saveTo.length() > 0) {
                    userCommand = SAVE;
                    setChanged();
                    notifyObservers(saveTo);
                }
            }
        };
    }

    private Listener loadListener(final Shell shell) {
        return new Listener() {
            @Override
            public void handleEvent(Event event) {
                FileDialog loadDialog = new FileDialog(shell, SWT.OPEN);
                loadDialog.setFilterExtensions(filterExtensions);
                String loadFrom = loadDialog.open();
                if (loadFrom != null && loadFrom.length() > 0) {
                    userCommand = LOAD;
                    setChanged();
                    notifyObservers(loadFrom);
                }
            }
        };
    }

    private Listener resetListener(final Shell shell) {
        return new Listener() {
            @Override
            public void handleEvent(Event event) {
                userCommand = RESET;
                setChanged();
                notifyObservers();
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
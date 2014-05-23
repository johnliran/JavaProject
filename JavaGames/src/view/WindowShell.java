package view;

import controller.Constants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import java.util.Observable;

/**
 * Generic Window Shell
 */
public class WindowShell extends Observable {
    private Label score;
    private int userCommand;
    private int numOfHints;
    private int solveDepth;

    public WindowShell(String title, int width, int height, Display display, Shell shell, Board board) {
        shell.setText(title);
        shell.setSize(width, height);
        shell.setLayout(new GridLayout(2, false));
        shell.setBackground(new Color(display, Constants.BCOLOR_R, Constants.BCOLOR_G, Constants.BCOLOR_B));
        shell.setBackgroundMode(SWT.INHERIT_DEFAULT);
        shell.addListener(SWT.Close, new Listener() {
            public void handleEvent(Event event) {
                closeAll();
            }
        });

        ((Composite) board).setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 8));
        createMenuBar(shell, board);

        TabFolder tabFolder = new TabFolder(shell, SWT.NULL);

        TabItem playTab = new TabItem(tabFolder, SWT.NULL);
        SashForm playForm = new SashForm(tabFolder, SWT.VERTICAL);
        playTab.setText("Play");
        createPlayButtons(playForm, board);
        playTab.setControl(playForm);

        TabItem settingsTab = new TabItem(tabFolder, SWT.NULL);
        SashForm settingsForm = new SashForm(tabFolder, SWT.VERTICAL);
        settingsTab.setText("Settings");
        createSettingsButtons(settingsForm, board);
        settingsTab.setControl(settingsForm);

        System.out.println("Default Settings: Number Of Hints " + getNumOfHints() + " and Solve Depth " + getSolveDepth());
    }


    private void createMenuBar(Shell parent, Board board) {
        Menu menuBar = new Menu(parent, SWT.BAR);
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

        MenuItem solveItem = new MenuItem(editMenu, SWT.PUSH);
        solveItem.setText("Solve");

        MenuItem undoItem = new MenuItem(editMenu, SWT.PUSH);
        undoItem.setText("Undo");

        MenuItem resetItem = new MenuItem(editMenu, SWT.PUSH);
        resetItem.setText("Reset");

        parent.setMenuBar(menuBar);

        solveItem.addListener(SWT.Selection, solveListener(board));
        undoItem.addListener(SWT.Selection, undoListener(board));
        resetItem.addListener(SWT.Selection, resetListener(board));
        exitItem.addListener(SWT.Selection, exitListener(board));
        saveItem.addListener(SWT.Selection, saveListener(parent, board));
        loadItem.addListener(SWT.Selection, loadListener(parent, board));
    }

    private void createPlayButtons(Composite parent, Board board) {
        Button solveButton = new Button(parent, SWT.PUSH);
        solveButton.setText("Solve");
        solveButton.setImage(new Image(Display.getCurrent(), Constants.IMAGE_BUTTON_SOLVE));
        solveButton.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));

        Button undoButton = new Button(parent, SWT.PUSH);
        undoButton.setText("Undo");
        undoButton.setImage(new Image(Display.getCurrent(), Constants.IMAGE_BUTTON_UNDO));
        undoButton.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));

        Button resetButton = new Button(parent, SWT.PUSH);
        resetButton.setText("Reset");
        resetButton.setImage(new Image(Display.getCurrent(), Constants.IMAGE_BUTTON_RESET));
        resetButton.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));

        Button loadButton = new Button(parent, SWT.PUSH);
        loadButton.setText("Load");
        loadButton.setImage(new Image(Display.getCurrent(), Constants.IMAGE_BUTTON_LOAD));
        loadButton.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));

        Button saveButton = new Button(parent, SWT.PUSH);
        saveButton.setText("Save");
        saveButton.setImage(new Image(Display.getCurrent(), Constants.IMAGE_BUTTON_SAVE));
        saveButton.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));

        score = new Label(parent, SWT.NONE);
        score.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
        score.setForeground(new Color(Display.getCurrent(), Constants.FCOLOR_R, Constants.FCOLOR_G, Constants.FCOLOR_B));
        Font font = score.getFont();
        score.setFont(new Font(Display.getCurrent(), font.getFontData()[0].getName(), Constants.SCORE_FONT_SIZE, SWT.BOLD));
        score.setText(0 + "         ");

        solveButton.addListener(SWT.Selection, solveListener(board));
        undoButton.addListener(SWT.Selection, undoListener(board));
        resetButton.addListener(SWT.Selection, resetListener(board));
        saveButton.addListener(SWT.Selection, saveListener(parent.getShell(), board));
        loadButton.addListener(SWT.Selection, loadListener(parent.getShell(), board));
    }

    private void createSettingsButtons(Composite parent, Board board) {
        Label numOfHints = new Label(parent, SWT.CENTER);
        Font hintsFont = numOfHints.getFont();
        numOfHints.setForeground(new Color(Display.getCurrent(), Constants.FCOLOR_R, Constants.FCOLOR_G, Constants.FCOLOR_B));
        numOfHints.setFont(new Font(Display.getCurrent(), hintsFont.getFontData()[0].getName(), Constants.DEFAULT_FONT_SIZE, SWT.BOLD));
        numOfHints.setText("Number Of Hints");

        Combo numOfHintsCombo = new Combo(parent, SWT.READ_ONLY);
        for (int defaultNumberOfHint : Constants.DEFAULT_NUMBER_OF_HINTS) {
            numOfHintsCombo.add(Integer.toString(defaultNumberOfHint));
        }
        numOfHintsCombo.select(0);
        setNumOfHints(Constants.DEFAULT_NUMBER_OF_HINTS[0]);

        Label solveDepth = new Label(parent, SWT.CENTER);
        Font solveFont = solveDepth.getFont();
        solveDepth.setForeground(new Color(Display.getCurrent(), Constants.FCOLOR_R, Constants.FCOLOR_G, Constants.FCOLOR_B));
        solveDepth.setFont(new Font(Display.getCurrent(), solveFont.getFontData()[0].getName(), Constants.DEFAULT_FONT_SIZE, SWT.BOLD));
        solveDepth.setText("Solve Depth");

        Combo solveDepthCombo = new Combo(parent, SWT.READ_ONLY);
        for (int defaultSolveDepth : Constants.DEFAULT_SOLVE_DEPTH) {
            solveDepthCombo.add(Integer.toString(defaultSolveDepth));
        }
        solveDepthCombo.select(solveDepthCombo.getItemCount() - 1);
        setSolveDepth(Constants.DEFAULT_SOLVE_DEPTH[Constants.DEFAULT_SOLVE_DEPTH.length - 1]);

        Label connectTo = new Label(parent, SWT.CENTER);
        Font connectToFont = connectTo.getFont();
        connectTo.setForeground(new Color(Display.getCurrent(), Constants.FCOLOR_R, Constants.FCOLOR_G, Constants.FCOLOR_B));
        connectTo.setFont(new Font(Display.getCurrent(), connectToFont.getFontData()[0].getName(), Constants.DEFAULT_FONT_SIZE, SWT.BOLD));
        connectTo.setText("Connect to Server");

        Combo connectToCombo = new Combo(parent, SWT.READ_ONLY);
        connectToCombo.setItems(Constants.DEFAULT_SERVERS);
        connectToCombo.select(0);
    }

    private Listener solveListener(final Board board) {
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

    private Listener undoListener(final Board board) {
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

    private Listener resetListener(final Board board) {
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

    private Listener exitListener(final Board board) {
        return new Listener() {
            @Override
            public void handleEvent(Event event) {
                closeAll();
                Display.getCurrent().dispose();
                System.exit(0);
            }
        };
    }

    private Listener saveListener(final Shell parent, final Board board) {
        return new Listener() {
            @Override
            public void handleEvent(Event event) {
                FileDialog saveDialog = new FileDialog(parent, SWT.SAVE);
                saveDialog.setFilterExtensions(Constants.EXTENSIONS);
                String saveTo = saveDialog.open();
                if (saveTo != null && saveTo.length() > 0) {
                    userCommand = Constants.SAVE;
                    setChanged();
                    notifyObservers(saveTo);
                }
                ((Composite) board).forceFocus();
            }
        };
    }

    private Listener loadListener(final Shell parent, final Board board) {
        return new Listener() {
            @Override
            public void handleEvent(Event event) {
                FileDialog loadDialog = new FileDialog(parent, SWT.OPEN);
                loadDialog.setFilterExtensions(Constants.EXTENSIONS);
                String loadFrom = loadDialog.open();
                if (loadFrom != null && loadFrom.length() > 0) {
                    userCommand = Constants.LOAD;
                    setChanged();
                    notifyObservers(loadFrom);
                }
                ((Composite) board).forceFocus();
            }
        };
    }

    public int getUserCommand() {
        return userCommand;
    }

    public void setScore(int score) {
        this.score.setText(score + "");
    }

    public int getNumOfHints() {
        return numOfHints;
    }

    public void setNumOfHints(int numOfHints) {
        this.numOfHints = numOfHints;
    }

    public int getSolveDepth() {
        return solveDepth;
    }

    public void setSolveDepth(int solveDepth) {
        this.solveDepth = solveDepth;
    }

    public void closeAll() {
        userCommand = Constants.CLOSETHREADS;
        setChanged();
        notifyObservers();
        Thread.currentThread().stop();
    }
}
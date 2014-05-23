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
    private String remoteServer;
    private boolean rmiConnected;

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

        // Initialize with default values
        setRmiConnected(false);
        setRemoteServer(Constants.DEFAULT_SERVERS[0]);
        setSolveDepth(Constants.DEFAULT_SOLVE_DEPTH[Constants.DEFAULT_SOLVE_DEPTH.length - 1]);
        setNumOfHints(Constants.DEFAULT_NUMBER_OF_HINTS[0]);

        createMenuBar(shell, board);

        TabFolder tabFolder = new TabFolder(shell, SWT.NULL);

        TabItem playTab = new TabItem(tabFolder, SWT.NULL);
        SashForm playForm = new SashForm(tabFolder, SWT.VERTICAL);
        createPlayButtons(playForm, board);
        playTab.setText("Play");
        playTab.setControl(playForm);

        TabItem settingsTab = new TabItem(tabFolder, SWT.NULL);
        SashForm settingsForm = new SashForm(tabFolder, SWT.VERTICAL);
        Composite settingsComposite = new Composite(settingsForm, SWT.FILL);
        GridLayout settingsLayout = new GridLayout(1, false);
        settingsLayout.verticalSpacing = 1;
        settingsComposite.setLayout(settingsLayout);
        createSettingsButtons(settingsComposite, board);
        settingsTab.setText("Settings");
        settingsTab.setControl(settingsForm);
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
        createButton(parent, "Solve", Constants.IMAGE_BUTTON_SOLVE).addListener(SWT.Selection, solveListener(board));
        createButton(parent, "Undo", Constants.IMAGE_BUTTON_UNDO).addListener(SWT.Selection, undoListener(board));
        createButton(parent, "Reset", Constants.IMAGE_BUTTON_RESET).addListener(SWT.Selection, resetListener(board));
        createButton(parent, "Save", Constants.IMAGE_BUTTON_SAVE).addListener(SWT.Selection, saveListener(parent.getShell(), board));
        createButton(parent, "Load", Constants.IMAGE_BUTTON_LOAD).addListener(SWT.Selection, loadListener(parent.getShell(), board));
        score = createLabel(parent, Constants.SCORE_FONT_SIZE, 0 + "         ");
    }

    private void createSettingsButtons(Composite parent, Board board) {
    /*  Group solverGroup = new Group(parent, SWT.SHADOW_ETCHED_IN);
        solverGroup.setText("Solver");
        solverGroup.setLayout(new GridLayout(1, false));

        Group serverGroup = new Group(parent, SWT.SHADOW_ETCHED_IN);
        serverGroup.setText("Remote Server");
        serverGroup.setLayout(new GridLayout(1, false));
    */
        createLabel(parent, Constants.DEFAULT_FONT_SIZE, "Number Of Hints");
        Combo numOfHintsCombo = new Combo(parent, SWT.READ_ONLY);
        numOfHintsCombo.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
        for (int defaultNumberOfHints : Constants.DEFAULT_NUMBER_OF_HINTS) {
            numOfHintsCombo.add(Integer.toString(defaultNumberOfHints));
        }
        numOfHintsCombo.add("To Resolve");
        numOfHintsCombo.select(0);

        createLabel(parent, Constants.DEFAULT_FONT_SIZE, "Solve Depth");
        Combo solveDepthCombo = new Combo(parent, SWT.READ_ONLY);
        solveDepthCombo.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
        for (int defaultSolveDepth : Constants.DEFAULT_SOLVE_DEPTH) {
            solveDepthCombo.add(Integer.toString(defaultSolveDepth));
        }
        solveDepthCombo.select(solveDepthCombo.getItemCount() - 1);

        createLabel(parent, Constants.DEFAULT_FONT_SIZE, "Connect to Server");
        Combo connectToCombo = new Combo(parent, SWT.DROP_DOWN);
        connectToCombo.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
        connectToCombo.setItems(Constants.DEFAULT_SERVERS);
        connectToCombo.select(0);

        Label connectionStatus = createLabel(parent, Constants.DEFAULT_FONT_SIZE, "");
        setConnectionStatusStyle(connectionStatus);

        numOfHintsCombo.addListener(SWT.Modify, numOfHintsListener());
        solveDepthCombo.addListener(SWT.Modify, solveDepthListener());
        connectToCombo.addListener(SWT.Modify, connectToLitener());
        createButton(parent, "Connect", Constants.IMAGE_BUTTON_CONNECT).addListener(SWT.Selection, connectListener());
    }

    private Label createLabel(Composite parent, int fontSize, String text) {
        Label label = new Label(parent, SWT.CENTER);
        label.setForeground(new Color(Display.getCurrent(), Constants.FCOLOR_R, Constants.FCOLOR_G, Constants.FCOLOR_B));
        label.setFont(new Font(Display.getCurrent(), label.getFont().getFontData()[0].getName(), fontSize, SWT.BOLD));
        label.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
        label.setText(text);
        return label;
    }

    private Button createButton(Composite parent, String text, String image) {
        Button button = new Button(parent, SWT.PUSH);
        button.setImage(new Image(Display.getCurrent(), image));
        button.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
        button.setText(text);
        return button;
    }

    private Listener numOfHintsListener() {
        return new Listener() {
            @Override
            public void handleEvent(Event event) {
                boolean solveGame = true;
                for (int defaultNumberOfHints : Constants.DEFAULT_NUMBER_OF_HINTS) {
                    if (Integer.toString(defaultNumberOfHints).equalsIgnoreCase(((Combo) event.widget).getText().toString())) {
                        setNumOfHints(defaultNumberOfHints);
                        solveGame = false;
                        break;
                    }
                }
                if (solveGame) {
                    setNumOfHints(Integer.MAX_VALUE);
                }
            }
        };
    }

    private Listener solveDepthListener() {
        return new Listener() {
            @Override
            public void handleEvent(Event event) {
                for (int defaultSolveDepth : Constants.DEFAULT_SOLVE_DEPTH) {
                    if (Integer.toString(defaultSolveDepth).equalsIgnoreCase(((Combo) event.widget).getText().toString())) {
                        setSolveDepth(defaultSolveDepth);
                        break;
                    }
                }
            }
        };
    }

    private Listener connectToLitener() {
        return new Listener() {
            @Override
            public void handleEvent(Event event) {
                System.out.println("Connect To");
                System.out.println(((Combo) event.widget).getText().toString());
                setRemoteServer(((Combo) event.widget).getText().toString());
                ((Combo) event.widget).getParent().forceFocus();
            }
        };
    }

    private Listener connectListener() {
        return new Listener() {
            @Override
            public void handleEvent(Event event) {
                userCommand = Constants.CONNECT;
                setChanged();
                notifyObservers();
            }
        };
    }

    private Listener solveListener(final Board board) {
        return new Listener() {
            @Override
            public void handleEvent(Event event) {
                boolean solveGame = true;
                for (int defaultNumberOfHints : Constants.DEFAULT_NUMBER_OF_HINTS) {
                    if (defaultNumberOfHints == getNumOfHints()) {
                        userCommand = Constants.HINT;
                        solveGame = false;
                        break;
                    }
                }
                if (solveGame) {
                    userCommand = Constants.SOLVE;
                }
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

    private void setNumOfHints(int numOfHints) {
        this.numOfHints = numOfHints;
    }

    public int getSolveDepth() {
        return solveDepth;
    }

    private void setSolveDepth(int solveDepth) {
        this.solveDepth = solveDepth;
    }

    public boolean isRmiConnected() {
        return rmiConnected;
    }

    private void setRmiConnected(boolean rmiConnected) {
        this.rmiConnected = rmiConnected;
    }

    public String getRemoteServer() {
        return remoteServer;
    }

    private void setRemoteServer(String remoteServer) {
        this.remoteServer = remoteServer;
    }

    private void setConnectionStatusStyle(Label connectionStatus) {
        if (isRmiConnected()) {
            connectionStatus.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GREEN));
            connectionStatus.setText("Connected");
        } else {
            connectionStatus.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_RED));
            connectionStatus.setText("Disconnected");
        }
    }

    public void closeAll() {
        userCommand = Constants.CLOSETHREADS;
        setChanged();
        notifyObservers();
        Thread.currentThread().stop();
    }
}
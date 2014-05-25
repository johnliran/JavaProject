package view;

import java.util.ArrayList;
import java.util.Observable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import controller.Constants;

/**
 * Generic Window Shell
 */
public class WindowShell extends Observable {
    private Label score;
    private int userCommand;
    private int numOfHints;
    private int solveDepth;
    private boolean rmiConnected;
    private String remoteServer;
    private Label connectionStatus;
    private ArrayList<String> serversList;

    public WindowShell(String title, int width, int height, Display display, Shell shell, Board board) {
        shell.setText(title);
        shell.setSize(width, height);
        shell.setLayout(new GridLayout(2, false));
        shell.setBackground(new Color(display, Constants.BCOLOR_R, Constants.BCOLOR_G, Constants.BCOLOR_B));
        shell.setBackgroundMode(SWT.INHERIT_DEFAULT);
        shell.addListener(SWT.Close, exitListener());
        

        ((Composite) board).setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 8));

        // Initialize with default values
        serversList = new ArrayList<>();
        serversList.add(Constants.DEFAULT_SERVER);
        rmiConnected = false;
        setSolveDepth(Constants.SOLVE_DEPTHS_LIST[Constants.SOLVE_DEPTHS_LIST.length / 2]);
        setNumOfHints(Constants.NUMBER_OF_HINTS_LIST[0]);

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
        settingsLayout.verticalSpacing = 5;
        settingsComposite.setLayout(settingsLayout);
        createSettingsButtons(settingsComposite, board);
        settingsTab.setText("Settings");
        settingsTab.setControl(settingsForm);
    }

    private void createMenuBar(Shell parent, Board board) {
        Menu menuBar = new Menu(parent, SWT.BAR);
        Menu fileMenu = new Menu(menuBar);
        Menu editMenu = new Menu(menuBar);
        

        createMenuItem(menuBar, SWT.CASCADE, "File").setMenu(fileMenu);
        createMenuItem(menuBar, SWT.CASCADE, "Edit").setMenu(editMenu);
        
        createMenuItem(fileMenu, SWT.PUSH, "Load").addListener(SWT.Selection, loadListener(parent, board));
        createMenuItem(fileMenu, SWT.PUSH, "Save").addListener(SWT.Selection, saveListener(parent, board));
        createMenuItem(fileMenu, SWT.PUSH, "Exit").addListener(SWT.Selection, exitListener());
        createMenuItem(editMenu, SWT.PUSH, "Solve").addListener(SWT.Selection, solvePauseListener(board));
        createMenuItem(editMenu, SWT.PUSH, "Undo").addListener(SWT.Selection, undoListener(board));
        createMenuItem(editMenu, SWT.PUSH, "Reset").addListener(SWT.Selection, resetListener(board));
        
        parent.setMenuBar(menuBar);

    }

    private void createPlayButtons(Composite parent, Board board) {
        createButton(parent, "Solve", Constants.IMAGE_BUTTON_SOLVE).addListener(SWT.Selection, solvePauseListener(board));
        createButton(parent, "Undo", Constants.IMAGE_BUTTON_UNDO).addListener(SWT.Selection, undoListener(board));
        createButton(parent, "Reset", Constants.IMAGE_BUTTON_RESET).addListener(SWT.Selection, resetListener(board));
        createButton(parent, "Save", Constants.IMAGE_BUTTON_SAVE).addListener(SWT.Selection, saveListener(parent.getShell(), board));
        createButton(parent, "Load", Constants.IMAGE_BUTTON_LOAD).addListener(SWT.Selection, loadListener(parent.getShell(), board));
        score = createLabel(parent, SWT.CENTER, Constants.SCORE_FONT_SIZE, 0 + "         ");
    }

    private void createSettingsButtons(Composite parent, Board board) {
        /*
		 * Group solverGroup = new Group(parent, SWT.SHADOW_ETCHED_IN);
		 * solverGroup.setText("Solver"); solverGroup.setLayout(new
		 * GridLayout(1, false));
		 * 
		 * Group serverGroup = new Group(parent, SWT.SHADOW_ETCHED_IN);
		 * serverGroup.setText("Remote Server"); serverGroup.setLayout(new
		 * GridLayout(1, false));
		 */
        createLabel(parent, SWT.LEFT, Constants.DEFAULT_FONT_SIZE, "Number Of Hints");
        Combo numOfHintsCombo = new Combo(parent, SWT.READ_ONLY);
        numOfHintsCombo.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
        for (int possibleNumberOfHints : Constants.NUMBER_OF_HINTS_LIST) {
            numOfHintsCombo.add(Integer.toString(possibleNumberOfHints));
        }
        numOfHintsCombo.add("To Resolve");
        numOfHintsCombo.select(0);

        createLabel(parent, SWT.LEFT, Constants.DEFAULT_FONT_SIZE, "Solve Depth");
        Combo solveDepthCombo = new Combo(parent, SWT.READ_ONLY);
        solveDepthCombo.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
        for (int possibleSolveDepth : Constants.SOLVE_DEPTHS_LIST) {
            solveDepthCombo.add(Integer.toString(possibleSolveDepth));
        }
        solveDepthCombo.select(solveDepthCombo.getItemCount() / 2);

        createLabel(parent, SWT.LEFT, Constants.DEFAULT_FONT_SIZE, "Connect to Server");
		Combo connectToCombo = new Combo(parent, SWT.DROP_DOWN);
        connectToCombo.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
        for (String server : serversList) {
            connectToCombo.add(server);
        }
        connectToCombo.select(0);

        Button connect = createButton(parent, "Connect", Constants.IMAGE_BUTTON_CONNECT);
        
        numOfHintsCombo.addListener(SWT.Modify, numOfHintsListener(board));
        solveDepthCombo.addListener(SWT.Modify, solveDepthListener(board));
//        connectToCombo.addListener(SWT.FocusIn, connectToLitener(connect));
//        connectToCombo.addListener(SWT.Traverse, connectToLitener(connect));
        connect.addListener(SWT.Selection, connectListener(connectToCombo, board));

        connectionStatus = createLabel(parent, SWT.CENTER, Constants.DEFAULT_FONT_SIZE, "");
        setConnectionStatusStyle();
    }

    private Label createLabel(Composite parent, int labelAlignment, int fontSize, String text) {
        Label label = new Label(parent, labelAlignment);
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
    
    private MenuItem createMenuItem(Menu menuBar, int menuItemStyle, String text) {
    	MenuItem item = new MenuItem(menuBar, menuItemStyle);
        item.setText(text);

        return item;
    }

    private Listener numOfHintsListener(final Board board) {
        return new Listener() {
            @Override
            public void handleEvent(Event event) {
                boolean solveGame = true;
                for (int possibleNumberOfHints : Constants.NUMBER_OF_HINTS_LIST) {
                    if (Integer.toString(possibleNumberOfHints).equalsIgnoreCase(((Combo) event.widget).getText().toString())) {
                        setNumOfHints(possibleNumberOfHints);
                        solveGame = false;
                        break;
                    }
                }
                if (solveGame) {
                    setNumOfHints(Integer.MAX_VALUE);
                }
                ((Composite)board).forceFocus();
            }
        };
    }

    private Listener solveDepthListener(final Board board) {
        return new Listener() {
            @Override
            public void handleEvent(Event event) {
                for (int possibleSolveDepth : Constants.SOLVE_DEPTHS_LIST) {
                    if (Integer.toString(possibleSolveDepth).equalsIgnoreCase(
                            ((Combo) event.widget).getText().toString())) {
                        setSolveDepth(possibleSolveDepth);
                        break;
                    }
                }
                ((Composite)board).forceFocus();
            }
        };
    }
//
//    private Listener connectToLitener(final Button connect) {
//        return new Listener() {
//            @Override
//            public void handleEvent(Event event) {
//                switch (event.type) {
//                    case SWT.FocusIn:
//                        System.out.println("FocusIn");
//                        break;
//                    case SWT.Traverse:
//                        if (event.detail == SWT.TRAVERSE_RETURN) {
//                            System.out.println("Enter");
//                            connect.forceFocus();
//                        }
//                        break;
//                }
//            }
//        };
//    }

    private Listener connectListener(final Combo connectToCombo,final Board board) {
        return new Listener() {
            @Override
            public void handleEvent(Event event) {
            	String server = connectToCombo.getText();
            	setRemoteServer(server);
                userCommand = Constants.CONNECT;
                setChanged();
                notifyObservers();
                if (!serversList.contains(server) && isRmiConnected()) {
            		serversList.add(server);
            		connectToCombo.add(server);
            	}
                ((Composite)board).forceFocus();
                
            }
        };
    }

    private Listener solvePauseListener(final Board board) {
        return new Listener() {
            @Override
            public void handleEvent(Event event) {
                // Is the user connected to remote solver server? YES;
                if (isRmiConnected()) {
                    String text;
                    Boolean isButton = false;
                    if (event.widget.toString().contains("Button")) {
                        text = ((Button) event.widget).getText();
                        isButton = true;
                    } else
                        text = ((MenuItem) event.widget).getText();
                    switch (text) {
                        case "Solve":
                            boolean solveGame = true;
                            for (int possibleNumberOfHints : Constants.NUMBER_OF_HINTS_LIST) {
                                if (possibleNumberOfHints == getNumOfHints()) {
                                    userCommand = Constants.HINT;
                                    solveGame = false;
                                    break;
                                }
                            }
                            if (solveGame) {
                                userCommand = Constants.SOLVE;
                                if (isButton) {
                                    ((Button) event.widget).setText("Pause");
                                    ((Button) event.widget).setImage(new Image(Display.getCurrent(), Constants.IMAGE_BUTTON_PAUSE));
                                    
                                } else ((MenuItem) event.widget).setText("Pause");
                            }
                            break;
                        case "Pause":
                            userCommand = Constants.PAUSE;
                            if (isButton) {
                                ((Button) event.widget).setText("Solve");
                                ((Button) event.widget).setImage(new Image(Display.getCurrent(), Constants.IMAGE_BUTTON_SOLVE));
                            } else
                                ((MenuItem) event.widget).setText("Solve");
                            break;
                    }
                    setChanged();
                    notifyObservers();
                } else { // Is the user connected to remote solver server? NO;
                    displayErrorMessage(Constants.ERROR_SOLVE_WITHOUT_CONNECT);
                }
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

    private Listener exitListener() {
        return new Listener() {
            @Override
            public void handleEvent(Event event) {
                closeAll();
                Display.getCurrent().dispose();
                System.exit(0);
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
                }
                ((Composite) board).forceFocus();
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

    public void setRmiConnected(boolean rmiConnected) {
        this.rmiConnected = rmiConnected;
        setConnectionStatusStyle();
    }

    public String getRemoteServer() {
        return remoteServer;
    }

    private void setRemoteServer(String remoteServer) {
        this.remoteServer = remoteServer;
    }

    private void setConnectionStatusStyle() {
        Display.getCurrent().syncExec(new Runnable() {
            @Override
            public void run() {
                if (isRmiConnected()) {
                    connectionStatus.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GREEN));
                    connectionStatus.setText("Connected");
                } else {
                    connectionStatus.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_RED));
                    connectionStatus.setText("Disconnected");
                }
            }
        });
    }

    public void displayErrorMessage(final String errorMessage) {
        Display.getCurrent().syncExec(new Runnable() {
            @Override
            public void run() {
                int style = SWT.ICON_WORKING | SWT.OK;
                MessageBox messageBox = new MessageBox(new Shell(Display.getCurrent()), style);
                messageBox.setMessage(errorMessage);
                messageBox.open();
            }
        });
    }

    public void closeAll() {
        userCommand = Constants.CLOSETHREADS;
        setChanged();
        notifyObservers();
        Thread.currentThread().stop();
        try {
			Thread.currentThread().join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
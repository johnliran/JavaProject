package view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import java.util.Observable;

public class WindowShell extends Observable {
    private final static int RESET = 1;
    private final static int SAVE = 2;
    private final static int LOAD = 3;
    private final static int UNDO = 4;
    private Label score;
    private int userCommand;

    public WindowShell(String title, int width, int height, Display display, Shell shell, Board board) {
        shell.setSize(width, height);
        shell.setLayout(new GridLayout(2, false));
        Menu menu = new Menu(shell, SWT.BAR);
        final MenuItem file = new MenuItem(menu, SWT.CASCADE);
        file.setText("File");
        final Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
        file.setMenu(fileMenu);

        final MenuItem loadItem = new MenuItem(fileMenu, SWT.PUSH);
        final FileDialog loadDialog = new FileDialog(shell, SWT.OPEN);
        loadItem.setText("Load");
        loadItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                loadDialog.open();
                if (loadDialog.getFileName().length() > 0) {
                    String loadFrom = loadDialog.getFilterPath() + "/" + loadDialog.getFileName();
                    userCommand = LOAD;
                    setChanged();
                    notifyObservers(loadFrom);
                }
            }
        });

        final MenuItem saveItem = new MenuItem(fileMenu, SWT.PUSH);
        final FileDialog saveDialog = new FileDialog(shell, SWT.SAVE);
        saveItem.setText("Save");
        saveItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                saveDialog.open();
                if (saveDialog.getFileName().length() > 0) {
                    String saveTo = saveDialog.getFilterPath() + "/" + saveDialog.getFileName();
                    userCommand = SAVE;
                    setChanged();
                    notifyObservers(saveTo);
                }
            }
        });

        final MenuItem exitItem = new MenuItem(fileMenu, SWT.PUSH);
        exitItem.setText("Exit");
        final MenuItem edit = new MenuItem(menu, SWT.CASCADE);
        edit.setText("Edit");
        final Menu editMenu = new Menu(shell, SWT.DROP_DOWN);
        edit.setMenu(editMenu);
        final MenuItem undoItem = new MenuItem(editMenu, SWT.PUSH);
        undoItem.setText("Undo");
        undoItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                userCommand = UNDO;
                setChanged();
                notifyObservers();
            }
        });

        final MenuItem resetItem = new MenuItem(editMenu, SWT.PUSH);
        resetItem.setText("Reset");
        resetItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                userCommand = RESET;
                setChanged();
                notifyObservers();
            }
        });
        shell.setMenuBar(menu);

        Button undoButton = new Button(shell, SWT.PUSH);
        undoButton.setText("Undo");
        undoButton.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
        undoButton.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
                switch (event.type) {
                    case SWT.Selection: {
                        userCommand = UNDO;
                        setChanged();
                        notifyObservers();
                        break;
                    }
                }
            }
        });

        //Define the Board Widget and initialize it:
        ((Composite) board).setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 6));

        Button resetButton = new Button(shell, SWT.PUSH);
        resetButton.setText("Reset");
        resetButton.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
        resetButton.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
                switch (event.type) {
                    case SWT.Selection: {
                        userCommand = RESET;
                        setChanged();
                        notifyObservers();
                        break;
                    }
                }
            }
        });

        Button loadButton = new Button(shell, SWT.PUSH);
        loadButton.setText("Load");
        loadButton.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
        loadButton.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
                switch (event.type) {
                    case SWT.Selection: {
                        loadDialog.open();
                        if (loadDialog.getFileName().length() > 0) {
                            String loadFrom = loadDialog.getFilterPath() + "/" + loadDialog.getFileName();
                            userCommand = LOAD;
                            setChanged();
                            notifyObservers(loadFrom);
                        }
                        break;
                    }
                }
            }
        });

        Button saveButton = new Button(shell, SWT.PUSH);
        saveButton.setText("Save");
        saveButton.setBackground(new Color(display, 239, 143, 0));
        saveButton.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
        saveButton.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
                switch (event.type) {
                    case SWT.Selection: {
                        saveDialog.open();
                        if (saveDialog.getFileName().length() > 0) {
                            String saveTo = saveDialog.getFilterPath() + "/" + saveDialog.getFileName();
                            userCommand = SAVE;
                            setChanged();
                            notifyObservers(saveTo);
                        }
                        break;
                    }
                }
            }
        });

        //Set the styled label who contains the game score;
        score = new Label(shell, SWT.BORDER);
        score.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
        score.setForeground(new Color(display, 119, 110, 101));
        Font font = score.getFont();
        score.setFont(new Font(display, font.getFontData()[0].getName(), 24, SWT.BOLD));
        score.setText(0 + "");
    }

    public int getUserCommand() {
        return userCommand;
    }

    public void setScore(int score) {
        this.score.setText(score + "");
    }
}

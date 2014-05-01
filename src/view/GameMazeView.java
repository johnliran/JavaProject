package view;

import java.util.Observable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class GameMazeView extends Observable implements View, Runnable {
	private final static int RESET 		= 1;
    private final static int SAVE 		= 2;
    private final static int LOAD		= 3;
    private GameMazeBoard board;
	private Display display;
	private Label score;
	
	private Shell shell;
	private int userCommand;
	private boolean userNotified;

	private void initComponents() {
		display = new Display();
	    shell = new Shell(display);
	    shell.setSize(400, 300);
	    shell.setText("Maze");
	    shell.setLayout(new GridLayout(2, false));

	    Menu m = new Menu(shell, SWT.BAR);
	    
	    final MenuItem file = new MenuItem(m, SWT.CASCADE);
	    file.setText("File");
	    final Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
	    file.setMenu(fileMenu);
	    final MenuItem openItem = new MenuItem(fileMenu, SWT.PUSH);
	    openItem.setText("Load");
	    final MenuItem saveItem = new MenuItem(fileMenu, SWT.PUSH);
	    saveItem.setText("Save");
	    final MenuItem exitItem = new MenuItem(fileMenu, SWT.PUSH);
	    exitItem.setText("Exit");
	    
	    final MenuItem edit = new MenuItem(m, SWT.CASCADE);
	    edit.setText("Edit");
	    final Menu editMenu = new Menu(shell, SWT.DROP_DOWN);
	    edit.setMenu(editMenu);
	    final MenuItem undoItem = new MenuItem(editMenu, SWT.PUSH);
	    undoItem.setText("Undo");
	    final MenuItem resetItem = new MenuItem(editMenu, SWT.PUSH);
	    resetItem.setText("Reset");

	    shell.setMenuBar(m);
	    
	    
	    Button undoButton = new Button(shell, SWT.PUSH);
        undoButton.setText("Undo");
        undoButton.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
        
      //Define the Board Widget and initialize it:
        board = new GameMazeBoard(shell, SWT.NO_BACKGROUND);
        board.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 6));
        
        Button resetButton = new Button(shell, SWT.PUSH);
        resetButton.setText("Reset");
        resetButton.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
        
        Button loadButton = new Button(shell, SWT.PUSH);
        loadButton.setText("Load");
        loadButton.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
        
        Button saveButton = new Button(shell, SWT.PUSH);
        saveButton.setText("Save");
        saveButton.setBackground(new Color(display, 239,143,0));
        saveButton.setForeground(new Color(display, 239,143,0));
        saveButton.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true, 1, 1));
	    
	    
      

      

        //Set the styled label who contains the game score;
        score = new Label(shell, SWT.BORDER);
        score.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
        score.setForeground(new Color(display, 119, 110, 101));
        Font font = score.getFont();
        score.setFont(new Font(display, font.getFontData()[0].getName(), 24, SWT.BOLD));
        score.setText(0 + "");

        board.setFocus();

        
		shell.setBackground(new Color(display, 187, 173, 160));
		
		board.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent event) {
			}

			@Override
			public void keyPressed(KeyEvent event) {
					userCommand = event.keyCode;
					setChanged();
					notifyObservers();

			}
			
//			
//
//            // Commented until we will have a buttons listener
//
//            /* @Override
//			public void keyPressed(KeyEvent event) {
//				if ((event.keyCode == SWT.ARROW_DOWN) ||
//				    (event.keyCode == SWT.ARROW_LEFT) ||
//					(event.keyCode == SWT.ARROW_UP)   ||
//					(event.keyCode == SWT.ARROW_RIGHT)) {
//					userCommand = event.keyCode;
//					setChanged();
//					notifyObservers();
//				}
//			}*/
		});
		shell.open();
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
		System.out.println("Print");
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

	public GameMazeView() {
		initComponents();
		userNotified = false;
	}

	@Override
	public void displayScore(int score) {
		this.score.setText(score + "");
	}

    @Override
    public void gameWon() {
    		userNotified = true;
    		int style = SWT.ICON_WORKING |SWT.YES | SWT.NO;
    	    MessageBox messageBox = new MessageBox(shell, style);
    	    messageBox.setMessage("You won! Do you want to continue?");
    	    int rc = messageBox.open();

    	    switch (rc) {
    	    	case SWT.YES: {
    	    		userCommand = RESET;
    	    		userNotified = false;
    	    		System.out.println("im in yes");
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
		int style = SWT.ICON_WORKING |SWT.YES | SWT.NO;
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
}

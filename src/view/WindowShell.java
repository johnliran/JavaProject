package view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

public class WindowShell {
	Display display;
	Shell shell;
	Board2048 board;
	Label score;
	
	public Label getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score.setText(score + "");
	}

	public Display getDisplay() {
		return display;
	}

	public void setDisplay(Display display) {
		this.display = display;
	}

	public Shell getShell() {
		return shell;
	}

	public void setShell(Shell shell) {
		this.shell = shell;
	}

	public Board2048 getBoard() {
		return board;
	}

	public void setBoard(Board2048 board) {
		this.board = board;
	}

	public WindowShell(int width,int height,String title) {
		    display = new Display();
		    shell = new Shell(display);
		    shell.setSize(width, height);
		    shell.setText(title);
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
	        board = new Board2048(shell, SWT.NO_BACKGROUND);
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

		    

		    
	}
	
	
}

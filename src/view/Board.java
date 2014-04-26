package view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;





public class Board extends Composite {
	private final static int BOARDSIZE = 4;
	int boardSize;
	int[][] boardData ;
	Tile[][] tiles;
	
	
	

	public void setBoardData(int[][] board) {
		this.boardData = board.clone();
		for (int row = 0; row < boardSize; row++) {
			for (int column = 0; column < boardSize; column++) {
				tiles[row][column].setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
				tiles[row][column].setValue(boardData[row][column]);
			}
		}
	}

	public Board(Composite parent,int style) {
		super(parent,style);		
		boardSize = BOARDSIZE;
		boardData = new int[boardSize][boardSize];
		setBackground(new Color(getDisplay(), 187, 173, 160));
		setLayout(new GridLayout(boardSize,true));
		
		tiles = new Tile[boardSize][boardSize];
		for (int row = 0; row < boardSize; row++) {
			for (int column = 0; column < boardSize; column++) {
				tiles[row][column] = new Tile(this,SWT.NO_BACKGROUND);
				tiles[row][column].setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
				
				tiles[row][column].setValue(boardData[row][column]);
			}
		}
	}
}

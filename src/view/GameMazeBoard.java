package view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class GameMazeBoard extends Composite {
    private final static int BOARDSIZE = 15;
    private int[][] board;
    TileMaze[][] tiles;

    public void setBoardData(int[][] board) {
        this.board = board;
        for (int row = 0; row < this.board.length; row++) {
            for (int column = 0; column < this.board.length; column++) {
                tiles[row][column].setValue(this.board[row][column]);
            }
        }
    }

    public GameMazeBoard(Composite parent, int style) {
        super(parent, style);
        this.board = new int[BOARDSIZE][BOARDSIZE];
        setBackground(new Color(getDisplay(), 187, 173, 160));
//        setLayout(new GridLayout(this.board.length, true));
        GridLayout layout = new GridLayout();
        layout.numColumns = BOARDSIZE;
        layout.makeColumnsEqualWidth=true;
        layout.horizontalSpacing=1;
        layout.verticalSpacing = 1;
        setLayout(layout);
        tiles = new TileMaze[this.board.length][this.board.length];
        for (int row = 0; row < this.board.length; row++) {
            for (int column = 0; column < this.board.length; column++) {
                tiles[row][column] = new TileMaze(this, SWT.NO_BACKGROUND);
                tiles[row][column].setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
                tiles[row][column].setValue(this.board[row][column]);
            }
        }
    }
}

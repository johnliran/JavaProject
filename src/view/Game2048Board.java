package view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class Game2048Board extends Composite {
    private final static int BOARDSIZE = 4;
    private int[][] board;
    Tile2048[][] tiles;

    public void setBoardData(int[][] board) {
        this.board = board;
        for (int row = 0; row < this.board.length; row++) {
            for (int column = 0; column < this.board.length; column++) {
                tiles[row][column].setValue(this.board[row][column]);
            }
        }
    }

    public Game2048Board(Composite parent, int style) {
        super(parent, style);
        this.board = new int[BOARDSIZE][BOARDSIZE];
        setBackground(new Color(getDisplay(), 187, 173, 160));
        setLayout(new GridLayout(this.board.length, true));
        tiles = new Tile2048[this.board.length][this.board.length];
        for (int row = 0; row < this.board.length; row++) {
            for (int column = 0; column < this.board.length; column++) {
                tiles[row][column] = new Tile2048(this, SWT.NO_BACKGROUND);
                tiles[row][column].setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
                tiles[row][column].setValue(this.board[row][column]);
            }
        }
    }
}

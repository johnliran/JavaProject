package view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

import sun.java2d.loops.DrawRect;

/**
 * Maze Board
 */
public class GameMazeBoard extends Composite implements Board {
    private int[][] board;
    TileMaze[][] tiles;

    public void setBoardData(int[][] board) {
        this.board = board;
        for (int row = 0; row < this.board.length; row++) {
            for (int column = 0; column < this.board[0].length; column++) {
                tiles[row][column].setValue(this.board[row][column]);
            }
        }
    }

    public GameMazeBoard(Composite parent, int style, int height, int width, MouseCommand mouseCommand) {
        super(parent, style);
        this.board = new int[height][width];
        setBackground(new Color(getDisplay(), 187, 173, 160));
        GridLayout layout = new GridLayout();
        layout.numColumns = width;
        layout.makeColumnsEqualWidth = false;
        layout.horizontalSpacing = 1;
        layout.verticalSpacing = 1;
        setLayout(layout);
        tiles = new TileMaze[this.board.length][this.board[0].length];
        for (int row = 0; row < this.board.length; row++) {
            for (int column = 0; column < this.board[0].length; column++) {
                tiles[row][column] = new TileMaze(this, SWT.NONE, mouseCommand);
                tiles[row][column].setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
                tiles[row][column].setValue(this.board[row][column]);
            }
        }
    }
}

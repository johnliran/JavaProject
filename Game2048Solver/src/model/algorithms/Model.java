package model.algorithms;

import java.util.ArrayList;
import java.util.List;

/**
 * Model
 */
public interface Model {

    public void initialize();

    public boolean moveUp(boolean simulate);

    public boolean moveDown(boolean simulate);

    public boolean moveRight(boolean simulate);

    public boolean moveLeft(boolean simulate);

    public int[][] getData();

    public int getScore();

    public boolean isGameWon();

    public boolean isGameOver();

    public void setGameWon(boolean gameWon);

    public int getNumOfFreeStates();
    
    public void generate();
	
	public List<Integer> getEmptyCellIds();
	
	public void setEmptyCell(int i, int j, int value);

	public Object clone() throws CloneNotSupportedException;
}

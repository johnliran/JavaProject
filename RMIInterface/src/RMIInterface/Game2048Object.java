package RMIInterface;

import java.io.Serializable;
import java.util.Stack;

public class Game2048Object implements Serializable{
	public static final long serialVersionUID = 1L;
	public int[][] board;
    public int score;
    public boolean gameWon;
    public boolean gameOver;
    
	

}

package model;

import java.io.Serializable;
import java.util.Stack;

public class Game2048Object implements Serializable {
	public static final long serialVersionUID = 1L;
	public int[][] board;
    public int score;
    public boolean gameWon;
    public boolean gameOver;
    
	public Game2048Object(Game2048Model game) {
		this.board = game.getData();
		this.score = game.getScore();
		this.gameWon = game.isGameWon();
		this.gameOver = game.isGameOver();	
	}
}

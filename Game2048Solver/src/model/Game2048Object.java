package model;

import java.io.Serializable;
import java.util.Stack;

public class Game2048Object implements Serializable {
	private static final long serialVersionUID = 1L;
	private int[][] board;
    private int score;
    private boolean gameWon;
    private boolean gameOver;
    
	public Game2048Object(Game2048Model game) {
		this.board = game.getData();
		this.score = game.getScore();
		this.gameWon = game.isGameWon();
		this.gameOver = game.isGameOver();	
	}

	public int[][] getBoard() {
		return board;
	}

	public void setBoard(int[][] board) {
		this.board = board;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public boolean isGameWon() {
		return gameWon;
	}

	public void setGameWon(boolean gameWon) {
		this.gameWon = gameWon;
	}

	public boolean isGameOver() {
		return gameOver;
	}

	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}
}

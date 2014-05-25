package model;

import java.io.Serializable;
import java.util.Stack;
import model.algorithms.*;

public class GameMazeObject implements Serializable{
	private static final long serialVersionUID = 1L;
	private int[][] maze;
    private GameMazeState state;
    private int score;
	
    
	public GameMazeObject(GameMazeModel game ) {
		this.maze = new int[game.getData().length][game.getData().length];
		this.state = new GameMazeState();
		this.maze = game.getData();
		this.state = game.getStartState();
		this.score = game.getScore();
	}


	public int[][] getData() {
		return maze;
	}


	public void setData(int[][] maze) {
		this.maze = maze;
	}


	public GameMazeState getState() {
		return state;
	}


	public void setState(GameMazeState state) {
		this.state = state;
	}


	public int getScore() {
		return score;
	}


	public void setScore(int score) {
		this.score = score;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
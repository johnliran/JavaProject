package model;

import org.eclipse.swt.graphics.Point;

import java.util.*;

public class Game2048Model extends Observable implements Model {
	private final static int BOARDSIZE = 4;
	private final static int TARGETSCORE = 32;
	private final static int RIGHT = 1;
	private final static int LEFT = 2;
	private int[][] board;
	private int score;
	private boolean gameWon;
	private boolean gameOver;
	private Stack<int[][]> previousBoards;
	private Stack<Integer> previousScores;

	public Game2048Model() {
		this.board = new int[BOARDSIZE][BOARDSIZE];
		this.score = 0;
		this.previousBoards = new Stack<int[][]>();
		this.previousScores = new Stack<Integer>();
		this.gameWon = false;
		this.gameOver = false;
	}

	public void rotate(int direction) {
		int[][] newBoard = new int[board.length][board.length];
		switch (direction) {
		case RIGHT: {
			for (int row = 0; row < newBoard.length; row++) {
				for (int column = 0; column < newBoard.length; column++) {
					newBoard[column][(newBoard.length - 1) - row] = board[row][column];
				}
			}
			break;
		}

		case LEFT: {
			for (int row = 0; row < newBoard.length; row++) {
				for (int column = 0; column < newBoard.length; column++) {
					newBoard[(newBoard.length - 1) - column][row] = board[row][column];
				}
			}
			break;
		}

		default:
			break;
		}
		setData(newBoard);
	}

	public boolean move(boolean simulate) {
		int[][] newBoard = new int[board.length][board.length];
		//We use linkedlist to organize all the cells which have numbers
		LinkedList<Integer> numbers = new LinkedList<Integer>();
		boolean moved = false;
		boolean seenZero;
		//Get over all the board and take out the numbers into List.
		for (int row = 0; row < newBoard.length; row++) {
			seenZero = false;
			for (int column = 0; column < newBoard.length; column++) {
				if (board[row][column] != 0) {
					numbers.add(board[row][column]);
					//After putting the numbers into the stack,We can override and pad the line with 0;
					if (!simulate) {
						newBoard[row][column] = 0;
					}
					if (seenZero) {
						moved = true;
					}
				} else {
					seenZero = true;
				}
			}
			//Merge if there are equal numbers
			for (int column = 0; column < newBoard.length && !numbers.isEmpty(); column++) {
				int numberToCheck = numbers.poll();
				if (!numbers.isEmpty()) {
					if (numberToCheck == numbers.peek()) {
						numberToCheck += numbers.poll();
						moved = true;
						if (!simulate) {
							setScore(getScore() + numberToCheck);
							if (numberToCheck == TARGETSCORE && !isGameWon()) {
								setGameWon(true);
								notifyObservers();
							}
						} else return moved;
					}
				}
				if (!simulate) {
					newBoard[row][column] = numberToCheck;
				}
			}
		}
		if (moved && !simulate) {
			setData(newBoard);
			generate();
		}
		return moved;
	}

	@Override
	public boolean moveUp(boolean simulate) {
		if (!simulate) {
			save();
		}
		rotate(LEFT);
		boolean moved = move(simulate);
		if (!simulate && !moved) {
			delete();
		}
		rotate(RIGHT);
		if (!simulate) {
			setChanged();
			notifyObservers();
		}
		return moved;
	}

	@Override
	public boolean moveDown(boolean simulate) {
		if (!simulate) {
			save();
		}
		rotate(RIGHT);
		boolean moved = move(simulate);
		if (!simulate && !moved) {
			delete();
		}
		rotate(LEFT);
		if (!simulate) {
			setChanged();
			notifyObservers();
		}
		return moved;
	}

	@Override
	public boolean moveRight(boolean simulate) {
		if (!simulate) {
			save();
		}
		rotate(LEFT);
		rotate(LEFT);
		boolean moved = move(simulate);
		if (!simulate && !moved) {
			delete();
		}
		rotate(RIGHT);
		rotate(RIGHT);
		if (!simulate) {
			setChanged();
			notifyObservers();
		}
		return moved;
	}

	@Override
	public boolean moveLeft(boolean simulate) {
		if (!simulate) {
			save();
		}
		boolean moved = move(simulate);
		if (!simulate && !moved) {
			delete();
		}
		if (!simulate) {
			setChanged();
			notifyObservers();
		}
		return moved;
	}

	@Override
	public int[][] getData() {
		return board;
	}

	public void setData(int[][] data) {
		this.board = data;
	}

	@Override
	public void initialize() {
		this.score = 0;
		this.previousBoards = new Stack<int[][]>();
		this.previousScores = new Stack<Integer>();
		this.gameWon = false;
		this.gameOver = false;

		for (int i = 0; i < board.length; ++i) {
			for (int j = 0; j < board[0].length; ++j) {
				board[i][j] = 0;
			}
		}
		generate();
		generate();
		setChanged();
		notifyObservers();
	}

	@Override
	public void restore() {
		if (!previousBoards.isEmpty()) {
			setData(previousBoards.pop());
			setScore(previousScores.pop());
			setChanged();
			notifyObservers();
		}
	}

	public void save() {
		previousBoards.push(board);
		previousScores.push(score);
	}

	public void delete() {
		previousBoards.pop();
		previousScores.pop();
	}

	public ArrayList<State> getFreeStates() {
		ArrayList<State> freeStates = new ArrayList<State>();
		for (int row = 0; row < board.length; row++) {
			for (int column = 0; column < board.length; column++) {
				if (board[row][column] == 0) {
					freeStates.add(new State(new Point(row, column)));
				}
			}
		}
		return freeStates;
	}

	public int generateValue() {
		return (Math.random() < 0.9) ? 2 : 4;
	}

	public void generate() {
		ArrayList<State> freeStates = getFreeStates();
		if (freeStates.size() > 0) {
			int index = new Random().nextInt(freeStates.size());
			Point point = (Point) (freeStates.get(index).getState());
			int row = point.x;
			int column = point.y;
			board[row][column] = generateValue();
			if (freeStates.size() == 1) {
				if (!(moveUp(true) || moveDown(true) || moveLeft(true) || moveRight(true))) {
					setGameOver(true);
					notifyObservers();
				}
			}
		}
	}

	@Override
	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	@Override
	public boolean isGameWon() {
		return gameWon;
	}
	@Override
	public void setGameWon(boolean gameWon) {
		this.gameWon = gameWon;
	}

	@Override
	public boolean isGameOver() {
		return gameOver;
	}

	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}
}
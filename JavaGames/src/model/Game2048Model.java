package model;

import java.beans.Transient;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.Stack;
import java.util.Vector;

import model.algorithms.Model;
import model.algorithms.Serializer;
import model.algorithms.State;

import org.eclipse.swt.graphics.Point;

import RMIInterface.RemoteInterface;
import RMIInterface.RMIConstants;
import controller.Constants;

/**
 * Game 2048 Model
 */
public class Game2048Model extends Observable implements Model{
	 
	private static final long serialVersionUID = 1L;
	private int[][] board;
    private int score;
    private boolean gameWon;
    private boolean gameOver;
    private Stack<int[][]> previousBoards;
    private Stack<Integer> previousScores;
    
    public Game2048Model() {
    	
        this.board = new int[Constants.BOARDSIZE][Constants.BOARDSIZE];
        this.previousBoards = new Stack<int[][]>();
        this.previousScores = new Stack<Integer>();
    }

    public void rotate(int direction) {
        int[][] newBoard = new int[board.length][board.length];
        switch (direction) {
            case Constants.RIGHT: {
                for (int row = 0; row < newBoard.length; row++) {
                    for (int column = 0; column < newBoard.length; column++) {
                        newBoard[column][(newBoard.length - 1) - row] = board[row][column];
                    }
                }
                break;
            }

            case Constants.LEFT: {
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
        // We use linkedlist to organize all the cells which have numbers
        LinkedList<Integer> numbers = new LinkedList<Integer>();
        boolean moved = false;
        boolean seenZero;
        // Get over all the board and take out the numbers into List
        for (int row = 0; row < newBoard.length; row++) {
            seenZero = false;
            for (int column = 0; column < newBoard.length; column++) {
                if (board[row][column] != 0) {
                    numbers.add(board[row][column]);
                    // After putting the numbers into the stack,We can override and pad the line with 0
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
            // Merge if there are equal numbers
            for (int column = 0; column < newBoard.length && !numbers.isEmpty(); column++) {
                int numberToCheck = numbers.poll();
                if (!numbers.isEmpty()) {
                    if (numberToCheck == numbers.peek()) {
                        numberToCheck += numbers.poll();
                        moved = true;
                        if (!simulate) {
                            setScore(getScore() + numberToCheck);
                            if (numberToCheck == Constants.TARGETSCORE && !isGameWon()) {
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

    /**
     * @param simulate Specify whether or not to make changes
     * @return True: Movement was made / False: No movement was made
     */
    @Override
    public boolean moveUp(boolean simulate) {
        if (!simulate) {
            backup();
        }
        rotate(Constants.LEFT);
        boolean moved = move(simulate);
        if (!simulate && !moved) {
            delete();
        }
        rotate(Constants.RIGHT);
        if (!simulate) {
            setChanged();
            notifyObservers();
        }
        return moved;
    }

    /**
     * @param simulate Specify whether or not to make changes
     * @return True: Movement was made / False: No movement was made
     */
    @Override
    public boolean moveDown(boolean simulate) {
        if (!simulate) {
            backup();
        }
        rotate(Constants.RIGHT);
        boolean moved = move(simulate);
        if (!simulate && !moved) {
            delete();
        }
        rotate(Constants.LEFT);
        if (!simulate) {
            setChanged();
            notifyObservers();
        }
        return moved;
    }

    /**
     * @param simulate Specify whether or not to make changes
     * @return True: Movement was made / False: No movement was made
     */
    @Override
    public boolean moveRight(boolean simulate) {
        if (!simulate) {
            backup();
        }
        rotate(Constants.LEFT);
        rotate(Constants.LEFT);
        boolean moved = move(simulate);
        if (!simulate && !moved) {
            delete();
        }
        rotate(Constants.RIGHT);
        rotate(Constants.RIGHT);
        if (!simulate) {
            setChanged();
            notifyObservers();
        }
        return moved;
    }

    /**
     * @param simulate Specify whether or not to make changes
     * @return True: Movement was made / False: No movement was made
     */
    @Override
    public boolean moveLeft(boolean simulate) {
        if (!simulate) {
            backup();
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

    /**
     * @return Board data
     */
    @Override
    public int[][] getData() {
        return board;
    }

    public void setData(int[][] data) {
        this.board = data;
    }

    /**
     * Initialize the model's data members when a new game starts
     */
    @Override
    public void initialize() {
        this.score = 0;
        this.previousBoards.clear();
        this.previousScores.clear();
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

    /**
     * Restores the player's last movement
     */
    @Override
    public void restore() {
        if (!previousBoards.isEmpty()) {
            setData(previousBoards.pop());
            setScore(previousScores.pop());
            setChanged();
            notifyObservers();
        }
    }

    public void backup() {
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
                    State free = new State();
                    free.setState(new Point(row, column));
                    freeStates.add(free);
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

    /**
     * @return Game score
     */
    @Override
    public int getScore() {
        return score;
    }

    /**
     * @param score Game score
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * @return True: Game Won
     */
    @Override
    public boolean isGameWon() {
        return gameWon;
    }

    /**
     * @param gameWon True: Game Won
     */
    @Override
    public void setGameWon(boolean gameWon) {
        this.gameWon = gameWon;
    }

    /**
     * @param xmlFileName Output file name
     */
    @Override
    public void saveGame(String xmlFileName) {
        try {
            Serializer.serializeToXML(this, xmlFileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param xmlFileName Input file name
     */
    @Override
    public void loadGame(String xmlFileName) {
        try {
            setData(((Game2048Model) Serializer.deserializeXML(xmlFileName)).getData());
            setScore(((Game2048Model) Serializer.deserializeXML(xmlFileName)).getScore());
        } catch (Exception e) {
            e.printStackTrace();
        }
        setChanged();
        notifyObservers();
    }

    /**
     * @return True: Game Over
     */
    @Override
    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    /**
     * @param simulate Specify whether or not to make changes
     * @return True: Movement was made / False: No movement was made
     */
    @Override
    public boolean moveUpRight(boolean simulate) {
        return false;
    }

    /**
     * @param simulate Specify whether or not to make changes
     * @return True: Movement was made / False: No movement was made
     */
    @Override
    public boolean moveUpLeft(boolean simulate) {
        return false;
    }

    /**
     * @param simulate Specify whether or not to make changes
     * @return True: Movement was made / False: No movement was made
     */
    @Override
    public boolean moveDownRight(boolean simulate) {
        return false;
    }

    /**
     * @param simulate Specify whether or not to make changes
     * @return True: Movement was made / False: No movement was made
     */
    @Override
    public boolean moveDownLeft(boolean simulate) {
        return false;
    }
    
    @Override
	 public int getHint() throws CloneNotSupportedException, RemoteException, NotBoundException {
    	Registry registry = LocateRegistry.getRegistry("localhost",RMIConstants.PORT);
    	RemoteInterface remote = (RemoteInterface) registry.lookup(RMIConstants.RMI_ID);
    	Game2048Object myGame = new Game2048Object(this);
    	int hint = 0;
		try {		
			hint = remote.getHint((Object)myGame);
		} catch (Exception e) {
			e.printStackTrace();
		}
//			 hint = AIsolver.findBestMove(this, 5);
			 switch (hint) {
			 case Constants.UP:
				 moveUp(false);
				 break;
			case Constants.DOWN:
				moveDown(false);
				break;
			case Constants.LEFT:
				moveLeft(false);
				break;
			case Constants.RIGHT:
				moveRight(false);
				break;
	
			default:
				break;
			 }
    	return hint;
	 }
    
    @Override
    public void solveGame() throws RemoteException, CloneNotSupportedException, NotBoundException{
    	while (!isGameWon())
    		getHint();
    }
    
    @Override
    public boolean equals(Object object) {
    	return true;
    }
}
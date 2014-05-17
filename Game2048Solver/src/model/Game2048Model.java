 package model;

import controller.Constants;
import model.algorithms.AIsolver;
import model.algorithms.Model;
import model.algorithms.Serializer;
import model.algorithms.State;

import org.eclipse.swt.graphics.Point;

import java.util.*;

/**
 * Game 2048 Model
 */
public class Game2048Model implements Cloneable, Model, Constants {
	public static final long serialVersionUID = 1L;
	public int[][] board;
    public int score;
    public boolean gameWon;
    public boolean gameOver;
//    private Stack<int[][]> previousBoards;
//    private Stack<Integer> previousScores;

    public Game2048Model() {
        this.board = new int[BOARDSIZE][BOARDSIZE];
//        this.previousBoards = new Stack<int[][]>();
//        this.previousScores = new Stack<Integer>();
    }
    
    public Game2048Model(Game2048Object gameObject) {
    	this.board = gameObject.board;
    	this.score = gameObject.score;
    	this.gameWon = gameObject.gameWon;
    	this.gameOver = gameObject.gameOver;
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
                            if (numberToCheck == TARGETSCORE && !isGameWon()) {
                                setGameWon(true);
//                                notifyObservers();
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
//            generate();
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
        rotate(LEFT);
        boolean moved = move(simulate);
        if (!simulate && !moved) {
            delete();
        }
        rotate(RIGHT);
        if (!simulate) {
//            setChanged();
//            notifyObservers();
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
        rotate(RIGHT);
        boolean moved = move(simulate);
        if (!simulate && !moved) {
            delete();
        }
        rotate(LEFT);
        if (!simulate) {
//            setChanged();
//            notifyObservers();
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
        rotate(LEFT);
        rotate(LEFT);
        boolean moved = move(simulate);
        if (!simulate && !moved) {
            delete();
        }
        rotate(RIGHT);
        rotate(RIGHT);
        if (!simulate) {
//            setChanged();
//            notifyObservers();
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
//            setChanged();
//            notifyObservers();
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
//        this.previousBoards.clear();
//        this.previousScores.clear();
        this.gameWon = false;
        this.gameOver = false;

        for (int i = 0; i < board.length; ++i) {
            for (int j = 0; j < board[0].length; ++j) {
                board[i][j] = 0;
            }
        }
        generate();
        generate();
        
    }

    /**
     * Restores the player's last movement
     */
    @Override
    public void restore() {
//        if (!previousBoards.isEmpty()) {
//            setData(previousBoards.pop());
//            setScore(previousScores.pop());
//            setChanged();
//            notifyObservers();
//        }
    }

    public void backup() {
//        previousBoards.push(board);
//        previousScores.push(score);
    }

    public void delete() {
//        previousBoards.pop();
//        previousScores.pop();
    }
    
//    @Override
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
    
    @Override
    public int getNumOfFreeStates() {
        int numOfFreeStates = 0;
    	for (int row = 0; row < board.length; row++) {
            for (int column = 0; column < board.length; column++) {
                if (board[row][column] == 0) {
                    numOfFreeStates++;
                }
            }
        }
        return numOfFreeStates;
    }

    public int generateValue() {
        return (Math.random() < 0.9) ? 2 : 4;
    }
//    @Override
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
//                    notifyObservers();
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
//        setChanged();
//        notifyObservers();
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
    
    private int[][] copyOf(int[][] array) {
        int newArray[][] = new int[array.length][array[0].length];
        for (int row = 0; row < array.length; row++) {
            for (int column = 0; column < array[0].length; column++) {
                newArray[row][column] = array[row][column];
            }
        }
        return newArray;
    }
    
	@Override
	public List<Integer> getEmptyCellIds() {
        List<Integer> cellList = new ArrayList<>();
        
        for(int i=0;i<BOARDSIZE;++i) {
            for(int j=0;j<BOARDSIZE;++j) {
                if(board[i][j]==0) {
                    cellList.add(BOARDSIZE*i+j);
                }
            }
        }
        
        return cellList;
    }
	
	@Override
	  public void setEmptyCell(int i, int j, int value) {
        if(board[i][j]==0) {
            board[i][j]=value;
        }
    }
	
	private int[][] clone2dArray(int[][] original) { 
        int[][] copy = new int[original.length][];
        for(int i=0;i<original.length;++i) {
            copy[i] = original[i].clone();
        }
        return copy;
    }
	
	 @Override
	    public Object clone() throws CloneNotSupportedException {
	        Game2048Model copy = (Game2048Model)super.clone();
	        copy.setData(clone2dArray(board));
	        return copy;
	    }
	 
	 
//
//	@Override
//	public void run() {
//	}
}
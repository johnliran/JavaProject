package model;

import org.eclipse.swt.graphics.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Random;

public class Game2048Model extends Observable implements Model {
    private final static int TARGETSCORE = 32;
    private final static int RIGHT = 1;
    private final static int LEFT = 2;
    private int[][] board;
    private int score;
    private boolean win;
    private boolean loose;

    public Game2048Model() {
        this.board = new int[4][4];
        this.score = 0;
        this.win = false;
        this.loose = false;
    }

    public void rotate(int direction) {
        int[][] newBoard = new int[board.length][board.length];
        switch (direction) {
            case RIGHT: {
                for (int row = 0; row < board.length; row++) {
                    for (int column = 0; column < board.length; column++) {
                        newBoard[column][(board.length - 1) - row] = board[row][column];
                    }
                }
                break;
            }

            case LEFT: {
                for (int row = 0; row < board.length; row++) {
                    for (int column = 0; column < board.length; column++) {
                        newBoard[(board.length - 1) - column][row] = board[row][column];
                    }
                }
                break;
            }

            default:
                break;
        }
        board = newBoard.clone();
    }

    public void move() {
        //We use linkedlist to organize all the cells which have numbers
        LinkedList<Integer> numbers = new LinkedList<Integer>();
        boolean moved = false;
        boolean seenZero;
        //Get over all the board and take out the numbers into List.
        for (int row = 0; row < board.length; row++) {
            seenZero = false;
            for (int column = 0; column < board.length; column++) {
                if (board[row][column] != 0) {
                    numbers.add(board[row][column]);
                    //After putting the numbers into the stack,We can override and pad the line with 0;
                    board[row][column] = 0;
                    if (seenZero)
                        moved = true;
                } else
                    seenZero = true;
            }
            //Merge if there are equal numbers
            for (int column = 0; column < board.length && !numbers.isEmpty(); column++) {
                int numberToCheck = numbers.poll();
                if (!numbers.isEmpty())
                    if (numberToCheck == numbers.peek()) {
                        numberToCheck += numbers.poll();
                        setScore(getScore() + numberToCheck);
                        if (numberToCheck == TARGETSCORE && !isWin()) {
                            setWin(true);
                            System.out.println("WIN");
                        }
                        moved = true;
                    }
                board[row][column] = numberToCheck;
            }
        }
        if (moved) {
            generate();
        }
    }

    @Override
    public void moveUp() {
        rotate(LEFT);
        move();
        rotate(RIGHT);
        setChanged();
        notifyObservers();

    }

    @Override
    public void moveDown() {
        rotate(RIGHT);
        move();
        rotate(LEFT);
        setChanged();
        notifyObservers();
    }

    @Override
    public void moveRight() {
        rotate(LEFT);
        rotate(LEFT);
        move();
        rotate(RIGHT);
        rotate(RIGHT);
        setChanged();
        notifyObservers();
    }

    @Override
    public void moveLeft() {
        move();
        setChanged();
        notifyObservers();
    }


    @Override
    public int[][] getData() {
        return board;
    }

    @Override
    public void initializeBoard() {
        for (int i = 0; i < board.length; ++i) {
            for (int j = 0; j < board[0].length; ++j) {
                board[i][j] = 0;
            }
        }
        generate();
        setChanged();
        notifyObservers();
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
             //   setLoose(!hasEqualNeighbour(row, column));
                if (isLoose()) {
                    System.out.println("LOOSE");
                    // setChanged();
                    // notifyObservers();
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

    public boolean isWin() {
        return win;
    }

    public void setWin(boolean win) {
        this.win = win;
    }

    public boolean isLoose() {
        return loose;
    }

    public void setLoose(boolean loose) {
        this.loose = loose;
    }
}


	










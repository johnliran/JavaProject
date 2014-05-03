package model;

import model.algorithms.*;

import org.eclipse.swt.graphics.Point;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Stack;

public class GameMazeModel extends Observable implements Model {
    private final int MOUSE = 1;
    private final int WALL = -1;
    private final int BLANK = 0;
    private final int MOUSE_RIGHT = 1;
    private final int MOUSE_UP = 2;
    private final int MOUSE_DOWN = 3;
    private final int MOUSE_LEFT = 4;
    private final int CHEESE = 5;
    private final int MOUSE_AND_CHEESE = 6;
    private int mouseDirection;
    private int[][] maze;
    private State state;
    private int score;
    private int numberOfMoves;
    private int minNumberOfMoves;
    private boolean gameWon;
    private boolean gameOver;
    private Stack<int[][]> previousBoards;
    private Stack<Integer> previousScores;
    private Serializer s;
    private int[][] initialMaze = {
            {WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL},
            {WALL, BLANK, BLANK, BLANK, WALL, BLANK, BLANK, BLANK, BLANK, BLANK, BLANK, BLANK, BLANK, BLANK, WALL},
            {WALL, BLANK, WALL, BLANK, WALL, BLANK, WALL, WALL, BLANK, WALL, WALL, BLANK, WALL, BLANK, WALL},
            {WALL, BLANK, WALL, BLANK, WALL, BLANK, BLANK, WALL, BLANK, WALL, BLANK, BLANK, WALL, BLANK, WALL},
            {WALL, BLANK, WALL, WALL, WALL, WALL, WALL, WALL, BLANK, WALL, WALL, BLANK, WALL, WALL, WALL},
            {WALL, BLANK, BLANK, BLANK, BLANK, BLANK, BLANK, BLANK, BLANK, BLANK, WALL, BLANK, WALL, BLANK, WALL},
            {WALL, BLANK, WALL, BLANK, WALL, WALL, WALL, WALL, WALL, WALL, WALL, BLANK, WALL, BLANK, WALL},
            {WALL, BLANK, WALL, BLANK, WALL, BLANK, BLANK, BLANK, BLANK, BLANK, WALL, BLANK, BLANK, BLANK, CHEESE},
            {WALL, BLANK, WALL, WALL, WALL, BLANK, WALL, BLANK, WALL, BLANK, WALL, BLANK, WALL, BLANK, WALL},
            {MOUSE, BLANK, BLANK, BLANK, WALL, BLANK, WALL, BLANK, WALL, BLANK, WALL, BLANK, WALL, BLANK, WALL},
            {WALL, WALL, WALL, BLANK, WALL, BLANK, WALL, BLANK, WALL, BLANK, WALL, BLANK, WALL, BLANK, WALL},
            {WALL, BLANK, BLANK, BLANK, WALL, BLANK, WALL, BLANK, WALL, BLANK, WALL, BLANK, WALL, BLANK, WALL},
            {WALL, BLANK, WALL, WALL, WALL, WALL, WALL, BLANK, WALL, WALL, WALL, WALL, WALL, BLANK, WALL},
            {WALL, BLANK, BLANK, BLANK, BLANK, BLANK, WALL, BLANK, BLANK, BLANK, BLANK, BLANK, BLANK, BLANK, WALL},
            {WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL},
    };


    public GameMazeModel() {
        this.maze = copyOf(initialMaze);
        this.state = getStartState();
        this.score = 0;
        mouseDirection = MOUSE_RIGHT;
        this.numberOfMoves = 0;
        this.previousBoards = new Stack<int[][]>();
        this.previousScores = new Stack<Integer>();
        this.gameWon = false;
        this.gameOver = false;
        this.s = new Serializer();
        solveGame();
    }

    public void printBoard() {
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                System.out.printf("%5d ", maze[i][j]);
            }
            System.out.println();
        }
        System.out.println("\n");

    }

    public boolean move(int row, int column,boolean simulate) {
        Point point = (Point) (state.getState());
        int px = point.x;
        int py = point.y;
        State current = new State(new Point(px, py));
        px += row;
        py += column;

        if (getPointValue(px, py) >= 0) {
            if (!simulate) {
            	state.setState(new Point(px, py));
            	numberOfMoves++;
            	score +=10;
            	if (getPointValue(px, py) == CHEESE){
            		if (numberOfMoves == minNumberOfMoves)
            			setGameWon(true);
            		else
            			setGameOver(true);
            	}        
                setData(current, state);
            }
            return true;
            
        }
        return false;

    }

    @Override
    public boolean moveUp(boolean simulate) {
        boolean moved;
        mouseDirection = MOUSE_UP;
        moved = move(-1, 0, simulate);
//    	if (moved && !simulate) {
//    		score +=10;
//    	}
        setChanged();
        notifyObservers();
        return moved;
    }

    @Override
    public boolean moveDown(boolean simulate) {
    	boolean moved;
    	mouseDirection = MOUSE_DOWN;
    	moved = move(1, 0 , simulate);
//    	if (moved && !simulate) {
//    		score +=10;
//    	}
        setChanged();
        notifyObservers();
        return moved;

    }

    @Override
    public boolean moveRight(boolean simulate) {
    	boolean moved;
    	mouseDirection = MOUSE_RIGHT;
    	moved = move(0,1, simulate);
//    	if (moved && !simulate) {
//    		score +=10;
//    	}
    	setChanged();
        notifyObservers();
        return moved;
    }

    @Override
    public boolean moveLeft(boolean simulate) {
    	boolean moved;
    	mouseDirection = MOUSE_LEFT;
    	moved = move(0, -1, simulate);
//    	if (moved && !simulate) {
//    		score +=10;
//    	}
        setChanged();
        notifyObservers();
        return moved;
    }
    


    @Override
    public int[][] getData() {
        return maze;
    }

    private void setData(int[][] data) {
        this.maze = data;
    }

    public void setData(State current, State goal) {
        Point pCurrent = (Point) (current.getState());
        Point pGoal = (Point) (goal.getState());
        int cx = pCurrent.x;
        int cy = pCurrent.y;
        int gx = pGoal.x;
        int gy = pGoal.y;
        maze[cx][cy] = 0;
        if (getPointValue(gx, gy) == CHEESE)
        	maze[gx][gy] = MOUSE_AND_CHEESE;
        else
        	maze[gx][gy] = mouseDirection;
    }

    @Override
    public void initialize() {
        maze = copyOf(initialMaze);
        this.state = getStartState();
        this.score = 0;
        this.numberOfMoves = 0;
        this.previousBoards = new Stack<int[][]>();
        this.previousScores = new Stack<Integer>();
        this.gameWon = false;
        this.gameOver = false;
        setChanged();
        notifyObservers();
    }

    @Override
    public void restore() {
        if (!previousBoards.isEmpty()) {
            //	setData(previousBoards.pop());
            setScore(previousScores.pop());
            setChanged();
            notifyObservers();
        }
    }

    public void backup() {
        previousBoards.push(maze);
        previousScores.push(score);
    }

    public void delete() {
        previousBoards.pop();
        previousScores.pop();
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
    public void saveGame(String xmlFileName) {
        try {
            s.serializeToXML(initialMaze, xmlFileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadGame(String xmlFileName) {
        try {
            setData(((GameMazeModel) s.deserializeXML(xmlFileName)).getData());
            setScore(((GameMazeModel) s.deserializeXML(xmlFileName)).getScore());
        } catch (Exception e) {
            e.printStackTrace();
        }
        setChanged();
        notifyObservers();
    }

    @Override
    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public int getPointValue(int x, int y) {
        if (x >= maze.length || x < 0 || y >= maze[0].length || y < 0) {
            return -1;
        }
        return maze[x][y];
    }

    public State getStartState() {
        for (int x = 0; x < maze.length; x++) {
            for (int y = 0; y < maze[0].length; y++) {
                if (maze[x][y] == MOUSE)
                    return new State(new Point(x, y));
            }
        }
        return null;
    }

    private int[][] copyOf(int[][] array) {
        int newArray[][] = new int[array.length][array[0].length];
        for (int row = 0; row < array.length; row++) {
            for (int column = 0; column < array.length; column++) {
                newArray[row][column] = array[row][column];
            }
        }
        return newArray;
    }

    public State getGoalState() {
        State newState = new State();
        for (int x = 0; x < maze.length; x++) {
            for (int y = 0; y < maze[0].length; y++) {
                if (maze[x][y] == CHEESE)
                    newState.setState(new Point(x, y));
            }
        }
        return newState;
    }

    private void solveGame() {
        AStar as = new AStar(new GameMazeDomain(this));
        ArrayList<Action> actions = as.search(this.getStartState(), this.getGoalState());
        minNumberOfMoves = actions.size();
    }

	@Override
	public boolean moveUpRight(boolean simulate) {
		boolean moved = moveRight(true);
		score -= 5;
		numberOfMoves--;
		if (moved) {
			moved = moveRight(false) && moveUp(false);
		}
    	else {
    		moved = moveUp(false) && moveRight(false);
    	}         
		if (!moved) {
			score += 5;
			numberOfMoves++;
		}
		setChanged();
        notifyObservers();
		return moved;
	}

	@Override
	public boolean moveUpLeft(boolean simulate) {
		boolean moved = moveLeft(true);
		score -= 5;
		numberOfMoves--;
		if (moved) {
			moved = moveLeft(false) && moveUp(false);
		}
    	else {
    		moved = moveUp(false) && moveLeft(false);
    	}         
		if (!moved) {
			score += 5;
			numberOfMoves++;
		}
		setChanged();
        notifyObservers();
		return moved;
	}

	@Override
	public boolean moveDownRight(boolean simulate) {
		boolean moved = moveRight(true);
		score -= 5;
		numberOfMoves--;
		if (moved) {
			moved = moveRight(false) && moveDown(false);
		}
    	else {
    		moved = moveDown(false) && moveRight(false);
    	}         
		if (!moved) {
			score += 5;
			numberOfMoves++;
		}
		setChanged();
        notifyObservers();
		return moved;
	}

	@Override
	public boolean moveDownLeft(boolean simulate) {
		boolean moved = moveLeft(true);
		score -= 5;
		numberOfMoves--;
		if (moved) {
			moved = moveLeft(false) && moveDown(false);
		}
    	else {
    		moved = moveDown(false) && moveLeft(false);
    	}       
		if (moved) {
			score += 5;
			numberOfMoves++;
		}
		setChanged();
        notifyObservers();
		return moved;
	}

}
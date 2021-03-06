package model;

import controller.Constants;
import model.algorithms.*;

import org.eclipse.swt.graphics.Point;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Game Maze Model
 */
public class GameMazeModel implements Model {
    private int[][] maze;
    private GameMazeState state;
    private int score;
    

    
    
    public GameMazeModel(GameMazeObject game) {
    	this.maze = new int[game.getData().length][game.getData().length];
		this.state = new GameMazeState();
		this.maze = game.getData();
		this.state = game.getState();
		this.score = game.getScore();
    	
    	
    }

//    public boolean move(int dx, int dy, boolean simulate) {
//        int x = ((Point) (state.getState())).x;
//        int y = ((Point) (state.getState())).y;
//        State current = new State();
//        current.setState(new Point(x, y));
//        if (getPointValue((x + dx), (y + dy)) >= Constants.BLANK) {
//            if (!simulate) {
//                // Backup the current state
//                State newState = new State();
//                newState.setState(new Point((x + dx), (y + dy)));
//                newState.setParentState(state);
//                newState.setLeadingAction(new GameMazeAction(dx, dy));
//                newState.setMouseDirection(mouseDirection);
//                state = newState;
//                numberOfMoves++;
//                if (getPointValue((x + dx), (y + dy)) == Constants.CHEESE) {
//                    if (numberOfMoves == minimalNumberOfMoves) {
//                        setGameWon(true);
//                    } else {
//                        setGameOver(true);
//                    }
//                }
//            }
//            return true;
//        }
//        return false;
//    }

    /**
     * @param simulate Specify whether or not to make changes
     * @return True: Movement was made / False: No movement was made
     */
//    @Override
//    public boolean moveUp(boolean simulate) {
//        boolean moved = move(-1, 0, simulate);
//        if (moved && !simulate) {
//            score += Constants.STRAIGHT_MOVEMENT_SCORE;
//            mouseDirection = Constants.MOUSE_UP;
//            // nextStraightDirection(MOUSE_UP);
//            updateMaze(state.getParentState(), state);
//            
//            
//        }
//        return moved;
//    }

    /**
     * @param simulate Specify whether or not to make changes
     * @return True: Movement was made / False: No movement was made
     */
//    @Override
//    public boolean moveDown(boolean simulate) {
//        boolean moved = move(1, 0, simulate);
//        if (moved && !simulate) {
//            score += Constants.STRAIGHT_MOVEMENT_SCORE;
//            mouseDirection = Constants.MOUSE_DOWN;
//            // nextStraightDirection(MOUSE_DOWN);
//            updateMaze(state.getParentState(), state);
//            
//            
//        }
//        return moved;
//    }

    /**
     * @param simulate Specify whether or not to make changes
     * @return True: Movement was made / False: No movement was made
     */
//    @Override
//    public boolean moveRight(boolean simulate) {
//        boolean moved = move(0, 1, simulate);
//        if (moved && !simulate) {
//            score += Constants.STRAIGHT_MOVEMENT_SCORE;
//            mouseDirection = Constants.MOUSE_RIGHT;
//            // nextStraightDirection(MOUSE_RIGHT);
//            updateMaze(state.getParentState(), state);
//            
//            
//        }
//        return moved;
//    }

    /**
     * @param simulate Specify whether or not to make changes
     * @return True: Movement was made / False: No movement was made
     */
//    @Override
//    public boolean moveLeft(boolean simulate) {
//        boolean moved = move(0, -1, simulate);
//        if (moved && !simulate) {
//            score += Constants.STRAIGHT_MOVEMENT_SCORE;
//            mouseDirection = Constants.MOUSE_LEFT;
//            // nextStraightDirection(MOUSE_LEFT);
//            updateMaze(state.getParentState(), state);
//            
//            
//        }
//        return moved;
//    }

    /**
     * @param simulate Specify whether or not to make changes
     * @return True: Movement was made / False: No movement was made
     */
//    @Override
//    public boolean moveUpRight(boolean simulate) {
//        boolean moved = move(-1, 1, simulate);
//        if (moved && !simulate) {
//            score += Constants.DIAGONAL_MOVEMENT_SCORE;
//            mouseDirection = Constants.MOUSE_RIGHT;
//            // nextDiagonalDirection(MOUSE_UP + MOUSE_RIGHT);
//            updateMaze(state.getParentState(), state);
//            
//            
//        }
//        return moved;
//    }

    /**
     * @param simulate Specify whether or not to make changes
     * @return True: Movement was made / False: No movement was made
     */
//    @Override
//    public boolean moveUpLeft(boolean simulate) {
//        boolean moved = move(-1, -1, simulate);
//        if (moved && !simulate) {
//            score += Constants.DIAGONAL_MOVEMENT_SCORE;
//            mouseDirection = Constants.MOUSE_LEFT;
//            // nextDiagonalDirection(MOUSE_UP + MOUSE_LEFT);
//            updateMaze(state.getParentState(), state);
//            
//            
//        }
//        return moved;
//    }

    /**
     * @param simulate Specify whether or not to make changes
     * @return True: Movement was made / False: No movement was made
     */
//    @Override
//    public boolean moveDownRight(boolean simulate) {
//        boolean moved = move(1, 1, simulate);
//        if (moved && !simulate) {
//            score += Constants.DIAGONAL_MOVEMENT_SCORE;
//            mouseDirection = Constants.MOUSE_RIGHT;
//            // nextDiagonalDirection(MOUSE_DOWN + MOUSE_RIGHT);
//            updateMaze(state.getParentState(), state);
//            
//            
//        }
//        return moved;
//    }

    /**
     * @param simulate Specify whether or not to make changes
     * @return True: Movement was made / False: No movement was made
     */
//    @Override
//    public boolean moveDownLeft(boolean simulate) {
//        boolean moved = move(1, -1, simulate);
//        if (moved && !simulate) {
//            score += Constants.DIAGONAL_MOVEMENT_SCORE;
//            mouseDirection = Constants.MOUSE_LEFT;
//            // nextDiagonalDirection(MOUSE_DOWN + MOUSE_LEFT);
//            updateMaze(state.getParentState(), state);
//            
//            
//        }
//        return moved;
//    }

    /**
     * @return Board data
     */
    @Override
    public int[][] getData() {
        return maze;
    }

    public void setData(int[][] data) {
        this.maze = data;
    }

//    public void updateMaze(State current, State goal) {
//        maze[((Point) (current.getState())).x][((Point) (current.getState())).y] = Constants.BLANK;
//        if (maze[((Point) (goal.getState())).x][((Point) (goal.getState())).y] == Constants.CHEESE) {
//            maze[((Point) (goal.getState())).x][((Point) (goal.getState())).y] = Constants.MOUSE_AND_CHEESE;
//        } else {
//            maze[((Point) (goal.getState())).x][((Point) (goal.getState())).y] = mouseDirection;
//        }
//    }

    /**
     * Initialize the model's data members when a new game starts
     */
    @Override
    public void initialize() {
        this.state = getStartState();
        this.score = 0;

    }

    
    /**
     * @return Game score
     */
    @Override
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }


  
  


    public int getPointValue(int x, int y) {
        if (x >= maze.length || x < 0 || y >= maze[0].length || y < 0) {
            return -1;
        }
        return maze[x][y];
    }

//    private int[][] copyOf(int[][] array) {
//        int newArray[][] = new int[array.length][array[0].length];
//        for (int row = 0; row < array.length; row++) {
//            for (int column = 0; column < array[0].length; column++) {
//                newArray[row][column] = array[row][column];
//            }
//        }
//        return newArray;
//    }

    public GameMazeState getStartState() {
        for (int row = 0; row < maze.length; row++) {
            for (int column = 0; column < maze[0].length; column++) {
                if (maze[row][column] > 0 && maze[row][column] != Constants.CHEESE) {
                	GameMazeState start = new GameMazeState();
                    start.setState(new Point(row, column));
                    return start;
                }
            }
        }
        return null;
    }

    public State getGoalState() {
        for (int row = 0; row < maze.length; row++) {
            for (int column = 0; column < maze[0].length; column++) {
                if (maze[row][column] == Constants.CHEESE) {
                    State goal = new State();
                    goal.setState(new Point(row, column));
                    return goal;
                }
            }
        }
        return null;
    }

    private int numberOfMovesToSolveGame() {
        AStar as = new AStar(new GameMazeDomain(this), new GameMazeDistanceG(), new GameMazeDistanceH());
        ArrayList<Action> actions = as.search(this.getStartState(), this.getGoalState());
        return actions.size();
    }

  

	@Override
	public int getNumOfFreeStates() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void generate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Integer> getEmptyCellIds() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Object clone() {
		return null;
	}
	
	@Override
	public void setEmptyCell(int i, int j, int value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean moveUp(boolean simulate) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean moveDown(boolean simulate) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean moveRight(boolean simulate) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean moveLeft(boolean simulate) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isGameWon() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isGameOver() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setGameWon(boolean gameWon) {
		// TODO Auto-generated method stub
		
	}
	
	

}
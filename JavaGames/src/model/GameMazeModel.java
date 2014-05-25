package model;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Observable;

import model.algorithms.AStar;
import model.algorithms.Action;
import model.algorithms.Model;
import model.algorithms.Serializer;
import model.algorithms.State;

import org.eclipse.swt.graphics.Point;

import RMIInterface.RMIConstants;
import RMIInterface.RemoteInterface;
import controller.Constants;

/**
 * Game Maze Model
 */
public class GameMazeModel extends Observable implements Model {
	private int mouseDirection;
	private int[][] maze;
	private GameMazeState state;
	private int score;
	private int numberOfMoves;
	private int minimalNumberOfMoves;
	private boolean gameWon;
	private boolean gameOver;
	private Registry registry;
	private RemoteInterface remote;
	private int[][] initialMaze = {
            {Constants.WALL, Constants.WALL, Constants.WALL, Constants.WALL, Constants.WALL, Constants.WALL, Constants.WALL, Constants.WALL, Constants.WALL, Constants.WALL, Constants.WALL, Constants.WALL, Constants.WALL, Constants.WALL, Constants.WALL},
            {Constants.WALL, Constants.BLANK, Constants.BLANK, Constants.BLANK, Constants.WALL, Constants.BLANK, Constants.BLANK, Constants.BLANK, Constants.BLANK, Constants.BLANK, Constants.BLANK, Constants.BLANK, Constants.BLANK, Constants.BLANK, Constants.WALL},
            {Constants.WALL, Constants.BLANK, Constants.WALL, Constants.BLANK, Constants.WALL, Constants.BLANK, Constants.WALL, Constants.WALL, Constants.BLANK, Constants.WALL, Constants.WALL, Constants.BLANK, Constants.WALL, Constants.BLANK, Constants.WALL},
            {Constants.WALL, Constants.BLANK, Constants.WALL, Constants.BLANK, Constants.WALL, Constants.BLANK, Constants.BLANK, Constants.WALL, Constants.BLANK, Constants.WALL, Constants.BLANK, Constants.BLANK, Constants.WALL, Constants.BLANK, Constants.WALL},
            {Constants.WALL, Constants.BLANK, Constants.WALL, Constants.WALL, Constants.WALL, Constants.WALL, Constants.WALL, Constants.WALL, Constants.BLANK, Constants.WALL, Constants.WALL, Constants.BLANK, Constants.WALL, Constants.WALL, Constants.WALL},
            {Constants.WALL, Constants.BLANK, Constants.BLANK, Constants.BLANK, Constants.BLANK, Constants.BLANK, Constants.BLANK, Constants.BLANK, Constants.BLANK, Constants.BLANK, Constants.WALL, Constants.BLANK, Constants.WALL, Constants.BLANK, Constants.WALL},
            {Constants.WALL, Constants.BLANK, Constants.WALL, Constants.BLANK, Constants.WALL, Constants.WALL, Constants.WALL, Constants.WALL, Constants.WALL, Constants.WALL, Constants.WALL, Constants.BLANK, Constants.WALL, Constants.BLANK, Constants.WALL},
            {Constants.WALL, Constants.BLANK, Constants.WALL, Constants.BLANK, Constants.WALL, Constants.BLANK, Constants.BLANK, Constants.BLANK, Constants.BLANK, Constants.BLANK, Constants.WALL, Constants.BLANK, Constants.BLANK, Constants.BLANK, Constants.CHEESE},
            {Constants.WALL, Constants.BLANK, Constants.WALL, Constants.WALL, Constants.WALL, Constants.BLANK, Constants.WALL, Constants.BLANK, Constants.WALL, Constants.BLANK, Constants.WALL, Constants.BLANK, Constants.WALL, Constants.BLANK, Constants.WALL},
            {Constants.MOUSE_RIGHT, Constants.BLANK, Constants.BLANK, Constants.BLANK, Constants.WALL, Constants.BLANK, Constants.WALL, Constants.BLANK, Constants.WALL, Constants.BLANK, Constants.WALL, Constants.BLANK, Constants.WALL, Constants.BLANK, Constants.WALL},
            {Constants.WALL, Constants.WALL, Constants.WALL, Constants.BLANK, Constants.WALL, Constants.BLANK, Constants.WALL, Constants.BLANK, Constants.WALL, Constants.BLANK, Constants.WALL, Constants.BLANK, Constants.WALL, Constants.BLANK, Constants.WALL},
            {Constants.WALL, Constants.BLANK, Constants.BLANK, Constants.BLANK, Constants.WALL, Constants.BLANK, Constants.WALL, Constants.BLANK, Constants.WALL, Constants.BLANK, Constants.WALL, Constants.BLANK, Constants.WALL, Constants.BLANK, Constants.WALL},
            {Constants.WALL, Constants.BLANK, Constants.WALL, Constants.WALL, Constants.WALL, Constants.WALL, Constants.WALL, Constants.BLANK, Constants.WALL, Constants.WALL, Constants.WALL, Constants.WALL, Constants.WALL, Constants.BLANK, Constants.WALL},
            {Constants.WALL, Constants.BLANK, Constants.BLANK, Constants.BLANK, Constants.BLANK, Constants.BLANK, Constants.WALL, Constants.BLANK, Constants.BLANK, Constants.BLANK, Constants.BLANK, Constants.BLANK, Constants.BLANK, Constants.BLANK, Constants.WALL},
            {Constants.WALL, Constants.WALL, Constants.WALL, Constants.WALL, Constants.WALL, Constants.WALL, Constants.WALL, Constants.WALL, Constants.WALL, Constants.WALL, Constants.WALL, Constants.WALL, Constants.WALL, Constants.WALL, Constants.WALL},
    };
	
	public GameMazeModel() {
		this.maze = new int[initialMaze.length][initialMaze[0].length];
		this.state = new GameMazeState();
	}
	/**
	 * @param dx,dy for movement, simulate Specify whether or not to make changes
	 * @return True: Movement was made / False: No movement was made
	 */
	public boolean move(int dx, int dy, boolean simulate) {
		int x = ((Point) (state.getState())).x;
		int y = ((Point) (state.getState())).y;
		GameMazeState current = new GameMazeState();
		current.setState(new Point(x, y));
		if (getPointValue((x + dx), (y + dy)) >= Constants.BLANK) {
			if (!simulate) {
				// Backup the current state
				GameMazeState newState = new GameMazeState();
				newState.setState(new Point((x + dx), (y + dy)));
				newState.setParentState(state);
				newState.setLeadingAction(new GameMazeAction(dx, dy));
				newState.setMouseDirection(mouseDirection);
				state = newState;
				numberOfMoves++;
				if (getPointValue((x + dx), (y + dy)) == Constants.CHEESE) {
					if (numberOfMoves == minimalNumberOfMoves) {
						setGameWon(true);
					} else {
						setGameOver(true);
					}
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * @param simulate
	 *            Specify whether or not to make changes
	 * @return True: Movement was made / False: No movement was made
	 */
	@Override
	public boolean moveUp(boolean simulate) {
		boolean moved = move(-1, 0, simulate);
		if (moved && !simulate) {
			score += Constants.STRAIGHT_MOVEMENT_SCORE;
			mouseDirection = Constants.MOUSE_UP;
			// nextStraightDirection(MOUSE_UP);
			updateMaze(state.getParentState(), state);
			setChanged();
			notifyObservers();
		}
		return moved;
	}

	/**
	 * @param simulate
	 *            Specify whether or not to make changes
	 * @return True: Movement was made / False: No movement was made
	 */
	@Override
	public boolean moveDown(boolean simulate) {
		boolean moved = move(1, 0, simulate);
		if (moved && !simulate) {
			score += Constants.STRAIGHT_MOVEMENT_SCORE;
			mouseDirection = Constants.MOUSE_DOWN;
			// nextStraightDirection(MOUSE_DOWN);
			updateMaze(state.getParentState(), state);
			setChanged();
			notifyObservers();
		}
		return moved;
	}

	/**
	 * @param simulate
	 *            Specify whether or not to make changes
	 * @return True: Movement was made / False: No movement was made
	 */
	@Override
	public boolean moveRight(boolean simulate) {
		boolean moved = move(0, 1, simulate);
		if (moved && !simulate) {
			score += Constants.STRAIGHT_MOVEMENT_SCORE;
			mouseDirection = Constants.MOUSE_RIGHT;
			// nextStraightDirection(MOUSE_RIGHT);
			updateMaze(state.getParentState(), state);
			setChanged();
			notifyObservers();
		}
		return moved;
	}

	/**
	 * @param simulate
	 *            Specify whether or not to make changes
	 * @return True: Movement was made / False: No movement was made
	 */
	@Override
	public boolean moveLeft(boolean simulate) {
		boolean moved = move(0, -1, simulate);
		if (moved && !simulate) {
			score += Constants.STRAIGHT_MOVEMENT_SCORE;
			mouseDirection = Constants.MOUSE_LEFT;
			// nextStraightDirection(MOUSE_LEFT);
			updateMaze(state.getParentState(), state);
			setChanged();
			notifyObservers();
		}
		return moved;
	}

	/**
	 * @param simulate
	 *            Specify whether or not to make changes
	 * @return True: Movement was made / False: No movement was made
	 */
	@Override
	public boolean moveUpRight(boolean simulate) {
		boolean moved = move(-1, 1, simulate);
		if (moved && !simulate) {
			score += Constants.DIAGONAL_MOVEMENT_SCORE;
			mouseDirection = Constants.MOUSE_RIGHT;
			// nextDiagonalDirection(MOUSE_UP + MOUSE_RIGHT);
			updateMaze(state.getParentState(), state);
			setChanged();
			notifyObservers();
		}
		return moved;
	}

	/**
	 * @param simulate
	 *            Specify whether or not to make changes
	 * @return True: Movement was made / False: No movement was made
	 */
	@Override
	public boolean moveUpLeft(boolean simulate) {
		boolean moved = move(-1, -1, simulate);
		if (moved && !simulate) {
			score += Constants.DIAGONAL_MOVEMENT_SCORE;
			mouseDirection = Constants.MOUSE_LEFT;
			// nextDiagonalDirection(MOUSE_UP + MOUSE_LEFT);
			updateMaze(state.getParentState(), state);
			setChanged();
			notifyObservers();
		}
		return moved;
	}

	/**
	 * @param simulate
	 *            Specify whether or not to make changes
	 * @return True: Movement was made / False: No movement was made
	 */
	@Override
	public boolean moveDownRight(boolean simulate) {
		boolean moved = move(1, 1, simulate);
		if (moved && !simulate) {
			score += Constants.DIAGONAL_MOVEMENT_SCORE;
			mouseDirection = Constants.MOUSE_RIGHT;
			// nextDiagonalDirection(MOUSE_DOWN + MOUSE_RIGHT);
			updateMaze(state.getParentState(), state);
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
	public boolean moveDownLeft(boolean simulate) {
		boolean moved = move(1, -1, simulate);
		if (moved && !simulate) {
			score += Constants.DIAGONAL_MOVEMENT_SCORE;
			mouseDirection = Constants.MOUSE_LEFT;
			// nextDiagonalDirection(MOUSE_DOWN + MOUSE_LEFT);
			updateMaze(state.getParentState(), state);
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
		return maze;
	}

	public void setData(int[][] data) {
		this.maze = data;
	}

	public void updateMaze(State current, State goal) {
		maze[((Point) (current.getState())).x][((Point) (current.getState())).y] = Constants.BLANK;
		if (maze[((Point) (goal.getState())).x][((Point) (goal.getState())).y] == Constants.CHEESE) {
			maze[((Point) (goal.getState())).x][((Point) (goal.getState())).y] = Constants.MOUSE_AND_CHEESE;
		} else {
			maze[((Point) (goal.getState())).x][((Point) (goal.getState())).y] = mouseDirection;
		}
	}

	/**
	 * Initialize the model's data members when a new game starts
	 */
	@Override
	public void initialize() {
		this.maze = copyOf(initialMaze);
		this.state = getStartState();
		this.minimalNumberOfMoves = numberOfMovesToSolveGame();
		this.mouseDirection = maze[((Point) (state.getState())).x][((Point) (state
				.getState())).y];
		this.numberOfMoves = 0;
		this.score = 0;
		this.gameWon = false;
		this.gameOver = false;
		setChanged();
		notifyObservers();
	}

	/**
	 * Restores the player's last movement
	 */
	@Override
	public void restore() {
		if (state.getParentState() != null) {
			if (state.getLeadingAction().getDx() != 0
					&& state.getLeadingAction().getDy() != 0) {
				score -= Constants.DIAGONAL_MOVEMENT_SCORE;
			} else {
				score -= Constants.STRAIGHT_MOVEMENT_SCORE;
			}
			mouseDirection = state.getMouseDirection();
			updateMaze(state, state.getParentState());
			state = (GameMazeState) state.getParentState();
			setChanged();
			notifyObservers();
		}
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
			setData(((GameMazeModel) Serializer.deserializeXML(xmlFileName)).getData());
			setScore(((GameMazeModel) Serializer.deserializeXML(xmlFileName)).getScore());
			updateMaze(state, getStartState());
			state.setState(getStartState().getState());
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

	public int getPointValue(int x, int y) {
		if (x >= maze.length || x < 0 || y >= maze[0].length || y < 0) {
			return -1;
		}
		return maze[x][y];
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

	public GameMazeState getGoalState() {
		for (int row = 0; row < maze.length; row++) {
			for (int column = 0; column < maze[0].length; column++) {
				if (maze[row][column] == Constants.CHEESE) {
					GameMazeState goal = new GameMazeState();
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
	public int getHint(int numOfHints, int solveDepth)
			throws CloneNotSupportedException, RemoteException, NotBoundException {
		ArrayList<Action> myActions = new ArrayList<Action>();
		try {
			myActions = getMoves();
			for (int i = 0; i < numOfHints; i++) {
				if (myActions.isEmpty())
					return 0;
				moveByAction(myActions.get(0));
				myActions.remove(0);

			}

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public void solveGame(int solveDepth) throws RemoteException,
			CloneNotSupportedException, NotBoundException {
		ArrayList<Action> myActions = new ArrayList<Action>();
		try {
			myActions = getMoves();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		int counter=0;
		while(!isGameWon() || !(myActions.isEmpty())){
			counter ++;
			moveByAction(myActions.get(0));
			myActions.remove(0);
		}
		
			
			
	}
	@SuppressWarnings("unchecked")
	private ArrayList<Action> getMoves() throws RemoteException, CloneNotSupportedException, InterruptedException {
		GameMazeObject myGame = new GameMazeObject(this);
		return (ArrayList<Action>) remote.getMazeHint(myGame);

	}

	private void moveByAction(Action action) {
		int rows = action.getDx();
		int cols = action.getDy();
		
		if (rows > 0 && cols > 0)
			moveDownRight(false);
		if (rows > 0 && cols < 0)
			moveDownLeft(false);
		if (rows < 0 && cols > 0)
			moveUpRight(false);
		if (rows < 0 && cols < 0)
			moveUpLeft(false);

		if (rows == 0 && cols > 0)
			moveRight(false);
		if (rows == 0 && cols < 0)
			moveLeft(false);
		if (rows < 0 && cols == 0)
			moveUp(false);
		if (rows > 0 && cols == 0)
			moveDown(false);
	}
	
	/**
	 * @param serverName to connect
	 */
	@Override
	public void connectRMI(String serverName) throws RemoteException, NotBoundException {
		registry = LocateRegistry.getRegistry(serverName, RMIConstants.PORT);
		remote = (RemoteInterface) registry.lookup(RMIConstants.RMI_ID);
	}
	
	/**
	 * @param xmlFileName	Output file name
	 */
	@Override
	public void saveConfiguration(ArrayList<String> serversList, String xmlFileName) {
		try {
			Serializer.serializeToXML(serversList, xmlFileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param xmlFileName	Input file name
	 */
	@Override
	public ArrayList<String> loadConfiguration(String xmlFileName) {
		try {
			ArrayList<String> serversList = new ArrayList<>();
			serversList = (ArrayList<String>) Serializer.deserializeXML(xmlFileName);
			return serversList;
		} catch (Exception e) {
			return new ArrayList<String>();
		}
	}
}
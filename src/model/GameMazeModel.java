package model;

import model.algorithms.*;
import org.eclipse.swt.graphics.Point;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Stack;

public class GameMazeModel extends Observable implements Model {
    private final static int MOUSE = 1;
    private final static int WALL = -1;
    private final static int BLANK = 0;
    private final static int MOUSE_UP = 1;
    private final static int MOUSE_DOWN = 2;
    private final static int MOUSE_RIGHT = 8;
    private final static int MOUSE_LEFT = 10;
    private final static int CHEESE = 12;
    private final static int MOUSE_AND_CHEESE = 14;
    private final static int DIAGONAL_MOVEMENT_SCORE = 15;
    private final static int STRAIGHT_MOVEMENT_SCORE = 10;
    private int mouseDirection;
    private int[][] maze;
    private State state;
    private int score;
    private int numberOfMoves;
    private int minimalNumberOfMoves;
    private boolean gameWon;
    private boolean gameOver;
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
        this.maze = new int[initialMaze.length][initialMaze[0].length];
        this.state = new State();
        this.s = new Serializer();
    }

    public boolean move(int dx, int dy, boolean simulate) {
        int x = ((Point) (state.getState())).x;
        int y = ((Point) (state.getState())).y;
        State current = new State();
        current.setState(new Point(x, y));
        if (getPointValue((x + dx), (y + dy)) >= BLANK) {
            if (!simulate) {
                // Backup the current state
                State newState = new State();
                newState.setState(new Point((x + dx), (y + dy)));
                newState.setParentState(state);
                newState.setLeadingAction(new GameMazeAction(dx, dy));
                state = newState;
                numberOfMoves++;
                if (getPointValue((x + dx), (y + dy)) == CHEESE) {
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

    @Override
    public boolean moveUp(boolean simulate) {
        boolean moved = move(-1, 0, simulate);
        if (moved && !simulate) {
            score += STRAIGHT_MOVEMENT_SCORE;
            nextStraightDirection(MOUSE_UP);
            updateMaze(state.getParentState(), state);
            setChanged();
            notifyObservers();
        }
        return moved;
    }

    @Override
    public boolean moveDown(boolean simulate) {
        boolean moved = move(1, 0, simulate);
        if (moved && !simulate) {
            score += STRAIGHT_MOVEMENT_SCORE;
            nextStraightDirection(MOUSE_DOWN);
            updateMaze(state.getParentState(), state);
            setChanged();
            notifyObservers();
        }
        return moved;
    }

    @Override
    public boolean moveRight(boolean simulate) {
        boolean moved = move(0, 1, simulate);
        if (moved && !simulate) {
            score += STRAIGHT_MOVEMENT_SCORE;
            nextStraightDirection(MOUSE_RIGHT);
            updateMaze(state.getParentState(), state);
            setChanged();
            notifyObservers();
        }
        return moved;
    }

    @Override
    public boolean moveLeft(boolean simulate) {
        boolean moved = move(0, -1, simulate);
        if (moved && !simulate) {
            score += STRAIGHT_MOVEMENT_SCORE;
            nextStraightDirection(MOUSE_LEFT);
            updateMaze(state.getParentState(), state);
            setChanged();
            notifyObservers();
        }
        return moved;
    }

    @Override
    public boolean moveUpRight(boolean simulate) {
        boolean moved = move(-1, 1, simulate);
        if (moved && !simulate) {
            score += DIAGONAL_MOVEMENT_SCORE;
            nextDiagonalDirection(MOUSE_UP + MOUSE_RIGHT);
            updateMaze(state.getParentState(), state);
            setChanged();
            notifyObservers();
        }
        return moved;
    }

    @Override
    public boolean moveUpLeft(boolean simulate) {
        boolean moved = move(-1, -1, simulate);
        if (moved && !simulate) {
            score += DIAGONAL_MOVEMENT_SCORE;
            nextDiagonalDirection(MOUSE_UP + MOUSE_LEFT);
            updateMaze(state.getParentState(), state);
            setChanged();
            notifyObservers();
        }
        return moved;
    }

    @Override
    public boolean moveDownRight(boolean simulate) {
        boolean moved = move(1, 1, simulate);
        if (moved && !simulate) {
            score += DIAGONAL_MOVEMENT_SCORE;
            nextDiagonalDirection(MOUSE_DOWN + MOUSE_RIGHT);
            updateMaze(state.getParentState(), state);
            setChanged();
            notifyObservers();
        }
        return moved;
    }

    @Override
    public boolean moveDownLeft(boolean simulate) {
        boolean moved = move(1, -1, simulate);
        if (moved && !simulate) {
            score += DIAGONAL_MOVEMENT_SCORE;
            nextDiagonalDirection(MOUSE_DOWN + MOUSE_LEFT);
            updateMaze(state.getParentState(), state);
            setChanged();
            notifyObservers();
        }
        return moved;
    }

    @Override
    public int[][] getData() {
        return maze;
    }

    public void setData(int[][] data) {
        this.maze = data;
    }

    public void updateMaze(State current, State goal) {
        maze[((Point) (current.getState())).x][((Point) (current.getState())).y] = BLANK;
        if (maze[((Point) (goal.getState())).x][((Point) (goal.getState())).y] == CHEESE) {
            maze[((Point) (goal.getState())).x][((Point) (goal.getState())).y] = MOUSE_AND_CHEESE;
        } else {
            maze[((Point) (goal.getState())).x][((Point) (goal.getState())).y] = mouseDirection;
        }
    }

    @Override
    public void initialize() {
        this.maze = copyOf(initialMaze);
        this.state = getStartState();
        this.score = 0;
        this.numberOfMoves = 0;
        this.minimalNumberOfMoves = numberOfMovesToSolveGame();
        this.gameWon = false;
        this.gameOver = false;
        this.mouseDirection = MOUSE_RIGHT;
        updateMaze(state, state);
        setChanged();
        notifyObservers();
    }

    @Override
    public void restore() {
        if (state.getParentState() != null) {
            updateMaze(state, state.getParentState());
            if (state.getLeadingAction().getDx() != 0 && state.getLeadingAction().getDy() != 0) {
                score -= DIAGONAL_MOVEMENT_SCORE;
            } else {
                score -= STRAIGHT_MOVEMENT_SCORE;
            }
            state = state.getParentState();
            setChanged();
            notifyObservers();
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

    private int[][] copyOf(int[][] array) {
        int newArray[][] = new int[array.length][array[0].length];
        for (int row = 0; row < array.length; row++) {
            for (int column = 0; column < array[0].length; column++) {
                newArray[row][column] = array[row][column];
            }
        }
        return newArray;
    }

    public State getStartState() {
        for (int row = 0; row < maze.length; row++) {
            for (int column = 0; column < maze[0].length; column++) {
                if (maze[row][column] == MOUSE) {
                    State start = new State();
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
                if (maze[row][column] == CHEESE) {
                    State goal = new State();
                    goal.setState(new Point(row, column));
                    return goal;
                }
            }
        }
        return null;
    }

    private int numberOfMovesToSolveGame() {
        AStar as = new AStar(new GameMazeDomain(this));
        ArrayList<Action> actions = as.search(this.getStartState(), this.getGoalState());
        return actions.size();
    }

    // Define the next direction based on the next possible movement
    private void nextStraightDirection(int mouseDirection) {
        switch (mouseDirection) {
            case MOUSE_UP:
                if (moveUp(true))
                    this.mouseDirection = MOUSE_UP;
                else
                    this.mouseDirection = MOUSE_DOWN;
                break;

            case MOUSE_DOWN:
                if (moveDown(true))
                    this.mouseDirection = MOUSE_DOWN;
                else
                    this.mouseDirection = MOUSE_UP;
                break;

            case MOUSE_RIGHT:
                if (moveRight(true))
                    this.mouseDirection = MOUSE_RIGHT;
                else
                    this.mouseDirection = MOUSE_LEFT;
                break;

            case MOUSE_LEFT:
                if (moveLeft(true))
                    this.mouseDirection = MOUSE_LEFT;
                else
                    this.mouseDirection = MOUSE_RIGHT;
                break;
        }
    }

    // Define the next direction based on the next possible movement
    private void nextDiagonalDirection(int mouseDirection) {
        switch (mouseDirection) {
            case (MOUSE_UP + MOUSE_RIGHT):
                if (moveUp(true))
                    this.mouseDirection = MOUSE_UP;
                else
                    this.mouseDirection = MOUSE_RIGHT;
                break;

            case (MOUSE_UP + MOUSE_LEFT):
                if (moveUp(true))
                    this.mouseDirection = MOUSE_UP;
                else
                    this.mouseDirection = MOUSE_LEFT;
                break;

            case (MOUSE_DOWN + MOUSE_RIGHT):
                if (moveDown(true))
                    this.mouseDirection = MOUSE_DOWN;
                else
                    this.mouseDirection = MOUSE_RIGHT;
                break;

            case (MOUSE_DOWN + MOUSE_LEFT):
                if (moveDown(true))
                    this.mouseDirection = MOUSE_DOWN;
                else
                    this.mouseDirection = MOUSE_LEFT;
                break;
        }
    }
}
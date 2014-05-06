package controller;

/**
 * Holds General, Maze and 2048 Constants
 */
public interface Constants {

    /* General Constants */

    public final static int RESET = 1;
    public final static int SAVE = 2;
    public final static int LOAD = 3;
    public final static int UNDO = 4;
    public final static int RIGHT = 10;
    public final static int RIGHT_UP = 11;
    public final static int RIGHT_DOWN = 12;
    public final static int LEFT = 15;
    public final static int LEFT_UP = 16;
    public final static int LEFT_DOWN = 17;
    public final static int UP = 20;
    public final static int DOWN = 21;
    public final static int DIAGONAL_MOVEMENT_SCORE = 15;
    public final static int STRAIGHT_MOVEMENT_SCORE = 10;
    public final static int SCORE_FONT_SIZE = 24;

    /* GameMaze Constants */

    public final static int BLANK = 0;
    public final static int WALL = -1;
    public final static int MOUSE_UP = 1;
    public final static int MOUSE_DOWN = 2;
    public final static int MOUSE_RIGHT = 8;
    public final static int MOUSE_LEFT = 10;
    public final static int CHEESE = 12;
    public final static int MOUSE_AND_CHEESE = 14;
    public final static String[] filterExtensions = {"xml"};

    /* Game2048 Constants */

    public final static int BOARDSIZE = 4;
    public final static int TARGETSCORE = 2048;
    public final static int TILE_FONT_SIZE = 16;
}

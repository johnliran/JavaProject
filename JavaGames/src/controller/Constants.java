package controller;

/**
 * Holds General, Maze and 2048 Constants
 */
public final class Constants {

    /* General Constants */

    public final static int RESET = 1;
    public final static int SAVE = 2;
    public final static int LOAD = 3;
    public final static int UNDO = 4;
    public final static int SOLVE = 5;
    public final static int HINT = 6;
    public final static int CONNECT = 7;
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
    public final static int DEFAULT_FONT_SIZE = 12;
    public final static int BCOLOR_R = 187;
    public final static int BCOLOR_G = 173;
    public final static int BCOLOR_B = 160;
    public final static int FCOLOR_R = 119;
    public final static int FCOLOR_G = 110;
    public final static int FCOLOR_B = 101;
    public final static int SPACE = 81;
    public final static int CLOSETHREADS = 999;
    public final static int[] SOLVE_DEPTHS_LIST = {3, 4, 5, 6, 7, 8, 9, 10};
    public final static int[] NUMBER_OF_HINTS_LIST = {1, 3, 5, 7, 10};
    public final static String[] SERVERS_LIST = {"localhost"};
    public final static String[] EXTENSIONS = {"xml"};
    public final static String IMAGE_BUTTON_CONNECT = "images/WindowShell/Connect.png";
    public final static String IMAGE_BUTTON_LOAD = "images/WindowShell/Load.png";
    public final static String IMAGE_BUTTON_PAUSE =  "images/WindowShell/Pause.png";
    public final static String IMAGE_BUTTON_RESET = "images/WindowShell/Reset.png";
    public final static String IMAGE_BUTTON_SAVE = "images/WindowShell/Save.png";
    public final static String IMAGE_BUTTON_SOLVE = "images/WindowShell/Solve.png";
    public final static String IMAGE_BUTTON_UNDO = "images/WindowShell/Undo.png";

    /* GameMaze Constants */

    public final static int BLANK = 0;
    public final static int WALL = -1;
    public final static int MOUSE_UP = 1;
    public final static int MOUSE_DOWN = 2;
    public final static int MOUSE_RIGHT = 8;
    public final static int MOUSE_LEFT = 10;
    public final static int CHEESE = 12;
    public final static int MOUSE_AND_CHEESE = 14;
    public final static String IMAGE_MAZE_CHEESE = "images/GameMaze/mazeCheese.png";
    public final static String IMAGE_MAZE_MOUSE_AND_CHEESE = "images/GameMaze/mazeMouseAndCheese.png";
    public final static String IMAGE_MAZE_MOUSE = "images/GameMaze/mazeMouse.png";
    public final static String IMAGE_MAZE_MOUSE_UP = "images/GameMaze/mazeMouse_up.png";
    public final static String IMAGE_MAZE_MOUSE_DOWN = "images/GameMaze/mazeMouse_down.png";
    public final static String IMAGE_MAZE_MOUSE_RIGHT = "images/GameMaze/mazeMouse_right.png";
    public final static String IMAGE_MAZE_MOUSE_LEFT = "images/GameMaze/mazeMouse_left.png";

    /* Game2048 Constants */

    public final static int BOARDSIZE = 4;
    public final static int TARGETSCORE = 2048;
    public final static int TILE_FONT_SIZE = 16;
    public final static int[] DIRECTIONS ={UP,DOWN,RIGHT,LEFT};
}

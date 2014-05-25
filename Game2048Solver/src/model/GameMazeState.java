package model;

import java.io.Serializable;

import model.algorithms.State;

/**
 * Game Maze State
 */
public class GameMazeState extends State implements Serializable {
    private int mouseDirection;

    public int getMouseDirection() {
        return mouseDirection;
    }

    public void setMouseDirection(int mouseDirection) {
        this.mouseDirection = mouseDirection;
    }
}

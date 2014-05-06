package model;

import model.algorithms.State;

public class MazeState extends State {
    private int mouseDirection;

    public int getMouseDirection() {
        return mouseDirection;
    }

    public void setMouseDirection(int mouseDirection) {
        this.mouseDirection = mouseDirection;
    }
}

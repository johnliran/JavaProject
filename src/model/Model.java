package model;

public interface Model {
    public void moveUp();

    public void moveDown();

    public void moveRight();

    public void moveLeft();

    public int[][] getData();

    public void initializeBoard();

    public int getScore();
}

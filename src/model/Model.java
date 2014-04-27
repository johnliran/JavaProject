package model;

public interface Model {
    public boolean moveUp(boolean simulate);

    public boolean moveDown(boolean simulate);

    public boolean moveRight(boolean simulate);

    public boolean moveLeft(boolean simulate);

    public int[][] getData();

    public void initializeBoard();

    public int getScore();
}

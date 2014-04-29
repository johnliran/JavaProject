package model;

public interface Model {

    public void initialize();

    public void restore();

    public boolean moveUp(boolean simulate);

    public boolean moveDown(boolean simulate);

    public boolean moveRight(boolean simulate);

    public boolean moveLeft(boolean simulate);

    public int[][] getData();

    public int getScore();

    public boolean isGameWon();

    public boolean isGameOver();
    
    public void setGameWon(boolean gameWon);
}

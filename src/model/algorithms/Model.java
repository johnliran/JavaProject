package model.algorithms;

/**
 *
 */
public interface Model {

    public void initialize();

    public void restore();

    public boolean moveUp(boolean simulate);

    public boolean moveDown(boolean simulate);

    public boolean moveRight(boolean simulate);

    public boolean moveLeft(boolean simulate);

    public boolean moveUpRight(boolean simulate);

    public boolean moveUpLeft(boolean simulate);

    public boolean moveDownRight(boolean simulate);

    public boolean moveDownLeft(boolean simulate);

    public int[][] getData();

    public int getScore();

    public boolean isGameWon();

    public boolean isGameOver();

    public void setGameWon(boolean gameWon);

    public void saveGame(String xmlFileName);

    public void loadGame(String xmlFileName);
}

package view;

public interface View extends Runnable {

    public void displayData(int[][] data);

    public int getUserCommand();

    public void displayScore(int score);

    public void gameWon();

    public void gameOver();

    public boolean isUserNotified();

    public WindowShell getWindowShell();


}

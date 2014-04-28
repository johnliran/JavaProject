package view;

public interface View {

    public void displayData(int[][] data);

    public int getUserCommand();

    public void displayScore(int score);

    public void gameWon();

    public void gameOver();
    
    public boolean isUserNotified();
		

}

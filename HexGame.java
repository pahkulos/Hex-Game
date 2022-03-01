
import java.awt.event.ActionEvent;
import java.io.IOException;


public interface HexGame {
	/**
	 * Start game and play
	 * @return
	 */
	public void Play();
	/**
	 * takes number and assign it
	 * @return
	 */
	public void takeSize();
	/**
	 * takes type of game and assign it
	 * @return
	 */
	public void gameType();
	/**
	 * reset the board
	 * @return
	 */
	public void reset();
	/**
	 * one move goes back
	 * @return
	 */
	public void undo();
	/**
	 * creates board
	 * @return
	 */
	public void createBoard();
	/**
	 * takes file name and save game to file
	 * @return
	 */
	public void saveGame() throws IOException;
	/**
	 * takes file name and load game from file
	 * @return
	 */
	public void loadGame() throws IOException;
	/**
	 * create random number and play 
	 * @return
	 */
	public void playRandom();
	/**
	 *checks whether the game is over
	 * @return
	 */
	public boolean checkEndGame();
	/**
	 *if the game is over, capitalizes the winner's letters.
	 * @return
	 */
	public void doUppercase(int row,int column,String ch);
}

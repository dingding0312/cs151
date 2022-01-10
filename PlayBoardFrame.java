import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
/**
 * 
 * @author dingchen
 *
 */
public class PlayBoardFrame extends JFrame {

	// Frame components
	private JPanel playBoardPanel;
	private final int GAMESIZE = 9;
	private JButton[] playBoardButtons = new JButton[GAMESIZE];
	private JPanel buttonPanel;
	private Button exitGame;
	private Button restartGame;
	private final JDialog dialog;
	private Timer timer;

	// Always player 1 first, player 2 second
	/**
	 *  Always player 1 first, player 2 second
	 */
	private JLabel playerLabel1; // "X"
	private JLabel playerLabel2; // "O"
	private JLabel vsLabel; // "VS"

	// private Player player1, player2;
	/**
	 * private Player player1, player2;
	 */
	private String mark; // "X" | "O", X first
	private Player player1, player2, currentPlayer; // player1 | player2, player1 first
	private final String playBoardButtonsValue[] = new String[GAMESIZE];
	private int stepsLeft;
    
	/**
	 * private Player player1, player2; draw the playBoard
	 * @param player1
	 * @param player2
	 */
	public PlayBoardFrame(Player player1, Player player2) {

		// set the frame
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 700);
		setTitle("Tic Tac Toe - by Ding Chen");

		/***** Game Panel ******/
		// add playBoardPanel
		playBoardPanel = new JPanel();
		playBoardPanel.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		playBoardPanel.setLayout(new GridLayout(3, 3));
		add(playBoardPanel, BorderLayout.CENTER);

		// add buttons of gamePanel
		for (int i = 0; i < GAMESIZE; i++) {
			JButton playBoardButton = new JButton("");
			playBoardButtons[i] = playBoardButton;
			playBoardButton.setName("" + i);
			playBoardButton.setFont(new Font("Lucida Grande", Font.BOLD, 50));
			playBoardPanel.add(playBoardButton);
			playBoardButton.addActionListener(event -> playBoardButtonClick(playBoardButton));
		}

		buttonPanel = new JPanel();
		buttonPanel.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		FlowLayout buttonPanel_fl = new FlowLayout(FlowLayout.CENTER);
		buttonPanel_fl.setHgap(50);
		buttonPanel.setLayout(buttonPanel_fl);

		// add labels of resultPanel
		playerLabel1 = new JLabel(player1.getName());
		vsLabel = new JLabel("VS");
		playerLabel2 = new JLabel(player2.getName());
		playerLabel1.setFont(new Font("Serif", Font.PLAIN, 25));
		vsLabel.setFont(new Font("Serif", Font.BOLD, 30));
		playerLabel2.setFont(new Font("Serif", Font.PLAIN, 25));

		// add buttons of resultPanel
		restartGame = new Button("Restart");
		restartGame.setFont(new Font("Serif", Font.PLAIN, 25));
		restartGame.addActionListener(event -> restartGameClick());
		exitGame = new Button("Exit");
		exitGame.setFont(new Font("Serif", Font.PLAIN, 25));
		exitGame.addActionListener(event -> exitGameClick());

		buttonPanel.add(playerLabel1);
		buttonPanel.add(vsLabel);
		buttonPanel.add(playerLabel2);
		buttonPanel.add(restartGame);
		buttonPanel.add(exitGame);
		add(buttonPanel, BorderLayout.SOUTH);

		// JDialog() - Starting message/dialog
		dialog = new JDialog();
		dialog.setTitle("Message");
		dialog.setModal(true);
		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

		final String gameStartMessage = "Game Starting...\n" + player1.getName() + " Move First.";
		final JOptionPane optionPane = new JOptionPane(gameStartMessage, JOptionPane.INFORMATION_MESSAGE,
				JOptionPane.DEFAULT_OPTION, null, new Object[] {}, null);
		dialog.setContentPane(optionPane);
		dialog.pack();

		// Initialize variables
		this.player1 = player1;
		this.player2 = player2;
		currentPlayer = player1;
		mark = "X";
		stepsLeft = GAMESIZE;
		playerLabel2.setForeground(Color.black);
		playerLabel1.setForeground(Color.yellow);

		// showStartDialog();

		setLocationRelativeTo(null); // center the frame
	}
	/**
	 * show start Dialog
	 *  create timer to dispose of dialog after 2 seconds
	 */

	private void showStartDialog() {
		// create timer to dispose of dialog after 2 seconds
		timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				dialog.dispose();
			}
		}, 2000);

		dialog.setLocationRelativeTo(null); // center the dialog box
		dialog.setVisible(true);
	}

	// action event for clicking play board button
	/**
	 * action event for clicking play board button
	 * @param playBoardButton
	 */
	private void playBoardButtonClick(JButton playBoardButton) {
		playBoardButton.setText(mark);
		if (mark.equals("X")) {
			playBoardButton.setForeground(Color.red);
			UIManager.put("Button.disabledText", Color.red); // Color change for disabled button text
		} else if (mark.equals("O")) {
			playBoardButton.setForeground(Color.red);
			UIManager.put("Button.disabledText", Color.red);
		}
		playBoardButton.setEnabled(false);
		int playBoardButtonIndex = Integer.valueOf(playBoardButton.getName());
		playBoardButtonsValue[playBoardButtonIndex] = mark;

		stepsLeft--;
		if (isWin(playBoardButtonIndex)) {
			// disable all buttons
			for (JButton btn : playBoardButtons) {
				btn.setEnabled(false);
			}

			String winningMessage = currentPlayer.getName() + " wins.";
			JOptionPane.showMessageDialog(this, winningMessage, "Tic Tac Toe", JOptionPane.INFORMATION_MESSAGE);
		} else {
			if (stepsLeft == 0) {

				String winningMessage = "Tie";
				JOptionPane.showMessageDialog(this, winningMessage, "Tic Tac Toe", JOptionPane.INFORMATION_MESSAGE);
			}

			changePlayer();
		}

	}
	/**
	 * show that game is winning
	 * @param playBoardButtonIndex
	 * @return
	 */

	private boolean isWin(int playBoardButtonIndex) {

		switch (playBoardButtonIndex) {
		case 0:
			if (isLined(0, 1, 2) || isLined(0, 3, 6) || isLined(0, 4, 8)) {
				return true;
			}
			break;
		case 1:
			if (isLined(1, 0, 2) || isLined(1, 4, 7)) {
				return true;
			}
			break;
		case 2:
			if (isLined(2, 1, 0) || isLined(2, 5, 8) || isLined(2, 4, 6)) {
				return true;
			}
			break;
		case 3:
			if (isLined(3, 4, 5) || isLined(3, 0, 6)) {
				return true;
			}
			break;
		case 4:
			if (isLined(4, 3, 5) || isLined(4, 1, 7) || isLined(4, 2, 6) || isLined(4, 0, 8)) {
				return true;
			}
			break;
		case 5:
			if (isLined(5, 4, 3) || isLined(5, 2, 8)) {
				return true;
			}
			break;
		case 6:
			if (isLined(6, 7, 8) || isLined(6, 3, 0) || isLined(6, 4, 2)) {
				return true;
			}
			break;
		case 7:
			if (isLined(7, 6, 8) || isLined(7, 4, 1)) {
				return true;
			}
			break;
		case 8:
			if (isLined(8, 7, 6) || isLined(8, 5, 2) || isLined(8, 4, 0)) {
				return true;
			}
			break;
		}

		return false;
	}
     /**
      * show how to win the game
      * @param a
      * @param b
      * @param c
      * @return
      * @precondition a = b = c
      */
	private boolean isLined(int a, int b, int c) {

		if (playBoardButtonsValue[a].equals(playBoardButtonsValue[b])
				&& playBoardButtonsValue[b].equals(playBoardButtonsValue[c])) {
			return true;
		} else {
			return false;
		}
	}

	
	/**
	 * mark changed with each play board button clicked
	 */
	private void changePlayer() {

		if (mark.equals("X")) {
			currentPlayer = player2;
			mark = "O";
			playerLabel1.setForeground(Color.black);
			playerLabel2.setForeground(Color.yellow);
		} else {
			currentPlayer = player1;
			mark = "X";
			playerLabel2.setForeground(Color.black);
			playerLabel1.setForeground(Color.yellow);
		}

		if (currentPlayer instanceof ComputerPlayer) {
			currentPlayer.move();
		}

	}

	
	/**
	 * // action event for clicking restart game button
	   // re-Initialize the board
	 */
	private void restartGameClick() {
		for (int i = 0; i < GAMESIZE; i++) {
			playBoardButtonsValue[i] = null;
		}

		for (JButton btn : playBoardButtons) {
			btn.setEnabled(true);
			btn.setText("");
		}

		currentPlayer = player1;
		mark = "X";
		stepsLeft = GAMESIZE;

		showStartDialog();
		playerLabel2.setForeground(Color.black);
		playerLabel1.setForeground(Color.yellow);

		if (currentPlayer instanceof ComputerPlayer) {
			currentPlayer.move();
		}
	}

	
	/**
	 * // action event for clicking exit game button
	 */
	private void exitGameClick() {
		if (JOptionPane.showConfirmDialog(this, "Back to Main Menu", "Tic Tac Toe",
				JOptionPane.YES_NO_OPTION) == JOptionPane.YES_NO_OPTION) {
			GameFrame mainMenu = new GameFrame();
			dispose();
			mainMenu.setVisible(true);
		}
	}

	
	/**
	 * // mimic clicking the play board button
	 * @param playBoardButtonNumber
	 */
	public void playBoardButtonClick(int playBoardButtonNumber) {
		playBoardButtons[playBoardButtonNumber].doClick();
	}

	
	/**
	 * // get current player
	 * @return
	 */
	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	
	/**
	 * // get play board buttons' values
	 * @postCondition string[] ! null
	 */
	public String[] getPlayBoardButtonsValue() {
		return playBoardButtonsValue;
	}

	
	/**
	 * // get stepsLeft
	 * @return
	 */
	public int getStepsLeft() {
		return stepsLeft;
	}

}

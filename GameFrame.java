import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
/**
 * 
 * @author dingchen
 *
 */
public class GameFrame extends JFrame {

	private JPanel contentPane;
	private JLabel gameTitleLabel;
	private JButton[] menuButtons;
	private final String GAME_TITLE = "Tic Tac Toe";
	private final String[] MENUS = new String[] { "Human VS Human", "Computer(Easy) VS Human",
			"Computer(Medium) VS Human" };
	private final JDialog dialog;
	private Timer timer;
	
	private Player[] players = new Player[2];
	private PlayBoardFrame playboard;
	

	/**
	 * Create the frame.
	 * @precondition length > 0
	 */
	public GameFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 700);
		setTitle("Tic Tac Toe - dd");

		// contentPane of the Frame
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new GridLayout(3, 1));
		setContentPane(contentPane);
		contentPane.setBackground(new Color(0x9391d6));

		// Title Label
		gameTitleLabel = new JLabel(GAME_TITLE, SwingConstants.CENTER);
		gameTitleLabel.setFont(new Font("Serif", Font.BOLD + Font.ITALIC, 50));
		contentPane.add(gameTitleLabel);

		// MenuBodyPanel
		JPanel menuBodyPanel = new JPanel();
		menuBodyPanel.setLayout(new GridLayout(1, 3));
		menuBodyPanel.setOpaque(false);
		JPanel menuLeftPanel = new JPanel();
		menuLeftPanel.setOpaque(false);
		JPanel menuRightPanel = new JPanel();
		menuRightPanel.setOpaque(false);
		JPanel menuPanel = new JPanel();
		menuPanel.setOpaque(false);
		menuPanel.setLayout(new GridLayout(MENUS.length, 1, 0, 20));
		menuButtons = new JButton[MENUS.length];
		for (int i = 0; i < MENUS.length; i++) {
			menuButtons[i] = new JButton(MENUS[i]);
			menuButtons[i].setFont(new Font("Serif", Font.BOLD, 12));
			menuPanel.add(menuButtons[i]);
			menuButtons[i].addActionListener(new myActionListener()); // Adding response event to buttons
		}
		menuBodyPanel.add(menuLeftPanel);
		menuBodyPanel.add(menuPanel);
		menuBodyPanel.add(menuRightPanel);
		contentPane.add(menuBodyPanel);

		// Game Start dialog box
		dialog = new JDialog();
		// dialog.setTitle("Message");
		dialog.setModal(true);
		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

		setLocationRelativeTo(null); // center the frame
	}
    /**
     * 
     * my action listener implements actionListener
     *
     */
	private class myActionListener implements ActionListener {
		// Implementing action listener for buttons
		public void actionPerformed(ActionEvent ae) {
			if (ae.getSource() == menuButtons[0]) {
				// "Human VS Human"
				players[0] = new HumanPlayer(1);
				players[1] = new HumanPlayer(2);

			} else if (ae.getSource() == menuButtons[1]) {
				// "Computer(Easy) VS Human"
				players[0] = new ComputerPlayer(0);
				players[1] = new HumanPlayer(2);

			} else if (ae.getSource() == menuButtons[2]) {
				// "Computer(Medium) VS Human"
				players[0] = new ComputerPlayer(1);
				players[1] = new HumanPlayer(2);
			}

			playboard = new PlayBoardFrame(players[0], players[1]);
			playboard.setVisible(true);
			players[0].setBoard(playboard);
			players[1].setBoard(playboard);
			showStartDialog(players[0].getName());
			dispose();
			if (players[0] instanceof ComputerPlayer) {
				players[0].move();
			}

		}
	}
    /**
     * show start dialog
     * @param firstPlayerName
     */
	private void showStartDialog(String firstPlayerName) {

		final JOptionPane optionPane = new JOptionPane(firstPlayerName + " Move First.",
				JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[] {}, null);
		dialog.setContentPane(optionPane);
		dialog.pack();

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

}

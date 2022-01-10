import javax.swing.JOptionPane;

public class HumanPlayer extends Player {
   /**
    * get Human player
    * @param playerNum
    */
	public HumanPlayer(int playerNum) {
		
		setName(JOptionPane.showInputDialog(null,"Enter Player" + playerNum + "'s name:","Player" + playerNum));

		if (this.getName() == null) {
			this.setName("Player " + playerNum);
		}

	}

	@Override
	public void move() {

	}

}

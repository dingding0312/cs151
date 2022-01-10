
abstract public class Player {

	private String name;
	private PlayBoardFrame board;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}

	public Player() {

	}

	
	/**
	 * // abstract function move(), polymorphism
	 */
	public abstract void move();
    /**
     * set Board
     * @param myplayboard
     */
	public void setBoard(PlayBoardFrame myplayboard) {
		board = myplayboard;
	}
    /**
     * 
     * @return board
     */
	public PlayBoardFrame getBoard() {
		return board;
	}
    /**
     * 
     * @return name
     */
	public String getName() {
		return name;
	}
     /**
      * 
      * @param name
      */
	public void setName(String name) {
		this.name = name;
	}

}

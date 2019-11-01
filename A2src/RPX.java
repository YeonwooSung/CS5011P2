/**
 * Class for RPX.
 * @author 160021429
 */
public class RPX extends Agent {
	RPX(Board board) {
		super(board);
	}

	RPX(Board board, int numOfLifes) {
		super(board, numOfLifes);
	}

	/**
	 * Make a move for RPX.
	 * @param x - current x
	 * @param y - current y
	 */
	public void makeMove(int x, int y) {
		try {
			// check if the given coordinate is already inspected
			if (!this.isInspected(x, y)) {

				// probe the current coordinate
				int result = this.probeCoordinate(x, y);

				// use switch statement to check the returned value of the probeCoordinate() method
				switch (result) {
					case 0:
					case 1:
					case 2:
					case 3:
					case 4:
					case 5:
					case 6:
						System.out.println("Probe " + x + " " + y);
						break;
					case -1:
						this.checkRemainingLife(x, y);
				}
			}
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
	}
}

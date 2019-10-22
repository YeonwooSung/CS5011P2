import java.util.ArrayList;


public abstract class Agent {
	protected int total;
	private int totalTornados;
	private boolean finished;

	protected boolean win;
	private int size1;
	private int size2;
	protected Board board;
	protected boolean[][] probed; //to check if the given coordinate is probed or yet to be probed
	protected boolean[][] flag;

	Agent(Board board) {
		this.board = board;

		size1 = board.board.length;
		size2 = board.board[0].length;
		probed = new boolean[size1][size2];
		flag = new boolean[size1][size2];

		total = size1 * size2;
		totalTornados = this.countNumOfTornados();
	}

	/**
	 * This method returns the array of neighbour coordinates.
	 * @param x - current x
	 * @param y - current y
	 * @return The array of neighbours.
	 */
	Coordinate[] getNeighbours(int x, int y) {
		ArrayList<Coordinate> neighbours = new ArrayList<Coordinate>();

		// check if the current x is 0
		if (x != 0) {
			Coordinate c = new Coordinate(x - 1, y);
			c.setInspected(this.isInspected(x - 1, y));
			neighbours.add(c);
		}

		// check if the current x is N - 1
		if (x != size1 - 1) {
			Coordinate c = new Coordinate(x + 1, y);
			c.setInspected(this.isInspected(x + 1, y));
			neighbours.add(c);
		}

		// check if the current x is 0
		if (y != 0) {
			if (x != 0) {
				Coordinate c = new Coordinate(x - 1, y - 1);
				c.setInspected(this.isInspected(x - 1, y - 1));
				neighbours.add(c);
			}

			Coordinate c = new Coordinate(x, y - 1);
			c.setInspected(this.isInspected(x, y - 1));
			neighbours.add(c);
		}

		// check if the current y is N - 1 
		if (y != size1 - 1) {
			if (x != size2 - 1) {
				Coordinate c = new Coordinate(x + 1, y + 1);
				c.setInspected(this.isInspected(x + 1, y + 1));
				neighbours.add(c);
			}

			Coordinate c = new Coordinate(x, y + 1);
			c.setInspected(this.isInspected(x, y + 1));
			neighbours.add(c);
		}

		Coordinate[] neighboursArray = new Coordinate[neighbours.size()];
		neighboursArray = neighbours.toArray(neighboursArray);

		return neighboursArray;
	}

	int probeCoordinate(int x, int y) {
		char c = board.board[y][x];
		probed[y][x] = true;

		// check if not tornado
		if (c != 't') {
			if (Character.isDigit(c)) {
				int i = Character.getNumericValue(c);
				switch (i) {
					case 0:
					case 1:
					case 2:
					case 3:
					case 4:
					case 5:
					case 6:
						return i;
					default:
						return -1;
				}
			} else {
				return -1;
			}
		} else {
			return -1;
		}
	}

	boolean isInspected(int x, int y) throws IndexOutOfBoundsException {
		return probed[y][x] || flag[y][x];
	}

	/**
	 * Getter for finished.
	 * @return finished
	 */
	boolean isFinished() {
		return finished;
	}

	/**
	 * Getter for totalTornados.
	 * @return totalTornados
	 */
	int getTotalTornados() {
		return totalTornados;
	}

	/**
	 * Count the number of total tornado.
	 * @return
	 */
	int countNumOfTornados() {
		int totalTornado = 0;
		char[][] map = board.board;

		for (int i = 0; i < flag.length; i++) {
			for (int j = 0; j < flag[i].length; j++) {
				if (map[i][j] == 't') {
					totalTornado += 1;
				}
			}
		}

		return totalTornado;
	}

	/**
	 * Count the number of neighboured flags.
	 * @param neighbours - array of neighboured coordinates
	 * @return the number of neighboured flags
	 */
	int countNumberOfNeighbouredFlags(Coordinate[] neighbours) {
		int numOfFlags = 0;

		for (int i = 0; i < neighbours.length; i++) {
			Coordinate coord = neighbours[i];
			// use if-else statement to check if the given coordinate is inspected
			if (this.isInspected(coord.getX(), coord.getY()) && flag[coord.getY()][coord.getX()]) {
				numOfFlags += 1;
			}
		}

		return numOfFlags;
	}

	int getAllFreeOrMakredNeighbours(Coordinate[] neighbours, ArrayList<Coordinate> probedCoords, ArrayList<Coordinate> uninspected) {
		int numOfFlags = 0;

		for (int i = 0; i < neighbours.length; i++) {
			Coordinate coord = neighbours[i];

			// use if-else statement to check if the given coordinate is inspected
			if (this.isInspected(coord.getX(), coord.getY())) {
				if (flag[coord.getY()][coord.getX()]) {
					numOfFlags += 1;
				} else {
					probedCoords.add(coord);
				}
			} else {
				uninspected.add(coord);
			}
		}

		return numOfFlags;
	}

	/**
	 * Check if all cells are inspected.
	 * @return If true, returns true. Otherwise, returns false.
	 */
	public boolean inspectedAll() {
		int counter = this.getTotalCount();
		return (counter == total);
	}

	/**
	 * Gets the total number of uncovered cells.
	 * @return the total number of uncovered cells
	 */
	public int getTotalCount() {
		int counter = 0;

		for (int i = 0; i < probed.length; i++) {
			for (int j = 0; j < probed[i].length; j++) {
				if (probed[i][j] || flag[i][j]) {
					counter += 1;
				}
			}
		}

		return counter;
	}

	/**
	 * Check if the number of flags is equal to T.
	 */
	void checkIfWin() {
		int counter = 0;

		for (int i = 0; i < flag.length; i++) {
			for (int j = 0; j < flag[i].length; j++) {
				if (flag[i][j]) {
					counter += 1;
				}
			}
		}

		// compare the total number of tornado and flags
		if (counter == totalTornados) {
			win = true;
		}
	}
}

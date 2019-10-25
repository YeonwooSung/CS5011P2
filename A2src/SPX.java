import java.util.ArrayList;

public class SPX extends Agent {
	SPX(Board board) {
		super(board);
	}

	SPX(Board board, int numOfLifes) {
		super(board, numOfLifes);
	}

	private boolean checkAllFreeNeighbours(int numOfMarked, int clue) {
		return (clue == numOfMarked);
	}

	private boolean checkAllMarkedNeighbours(int numOfMarked, int clue, int numOfUninspected) {
		return (clue - numOfMarked == numOfUninspected);
	}

	private boolean checkNeighbours(ArrayList<Coordinate> probedCoords, int x, int y) {
		for (Coordinate coord : probedCoords) {
			int x1 = coord.getX();
			int y1 = coord.getY();

			char c = board.board[y1][x1];
			if (!Character.isDigit(c)) {
				continue;
			}

			int i = Character.getNumericValue(c);

			if (i > 0) {
				Coordinate[] neighbourArray = this.getNeighbours(x1, y1);
				ArrayList<Coordinate> uninspectedArr = new ArrayList<Coordinate>();
				ArrayList<Coordinate> probedArr = new ArrayList<Coordinate>();

				int numOfNeighbouredFlags = this.getAllFreeOrMakredNeighbours(neighbourArray, probedArr, uninspectedArr);

				// check "All Free Neighbours"
				if (this.checkAllFreeNeighbours(numOfNeighbouredFlags, i)) {
					return false;
				}

				// check "All Marked Neighbours"
				if (this.checkAllMarkedNeighbours(numOfNeighbouredFlags, i, uninspectedArr.size())) {
					System.out.println("Flag " + x + " " + y);
					flag[y][x] = true; //mark a danger
				}
			} else if (i == 0) {
				Coordinate[] neighbourArray = this.getNeighbours(x1, y1);

				for (Coordinate coor : neighbourArray) {
					this.probeCoordinate(coor.getX(), coor.getY());
				}

				return false;
			}
		}

		return true;
	}

	void makeRandomMove(int counter) {
		int size = board.board.length;
		int newCounter = this.getTotalCount();

		do {
			int x = A2main.getRandomIntegerBetweenRange(0, size - 1); //get random integer
			int y = A2main.getRandomIntegerBetweenRange(0, size - 1); //get random integer

			if (isInspected(x, y)) {
				continue;
			}

			int ret = probeCoordinate(x, y);
			ArrayList<Coordinate> uninspected = this.getUninspectedNeighbours(this.getNeighbours(x, y));
			probeAndCheckReturnedValue(x, y, ret, uninspected);

			newCounter = this.getTotalCount();
		} while (newCounter == counter);
	}

	public void makeMove(int x, int y) {
		try {
			if (!this.isInspected(x, y)) {
				Coordinate[] neighbours = this.getNeighbours(x, y);

				ArrayList<Coordinate> probedCoords = new ArrayList<Coordinate>();
				ArrayList<Coordinate> uninspected = new ArrayList<Coordinate>();

				// get the number of neighboured flags, and fill the array lists with suitable coordinates
				this.getAllFreeOrMakredNeighbours(neighbours, probedCoords, uninspected);

				boolean checker = true;

				for (Coordinate coord : probedCoords) {
					int tempX = coord.getX();
					int tempY = coord.getY();

					char c = board.board[tempY][tempX];

					// check if one of neighbours is a cell that contains '0'
					if (Character.isDigit(c) && (Character.getNumericValue(c) == 0)) {
						Coordinate[] neighbourArr = this.getNeighbours(tempX, tempY);

						// use for loop to probe all adjacent cells
						for (int i = 0; i < neighbourArr.length; i++) {
							if (!this.isInspected(tempX, tempY)) {
								System.out.println("Probe " + tempX + " " + tempY);
								probed[tempY][tempX] = true;
							}
						}

						checker = false;
						break;
					}
				}

				// check if any of neighboured cells contains non-zero
				if (checker) {
					// check neighbour cells - "All Free Neighbours" & "All Marked Neighbours"
					if (this.checkNeighbours(probedCoords, x, y)) {
						return;
					}
				}

				// check if the current cell is uncovered
				if (this.isInspected(x, y)) {
					return;
				}

				int result = this.probeCoordinate(x, y); //probe the current coordinate

				probeAndCheckReturnedValue(x, y, result, uninspected);
			}
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get list of uninspected neighbours.
	 * @param neighbours - array of neighboured cells
	 * @return List of uninspected neighbours
	 */
	ArrayList<Coordinate> getUninspectedNeighbours(Coordinate[] neighbours) {
		ArrayList<Coordinate> uninspected = new ArrayList<Coordinate>();

		for (Coordinate coord : neighbours) {
			if (!this.isInspected(coord.getX(), coord.getY())) {
				uninspected.add(coord);
			}
		}

		return uninspected;
	}

	/**
	 * Check the value that the current cell contains.
	 * If the result is -1, then that means that the current cell contains the tornado.
	 * Henceforth, the game will be over, and error message will be printed out.
	 * Or if the result is 0, all neighbours will be probed.
	 * Otherwise, this method will do nothing.
	 *
	 * @param x - x coordinate of current cell
	 * @param y - y coordinate of current cell
	 * @param result - the result value that the current cell contains
	 * @param uninspected - list of uninspected neighbours
	 */
	void probeAndCheckReturnedValue(int x, int y, int result, ArrayList<Coordinate> uninspected) {
		System.out.println("Probe " + x + " " + y);

		switch (result) {
			case 0:
				/*
				 * As the current cell contains the digit '0', all neighbours are safe cells.
				 * Henceforth, call this method recursively to probe all neighbours.
				 */
				for (Coordinate coord : uninspected) {
					this.makeMove(coord.getX(), coord.getY());
				}

				break;
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
				break;
			case 6:
				for (Coordinate coord : uninspected) {
					flag[coord.getY()][coord.getX()] = true;
				}
				break;
			case -1:
				this.checkRemainingLife(x, y);
		}
	}
}

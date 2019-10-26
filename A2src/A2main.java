import org.logicng.io.parsers.ParserException;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.TimeoutException;

public class A2main {
	/**
	 * Print out the error message for the invalid id error
	 * @param id - invalid id string
	 */
	private static void printErrorMessageForInvalidID(String id) {
		System.out.println("Invalid ID - " + id);
		System.exit(0);
	}

	/**
	 * Print out the error message for the invalid agent type error.
	 * @param agent - invalid agent name
	 */
	private static void printErrorMessageForInvalidAgentType(String agent) {
		System.out.println("Invalid Agent - " + agent);
		System.exit(0);
	}

	/**
	 * Returns random integer within the given range.
	 * @param min - minimum value of the range
	 * @param max - maximum value of the range
	 * @return random integer
	 */
	public static int getRandomIntegerBetweenRange(int min, int max){
	    int x = (int) (Math.random()* ((max - min) + 1)) + min;
	    return x;
	}

	/**
	 * Check the id string, and returns the suitable World instance.
	 * @param id - id string
	 * @return Returns the corresponding World object.
	 */
	private static World getWorld(String id) {
		World world = null;

		// use if - else statement to validate the world ID
		if (id.startsWith("TEST")) {
			if (id.equals("TEST0")) {
				world = World.TEST0;
			} else if (id.equals("TEST1")) {
				world = World.TEST1;
			} else if (id.equals("TEST2")) {
				world = World.TEST2;
			} else {
				printErrorMessageForInvalidID(id);
			}
		} else {
			try {
				if (id.startsWith("S")) {
					String subStr = id.substring(1);
					int i = Integer.parseInt(subStr);

					// use switch statement to generate suitable world instance
					switch (i) {
						case 1:
							world = World.S1;
							break;
						case 2:
							world = World.S2;
							break;
						case 3:
							world = World.S3;
							break;
						case 4:
							world = World.S4;
							break;
						case 5:
							world = World.S5;
							break;
						case 6:
							world = World.S6;
							break;
						case 7:
							world = World.S7;
							break;
						case 8:
							world = World.S8;
							break;
						case 9:
							world = World.S9;
							break;
						case 10:
							world = World.S10;
							break;
						default:
							printErrorMessageForInvalidID(id);
					}
				} else if (id.startsWith("M")) {
					String subStr = id.substring(1);
					int i = Integer.parseInt(subStr);

					// use switch statement to generate suitable world instance
					switch (i) {
						case 1:
							world = World.M1;
							break;
						case 2:
							world = World.M2;
							break;
						case 3:
							world = World.M3;
							break;
						case 4:
							world = World.M4;
							break;
						case 5:
							world = World.M5;
							break;
						case 6:
							world = World.M6;
							break;
						case 7:
							world = World.M7;
							break;
						case 8:
							world = World.M8;
							break;
						case 9:
							world = World.M9;
							break;
						case 10:
							world = World.M10;
							break;
						default:
							printErrorMessageForInvalidID(id);
					}
				} else if (id.startsWith("L")) {
					String subStr = id.substring(1);
					int i = Integer.parseInt(subStr);

					// use switch statement to generate suitable world instance
					switch (i) {
						case 1:
							world = World.L1;
							break;
						case 2:
							world = World.L2;
							break;
						case 3:
							world = World.L3;
							break;
						case 4:
							world = World.L4;
							break;
						case 5:
							world = World.L5;
							break;
						case 6:
							world = World.L6;
							break;
						case 7:
							world = World.L7;
							break;
						case 8:
							world = World.L8;
							break;
						case 9:
							world = World.L9;
							break;
						case 10:
							world = World.L10;
							break;
						default:
							printErrorMessageForInvalidID(id);
					}
				} else if (id.startsWith("T")) {
					String subStr = id.substring(1);
					int i = Integer.parseInt(subStr);

					// use switch statement to generate suitable world instance
					switch (i) {
						case 1:
							world = World.T1;
							break;
						case 2:
							world = World.T2;
							break;
						case 3:
							world = World.T3;
							break;
						case 4:
							world = World.T4;
							break;
						default:
							printErrorMessageForInvalidID(id);
					}

				} else {
					printErrorMessageForInvalidID(id);
				}
			} catch (IndexOutOfBoundsException e) {
				printErrorMessageForInvalidID(id);
			} catch (NumberFormatException e) {
				printErrorMessageForInvalidID(id);
			}
		}

		return world;
	}

	public static void main(String[] args) throws ParserException, TimeoutException, ContradictionException {
		if (args.length < 2) {
			System.out.println("Usage: java A2main <RPX|SPX|SATX> <ID> [any param]");
			System.exit(0);
		}

		boolean multipleLife = false;

		if (args.length > 2) {
			try {
				int i = Integer.parseInt(args[2]);
				
				if (i < 1) {
					System.out.println("The number of lifes should be an positive integer!");
					System.exit(0);
				}

				multipleLife = true;
			} catch (NumberFormatException e) {
				System.out.println("Usage: java A2main <RPX|SPX|SATX> <ID> <numberOfLifes>");
				System.exit(0);
			}
		}

		String agentType = args[0];

		World world = getWorld(args[1]);
		Board board = new Board(world.map);

		int size = world.map.length;

		/* use if-else statement to check the agent type */

		if (agentType.equals("RPX")) {
			System.out.println("Randome Probe (RPX)\n");
			RPX rpx = new RPX(board);

			if (multipleLife) {
				int i = Integer.parseInt(args[2]);
				rpx = new RPX(board, i);
			}

			int maxVal = size - 1;
			int minVal = 0;

			int x = minVal;
			int y = maxVal;

			rpx.makeMove(x, y);

			x = size / 2;
			y = x;

			// use while loop to loop until the RPX probe all non-tornado cells
			while (!rpx.inspectedAll()) {
				rpx.makeMove(x, y);

				x = getRandomIntegerBetweenRange(minVal, maxVal); //get random integer
				y = getRandomIntegerBetweenRange(minVal, maxVal); //get random integer
			}

			rpx.checkIfWin(); //check if won the game

			System.out.print("\nGame end!\nResult : ");

			// check if win the game
			if (rpx.win) {
				System.out.println("Game won!");
			} else {
				System.out.println("Game lost!");
			}

		} else if (agentType.equals("SPX")) {
			System.out.println("Single point strategy for hexagonal worlds (SPX)\n");
			SPX spx = new SPX(board);

			if (multipleLife) {
				int i = Integer.parseInt(args[2]);
				spx = new SPX(board, i);
			}

			int centerX = size / 2;
			int centerY = centerX;

			System.out.println("Probe 0 0");
			spx.probed[0][0] = true;

			System.out.println("Probe " + centerX + " " + centerY);
			spx.probed[centerY][centerX] = true;

			int counter = 0;

			// use the while loop to loop until the Single Point Strategy Agent probed all cells
			while (!spx.inspectedAll()) {
				for (int i = 0; i < size; i++) {
					for (int j = 0; j < size; j++) {
						spx.makeMove(i, j);
					}
				}

				for (int i = centerX; i >= 0; i--) {
					for (int j = 0; j < size; j++) {
						spx.makeMove(i, j);
					}
				}

				for (int i = centerX; i < size; i++) {
					for (int j = 0; j < size; j++) {
						spx.makeMove(i, j);
					}
				}

				int newCounter = spx.getTotalCount();

				// check if no cells are uncovered
				if (newCounter == counter) {
					spx.makeRandomMove(counter);
				}

				counter = newCounter;
			}

			spx.checkIfWin(); //check if won the game

			System.out.print("\nGame end!\nResult : ");

			// check if win the game
			if (spx.win) {
				System.out.println("Game won!");
			} else {
				System.out.println("Game lost!");
			}

		} else if (agentType.equals("SATX")) {
			System.out.println("Satisfiability Strategy for hexagonal worlds (SATX)\n");
			SATX satx = new SATX(board);

			if (multipleLife) {
				int i = Integer.parseInt(args[2]);
				satx = new SATX(board, i);
			}

			int centerX = size / 2;
			int centerY = centerX;

			System.out.println("Probe 0 0");
			satx.probed[0][0] = true;

			System.out.println("Probe " + centerX + " " + centerY);
			satx.probed[centerY][centerX] = true;

			int counter = 0;

			// use the while loop to loop until the Single Point Strategy Agent probed all cells
			while (!satx.inspectedAll()) {
				for (int i = 0; i < size; i++) {
					for (int j = 0; j < size; j++) {
						satx.makeMove(i, j);
					}
				}

				for (int i = centerX; i >= 0; i--) {
					for (int j = 0; j < size; j++) {
						satx.makeMove(i, j);
					}
				}

				for (int i = centerX; i < size; i++) {
					for (int j = 0; j < size; j++) {
						satx.makeMove(i, j);
					}
				}

				int newCounter = satx.getTotalCount();

				// check if no cells are uncovered
				if (newCounter == counter) {
					satx.makeMoveWithSAT(); // use SAT solver to make a move

					newCounter = satx.getTotalCount();

					if (newCounter == counter) {
						satx.makeRandomMove(counter);
					}
				}

				counter = newCounter;
			}

			satx.checkIfWin(); //check if won the game

			System.out.print("\nGame end!\nResult : ");

			// check if win the game
			if (satx.win) {
				System.out.println("Game won!");
			} else {
				System.out.println("Game lost!");
			}

		} else {
			printErrorMessageForInvalidAgentType(agentType);
		}
	}
}

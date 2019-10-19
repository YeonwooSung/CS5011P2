import java.util.ArrayList;


public class A2main {
	private static void printErrorMessageForInvalidID(String id) {
		System.out.println("Invalid ID - " + id);
		System.exit(0);
	}

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
	private static int getRandomIntegerBetweenRange(int min, int max){
	    int x = (int) (Math.random()* ((max - min) + 1)) + min;
	    return x;
	}

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

	public static void main(String[] args) {
		if (args.length < 2) {
			System.out.println("Usage: java A2main <RPX|SPX|SATX> <ID> [any param]");
			System.exit(0);
		}

		String agentType = args[0];

		World world = getWorld(args[1]);
		Board board = new Board(world.map);

		int size = world.map.length;

		/* use if-else statement to check the agent type */

		if (agentType.equals("RPX")) {
			System.out.println("Randome Probe\n");
			RPX rpx = new RPX(board);

			int total = size * size;
			int totalTornados = rpx.getTotalTornados();
			total -= totalTornados; //get the number of non-tornado cells

			// use while loop to loop until the RPX probe all non-tornado cells
			while (total > 0) {
				int x = getRandomIntegerBetweenRange(0, size - 1); //get random integer
				int y = getRandomIntegerBetweenRange(0, size - 1); //get random integer

				rpx.makeMove(x, y);

				total -= 1;
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
			System.out.println("Single point strategy for hexagonal worlds\n");
			SPX spx = new SPX(board);

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

				if (newCounter == counter) {
					do {
						int x = getRandomIntegerBetweenRange(0, size - 1); //get random integer
						int y = getRandomIntegerBetweenRange(0, size - 1); //get random integer

						int ret = spx.probeCoordinate(x, y);
						ArrayList<Coordinate> uninspected = spx.getUninspectedNeighbours(spx.getNeighbours(x, y));
						spx.probeAndCheckReturnedValue(x, y, ret, uninspected);

						newCounter = spx.getTotalCount();
					} while (newCounter == counter);
				} else {
					counter = newCounter;
				}
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
			//TODO

		} else {
			printErrorMessageForInvalidAgentType(agentType);
		}
	}
}

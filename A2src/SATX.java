import java.util.ArrayList;

import org.logicng.formulas.Formula;
import org.logicng.formulas.FormulaFactory;
import org.logicng.io.parsers.ParserException;
import org.logicng.io.parsers.PropositionalParser;
import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;

public class SATX extends SPX {
	private ArrayList<Coordinate> literals;
	private ArrayList<ArrayList<Integer>> clauses;

	SATX(Board board) {
		super(board);
	}

	public void makeMoveWithSAT() throws ParserException, TimeoutException, ContradictionException {
		literals = new ArrayList<>();
        clauses = new ArrayList<>();

        String kbu = buildKBU(); //build the KBU

        if (kbu == null) {
        	return;
        }

        System.out.println("KBU in DIMACS form:");
        System.out.println(kbu);
        System.out.println();

        this.parseAndBuildDIMACS(kbu);

        if (!checkSafeProbe()) {
        	//TODO
        	System.out.println("OOOOOOOOOOOOOOOOOOOOO");
        }
	}

	/**
	 * Build the CNF KBU from propositional cell formulas.
	 * @return KBU string with CNF format
	 * @throws ParserException 
	 */
	private String buildKBU() throws ParserException {
		ArrayList<String> formulas = new ArrayList<>();
		char[][] map = board.board;
		boolean checker = false;

		for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                char current = map[i][j];

                if (this.probed[i][j] && current != '0') {
                	String formula = generateCellFormula(j, i).trim();

                	// check if the generated formula is an empty string
                	if (formula.isEmpty() || formula.equals("") || formula.matches("\\s+")) {
                		checker = true;
                		continue;
                	}
                	formulas.add(formula);
                }
            }
        }

		if (formulas.size() == 0 || checker) {
			return null;
		}

		StringBuilder sb = new StringBuilder();
		int finalIndex = formulas.size() - 1;

		/* Build single formula (KBU) */

        for (int i = 0; i <= finalIndex; i++) {
        	sb.append("(");
            sb.append(formulas.get(i));
            sb.append(")");

            if (i < finalIndex) {
            	sb.append(" & ");
            }
        }

        String kbu = sb.toString();

        /* Use LogicNG to convert KBU to CNF */

        FormulaFactory f = new FormulaFactory();
        PropositionalParser p = new PropositionalParser(f);
        Formula formula = p.parse(kbu);

        //Convert KBU to CNF
        String cnf = formula.cnf().toString();

        System.out.println("Unique Literals: ");

        for (int i = 0; i < literals.size(); i++) {
        	Coordinate coord = literals.get(i);
            System.out.println((i + 1) + ": " + coord.getName() + " = (" + coord.getX() + ", " + coord.getY() + ")");
        }

        System.out.println("Before CNF:");
        System.out.println(kbu);

        System.out.println("CNF KBU:");
        System.out.println(cnf);
        System.out.println();

        boolean buildingLiteral = false;

        for (int i = 0; i < cnf.length(); i++) {
        	char currentChar = cnf.charAt(i);
            if (currentChar == '@') {
                buildingLiteral = true;
                sb = new StringBuilder();
            }

            if (buildingLiteral) {
                if (currentChar != ' ' && currentChar != ')') {
                    sb.append(currentChar);
                } else {
                    buildingLiteral = false;
                    String currentLiteral = sb.toString();
                    for (int j = 0; j < literals.size(); j++) {
                        if (literals.get(j).getName().equals(currentLiteral)) {
                            cnf = cnf.substring(0, i - currentLiteral.length()) + (j+1) + cnf.substring(i);
                            i -= currentLiteral.length();
                            break;
                        }
                    }
                }
            }
        }

		return cnf;
	}

	/**
	 * Build propositional logic formula for a given cell.
	 * @param x - x coordinate
	 * @param y - y coordinate
	 * @return Propositional logic string
	 */
	private String getCellFormula(int x, int y) {
		Coordinate[] neighbours = this.getNeighbours(x, y);
		ArrayList<Coordinate> uninspected = this.getUninspectedNeighbours(neighbours);
		ArrayList<Coordinate> vars = new ArrayList<Coordinate>();

		for (Coordinate c : uninspected) {
			boolean found = false;

			for (Coordinate literal : literals) {
				if (c.isIdentical(literal)) {
					c.setName(literal.getName());
					found = true;
					break;
				}
			}

			if (!found) {
				c.setName("@RESERVED_CNF_" + literals.size());
				literals.add(c);
			}
			vars.add(c);
		}

		int numberOfVars = vars.size();
		String[] formulas = new String[numberOfVars];
		StringBuilder sb;

		/* use nested for loops to generate all possible formulas */

		for (int i = 0; i < numberOfVars; i++) {
			sb = new StringBuilder();

			for (int j = 0; j < numberOfVars; j++) {
				if (j != i) {
					sb.append("~");
				}

				sb.append(vars.get(j).getName());
				if (j < numberOfVars-1) {
					sb.append(" & ");
				}
			}

			formulas[i] = sb.toString();
		}

		sb = new StringBuilder();

		// use for loop to bind all formulas with 'OR' operator
		for (int i = 0; i < numberOfVars; i++) {
			sb.append("(");
            sb.append(formulas[i]);
            sb.append(")");

            if (i < numberOfVars - 1) {
            	sb.append(" | ");
            }
		}

		return sb.toString();
	}

	/**
	 * Build propositional logic formula for a given cell.
	 * @param x - x coordinate
	 * @param y - y coordinate
	 * @return Propositional logic string
	 */
	private String generateCellFormula(int x, int y) {
		Coordinate[] neighbours = this.getNeighbours(x, y);
		ArrayList<Coordinate> uninspected = this.getUninspectedNeighbours(neighbours);
		ArrayList<Coordinate> vars = new ArrayList<Coordinate>();

		for (Coordinate c : uninspected) {
			boolean found = false;

			for (Coordinate literal : literals) {
				if (c.isIdentical(literal)) {
					c.setName(literal.getName());
					found = true;
					break;
				}
			}

			if (!found) {
				c.setName("@RESERVED_CNF_" + literals.size());
				literals.add(c);
			}
			vars.add(c);
		}

		int numberOfVars = vars.size();
		String[] formulas = new String[numberOfVars];
		StringBuilder sb;

		int num = Character.getNumericValue(board.board[y][x]);
		int numOfFlags = this.countNumberOfNeighbouredFlags(neighbours);
		int r = num - numOfFlags;

		/* use nested for loops to generate all possible formulas */

		if (r > 1) {
			ArrayList<int[]> combinations = this.generateCombinations(numberOfVars, r);
			sb = new StringBuilder();
			int combinationSize = combinations.size();

			for (int a = 0; a < combinationSize; a++) {
				int[] combination = combinations.get(a);
				int index = 0;
				int target = combination[index];
				int finalIndex = combination.length - 1;

				sb.append("(");

				for (int i = 0; i < numberOfVars; i++) {
					if (i == target) {
						if (index != finalIndex) {
							index += 1;
							target = combination[index];
						}
					} else {
						sb.append("~");
					}

					sb.append(vars.get(i).getName());

					if (i < numberOfVars - 1) {
						sb.append(" & ");
					}
				}

				sb.append(")");

				if (a < combinationSize - 1) {
	            	sb.append(" | ");
	            }
			}

		} else {
			for (int i = 0; i < numberOfVars; i++) {
				sb = new StringBuilder();

				for (int j = 0; j < numberOfVars; j++) {
					if (j != i) {
						sb.append("~");
					}

					sb.append(vars.get(j).getName());
					if (j < numberOfVars-1) {
						sb.append(" & ");
					}
				}

				formulas[i] = sb.toString();
			}

			sb = new StringBuilder();

			// use for loop to bind all formulas with 'OR' operator
			for (int i = 0; i < numberOfVars; i++) {
				sb.append("(");
	            sb.append(formulas[i]);
	            sb.append(")");

	            if (i < numberOfVars - 1) {
	            	sb.append(" | ");
	            }
			}
		}

		System.out.println("num = " + num);
		System.out.println("r = " + r);
		System.out.println(sb.toString());
		System.out.println();
		return sb.toString();
	}

	/**
	 * Generates the list of combinations (n_C_r).
	 * @param n - n for n_C_r
	 * @param r - r for n_C_r
	 * @return the list of combinations
	 */
	public ArrayList<int[]> generateCombinations(int n, int r) {
		ArrayList<int[]> combinations = new ArrayList<>();
	    int[] combination = new int[r];

	    // initialize with lowest lexicographic combination
	    for (int i = 0; i < r; i++) {
	        combination[i] = i;
	    }

	    while (combination[r - 1] < n) {
	        combinations.add(combination.clone());

	        // generate next combination in lexicographic order
	        int t = r - 1;

	        while (t != 0 && combination[t] == n - r + t) {
	            t--;
	        }

	        combination[t]++;

	        for (int i = t + 1; i < r; i++) {
	            combination[i] = combination[i - 1] + 1;
	        }
	    }

	    return combinations;
	}

	/**
     * Build a list of clauses in DIMACS form.
     * @param cnf - KBU with DIMACS format
     */
	public void parseAndBuildDIMACS(String cnf) {
        StringBuilder sb = new StringBuilder();
        boolean inClause = false;
        boolean ignoreSpace = false;
        int negative = 1;

        ArrayList<Integer> clause =  new ArrayList<>();

        for (int i = 0; i < cnf.length(); i++) {
            char currentChar = cnf.charAt(i);
            if (currentChar == '(') {
                inClause = true;
                clause = new ArrayList<>();
                sb = new StringBuilder();
                continue;
            }

            if (inClause) {
                if (currentChar == '~') {
                    negative = -1;

                } else if (currentChar == ' ') {
                    if (!ignoreSpace) {
                        int literal = Integer.parseInt(sb.toString()) * negative;

                        clause.add(literal);

                        negative = 1;
                        sb = new StringBuilder();
                    }
                } else if (currentChar == ')') {
                    ignoreSpace = false;
                    inClause = false;

                    int literal = Integer.parseInt(sb.toString()) * negative;

                    clause.add(literal);
                    clauses.add(clause);

                    negative = 1;
                } else if (currentChar == '|') {
                    if (!ignoreSpace) {
                        ignoreSpace = true;
                    } else {
                        int literal = Integer.parseInt(sb.toString()) * negative;

                        clause.add(literal);

                        negative = 1;
                        sb = new StringBuilder();
                    }
                } else {
                    sb.append(currentChar);
                }
            }
        }
    }

	/**
     * Loops through all literals, checking the satisfiability of each one.
     * @return Returns true if a literal fails to satisfy the formula. Or if all satisfy the formula, returns false.
     * @throws TimeoutException
     * @throws ContradictionException
     */
    public boolean checkSafeProbe() throws TimeoutException, ContradictionException {
        for (int n = 0; n < literals.size(); n++) {
            if (n > 0) {
                clauses.remove(clauses.size() - 1);
            }

            ArrayList<Integer> aim = new ArrayList<>();

            aim.add((n + 1));
            clauses.add(aim);

            ISolver solver = SolverFactory.newDefault();
            solver.newVar(literals.size() + 1);
            solver.setExpectedNumberOfClauses(clauses.size());

            for (int i = 0; i < clauses.size(); i++) {
                ArrayList<Integer> clause = clauses.get(i);
                int[] arrayClause = new int[clause.size()];

                for (int j = 0; j < clause.size(); j++) {
                    arrayClause[j] = (int) clause.get(j);
                }

                solver.addClause(new VecInt(arrayClause));
            }

            IProblem problem = solver;

            System.out.println("Proving ~D(" + literals.get(n).getX() + ", " + literals.get(n).getY() + ")");

            // check if the formula is satisfiable
            if (!problem.isSatisfiable()) {
                System.out.println("Formula is NOT satisfiable - Cell is safe!");
                flagCell(n);

                return true;
            } else {
                System.out.println("Formula is satisfiable - Cannot be sure if the cell is safe.");
            }
        }

        System.out.println("The SAT solver cannot be certain any cell is safe. Must resort to random probe.\n");
        return false;
    }

    /**
     * Extracts cell information from the given literal
     * @param n - literal index
     */
    private void flagCell(int n) {
    	Coordinate coord = literals.get(n);

    	int x = coord.getX();
    	int y = coord.getY();

    	System.out.println("\nSAT solver - " + x + " " + y);
    	System.out.println("Probe " + x + " " + y);
    	int result = this.probeCoordinate(x, y);

    	if (result == -1) {
    		System.out.println("The coordinate (" + x + ", " + y + ") contains the tornado!");
			System.out.println("Game over!");
			System.exit(1);
    	}
    }
}

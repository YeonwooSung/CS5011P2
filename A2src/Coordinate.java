
public class Coordinate {
	private int x;
	private int y;
	private boolean inspected;

	Coordinate(int x, int y) {
		this.setX(x);
		this.setY(y);
	}

	/**
	 * Getter for x.
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * Setter for x.
	 * @param x the x to set
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Getter for y.
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * Setter for y.
	 * @param y the y to set
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * Getter for inspected.
	 * @return the inspected
	 */
	public boolean isInspected() {
		return inspected;
	}

	/**
	 * Setter for inspected.
	 * @param inspected the inspected to set
	 */
	public void setInspected(boolean inspected) {
		this.inspected = inspected;
	}

	/**
	 * Returns the string that contains the information about this coordinate.
	 * @return Coordinate information string
	 */
	public String getCoordinateString() {
		return "(" + x + ", " + y + ") - inspected = " + inspected;
	}
}

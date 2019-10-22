
public class Coordinate {
	private int x;
	private int y;
	private boolean inspected;
	private String name;

	Coordinate(int x, int y) {
		this.setX(x);
		this.setY(y);

		name = null;
	}

	/**
	 * Setter for name
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Getter for name.
	 * @return name
	 */
	public String getName() {
		return name;
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

	/**
	 * Check if the given coordinate is identical with this coordinate.
	 * @param other - other coordinate
	 * @return If identical, returns true. Otherwise, returns false.
	 */
	public boolean isIdentical(Coordinate other) {
		if (this.x == other.x && this.y == other.y) {
			return true;
		}
		return false;
	}
}

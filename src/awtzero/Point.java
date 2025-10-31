package awtzero;

/** 
 * A class representing a point in 2D space with integer coordinates.
*/
public class Point {
    /** The x coordinate */
	public int x;
    /** The y coordinate */
	public int y;
	
    /**
     * Accepts two floating point values, and {@code floor()}s them to an int
     * @param x X position as float
     * @param y Y position as float
     */
    public Point(float x, float y) {
    	this.x = (int) x;
    	this.y = (int) y;
    }
    
    /**
     * Accepts two integer values and stores it directly
     * @param x x postition
     * @param y y position
     */
    public Point(int x, int y) {
    	this.x = x;
    	this.y = y;
    }
    
    /** 
     * Get the x coordinate
     * @return the x coordinate as an {@code int}
     */
    public int getX() {
    	return this.x;
    }
    
    /** 
     * Get the y coordinate
     * @return the y coordinate as an {@code int}
     */
    public int getY() {
    	return this.y;
    }

    /**
     * Calculates the euclidean distance between this point and a different point
     * @param that The other point to calculate distance to
     * @return The euclidean distance as a {@code double}
     */
    public double getDistanceTo(Point that) {
    	return Math.sqrt(Math.pow(Math.abs(this.x - that.x), 2) + Math.pow(Math.abs(this.y - that.y), 2));
    }

    /**
     * @return The Point as a Vector2
     * @see Vector2
     */
    public Vector2 asVector2() {
    	return new Vector2(this.x, this.y);
    }
}

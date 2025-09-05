package awtzero;

public class Point {
	public int x;
	public int y;
	
    public Point(float x, float y) {
    	this.x = (int) x;
    	this.y = (int) y;
    }
    
    public Point(int x, int y) {
    	this.x = x;
    	this.y = y;
    }
    
    public int getX() {
    	return this.x;
    }
    
    public int getY() {
    	return this.y;
    }
    
    public int getDistanceTo(Point that) {
    	return (int) Math.sqrt(Math.pow(Math.abs(this.x - that.x), 2) + Math.pow(Math.abs(this.y - that.y), 2));
    }
}

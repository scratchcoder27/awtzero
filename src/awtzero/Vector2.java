package awtzero; 

import java.util.Vector;

public class Vector2 extends Vector<Integer> {
	public int x, y;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private void setVars () {
		this.set(0, x);
		this.set(0, y);
	}
	
	public Vector2 (int px, int py) {
		super(2);
		x = px;
		y = py;
        this.setVars();
	}
	
	public Vector2 (Point p) {
		super(2);
		x = p.x;
		y = p.y;
		this.setVars();		
	}
	
	public Vector2 add(Vector2 v) {
		return (new Vector2(x + v.x, y + v.y));
	}
	
	public void add_ip(Vector2 v) {
		x = x + v.x;
		y = y + v.y;
		this.setVars();
	}
	
	

}

package awtzero;

public class Vector2 {
	public double x, y;

	public Vector2(double px, double py) {
		x = px;
		y = py;
	}

	public Vector2(Vector2 v) {
		x = v.x;
		y = v.y;
	}

	public Vector2(Point p) {
		x = (double) p.x;
		y = (double) p.y;
	}

	public Vector2 add(Vector2 v) {
		return (new Vector2(x + v.x, y + v.y));
	}

	public void add_ip(Vector2 v) {
		x = x + v.x;
		y = y + v.y;
	}

	public Vector2 subtract(Vector2 v) {
		return (new Vector2(x - v.x, y - v.y));
	}

	public void subtract_ip(Vector2 v) {
		this.x -= v.x;
		this.y -= v.y;
	}

	public double getMagnitude() {
		return Math.sqrt((x * x) + (y * y)); // sqrt(a^2 + b^2)
	}

	public double getDirection() {
		return Math.atan2(y, x); // -pi to pi
	}

	public static double getMagnitude(Vector2 v) {
		return Math.sqrt((v.x * v.x) + (v.y * v.y)); // sqrt(a^2 + b^2)
	}

	public static double getDirection(Vector2 v) {
		return Math.atan2(v.y, v.x); // -pi to pi
	}

	public void fromMagnitudeDirection(double magnitude, double direction) {
		this.x = Math.cos(direction) * magnitude;
		this.y = Math.sin(direction) * magnitude;
	}

	public static double getDotProduct(Vector2 v1, Vector2 v2) {
		return (v1.x * v2.x) + (v1.y * v2.y);
	}

	public double dotProduct(Vector2 v) {
		return getDotProduct(this, v);
	}

	public static double getAngleBetween(Vector2 v1, Vector2 v2) {
		double dot = getDotProduct(v1, v2);
		double mag1 = v1.getMagnitude();
		double mag2 = v2.getMagnitude();

		// Avoid division by zero
		if (mag1 == 0 || mag2 == 0)
			return 0;

		double cosTheta = dot / (mag1 * mag2); // θ = arccos((A⋅B) / (∣A∣*∣B∣)​)

		// Clamp due to floating-point rounding errors
		cosTheta = Math.max(-1.0, Math.min(1.0, cosTheta));

		return Math.acos(cosTheta); // in radians
	}

	public double angleBetween(Vector2 v) {
		return getAngleBetween(this, v);
	}

	public Vector2 setDirection(double dir) {
		Vector2 vec = new Vector2(0, 0);
		vec.fromMagnitudeDirection(this.getMagnitude(), dir);
		return vec;
	}

	public void setDirection_ip(double dir) {
		fromMagnitudeDirection(this.getMagnitude(), dir);
	}

	public static Vector2 normalizeVector(Vector2 v) {
		double mag = v.getMagnitude();
		return new Vector2(v.x / mag, v.y / mag);
	}

	public Vector2 normalize() {
		return normalizeVector(this);
	}

	public void normalize_ip() {
		double mag = getMagnitude();
		x /= mag;
		y /= mag;
	}

	public Point asPoint() {
		return new Point((int)x, (int) y);
	}

	@Override
	public String toString() {
		return String.format("(%.5f, %.5f)", x, y);
	}
}

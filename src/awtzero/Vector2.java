package awtzero;

/**
 * Represents a two-dimensional vector with double-precision components (x, y).
 * This class provides standard vector operations such as addition, subtraction,
 * magnitude, direction, dot product, normalization, and angle calculation.
 */
public class Vector2 {
    /** The x-component of the vector. */
    public double x;
    /** The y-component of the vector. */
    public double y;

    /**
     * Constructs a new Vector2 with the specified x and y components.
     *
     * @param px The x-component.
     * @param py The y-component.
     */
    public Vector2(double px, double py) {
        x = px;
        y = py;
    }

    /**
     * Constructs a new Vector2 as a copy of another Vector2.
     *
     * @param v The Vector2 to copy.
     */
    public Vector2(Vector2 v) {
        x = v.x;
        y = v.y;
    }

    /**
     * Constructs a new Vector2 from an existing {@link Point} object,
     * converting its integer coordinates to double.
     *
     * @param p The {@link Point} to convert.
     */
    public Vector2(Point p) {
        x = (double) p.x;
        y = (double) p.y;
    }

    /**
     * Returns a new Vector2 that is the sum of this vector and another vector.
     * The original vector is not modified.
     *
     * @param v The vector to add.
     * @return A new Vector2 representing the result of the addition.
     */
    public Vector2 add(Vector2 v) {
        return (new Vector2(x + v.x, y + v.y));
    }

    /**
     * Adds another vector to this vector in place (in-place addition).
     * This vector's x and y components are modified.
     *
     * @param v The vector to add.
     */
    public void add_ip(Vector2 v) {
        x = x + v.x;
        y = y + v.y;
    }

    /**
     * Returns a new Vector2 that is the difference between this vector and another vector.
     * The original vector is not modified.
     *
     * @param v The vector to subtract.
     * @return A new Vector2 representing the result of the subtraction.
     */
    public Vector2 subtract(Vector2 v) {
        return (new Vector2(x - v.x, y - v.y));
    }

    /**
     * Subtracts another vector from this vector in place (in-place subtraction).
     * This vector's x and y components are modified.
     *
     * @param v The vector to subtract.
     */
    public void subtract_ip(Vector2 v) {
        this.x -= v.x;
        this.y -= v.y;
    }

    /**
     * Calculates the magnitude (length) of this vector.
     *
     * @return The magnitude of the vector: $\sqrt{x^2 + y^2}$.
     */
    public double getMagnitude() {
        return Math.sqrt((x * x) + (y * y)); // sqrt(x^2 + y^2)
    }

    /**
     * Calculates the direction (angle) of this vector in radians.
     * The angle is measured counter-clockwise from the positive x-axis,
     * in the range $(-\pi, \pi]$.
     *
     * @return The angle of the vector in radians.
     */
    public double getDirection() {
        return Math.atan2(y, x); // -pi to pi
    }

    /**
     * Calculates the magnitude (length) of a static vector.
     *
     * @param v The vector to measure.
     * @return The magnitude of the vector: $\sqrt{x^2 + y^2}$.
     */
    public static double getMagnitude(Vector2 v) {
        return Math.sqrt((v.x * v.x) + (v.y * v.y)); // sqrt(x^2 + y^2)
    }

    /**
     * Calculates the direction (angle) of a static vector in radians.
     *
     * @param v The vector to measure.
     * @return The angle of the vector in radians, in the range $(-\pi, \pi]$.
     */
    public static double getDirection(Vector2 v) {
        return Math.atan2(v.y, v.x); // -pi to pi
    }

    /**
     * Sets the x and y components of this vector based on a given magnitude and direction (angle).
     *
     * @param magnitude The new length of the vector.
     * @param direction The new angle of the vector in radians.
     */
    public void fromMagnitudeDirection(double magnitude, double direction) {
        this.x = Math.cos(direction) * magnitude;
        this.y = Math.sin(direction) * magnitude;
    }

    /**
     * Calculates the dot product of two vectors.
     *
     * @param v1 The first vector.
     * @param v2 The second vector.
     * @return The dot product: $v1.x \cdot v2.x + v1.y \cdot v2.y$.
     */
    public static double getDotProduct(Vector2 v1, Vector2 v2) {
        return (v1.x * v2.x) + (v1.y * v2.y);
    }

    /**
     * Calculates the dot product of this vector and another vector.
     *
     * @param v The other vector.
     * @return The dot product.
     */
    public double dotProduct(Vector2 v) {
        return getDotProduct(this, v);
    }

    /**
     * Calculates the angle between two vectors in radians.
     * The result is in the range $[0, \pi]$.
     *
     * @param v1 The first vector.
     * @param v2 The second vector.
     * @return The angle between the two vectors in radians.
     */
    public static double getAngleBetween(Vector2 v1, Vector2 v2) {
        double dot = getDotProduct(v1, v2);
        double mag1 = v1.getMagnitude();
        double mag2 = v2.getMagnitude();

        // Avoid division by zero
        if (mag1 == 0 || mag2 == 0)
            return 0;

        double cosTheta = dot / (mag1 * mag2); // $\cos \theta = \frac{v_1 \cdot v_2}{|v_1||v_2|}$

        // Clamp due to floating-point rounding errors (must be in [-1, 1] for acos)
        cosTheta = Math.max(-1.0, Math.min(1.0, cosTheta));

        return Math.acos(cosTheta); // in radians
    }

    /**
     * Calculates the angle between this vector and another vector in radians.
     *
     * @param v The other vector.
     * @return The angle between the two vectors in radians.
     */
    public double angleBetween(Vector2 v) {
        return getAngleBetween(this, v);
    }

    /**
     * Returns a new Vector2 with the same magnitude as this vector but with the specified direction.
     * The original vector is not modified.
     *
     * @param dir The new direction (angle) in radians.
     * @return A new Vector2 with the adjusted direction.
     */
    public Vector2 setDirection(double dir) {
        Vector2 vec = new Vector2(0, 0);
        vec.fromMagnitudeDirection(this.getMagnitude(), dir);
        return vec;
    }

    /**
     * Changes the direction of this vector in place while preserving its magnitude.
     *
     * @param dir The new direction (angle) in radians.
     */
    public void setDirection_ip(double dir) {
        fromMagnitudeDirection(this.getMagnitude(), dir);
    }

    /**
     * Returns a new Vector2 that is the unit vector (magnitude 1) of the given vector.
     *
     * @param v The vector to normalize.
     * @return A new unit Vector2 in the same direction as {@code v}.
     */
    public static Vector2 normalizeVector(Vector2 v) {
        double mag = v.getMagnitude();
        // Handle zero vector case (though normalization isn't well-defined for a zero vector)
        if (mag == 0) return new Vector2(0, 0);
        return new Vector2(v.x / mag, v.y / mag);
    }

    /**
     * Returns a new Vector2 that is the unit vector (magnitude 1) of this vector.
     * The original vector is not modified.
     *
     * @return A new unit Vector2 in the same direction.
     */
    public Vector2 normalize() {
        return normalizeVector(this);
    }

    /**
     * Normalizes this vector in place, changing its magnitude to 1 (making it a unit vector).
     *
     */
    public void normalize_ip() {
        double mag = getMagnitude();
        if (mag != 0) {
            x /= mag;
            y /= mag;
        }
    }

    /**
     * Multiply the components of the vector by a scalar
     * @param scalar the scalar value
     */
    public void multiplyScalar_ip(double scalar) {
        this.x *= scalar;
        this.y *= scalar;
    }

    /**
     * Multiply the components of the vector by a scalar and returns it without changing the original 
     * @param scalar the scalar value
     * @return The vector with the multiplication applied
     */
    public Vector2 multiplyScalar(double scalar) {
        Vector2 v = new Vector2(this);
        v.x *= scalar;
        v.y *= scalar;
        return v;
    }


    /**
     * Converts this Vector2 to an AWT {@link Point} by casting the double components to integers.
     * <p><strong> NOTE: This results in a loss of precision. </strong></p>
     *
     * @return A new {@link Point} object.
     */
    public Point asPointFast() {
        return new Point((int)x, (int) y);
    }

    /**
     * Converts this Vector2 to an AWT {@link Point} by rounding the double components
     * <p><strong> NOTE: This results in a loss of precision. </strong></p>
     *
     * @return A new {@link Point} object.
     */
    public Point asPoint() {
        return new Point((int) Math.round(x), (int) Math.round(y));
    }

    /**
     * Returns a string representation of this Vector2, formatted to five decimal places.
     *
     * @return A string in the format "(x.xxxxx, y.yyyyy)".
     */
    @Override
    public String toString() {
        return String.format("(%.5f, %.5f)", x, y);
    }
}
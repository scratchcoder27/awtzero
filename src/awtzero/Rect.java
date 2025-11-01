package awtzero;

import java.awt.Point;
import java.awt.Dimension;
import java.awt.geom.Line2D;
import java.util.*;

/**
 * <p>A class representing a rectangular area, defined by its top-left corner (x, y)
 * and its dimensions (width, height).</p>
 * <p>This class provides utility methods for manipulation, collision detection,
 * and querying various rectangular properties.</p>
 * <p>It is (almost) a direct adaptation of Pygame's Rect class to Java.</p>
 * {@link https://www.pygame.org/docs/ref/rect.html}
 */

public class Rect {
    /** The x-coordinate of the top-left corner. */
    public int x;
    /** The y-coordinate of the top-left corner. */
    public int y;
    /** The width of the rectangle. */
    public int width;
    /** The height of the rectangle. */
    public int height;

    /**
     * Constructs a new Rect with specified position and dimensions.
     *
     * @param left The x-coordinate of the left edge.
     * @param top The y-coordinate of the top edge.
     * @param width The width of the rectangle.
     * @param height The height of the rectangle.
     */
    public Rect(int left, int top, int width, int height) {
        this.x = left;
        this.y = top;
        this.width = width;
        this.height = height;
    }

    /**
     * Constructs a new Rect using a {@link Point} for position and a {@link Dimension} for size.
     *
     * @param pos The top-left position of the rectangle.
     * @param size The width and height of the rectangle.
     */
    public Rect(Point pos, Dimension size) {
        this(pos.x, pos.y, size.width, size.height);
    }

    /**
     * Constructs a copy of an existing Rect.
     *
     * @param r The Rect to copy.
     */
    public Rect(Rect r) {
        this(r.x, r.y, r.width, r.height);
    }

    // Virtual attributes
    /**
     * Returns the x-coordinate of the left edge of the rectangle.
     *
     * @return The x-coordinate of the left edge.
     */
    public int left() { return x; }

    /**
     * Returns the x-coordinate of the right edge of the rectangle (exclusive).
     *
     * @return The x-coordinate of the right edge.
     */
    public int right() { return x + width; }

    /**
     * Returns the y-coordinate of the top edge of the rectangle.
     *
     * @return The y-coordinate of the top edge.
     */
    public int top() { return y; }

    /**
     * Returns the y-coordinate of the bottom edge of the rectangle (exclusive).
     *
     * @return The y-coordinate of the bottom edge.
     */
    public int bottom() { return y + height; }

    /**
     * Calculates and returns the center point of the rectangle.
     *
     * @return A new {@link Point} representing the center of the rectangle.
     */
    public Point center() { return new Point(x + width / 2, y + height / 2); }

    /**
     * Moves the rectangle so that its center is at the specified point.
     *
     * @param p The new center point for the rectangle.
     */
    public void setCenter(Point p) {
        x = p.x - width / 2;
        y = p.y - height / 2;
    }

    /**
     * Returns the dimensions (width and height) of the rectangle.
     *
     * @return A new {@link Dimension} object with the width and height.
     */
    public Dimension size() { return new Dimension(width, height); }

    /**
     * Creates a new Rect that is a copy of this Rect.
     *
     * @return A new Rect instance with the same position and dimensions.
     */
    public Rect copy() {
        return new Rect(this);
    }

    /**
     * Creates a new Rect that is moved by the specified offsets.
     * The original Rect is not modified.
     *
     * @param dx The horizontal offset to move by.
     * @param dy The vertical offset to move by.
     * @return A new Rect instance at the new position.
     */
    public Rect move(int dx, int dy) {
        return new Rect(x + dx, y + dy, width, height);
    }

    /**
     * Moves the current Rect instance by the specified offsets in place.
     *
     * @param dx The horizontal offset to move by.
     * @param dy The vertical offset to move by.
     */
    public void move_ip(int dx, int dy) {
        x += dx;
        y += dy;
    }

    /**
     * Creates a new Rect that is grown or shrunk by the specified amounts.
     * The center remains the same. The original Rect is not modified.
     *
     * @param dx The amount to change the width by (will be divided between left and right edges).
     * @param dy The amount to change the height by (will be divided between top and bottom edges).
     * @return A new Rect instance with the new dimensions.
     */
    public Rect inflate(int dx, int dy) {
        return new Rect(x - dx / 2, y - dy / 2, width + dx, height + dy);
    }

    /**
     * Grows or shrinks the current Rect instance by the specified amounts in place.
     * The center remains the same.
     *
     * @param dx The amount to change the width by.
     * @param dy The amount to change the height by.
     */
    public void inflate_ip(int dx, int dy) {
        x -= dx / 2;
        y -= dy / 2;
        width += dx;
        height += dy;
    }

    /**
     * Creates a new Rect that is scaled by the given factors.
     * Scaling is relative to the center of the rectangle, which remains constant.
     * The original Rect is not modified.
     *
     * @param sx The factor to scale the width by.
     * @param sy The factor to scale the height by.
     * @return A new Rect instance with scaled dimensions and adjusted position.
     */
    public Rect scale_by(double sx, double sy) {
        int newW = (int) Math.round(width * sx);
        int newH = (int) Math.round(height * sy);
        // Recalculate position to keep center stable
        int newX = (int) Math.round(center().x - newW / 2.0);
        int newY = (int) Math.round(center().y - newH / 2.0);
        return new Rect(newX, newY, newW, newH);
    }

    /**
     * Creates a new Rect that is uniformly scaled by the given factor.
     * The original Rect is not modified.
     *
     * @param scale The uniform factor to scale both width and height by.
     * @return A new Rect instance with scaled dimensions and adjusted position.
     */
    public Rect scale_by(double scale) {
        return scale_by(scale, scale);
    }

    /**
     * Scales the current Rect instance by the given factors in place.
     * The center remains the same.
     *
     * @param sx The factor to scale the width by.
     * @param sy The factor to scale the height by.
     */
    public void scale_by_ip(double sx, double sy) {
        Rect r = scale_by(sx, sy);
        this.x = r.x; this.y = r.y;
        this.width = r.width; this.height = r.height;
    }

    /**
     * Uniformly scales the current Rect instance by the given factor in place.
     * The center remains the same.
     *
     * @param scale The uniform factor to scale both width and height by.
     */
    public void scale_by_ip(double scale) {
        scale_by_ip(scale, scale);
    }


    /**
     * Updates the position and dimensions of this Rect.
     *
     * @param left The new x-coordinate of the left edge.
     * @param top The new y-coordinate of the top edge.
     * @param width The new width.
     * @param height The new height.
     */
    public void update(int left, int top, int width, int height) {
        this.x = left;
        this.y = top;
        this.width = width;
        this.height = height;
    }

    /**
     * Updates the position and dimensions of this Rect using {@link Point} and {@link Dimension}.
     *
     * @param pos The new top-left position.
     * @param size The new width and height.
     */
    public void update(Point pos, Dimension size) {
        update(pos.x, pos.y, size.width, size.height);
    }

    /**
     * Updates the position and dimensions of this Rect to match another Rect.
     *
     * @param other The Rect to copy properties from.
     */
    public void update(Rect other) {
        update(other.x, other.y, other.width, other.height);
    }


    /**
     * Creates a new Rect that is restricted to remain inside the given bounding Rect.
     * The size of the Rect is preserved. The original Rect is not modified.
     *
     * @param bounds The Rect to clamp against.
     * @return A new Rect instance clamped to the bounds.
     */
    public Rect clamp(Rect bounds) {
        int newX = Math.max(bounds.x, Math.min(x, bounds.right() - width));
        int newY = Math.max(bounds.y, Math.min(y, bounds.bottom() - height));
        return new Rect(newX, newY, width, height);
    }

    /**
     * Restricts the current Rect instance to remain inside the given bounding Rect in place.
     * The size of the Rect is preserved.
     *
     * @param bounds The Rect to clamp against.
     */
    public void clamp_ip(Rect bounds) {
        Rect r = clamp(bounds);
        this.x = r.x;
        this.y = r.y;
    }

    /**
     * Creates a new Rect that is the intersection of this Rect and another Rect.
     * If the rectangles do not overlap, a Rect with zero width and height at (0, 0) is returned.
     * The original Rect is not modified.
     *
     * @param other The Rect to clip against.
     * @return A new Rect representing the area of intersection.
     */
    public Rect clip(Rect other) {
        int nx = Math.max(x, other.x);
        int ny = Math.max(y, other.y);
        int nr = Math.min(right(), other.right());
        int nb = Math.min(bottom(), other.bottom());
        if (nr < nx || nb < ny) return new Rect(0, 0, 0, 0);
        return new Rect(nx, ny, nr - nx, nb - ny);
    }

    /**
     * Clips a line segment to the boundaries of this Rect.
     *
     * @param r The Rect boundary to clip against.
     * @param x1 The starting x-coordinate of the line.
     * @param y1 The starting y-coordinate of the line.
     * @param x2 The ending x-coordinate of the line.
     * @param y2 The ending y-coordinate of the line.
     * @return An array of {@link Point}s. If the line intersects the Rect, it returns
     * an array with the clipped start and end points (currently a stub returning
     * the original points if it intersects at all). If it does not intersect, an empty array.
     */
    public static Point[] clipline(Rect r, int x1, int y1, int x2, int y2) {
        Line2D line = new Line2D.Float(x1, y1, x2, y2);
        java.awt.Rectangle bounds = new java.awt.Rectangle(r.x, r.y, r.width, r.height);
        if (!line.intersects(bounds)) return new Point[0];
        // NOTE: This is a stub implementation. Real line clipping (e.g., Cohen-Sutherland)
        // would calculate the new intersection points.
        return new Point[]{new Point(x1, y1), new Point(x2, y2)};
    }

    // Union
    /**
     * Creates a new Rect that is the smallest rectangle encompassing both this Rect and another Rect.
     * The original Rect is not modified.
     *
     * @param other The Rect to include in the union.
     * @return A new Rect representing the union of the two rectangles.
     */
    public Rect union(Rect other) {
        int nx = Math.min(x, other.x);
        int ny = Math.min(y, other.y);
        int nr = Math.max(right(), other.right());
        int nb = Math.max(bottom(), other.bottom());
        return new Rect(nx, ny, nr - nx, nb - ny);
    }

    /**
     * Expands this Rect in place to become the smallest rectangle encompassing both this Rect and another Rect.
     *
     * @param other The Rect to include in the union.
     */
    public void union_ip(Rect other) {
        Rect r = union(other);
        update(r);
    }

    /**
     * Creates a new Rect that is the smallest rectangle encompassing this Rect and a list of other Rects.
     * The original Rect is not modified.
     *
     * @param rects A list of Rects to include in the union.
     * @return A new Rect representing the union of all rectangles.
     */
    public Rect unionall(List<Rect> rects) {
        Rect result = this.copy();
        for (Rect r : rects) result = result.union(r);
        return result;
    }

    /**
     * Expands this Rect in place to become the smallest rectangle encompassing this Rect and a list of other Rects.
     *
     * @param rects A list of Rects to include in the union.
     */
    public void unionall_ip(List<Rect> rects) {
        Rect r = unionall(rects);
        update(r);
    }

    // Fit
    /**
     * Creates a new Rect, scaled down to fit inside the *target* Rect while maintaining its aspect ratio,
     * and then clamped within the *target* Rect.
     * The original Rect is not modified.
     *
     * @param target The Rect to fit this Rect into.
     * @return A new Rect instance that is scaled and centered to fit within the target.
     */
    public Rect fit(Rect target) {
        double scale = Math.min((double)target.width / width, (double)target.height / height);
        return this.scale_by(scale).clamp(target);
    }

    // Normalize
    /**
     * Normalizes the Rect's width and height to be non-negative.
     * If width or height is negative, the x or y coordinate is adjusted, and the
     * dimension is made positive (e.g., swapping top-left and bottom-right points).
     */
    public void normalize() {
        if (width < 0) {
            x += width;
            width = -width;
        }
        if (height < 0) {
            y += height;
            height = -height;
        }
    }

    // Collision methods
    /**
     * Checks if this Rect completely contains the other Rect.
     *
     * @param r The other Rect to check for containment.
     * @return true if {@code r} is entirely inside this Rect, false otherwise.
     */
    public boolean contains(Rect r) {
        return r.x >= x && r.right() <= right() && r.y >= y && r.bottom() <= bottom();
    }

    /**
     * Checks if a point is inside this Rect.
     * The right and bottom edges are exclusive.
     *
     * @param px The x-coordinate of the point.
     * @param py The y-coordinate of the point.
     * @return true if the point is inside the Rect, false otherwise.
     */
    public boolean collidepoint(int px, int py) {
        return px >= x && px < right() && py >= y && py < bottom();
    }

    /**
     * Checks if this Rect overlaps or touches another Rect.
     *
     * @param r The other Rect to check for collision.
     * @return true if the two Rects intersect, false otherwise.
     */
    public boolean colliderect(Rect r) {
        return r.right() > x && r.x < right() && r.bottom() > y && r.y < bottom();
    }

    /**
     * Finds the index of the first Rect in the list that collides with this Rect.
     *
     * @param list The list of Rects to check against.
     * @return The index of the first colliding Rect, or -1 if no collision is found.
     */
    public int collidelist(List<Rect> list) {
        for (int i = 0; i < list.size(); i++) {
            if (colliderect(list.get(i))) return i;
        }
        return -1;
    }

    /**
     * Finds the indices of all Rects in the list that collide with this Rect.
     *
     * @param list The list of Rects to check against.
     * @return A list of integer indices for all colliding Rects.
     */
    public List<Integer> collidelistall(List<Rect> list) {
        List<Integer> hits = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (colliderect(list.get(i))) hits.add(i);
        }
        return hits;
    }

    /**
     * Checks if this Rect collides with any Rect in the provided list.
     *
     * @param objs The list of Rects to check against.
     * @return true if a collision is found, false otherwise.
     */
    public boolean collideobjects(List<Rect> objs) {
        return collidelist(objs) != -1;
    }

    /**
     * Finds the first key-value entry in the map whose Rect value collides with this Rect.
     *
     * @param map A map where the values are Rects.
     * @return The first {@code Map.Entry<String, Rect>} that collides, or {@code null} if no collision is found.
     */
    public Map.Entry<String, Rect> collidedict(Map<String, Rect> map) {
        for (Map.Entry<String, Rect> entry : map.entrySet()) {
            if (colliderect(entry.getValue())) return entry;
        }
        return null;
    }

    /**
     * Finds all key-value entries in the map whose Rect value collides with this Rect.
     *
     * @param map A map where the values are Rects.
     * @return A list of all colliding {@code Map.Entry<String, Rect>} instances.
     */
    public List<Map.Entry<String, Rect>> collidedictall(Map<String, Rect> map) {
        List<Map.Entry<String, Rect>> hits = new ArrayList<>();
        for (Map.Entry<String, Rect> entry : map.entrySet()) {
            if (colliderect(entry.getValue())) hits.add(entry);
        }
        return hits;
    }

    /**
     * Returns a string representation of this Rect.
     *
     * @return A formatted string showing the Rect's position and dimensions.
     */
    @Override
    public String toString() {
        return String.format("Rect(x=%d, y=%d, width=%d, height=%d)", x, y, width, height);
    }
}
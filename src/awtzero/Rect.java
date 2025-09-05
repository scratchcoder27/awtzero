package awtzero;

import java.awt.Point;
import java.awt.Dimension;
import java.awt.geom.Line2D;
import java.util.*;

public class Rect {
    public int x, y, width, height;

    // Constructors
    public Rect(int left, int top, int width, int height) {
        this.x = left;
        this.y = top;
        this.width = width;
        this.height = height;
    }

    public Rect(Point pos, Dimension size) {
        this(pos.x, pos.y, size.width, size.height);
    }

    public Rect(Rect r) {
        this(r.x, r.y, r.width, r.height);
    }

    // Virtual attributes
    public int left() { return x; }
    public int right() { return x + width; }
    public int top() { return y; }
    public int bottom() { return y + height; }

    public Point center() { return new Point(x + width / 2, y + height / 2); }
    public void setCenter(Point p) {
        x = p.x - width / 2;
        y = p.y - height / 2;
    }

    public Dimension size() { return new Dimension(width, height); }

    // Copy
    public Rect copy() {
        return new Rect(this);
    }

    // Move
    public Rect move(int dx, int dy) {
        return new Rect(x + dx, y + dy, width, height);
    }

    public void move_ip(int dx, int dy) {
        x += dx;
        y += dy;
    }

    // Inflate
    public Rect inflate(int dx, int dy) {
        return new Rect(x - dx / 2, y - dy / 2, width + dx, height + dy);
    }

    public void inflate_ip(int dx, int dy) {
        x -= dx / 2;
        y -= dy / 2;
        width += dx;
        height += dy;
    }

    // Scale
    public Rect scale_by(double sx, double sy) {
        int newW = (int) Math.round(width * sx);
        int newH = (int) Math.round(height * sy);
        int newX = (int) Math.round(center().x - newW / 2.0);
        int newY = (int) Math.round(center().y - newH / 2.0);
        return new Rect(newX, newY, newW, newH);
    }

    public Rect scale_by(double scale) {
        return scale_by(scale, scale);
    }

    public void scale_by_ip(double sx, double sy) {
        Rect r = scale_by(sx, sy);
        this.x = r.x; this.y = r.y;
        this.width = r.width; this.height = r.height;
    }

    public void scale_by_ip(double scale) {
        scale_by_ip(scale, scale);
    }

    // Update
    public void update(int left, int top, int width, int height) {
        this.x = left;
        this.y = top;
        this.width = width;
        this.height = height;
    }

    public void update(Point pos, Dimension size) {
        update(pos.x, pos.y, size.width, size.height);
    }

    public void update(Rect other) {
        update(other.x, other.y, other.width, other.height);
    }

    // Clamp
    public Rect clamp(Rect bounds) {
        int newX = Math.max(bounds.x, Math.min(x, bounds.right() - width));
        int newY = Math.max(bounds.y, Math.min(y, bounds.bottom() - height));
        return new Rect(newX, newY, width, height);
    }

    public void clamp_ip(Rect bounds) {
        Rect r = clamp(bounds);
        this.x = r.x;
        this.y = r.y;
    }

    // Clip
    public Rect clip(Rect other) {
        int nx = Math.max(x, other.x);
        int ny = Math.max(y, other.y);
        int nr = Math.min(right(), other.right());
        int nb = Math.min(bottom(), other.bottom());
        if (nr < nx || nb < ny) return new Rect(0, 0, 0, 0);
        return new Rect(nx, ny, nr - nx, nb - ny);
    }

    // Clipline (simple implementation using AWT)
    public static Point[] clipline(Rect r, int x1, int y1, int x2, int y2) {
        Line2D line = new Line2D.Float(x1, y1, x2, y2);
        java.awt.Rectangle bounds = new java.awt.Rectangle(r.x, r.y, r.width, r.height);
        if (!line.intersects(bounds)) return new Point[0];
        return new Point[]{new Point(x1, y1), new Point(x2, y2)}; // Stub
    }

    // Union
    public Rect union(Rect other) {
        int nx = Math.min(x, other.x);
        int ny = Math.min(y, other.y);
        int nr = Math.max(right(), other.right());
        int nb = Math.max(bottom(), other.bottom());
        return new Rect(nx, ny, nr - nx, nb - ny);
    }

    public void union_ip(Rect other) {
        Rect r = union(other);
        update(r);
    }

    public Rect unionall(List<Rect> rects) {
        Rect result = this.copy();
        for (Rect r : rects) result = result.union(r);
        return result;
    }

    public void unionall_ip(List<Rect> rects) {
        Rect r = unionall(rects);
        update(r);
    }

    // Fit
    public Rect fit(Rect target) {
        double scale = Math.min((double)target.width / width, (double)target.height / height);
        return this.scale_by(scale).clamp(target);
    }

    // Normalize
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
    public boolean contains(Rect r) {
        return r.x >= x && r.right() <= right() && r.y >= y && r.bottom() <= bottom();
    }

    public boolean collidepoint(int px, int py) {
        return px >= x && px < right() && py >= y && py < bottom();
    }

    public boolean colliderect(Rect r) {
        return r.right() > x && r.x < right() && r.bottom() > y && r.y < bottom();
    }

    public int collidelist(List<Rect> list) {
        for (int i = 0; i < list.size(); i++) {
            if (colliderect(list.get(i))) return i;
        }
        return -1;
    }

    public List<Integer> collidelistall(List<Rect> list) {
        List<Integer> hits = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (colliderect(list.get(i))) hits.add(i);
        }
        return hits;
    }

    public boolean collideobjects(List<Rect> objs) {
        return collidelist(objs) != -1;
    }

    public boolean collideobjectsall(List<Rect> objs) {
        return collidelistall(objs).size() == objs.size();
    }

    public Map.Entry<String, Rect> collidedict(Map<String, Rect> map) {
        for (Map.Entry<String, Rect> entry : map.entrySet()) {
            if (colliderect(entry.getValue())) return entry;
        }
        return null;
    }

    public List<Map.Entry<String, Rect>> collidedictall(Map<String, Rect> map) {
        List<Map.Entry<String, Rect>> hits = new ArrayList<>();
        for (Map.Entry<String, Rect> entry : map.entrySet()) {
            if (colliderect(entry.getValue())) hits.add(entry);
        }
        return hits;
    }

    @Override
    public String toString() {
        return String.format("Rect(x=%d, y=%d, width=%d, height=%d)", x, y, width, height);
    }
}

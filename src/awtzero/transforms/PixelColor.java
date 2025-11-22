package awtzero.transforms;

/**
 * A mutable container for a single pixel's ARGB components.
 * An instance of this class will be passed to the Consumer
 * for modification.
 */
public class PixelColor {
    public int a, r, g, b;

    public PixelColor(int a, int r, int g, int b) {
        this.a = a;
        this.r = r;
        this.g = g;
        this.b = b;
    }
}
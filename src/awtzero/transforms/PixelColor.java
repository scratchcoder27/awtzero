package awtzero.transforms;

/**
 * A mutable container for a single pixel's ARGB components and normalized UV coordinates.
 * An instance of this class will be passed to the Consumer
 * for modification.
 */
public class PixelColor {
    public int a, r, g, b;
    
    /** Normalized horizontal coordinate (0.0 to 1.0) */
    public float u; 
    
    /** Normalized vertical coordinate (0.0 to 1.0) */
    public float v;

    /**
     * Constructs a new PixelColor container.
     * @param a Alpha component
     * @param r Red component
     * @param g Green component
     * @param b Blue component
     * @param u Normalized U coordinate (horizontal)
     * @param v Normalized V coordinate (vertical)
     */
    public PixelColor(int a, int r, int g, int b, float u, float v) {
        this.a = a;
        this.r = r;
        this.g = g;
        this.b = b;
        this.u = u;
        this.v = v;
    }
}
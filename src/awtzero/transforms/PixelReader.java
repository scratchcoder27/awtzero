package awtzero.transforms;

/**
 * A read-only utility to safely get pixel data from an input array.
 * This class handles all out-of-bounds checks by "clamping"
 * coordinates to the image edge.
 */
public class PixelReader {
    private final int[] inputPixels;
    private final int width;
    private final int height;

    public PixelReader(int[] inputPixels, int width, int height) {
        this.inputPixels = inputPixels;
        this.width = width;
        this.height = height;
    }

    /**
     * Gets the packed ARGB color from the original image.
     * Clamps coordinates to be safely within the image bounds.
     *
     * @param x The x coordinate
     * @param y The y coordinate
     * @return The packed ARGB integer value of the pixel
     */
    public int getRGB(int x, int y) {
        // Clamp to edges
        int safeX = Math.max(0, Math.min(width - 1, x));
        int safeY = Math.max(0, Math.min(height - 1, y));
        
        return inputPixels[safeY * width + safeX];
    }
    
    /**
     * Get a {@link PixelColor} from the image
     * Clamps coordinates safely
     * 
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the {@link PixelColor}
     */
    public PixelColor getPixelColor(int x, int y) {
        // Clamp to edges
        int safeX = Math.max(0, Math.min(width - 1, x));
        int safeY = Math.max(0, Math.min(height - 1, y));
        
        int color = inputPixels[safeY * width + safeX];

        return new PixelColor(
                (color >>> 24) & 0xFF,
                (color >>> 16) & 0xFF,
                (color >>> 8) & 0xFF,
                color & 0xFF
        );
    }
}
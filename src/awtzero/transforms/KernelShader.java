package awtzero.transforms;

/**
 * A functional interface for a "kernel shader."
 * It computes the new color for a single pixel based on its
 * coordinates and a reader for the original image.
 */
@FunctionalInterface
public interface KernelShader {
    /**
     * Computes the new color for a pixel.
     * @param x The current pixel's x-coordinate
     * @param y The current pixel's y-coordinate
     * @param reader A PixelReader for safely reading from the original image
     * @return The new, packed ARGB integer for the pixel at (x, y)
     */
    int compute(int x, int y, PixelReader reader);
}
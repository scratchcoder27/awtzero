package awtzero.prefab;

import java.util.function.Consumer;
import java.util.function.BiFunction;
import awtzero.transforms.*;

/**
 * A utility class providing a collection of pre-defined static "shaders"
 * for use with the parallel image processing functions.
 *
 * <p>This class is non-instantiable and provides:
 * <ul>
 * <li><b>Point Shaders:</b> {@code Consumer<PixelColor>} for {@code applyPixelShader}.</li>
 * <li><b>Kernel Shaders:</b> {@code KernelShader} for {@code applyKernelShader}.</li>
 * </ul>
 * </p>
 */
public class InbuiltShaders {

    // Private constructor to prevent instantiation.
    private InbuiltShaders() {}

    // --- Private Helper Utilities ---

    /**
     * Clamps an integer value to the valid 0-255 color range.
     */
    private static int clamp(int val) {
        if (val < 0) return 0;
        if (val > 255) return 255;
        return val;
    }
    
    /**
     * Clamps a float value to the valid 0-255 color range.
     */
    private static int clamp(float val) {
        if (val < 0f) return 0;
        if (val > 255f) return 255;
        return (int) val;
    }

    /**
     * A reusable helper function to get the NTSC/PAL standard
     * grayscale luminosity of a packed ARGB pixel.
     */
    private static int getLuminosity(int rgba) {
        int r = (rgba >> 16) & 0xFF;
        int g = (rgba >> 8) & 0xFF;
        int b = rgba & 0xFF;
        return (int) (0.299 * r + 0.587 * g + 0.114 * b);
    }


    // ========================================================================
    // ===                 POINT (PIXEL) SHADERS                          ===
    // ===    (For use with applyPixelShader(Image, Consumer<PixelColor>))  ===
    // ========================================================================

    /**
     * A point shader that inverts the image's colors (like a photo negative).
     * Alpha is preserved.
     */
    public static final Consumer<PixelColor> INVERT = (pixel) -> {
        pixel.r = 255 - pixel.r;
        pixel.g = 255 - pixel.g;
        pixel.b = 255 - pixel.b;
    };

    /**
     * A point shader that converts the image to grayscale using
     * NTSC/PAL luminosity coefficients.
     */
    public static final Consumer<PixelColor> GRAYSCALE = (pixel) -> {
        int gray = (int) (pixel.r * 0.299 + pixel.g * 0.587 + pixel.b * 0.114);
        pixel.r = gray;
        pixel.g = gray;
        pixel.b = gray;
    };

    /**
     * A point shader that applies a sepia tone, giving the
     * image an antique, brownish look.
     */
    public static final Consumer<PixelColor> SEPIA = (pixel) -> {
        int r = pixel.r, g = pixel.g, b = pixel.b;

        int newR = clamp((int) (0.393 * r + 0.769 * g + 0.189 * b));
        int newG = clamp((int) (0.349 * r + 0.686 * g + 0.168 * b));
        int newB = clamp((int) (0.272 * r + 0.534 * g + 0.131 * b));

        pixel.r = newR;
        pixel.g = newG;
        pixel.b = newB;
    };

    /**
     * Returns a point shader that adjusts the image's contrast.
     *
     * @param factor A contrast factor.
     * Values > 1.0 increase contrast.
     * Values < 1.0 decrease contrast.
     * 1.0 = no change.
     * @return A Consumer<PixelColor> shader.
     */
    public static Consumer<PixelColor> contrast(float factor) {
        return (pixel) -> {
            pixel.r = clamp(((pixel.r - 128) * factor) + 128);
            pixel.g = clamp(((pixel.g - 128) * factor) + 128);
            pixel.b = clamp(((pixel.b - 128) * factor) + 128);
        };
    }

    /**
     * Returns a point shader that applies a binary threshold.
     * Pixels lighter than the threshold become white, and pixels
     * darker become black.
     *
     * @param level The threshold level (0-255).
     * @return A Consumer<PixelColor> shader.
     */
    public static Consumer<PixelColor> threshold(int level) {
        return (pixel) -> {
            int gray = (int) (pixel.r * 0.299 + pixel.g * 0.587 + pixel.b * 0.114);
            int value = (gray > level) ? 255 : 0;
            pixel.r = value;
            pixel.g = value;
            pixel.b = value;
        };
    }


    // ========================================================================
    // ===              KERNEL (CONVOLUTION) SHADERS                      ===
    // ===    (For use with applyKernelShader(Image, KernelShader))       ===
    // ========================================================================

    /**
     * A kernel shader that applies a 3x3 sharpen filter.
     * This enhances edges by increasing the contrast between
     * adjacent pixels.
     *
     * Kernel:
     * 0 -1  0
     * -1  5 -1
     * 0 -1  0
     */
    public static final KernelShader SHARPEN = (x, y, reader) -> {
        int r = 0, g = 0, b = 0;
        
        for (int ky = -1; ky <= 1; ky++) {
            for (int kx = -1; kx <= 1; kx++) {
                // The kernel "matrix"
                int weight = 0;
                if (kx == 0 && ky == 0) weight = 5;
                else if (kx == 0 || ky == 0) weight = -1;

                int rgba = reader.getRGB(x + kx, y + ky);
                r += ((rgba >> 16) & 0xFF) * weight;
                g += ((rgba >> 8) & 0xFF) * weight;
                b += (rgba & 0xFF) * weight;
            }
        }
        
        int alpha = (reader.getRGB(x, y) >>> 24) & 0xFF;
        return (alpha << 24) | (clamp(r) << 16) | (clamp(g) << 8) | clamp(b);
    };

    /**
     * A kernel shader for Sobel edge detection.
     * This highlights pixels that form strong horizontal or
     * vertical edges, resulting in a black-and-white edge map.
     */
    public static final KernelShader SOBEL_EDGE_DETECT = (x, y, reader) -> {
        // Reusable helper for this lambda
        BiFunction<Integer, Integer, Integer> getGray = (px, py) -> 
            getLuminosity(reader.getRGB(px, py));

        // Gx Kernel (Horizontal)
        int gx = 
            (-1 * getGray.apply(x - 1, y - 1)) + (1 * getGray.apply(x + 1, y - 1)) +
            (-2 * getGray.apply(x - 1, y))   + (2 * getGray.apply(x + 1, y)) +
            (-1 * getGray.apply(x - 1, y + 1)) + (1 * getGray.apply(x + 1, y + 1));

        // Gy Kernel (Vertical)
        int gy = 
            (-1 * getGray.apply(x - 1, y - 1)) + (-2 * getGray.apply(x, y - 1)) + (-1 * getGray.apply(x + 1, y - 1)) +
            ( 1 * getGray.apply(x - 1, y + 1)) + ( 2 * getGray.apply(x, y + 1)) + ( 1 * getGray.apply(x + 1, y + 1));

        int magnitude = clamp(Math.abs(gx) + Math.abs(gy));
        int alpha = (reader.getRGB(x, y) >>> 24) & 0xFF;
        
        return (alpha << 24) | (magnitude << 16) | (magnitude << 8) | magnitude;
    };

    /**
     * A kernel shader that creates a 3D "embossed" or "stamped" effect.
     * Works best on grayscale images.
     *
     * Kernel:
     * -2 -1  0
     * -1  1  1
     * 0  1  2
     */
    public static final KernelShader EMBOSS = (x, y, reader) -> {
        int r = 0, g = 0, b = 0;

        // Emboss kernel
        final int[][] kernel = {
            { -2, -1,  0 },
            { -1,  1,  1 },
            {  0,  1,  2 }
        };
        
        for (int ky = -1; ky <= 1; ky++) {
            for (int kx = -1; kx <= 1; kx++) {
                int weight = kernel[ky + 1][kx + 1];
                int rgba = reader.getRGB(x + kx, y + ky);
                
                // We apply emboss to each channel
                r += ((rgba >> 16) & 0xFF) * weight;
                g += ((rgba >> 8) & 0xFF) * weight;
                b += (rgba & 0xFF) * weight;
            }
        }
        
        // After convolution, shift the midpoint to 128 (gray)
        // This makes non-edges gray and edges light/dark.
        r = clamp(r + 128);
        g = clamp(g + 128);
        b = clamp(b + 128);
        
        // For a true monochrome emboss, average the results
        int gray = (r + g + b) / 3;
        
        int alpha = (reader.getRGB(x, y) >>> 24) & 0xFF;
        return (alpha << 24) | (gray << 16) | (gray << 8) | gray;
    };

    /**
     * Returns a kernel shader that performs a box blur.
     * It averages the color of all pixels in a square
     * kernel of a given radius.
     *
     * @param radius The radius of the blur. 1 = 3x3 kernel, 2 = 5x5, etc.
     * @return A KernelShader that can be passed to {@code applyKernelShader}.
     */
    public static KernelShader boxBlur(int radius) {
        // Create an effectively final variable for the lambda to use
        final int finalRadius = (radius < 1) ? 1 : radius; 
        
        // This is the shader lambda
        return (x, y, reader) -> {
            long totalA = 0, totalR = 0, totalG = 0, totalB = 0;
            int count = 0;

            // Use finalRadius instead of radius
            for (int ky = -finalRadius; ky <= finalRadius; ky++) {
                for (int kx = -finalRadius; kx <= finalRadius; kx++) {
                    int rgba = reader.getRGB(x + kx, y + ky);
                    
                    totalA += (rgba >>> 24) & 0xFF;
                    totalR += (rgba >>> 16) & 0xFF;
                    totalG += (rgba >>> 8) & 0xFF;
                    totalB += rgba & 0xFF;
                    
                    count++;
                }
            }

            // Calculate the average
            int finalA = (int) (totalA / count);
            int finalR = (int) (totalR / count);
            int finalG = (int) (totalG / count);
            int finalB = (int) (totalB / count);

            return (finalA << 24) | (finalR << 16) | (finalG << 8) | finalB;
        };
    }
}
package awtzero.transforms;

import java.awt.Image;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.image.BufferedImage;
// import java.awt.image.DataBufferInt;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import awtzero.ImageWrapper;
// import awtzero.Surface;


public class Colors {

    /**
     * Applies a pixel-by-pixel "shader" to an image in parallel.
     *
     * <p>This method processes each pixel independently using all available
     * CPU cores via a parallel stream. It is ideal for "point operations"
     * where the new color of a pixel depends *only* on its original color
     * (e.g., brightness, contrast, grayscale, invert).</p>
     *
     * <p><b>Important Thread-Safety Warning:</b> The provided {@code shader}
     * Consumer <b>must</b> be thread-safe. The easiest way to ensure this
     * is to not use any shared, mutable state. Any helper variables
     * should be declared *inside* the lambda expression, not outside it.</p>
     *
     * @param source The source Image to process.
     * @param shader A thread-safe {@code Consumer<PixelColor>} that will be
     * called once for each pixel. The Consumer can modify the
     * a, r, g, b fields of the passed {@code PixelColor}
     * object to change the pixel's final color.
     * @return A new BufferedImage with the shader applied.
     */
    public static BufferedImage applyPixelShader(Image source, Consumer<PixelColor> shader) {

        int width = source.getWidth(null);
        int height = source.getHeight(null);

        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = bi.createGraphics();
        g.drawImage(source, 0, 0, null);
        g.dispose();

        int[] pixels = ((java.awt.image.DataBufferInt) bi.getRaster().getDataBuffer()).getData();

        // Store dimensions in final variables for the lambda
        final int w = width;
        final int h = height;
        
        // We use (width - 1) and (height - 1) as divisors to ensure
        // the UV coordinates span the full 0.0 to 1.0 range.
        final float invWidth = 1.0f / (w - 1);
        final float invHeight = 1.0f / (h - 1);

        IntStream.range(0, pixels.length).parallel().forEach(i -> {
            
            // Calculate (x, y) and (u, v) from the 1D index 'i'
            int x = i % w;
            int y = i / w;
            float u = (float)x * invWidth;
            float v = (float)y * invHeight;
            
            int rgba = pixels[i];

            // 1. Unpack ARGB and UV into the container
            PixelColor pixelContainer = new PixelColor(
                (rgba >>> 24) & 0xFF,
                (rgba >>> 16) & 0xFF,
                (rgba >>> 8) & 0xFF,
                rgba & 0xFF,
                u, v
            );

            // 2. Run the user's "shader"
            shader.accept(pixelContainer);

            // 3. Pack the modified vals back into the array
            pixels[i] = ((pixelContainer.a & 0xFF) << 24) |
                        ((pixelContainer.r & 0xFF) << 16) |
                        ((pixelContainer.g & 0xFF) << 8)  |
                        (pixelContainer.b & 0xFF);
        });

        return bi;
    }

    /**
     * Applies a parallelized "kernel" shader to an image.
     *
     * <p>This method is for complex operations like blurs, sharpens,
     * and edge detection, where the new color of a pixel depends
     * on its neighbors.</p>
     *
     * @param source The source Image to process.
     * @param shader A KernelShader that computes the new value for each pixel.
     * @return A new BufferedImage with the kernel applied.
     */
    public static BufferedImage applyKernelShader(Image source, KernelShader shader) {
        
        int width = source.getWidth(null);
        int height = source.getHeight(null);

        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = bi.createGraphics();
        g.drawImage(source, 0, 0, null);
        g.dispose();

        int[] outputPixels = ((java.awt.image.DataBufferInt) bi.getRaster().getDataBuffer()).getData();

        int[] inputPixels = outputPixels.clone();

        PixelReader reader = new PixelReader(inputPixels, width, height);

        IntStream.range(0, height).parallel().forEach(y -> {
            for (int x = 0; x < width; x++) {
                
                int newColor = shader.compute(x, y, reader);

                outputPixels[y * width + x] = newColor;
            }
        });

        return bi;
    }

    /**
     * Allows changing the relative brightness of an {@link Image}
     * @param source The source {@link Image}
     * @param brightnessMultiplier The multiplier, eg: 2.0f
     * @return The image with the recolor applied
     */
    public static BufferedImage changeBrightness(Image source, float brightnessMultiplier) {
        // Define the shader.
        // This lambda will be executed by multiple threads in parallel.
        Consumer<PixelColor> brightnessShader = (pixel) -> {
            
            
            // Declare hsb inside the lambda.
            // Each thread now gets its own private array.
            float[] hsb = new float[3];

            // 1. Convert to HSB
            Color.RGBtoHSB(pixel.r, pixel.g, pixel.b, hsb);

            // 2. Update brightness
            float newBrightness = hsb[2] * brightnessMultiplier;
            if (newBrightness > 1f) newBrightness = 1f;

            // 3. Convert back to RGB
            int newRGB = Color.HSBtoRGB(hsb[0], hsb[1], newBrightness);

            // 4. Update the pixel container's values
            // (Alpha is automatically preserved since we don't touch pixel.a)
            pixel.r = (newRGB >>> 16) & 0xFF;
            pixel.g = (newRGB >>> 8) & 0xFF;
            pixel.b = newRGB & 0xFF;
        };

        // Run the parallel shader function
        return applyPixelShader(source, brightnessShader);
    }

    /**
    * Allows changing the relative brightness of a {@link ImageWrapper}
    * @param source The source {@link ImageWrapper}
    * @param brightnessMultiplier The multiplier, eg: 2.0f
    * @return The image with the recolor applied
    */
    public static ImageWrapper changeBrightness(ImageWrapper img, float brightnessMultiplier) {
       BufferedImage changedImg = changeBrightness(img.image, brightnessMultiplier);
       return new ImageWrapper(changedImg);
    }

    /**
     * Apply a box blur on the image with a KernelShader
     * @param source the source image
     * @param radius the radius to perform the operation (more means more blurry)
     * @return The blurred imahe
     */
    public static BufferedImage applyBoxBlur(Image source, int radius) {
        KernelShader blurShader = (x, y, reader) -> {
            
            long totalA = 0, totalR = 0, totalG = 0, totalB = 0;
            int count = 0;

            // Iterate over the kernel
            for (int ky = -radius; ky <= radius; ky++) {
                for (int kx = -radius; kx <= radius; kx++) {
                    
                    // Get the neighbor's color from the reader
                    int rgba = reader.getRGB(x + kx, y + ky);
                    
                    totalA += (rgba >>> 24) & 0xFF;
                    totalR += (rgba >>> 16) & 0xFF;
                    totalG += (rgba >>> 8) & 0xFF;
                    totalB += rgba & 0xFF;
                    
                    count++;
                }
            }

            int finalA = (int) (totalA / count);
            int finalR = (int) (totalR / count);
            int finalG = (int) (totalG / count);
            int finalB = (int) (totalB / count);

            // Pack the channels back into an ARGB int and return it
            return (finalA << 24) | (finalR << 16) | (finalG << 8) | finalB;
        };
        return applyKernelShader(source, blurShader);
    }
}

package awtzero.transforms;

import java.awt.*;

import awtzero.ImageWrapper;

/**
 * Provides static methods for scaling {@link Image} and {@link ImageWrapper}
 * objects using different quality algorithms provided by {@link Image#getScaledInstance(int, int, int)}.
 */
public class Scale {

    /**
     * Scales an {@link Image} to the specified dimensions using the default scaling algorithm.
     * This typically uses a moderately quick algorithm that produces reasonable quality.
     *
     * @param original The original {@link Image} to be scaled.
     * @param width The desired new width in pixels.
     * @param height The desired new height in pixels.
     * @return A new scaled {@link Image} object.
     * @see Image#SCALE_DEFAULT
     */
    public static Image scaleDefault(Image original, int width, int height) {
        return original.getScaledInstance(width, height, Image.SCALE_DEFAULT);
    }

    /**
     * Scales an {@link ImageWrapper}'s internal image using the default scaling algorithm.
     *
     * @param original The original {@link ImageWrapper} containing the image to be scaled.
     * @param width The desired new width in pixels.
     * @param height The desired new height in pixels.
     * @return A new {@link ImageWrapper} containing the scaled image.
     * @see Image#SCALE_DEFAULT
     */
    public static ImageWrapper scaleDefault(ImageWrapper original, int width, int height) {
        return new ImageWrapper(original.image.getScaledInstance(width, height, Image.SCALE_DEFAULT));
    }

    /**
     * Scales an {@link Image} to the specified dimensions using an algorithm optimized for speed.
     * The image quality may be compromised for faster processing.
     *
     * @param original The original {@link Image} to be scaled.
     * @param width The desired new width in pixels.
     * @param height The desired new height in pixels.
     * @return A new scaled {@link Image} object.
     * @see Image#SCALE_FAST
     */
    public static Image scaleFast(Image original, int width, int height) {
        return original.getScaledInstance(width, height, Image.SCALE_FAST);
    }

    /**
     * Scales an {@link ImageWrapper}'s internal image using an algorithm optimized for speed.
     *
     * @param original The original {@link ImageWrapper} containing the image to be scaled.
     * @param width The desired new width in pixels.
     * @param height The desired new height in pixels.
     * @return A new {@link ImageWrapper} containing the scaled image.
     * @see Image#SCALE_FAST
     */
    public static ImageWrapper scaleFast(ImageWrapper original, int width, int height) {
        return new ImageWrapper(original.image.getScaledInstance(width, height, Image.SCALE_FAST));
    }

    /**
     * Scales an {@link Image} to the specified dimensions using an algorithm optimized for smoothness (quality).
     * This may be slower than other methods but generally produces the highest quality result.
     *
     * @param original The original {@link Image} to be scaled.
     * @param width The desired new width in pixels.
     * @param height The desired new height in pixels.
     * @return A new scaled {@link Image} object.
     * @see Image#SCALE_SMOOTH
     */
    public static Image scaleSmooth(Image original, int width, int height) {
        return original.getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }

    /**
     * Scales an {@link ImageWrapper}'s internal image using an algorithm optimized for smoothness (quality).
     *
     * @param original The original {@link ImageWrapper} containing the image to be scaled.
     * @param width The desired new width in pixels.
     * @param height The desired new height in pixels.
     * @return A new {@link ImageWrapper} containing the scaled image.
     * @see Image#SCALE_SMOOTH
     */
    public static ImageWrapper scaleSmooth(ImageWrapper original, int width, int height) {
        return new ImageWrapper(original.image.getScaledInstance(width, height, Image.SCALE_SMOOTH));
    }
}
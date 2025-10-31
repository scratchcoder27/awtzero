package awtzero.transforms;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import awtzero.ImageWrapper;
import awtzero.Point;

/**
 * A class providing static methods to rotate images.
 */
public class Rotate {
    private static BufferedImage toBufferedImage(java.awt.Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }
        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(
                img.getWidth(null), img.getHeight(null),
                BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        java.awt.Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        return bimage;
    }

    /**
     * Rotate an image by specified number of degrees.
     * @param img the {@link BufferedImage} to be rotated
     * @param degrees the degrees to rotate the image by
     * @return the rotated {@link BufferedImage}
     */
    public static BufferedImage rotateDegrees(BufferedImage img, double degrees) {

        double rads = Math.toRadians(degrees);
        double sin = Math.abs(Math.sin(rads));
        double cos = Math.abs(Math.cos(rads));

        int w = img.getWidth(null);
        int h = img.getHeight(null);

        // new dimensions after rotation
        int newW = (int) Math.floor(w * cos + h * sin);
        int newH = (int) Math.floor(h * cos + w * sin);

        // force ARGB so background is transparent
        BufferedImage rotated = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = rotated.createGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                             RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        // move image to center, then rotate around its center
        AffineTransform at = new AffineTransform();
        at.translate((newW - w) / 2.0, (newH - h) / 2.0);

        int x = w / 2;
        int y = h / 2;

        at.rotate(rads, x, y);
        g2d.drawRenderedImage(img, at);
        g2d.dispose();

        return rotated;
    }

    /**
     * Rotate an {@link Image} by a specified number of degrees.
     * @param img the AWT {@link Image} to be rotated
     * @param degrees the number of degrees to rotate the image by
     * @return the rotated {@link Image}
     */
    public static Image rotateDegrees(java.awt.Image img, double degrees) {
        BufferedImage bimg = toBufferedImage(img);
        return rotateDegrees(bimg, degrees);
    }

    /**
     * Rotate an {@link Image} so that it faces towards a specified point.
     * NOTE: It is assumed that the image is initially facing right (0 degrees).
     * <p>WARNING: This method uses {@link Math#atan2(double, double)} which might be slow for real-time applications.</p>
     * @param img the AWT {@link Image} to be rotated
     * @param me the {@link Point} representing the current position
     * @param toward the {@link Point} representing the target position
     * @return the rotated {@link Image}
     */
    public static Image rotateTowards(java.awt.Image img, Point me, Point toward) {
        BufferedImage bimg = toBufferedImage(img);
        double degrees = Math.toDegrees(Math.atan2(toward.y - me.y, toward.x - me.x));
        return rotateDegrees(bimg, degrees);
    }

    /**
     * Rotate an {@link ImageWrapper} by a specified number of degrees.
     * @param img the {@link ImageWrapper} to be rotated
     * @param degrees the number of degrees to rotate the image by
     * @return the rotated {@link ImageWrapper}
     */
    public static ImageWrapper rotateDegrees(ImageWrapper img, double degrees) {
        BufferedImage bimg = toBufferedImage(img.image);
        BufferedImage rotated = rotateDegrees(bimg, degrees);
        return new ImageWrapper(rotated, null);
    }
}

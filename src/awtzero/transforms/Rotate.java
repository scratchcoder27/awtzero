package awtzero.transforms;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import awtzero.ImageWrapper;
import awtzero.Point;

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

    public static Image rotateDegrees(java.awt.Image img, double degrees) {
        BufferedImage bimg = toBufferedImage(img);
        return rotateDegrees(bimg, degrees);
    }

    public static Image rotateTowards(java.awt.Image img, Point me, Point toward) {
        BufferedImage bimg = toBufferedImage(img);
        double degrees = Math.toDegrees(Math.atan2(toward.y - me.y, toward.x - me.x));
        return rotateDegrees(bimg, degrees);
    }

    public static ImageWrapper rotateDegrees(ImageWrapper img, double degrees) {
        BufferedImage bimg = toBufferedImage(img.image);
        BufferedImage rotated = rotateDegrees(bimg, degrees);
        return new ImageWrapper(rotated, null);
    }
}

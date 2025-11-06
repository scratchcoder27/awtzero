package awtzero.transforms;

import java.awt.Image;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.image.BufferedImage;
// import java.awt.image.DataBufferInt;

import awtzero.ImageWrapper;
// import awtzero.Surface;

public class Colors {

    /**
     * Allows changing the relative brightness of an {@link Image}
     * @param source The source {@link Image}
     * @param brightnessMultiplier The multiplier
     * @return The image with the recolor applied
     */
    public static BufferedImage changeBrightness(Image source, float brightnessMultiplier) {

        int width = source.getWidth(null);
        int height = source.getHeight(null);

        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = bi.createGraphics();
        g.drawImage(source, 0, 0, null);
        g.dispose();
        
        int[] pixels = ((java.awt.image.DataBufferInt) bi.getRaster().getDataBuffer()).getData();
        float[] hsb = new float[3];

        for (int i = 0; i < pixels.length; i++) {

            int rgba = pixels[i];

            // Extract color components from ARGB integer
            int a = (rgba >>> 24) & 0xFF;
            int r = (rgba >>> 16) & 0xFF;
            int gCol = (rgba >>> 8) & 0xFF;
            int b = rgba & 0xFF;

            // Convert to HSB
            Color.RGBtoHSB(r, gCol, b, hsb);

            // Update brightness
            float newBrightness = hsb[2] * brightnessMultiplier;
            if (newBrightness > 1f) newBrightness = 1f;

            // Convert back to RGB (HSBtoRGB returns packed RGB w/o alpha)
            int newRGB = Color.HSBtoRGB(hsb[0], hsb[1], newBrightness);

            // Add alpha back into RGB
            pixels[i] = (a << 24) | (newRGB & 0x00FFFFFF);
        }

        return bi;
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
}

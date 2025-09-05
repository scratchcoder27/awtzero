package awtzero.transforms;

import java.awt.*;

import awtzero.ImageWrapper;

public class Scale {
	
    public static Image scale_default(Image original, int width, int height) {
    	return original.getScaledInstance(width, height, Image.SCALE_DEFAULT);
    }
    
    public static ImageWrapper scale_default(ImageWrapper original, int width, int height) {
    	return new ImageWrapper(original.image.getScaledInstance(width, height, Image.SCALE_DEFAULT));
    }
    
    public static Image scale_fast(Image original, int width, int height) {
    	return original.getScaledInstance(width, height, Image.SCALE_FAST);
    }
    
    public static ImageWrapper scale_fast(ImageWrapper original, int width, int height) {
    	return new ImageWrapper(original.image.getScaledInstance(width, height, Image.SCALE_FAST));
    }
    
    public static Image scale_smooth(Image original, int width, int height) {
    	return original.getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }
    
    public static ImageWrapper scale_smooth(ImageWrapper original, int width, int height) {
    	return new ImageWrapper(original.image.getScaledInstance(width, height, Image.SCALE_SMOOTH));
    }
}

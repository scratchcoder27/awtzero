package awtzero;

import java.awt.*;
import java.util.HashMap;

public class ImageWrapper {
    private static final HashMap<String, Image> cache = new HashMap<>();

    public int width, height;
    public final Image image;
    public final Component observer;

    public ImageWrapper(String path, Component observer) {
        this.observer = observer;
        this.image = loadImage(observer, path);
        this.width = this.image.getWidth(observer);
        this.height = this.image.getHeight(observer);
    }
    
    public ImageWrapper(String path) {
        this.observer = null;
        this.image = loadImage(null, path);
        this.width = this.image.getWidth(null);
        this.height = this.image.getHeight(null);
    }
    
    public ImageWrapper(Image img, Component observer) {
    	this.image = img;
    	this.observer = observer;
        this.width = this.image.getWidth(observer);
        this.height = this.image.getHeight(observer);
    }
    
    public ImageWrapper(Image img) {
    	this.image = img;
    	this.observer = null;
        this.width = this.image.getWidth(this.observer);
        this.height = this.image.getHeight(this.observer);
    }

    public static Image loadImage(Component observer, String path) {
        if (cache.containsKey(path)) {
            return cache.get(path);
        }

        Image img = Toolkit.getDefaultToolkit().getImage(path);
        MediaTracker tracker = new MediaTracker(observer);
        tracker.addImage(img, 0);

        try {
            tracker.waitForID(0);
        } catch (InterruptedException e) {
            System.err.println("Image loading interrupted: " + path);
        }

        cache.put(path, img);
        return img;
    }
    
    public int getHeight() {
    	return this.height;
    }
    
    public int getWidth() {
    	return this.width;
    }
    
    public Dimension getSize() {
    	return new Dimension(this.width, this.height);
    }
    
    public Rect getRect(int x, int y) {
    	return new Rect(x, y, this.width, this.height);
    }
    
    public void blit(Graphics g, int x, int y) {
        g.drawImage(image, x, y, width, height, null);
    }
    
}

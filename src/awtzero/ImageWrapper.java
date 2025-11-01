package awtzero;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

/**
 * This class wraps around AWT Image loading and caching.
 * It provides methods to load images, get their dimensions, and draw them.
 * @see Image
 */

public class ImageWrapper {
    private static final HashMap<String, Image> cache = new HashMap<>();

    private class ImageObserverComponent extends Component {
        //stub
    }

    /**  Stores the width of the image*/
    public int width;
    /**  Stores the height of the image*/
    public int height;
    /** The AWT Image component which is compostited */
    public final Image image;
    /** The Component observing operations */
    public final Component observer;

    /**
     * Constructor to create an ImageWrapper from a file path. (loads the file)
     * @param path The file path of the image to load.
     * @param observer The Component observing the image loading. 
     * <strong> NOTE: The Component must be fully loaded </strong>
     * @throws IOException If the component was not loaded fully when used
     * @see ImageWrapper#loadImage(String)
     */
    public ImageWrapper(String path, Component observer) throws IOException {
        this.observer = observer;
        try {
            this.image = loadImage(observer, path);
            this.width = this.image.getWidth(observer);
            this.height = this.image.getHeight(observer);
        } catch (NullPointerException e) {
            throw new IOException("The observer component was not loaded");
        }
    }
    
    /**
     * Constructor to create an ImageWrapper from a file path without an observer. (loads the file)
     * @param path The file path of the image to load.
     * @see ImageWrapper#loadImage(String, Component)
     */
    public ImageWrapper(String path) {
        this.observer = new ImageObserverComponent();
        this.image = loadImage(observer, path);
        this.width = this.image.getWidth(observer);
        this.height = this.image.getHeight(observer);
    }
    
    /**
     * Constructor to construct an ImageWrapper from an existing AWT Image.
     * @param img The AWT Image to wrap.
     * @param observer The Component observing the image loading. (recommended, pass the Window or Screen)
     * @see #loadImage(Image)
     */
    public ImageWrapper(Image img, Component observer) {
    	this.image = img;
    	this.observer = observer;
        this.width = this.image.getWidth(observer);
        this.height = this.image.getHeight(observer);
    }
    
    /**
     * Constructor to construct an ImageWrapper from an existing AWT Image without an observer
     * @param img The AWT Image to wrap.
     * @see #loadImage(String)
     */
    public ImageWrapper(Image img) {
    	this.image = img;
    	this.observer = null;
        this.width = this.image.getWidth(this.observer);
        this.height = this.image.getHeight(this.observer);
    }

    /**
     * A static method to load an image from the specified path with caching, directly returning an AWT Image.
     * @param observer
     * @param path
     * @return The loaded AWT Image.
     */
    public static Image loadImage(Component observer, String path) {
        if (cache.containsKey(path)) {
            return cache.get(path);
        }

        try {
            Image img;
            java.net.URL resource = ImageWrapper.class.getResource(path);
            if (resource != null) {
                img = javax.imageio.ImageIO.read(resource);
            } else {
                img = javax.imageio.ImageIO.read(new java.io.File(path));
            }

            if (img == null) throw new IOException("ImageIO.read returned null for " + path);

            cache.put(path, img);
            return img;
        } catch (IOException e) {
            System.err.println("Failed to load image: " + path + " (" + e.getMessage() + ")");
            return new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        }
    }



    /**
     * returns the height of the image in pixels.
     * @return The height of the image in pixels.
     */
    public int getHeight() {
    	return this.height;
    }
    
    /**
     * returns the width of the image in pixels.
     * @return The width of the image in pixels.
     */
    public int getWidth() {
    	return this.width;
    }
    
    /**
     * returns the size of the image as a Dimension object.
     * @return The size of the image
     * @see Dimension
     */
    public Dimension getSize() {
    	return new Dimension(this.width, this.height);
    }
    
    /**
     * returns a Rect object representing the dimensions of the image at the specified position.
     * @param x the x position of the object
     * @param y the y position of the object
     * @return The Rect object
     * @see Rect
     */
    public Rect getRect(int x, int y) {
    	return new Rect(x, y, this.width, this.height);
    }
    
    /**
     * Draws the image onto the provided Graphics context at the specified position.
     * @param g The Graphics context to draw on.
     * @param x The x position to draw the image.
     * @param y The y position to draw the image.
     * @deprecated Use RenderInstance's drawImage method instead.
     * @see RenderInstance#drawImage(ImageWrapper, int, int)
     */
    public void blit(Graphics g, int x, int y) {
        g.drawImage(image, x, y, width, height, null);
    }
    
}

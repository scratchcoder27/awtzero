package awtzero;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * Allows edition of a surface
 * <p> This composits {@link Image} and {@link RenderInstance}
 * <p> inspired by pygame Surfaces
 */
public class Surface {
    /**
     * The AWT Image component which is compostited
     */
    public BufferedImage image;
    /**
     * The RenderInstance on which drawing operations can be performed
     */
    public RenderInstance graphics;
    
    /**
     * Constructor to create a Surface from an existing AWT Image.
     * @param img The AWT Image to wrap.
     */
    public Surface(Image img) {
        this.image = (BufferedImage) img;
    }

    /**
     * Constructor to create a blank Surface with given width and height.
     * <p>Note: This creates an image of type ARGB, which may not be very memory efficient
     * @param width The width of the surface
     * @param height The height of the surface
     */
    public Surface(int width, int height) {
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }

    /**
     * Constructor to create a blank Surface with given width, height and image type.
     * @param width The width of the surface
     * @param height The height of the surface
     * @param imageType The type of the image (use BufferedImage.TYPE_ constants), eg: {@link BufferedImage#TYPE_INT_ARGB}
     */
    public Surface (int width, int height, int imageType) {
        this.image = new BufferedImage(width, height, imageType);
    }

    /**
     * Constructor to create a Surface from an existing BufferedImage.
     * @param img The BufferedImage to wrap.
     */
    public Surface(BufferedImage img) {
        this.image = img;
    }

    /**
     * Constructor to create a Surface from an existing ImageWrapper.
     * @param imgWrapper The ImageWrapper to wrap.
     */
    public Surface (ImageWrapper imgWrapper) {
        this.image = (BufferedImage) imgWrapper.image;
    }

    /**
     * Gets the RenderInstance for this Surface, creating it if it does not exist.
     * <p>Can be closed with {@link #closeGraphics()} when done to save memory.
     * @return The RenderInstance for this Surface.
     */
    public RenderInstance getGraphics() {
        if (this.graphics == null) {
            this.graphics = new RenderInstance(this.image.createGraphics());
        }
        return this.graphics;
    }

    public void closeGraphics() {
        if (this.graphics == null) {
            return;
        }

        this.graphics.dispose();
        this.graphics = null;
    }

    /**
     * Gets the width of the Surface.
     * @return The width of the Surface.
     */
    public int getWidth() {
        return this.image.getWidth();
    }

    /**
     * Gets the height of the Surface.
     * @return The height of the Surface.
     */
    public int getHeight() {
        return this.image.getHeight();
    }

    /**
     * Gets the image type of the Surface.
     * @return the image type of the Surface as defined in {@link BufferedImage}
     */
    public int getType() {
        return this.image.getType();
    }

    /**
     * Gets the underlying AWT {@link BufferedImage} of the Surface.
     * @return The composited BufferedImage.
     */
    public Image asBufferedImageImage() {
        return this.image;
    }

    /**
     * Gets the underlying AWT {@link BufferedImage} of the Surface wrapped in an {@link ImageWrapper}.
     * @return The composited BufferedImage wrapped in an ImageWrapper.
     */
    public ImageWrapper asImageWrapper() {
        return new ImageWrapper(this.image);
    }

    /**
     * Returns a string representation of the Surface.
     * @return A string describing the Surface's dimensions and type.
     */
    @Override
    public String toString() {
        return "Surface: " + this.image.getWidth() + "x" + this.image.getHeight() + ", Type: " + this.image.getType();
    }

    //MARK: RenderInstance methods:
    //If anyone has any idea to remove this code while still keeping easy functionality, I will be very happy

    
    /**
     * Draws a given Surface onto the Surface
     * <p> NOTE: If the Surface is larger than the new Surface, portions may be cropped off
     * @param src The surface to draw
     * @param destX The destination x coordinate
     * @param destY The destination y coordinate
     */
    public void blit(Surface src, int destX, int destY) {
        RenderInstance g = this.getGraphics();
        g.blit(src.asBufferedImageImage(), new Point(destX, destY));
        this.closeGraphics();
    }

    /**
     * Draws an {@link ImageWrapper} at a given point.
     * <p>This method acquires a rendering context, calls the operation on it, and disposes of the context.</p>
     *
     * @param img the {@link ImageWrapper} containing the image and metadata
     * @param p   the position to draw at
     * @see awtzero.RenderInstance#blit(ImageWrapper, Point)
     */
    public void blit(ImageWrapper img, Point p) {
        RenderInstance g = this.getGraphics();
        g.blit(img.image, p);
        this.closeGraphics();
    }

    /**
     * Draws a {@link Surface} to the current Surface
     * @param surf The surface to draw
     * @param p The point to draw at
     */
    public void blit(Surface surf, Point p) {
        RenderInstance g = this.getGraphics();
        try {
            g.blit(surf.image, p);
        } catch (Exception e) {
            //pass
        } finally {
            this.closeGraphics();
        }
    }
    
    /**
     * Draws an image at the specified rectangle position and size.
     * <p>This method acquires a rendering context, calls the operation on it, and disposes of the context.</p>
     *
     * @param img the {@link Image} to draw
     * @param x   the x coordinate
     * @param y   the y coordinate
     * @param w   the width
     * @param h   the height
     * @see awtzero.RenderInstance#blit(Image, int, int, int, int)
     */
    public void blit(Image img, int x, int y, int w, int h) {
        RenderInstance g = this.getGraphics();
        g.blit(img, x, y, w, h);
        this.closeGraphics();
    }
    
    /**
     * Draws an image at the specified rectangle position and size, with an observer.
     * <p>This method acquires a rendering context, calls the operation on it, and disposes of the context.</p>
     *
     * @param img       the {@link Image} to draw
     * @param x         the x coordinate
     * @param y         the y coordinate
     * @param w         the width
     * @param h         the height
     * @param observer  the {@link Component} to notify as the image is updated
     * @see awtzero.RenderInstance#blit(Image, int, int, int, int, Component)
     */
    public void blit(Image img, int x, int y, int w, int h, Component observer) {
        RenderInstance g = this.getGraphics();
        g.blit(img, x, y, w, h, observer); 
        this.closeGraphics();
    }
    
    /**
     * Draws an image at the specified point with an image observer.
     * <p>This method acquires a rendering context, calls the operation on it, and disposes of the context.</p>
     *
     * @param img       the {@link Image} to draw
     * @param p         the {@link Point} representing the image position
     * @param observer  the {@link Component} to notify as the image is updated
     * @see awtzero.RenderInstance#blit(Image, Point, Component)
     */
    public void blit(Image img, Point p, Component observer) {
        RenderInstance g = this.getGraphics();
        g.blit(img, p, observer);
        this.closeGraphics();
    }
    
    /**
     * Draws an image inside a specified rectangle.
     * <p>This method acquires a rendering context, calls the operation on it, and disposes of the context.</p>
     *
     * @param img  the {@link Image} to draw
     * @param rect the {@link Rect} specifying position and size
     * @see awtzero.RenderInstance#blit(Image, Rect)
     */
    public void blit(Image img, Rect rect) {
        RenderInstance g = this.getGraphics();
        g.blit(img, rect);
        this.closeGraphics();
    }
    
    /**
     * Draws an unfilled circle at a given position and radius.
     * <p>This method acquires a rendering context, calls the operation on it, and disposes of the context.</p>
     *
     * @param pos    the {@link Point} at the center
     * @param radius the circle radius
     * @param color  the {@link Color} of the outline
     * @see awtzero.RenderInstance#drawCircle(Point, int, Color)
     */
    public void drawCircle(Point pos, int radius, Color color) {
        RenderInstance g = this.getGraphics();
        g.drawCircle(pos, radius, color);
        this.closeGraphics();
    }
    
    /**
     * Draws a filled circle at a given position and radius.
     * <p>This method acquires a rendering context, calls the operation on it, and disposes of the context.</p>
     *
     * @param pos    the {@link Point} at the center
     * @param radius the circle radius
     * @param color  the {@link Color} to fill the circle
     * @see awtzero.RenderInstance#drawFilledCircle(Point, int, Color)
     */
    public void drawFilledCircle(Point pos, int radius, Color color) {
        RenderInstance g = this.getGraphics();
        g.drawFilledCircle(pos, radius, color);
        this.closeGraphics();
    }
    
    /**
     * Draws a filled rectangle with the given color.
     * <p>This method acquires a rendering context, calls the operation on it, and disposes of the context.</p>
     *
     * @param rect  the {@link Rect} specifying position and size
     * @param color the {@link Color} to fill the rectangle
     * @see awtzero.RenderInstance#drawFilledRect(Rect, Color)
     */
    public void drawFilledRect(Rect rect, Color color) {
        RenderInstance g = this.getGraphics();
        g.drawFilledRect(rect, color);
        this.closeGraphics();
    }
    
    /**
     * Draws a filled rounded rectangle.
     * <p>This method acquires a rendering context, calls the operation on it, and disposes of the context.</p>
     *
     * @param rect      the {@link Rect} specifying position and size
     * @param color     the {@link Color} to fill the rectangle
     * @param arcWidth  the horizontal diameter of the arc at the corners
     * @param arcHeight the vertical diameter of the arc at the corners
     * @see awtzero.RenderInstance#drawFilledRoundedRect(Rect, Color, int, int)
     */
    public void drawFilledRoundedRect(Rect rect, Color color, int arcWidth, int arcHeight) {
        RenderInstance g = this.getGraphics();
        g.drawFilledRoundedRect(rect, color, arcWidth, arcHeight);
        this.closeGraphics();
    }
    
    /**
     * Draws a line between two points with the specified color.
     * <p>This method acquires a rendering context, calls the operation on it, and disposes of the context.</p>
     *
     * @param startx starting x coordinate
     * @param starty starting y coordinate
     * @param endx   ending x coordinate
     * @param endy   ending y coordinate
     * @param color  the {@link Color} of the line
     * @see awtzero.RenderInstance#drawLine(int, int, int, int, Color)
     */
    public void drawLine(int startx, int starty, int endx, int endy, Color color) {
        RenderInstance g = this.getGraphics();
        g.drawLine(startx, starty, endx, endy, color);
        this.closeGraphics();
    }
    
    /**
     * Draws a line between two {@link Point} objects.
     * <p>This method acquires a rendering context, calls the operation on it, and disposes of the context.</p>
     *
     * @param start the start point
     * @param end   the end point
     * @param color the {@link Color} of the line
     * @see awtzero.RenderInstance#drawLine(Point, Point, Color)
     */
    public void drawLine(Point start, Point end, Color color) {
        RenderInstance g = this.getGraphics();
        g.drawLine(start, end, color);
        this.closeGraphics();
    }
    
    /**
     * Draws an unfilled rectangle with the given color.
     * <p>This method acquires a rendering context, calls the operation on it, and disposes of the context.</p>
     *
     * @param rect  the {@link Rect} specifying position and size
     * @param color the {@link Color} of the outline
     * @see awtzero.RenderInstance#drawRect(Rect, Color)
     */
    public void drawRect(Rect rect, Color color) {
        RenderInstance g = this.getGraphics();
        g.drawRect(rect, color);
        this.closeGraphics();
    }
    
    /**
     * Draws an unfilled rounded rectangle.
     * <p>This method acquires a rendering context, calls the operation on it, and disposes of the context.</p>
     *
     * @param rect      the {@link Rect} specifying position and size
     * @param color     the {@link Color} of the outline
     * @param arcWidth  the horizontal diameter of the arc at the corners
     * @param arcHeight the vertical diameter of the arc at the corners
     * @see awtzero.RenderInstance#drawRoundedRect(Rect, Color, int, int)
     */
    public void drawRoundedRect(Rect rect, Color color, int arcWidth, int arcHeight) {
        RenderInstance g = this.getGraphics();
        g.drawRoundedRect(rect, color, arcWidth, arcHeight);
        this.closeGraphics();
    }
    
    /**
     * Draws a string of text at a given position using current color and font.
     * <p>This method acquires a rendering context, calls the operation on it, and disposes of the context.</p>
     *
     * @param s the string to draw
     * @param x the x coordinate
     * @param y the y coordinate
     * @see awtzero.RenderInstance#drawText(String, int, int)
     */
    public void drawText(String s, int x, int y) {
        RenderInstance g = this.getGraphics();
        g.drawText(s, x, y);
        this.closeGraphics();
    }
    
    /**
     * Draws a string of text at a given position with specified color and font.
     * <p>This method acquires a rendering context, calls the operation on it, and disposes of the context.</p>
     *
     * @param s     the string to draw
     * @param x     the x coordinate
     * @param y     the y coordinate
     * @param color the {@link Color} of the text
     * @param font  the {@link Font} to use
     * @see awtzero.RenderInstance#drawText(String, int, int, Color, Font)
     */
    public void drawText(String s, int x, int y, Color color, Font font) {
        RenderInstance g = this.getGraphics();
        g.drawText(s, x, y, color, font);
        this.closeGraphics();
    }
    
    /**
     * Draws a string of text at a given position using current color and font.
     * <p>This method acquires a rendering context, calls the operation on it, and disposes of the context.</p>
     *
     * @param s the string to draw
     * @param p the position as a {@link Point}
     * @see awtzero.RenderInstance#drawText(String, Point)
     */
    public void drawText(String s, Point p) {
        RenderInstance g = this.getGraphics();
        g.drawText(s, p);
        this.closeGraphics();
    }
    
    /**
     * Draws a string of text at a given position with specified color and font.
     * <p>This method acquires a rendering context, calls the operation on it, and disposes of the context.</p>
     *
     * @param s     the string to draw
     * @param p     the position as a {@link Point}
     * @param color the {@link Color} of the text
     * @param font  the {@link Font} to use
     * @see awtzero.RenderInstance#drawText(String, Point, Color, Font)
     */
    public void drawText(String s, Point p, Color color, Font font) {
        RenderInstance g = this.getGraphics();
        g.drawText(s, p, color, font);
        this.closeGraphics();
    }
    
    /**
     * Draws a triangle by connecting three points.
     * <p>This method acquires a rendering context, calls the operation on it, and disposes of the context.</p>
     *
     * @param p1    the first vertex
     * @param p2    the second vertex
     * @param p3    the third vertex
     * @param color the {@link Color} of the outline
     * @see awtzero.RenderInstance#drawTriangle(Point, Point, Point, Color)
     */
    public void drawTriangle(Point p1, Point p2, Point p3, Color color) {
        RenderInstance g = this.getGraphics();
        g.drawTriangle(p1, p2, p3, color);
        this.closeGraphics();
    }
    
    /**
     * Fills the entire surface with the specified color.
     * <p>This method acquires a rendering context, calls the operation on it, and disposes of the context.</p>
     * @param color  the {@link Color} to fill the screen with
     * @see awtzero.RenderInstance#fillScreen(Canvas, Color)
     */
    public void fill(Color color) {
        RenderInstance g = this.getGraphics();
        g.drawFilledRect(new Rect(0, 0, this.getWidth(), this.getHeight()), color);
        this.closeGraphics();
    }

    /**
     * Sets the pixel at the specified coordinates to the given color.
     * <p> This method directly modifies the underlying image of the Surface, without using a {@link RenderInstance} object.</p>
     * @param x     the x coordinate of the pixel
     * @param y     the y coordinate of the pixel
     * @param color the {@link Color} to set the pixel to
     */
    public void setPixel(int x, int y, Color color) {
        this.image.setRGB(x, y, color.getRGB());
    }

    /**
     * Sets the pixel at the specified coordinates to the given color.
     * <p> This method directly modifies the underlying image of the Surface, without using a {@link RenderInstance} object.</p>
     * @param point the {@link Point} representing the pixel coordinates
     * @param color the {@link Color} to set the pixel to
     */
    public void setPixel(Point p, Color color) {
        setPixel(p.x, p.y, color);
    }
}
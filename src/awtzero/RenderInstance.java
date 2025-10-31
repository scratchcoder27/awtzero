package awtzero;

import java.awt.*;

/**
 * The {@code RenderInstance} class provides a simplified wrapper around a {@link Graphics}
 * object for common 2D rendering operations such as drawing shapes, images, and text.
 * <p>
 * It acts as a lightweight rendering helper for use with AWT components like {@link Canvas}.
 * </p>
 */

public class RenderInstance {
    private final Graphics delegate;

    /**
     * Creates a new {@code RenderInstance} that delegates rendering operations to the specified {@link Graphics} object.
     *
     * @param delegate the {@link Graphics} object to delegate drawing operations to
     */
    public RenderInstance(Graphics delegate) {
        this.delegate = delegate;
    }

    /**
     * Disposes of this rendering context and releases system resources.
     * Equivalent to calling {@link Graphics#dispose()}.
     */
    public void dispose() {
        delegate.dispose();
    }

    // MARK: ADDITIONS

    /**
     * Clears the entire screen by filling it with black color.
     *
     * @param screen the {@link Canvas} representing the screen to clear
     */
    public void clearScreen(Canvas screen) {
        screen.setBackground(Color.black);
        delegate.fillRect(0, 0, screen.getWidth(), screen.getHeight());
    }

    /**
     * Fills the entire screen with the specified color.
     *
     * @param screen the {@link Canvas} representing the screen
     * @param color  the {@link Color} to fill the screen with
     */
    public void fillScreen(Canvas screen, Color color) {
        delegate.setColor(color);
        delegate.fillRect(0, 0, screen.getWidth(), screen.getHeight());
    }

    /**
     * Fills the entire screen with a color defined by RGB components.
     *
     * @param screen the {@link Canvas} representing the screen
     * @param red    red component (0–255)
     * @param green  green component (0–255)
     * @param blue   blue component (0–255)
     */
    public void fillScreen(Canvas screen, int red, int green, int blue) {
        fillScreen(screen, new Color(red, green, blue));
    }

    /**
     * Fills the entire screen with a single integer color value.
     *
     * @param screen the {@link Canvas} representing the screen
     * @param color  an integer RGB color value
     */
    public void fillScreen(Canvas screen, int color) {
        fillScreen(screen, new Color(color));
    }

    /**
     * Fills the entire screen with a color defined by floating-point RGB components.
     *
     * @param screen the {@link Canvas} representing the screen
     * @param red    red component (0.0–1.0)
     * @param green  green component (0.0–1.0)
     * @param blue   blue component (0.0–1.0)
     */
    public void fillScreen(Canvas screen, float red, float green, float blue) {
        fillScreen(screen, new Color(red, green, blue));
    }

    /**
     * Draws an image at the specified point.
     *
     * @param img the {@link Image} to draw
     * @param p   the {@link Point} representing the image position
     */
    public void blit(Image img, Point p) {
        delegate.drawImage(img, p.x, p.y, null);
    }

    /**
     * Draws an image at the specified point with an image observer.
     *
     * @param img       the {@link Image} to draw
     * @param p         the {@link Point} representing the image position
     * @param observer  the {@link Component} to notify as the image is updated
     */
    public void blit(Image img, Point p, Component observer) {
        delegate.drawImage(img, p.x, p.y, observer);
    }

    /**
     * Draws an image at the specified rectangle position and size.
     *
     * @param img the {@link Image} to draw
     * @param x   the x coordinate
     * @param y   the y coordinate
     * @param w   the width
     * @param h   the height
     */
    public void blit(Image img, int x, int y, int w, int h) {
        delegate.drawImage(img, x, y, w, h, null);
    }

    /**
     * Draws an image at the specified rectangle position and size, with an observer.
     *
     * @param img       the {@link Image} to draw
     * @param x         the x coordinate
     * @param y         the y coordinate
     * @param w         the width
     * @param h         the height
     * @param observer  the {@link Component} to notify as the image is updated
     */
    public void blit(Image img, int x, int y, int w, int h, Component observer) {
        delegate.drawImage(img, x, y, w, h, observer);
    }

    /**
     * Draws an image inside a specified rectangle.
     *
     * @param img  the {@link Image} to draw
     * @param rect the {@link Rect} specifying position and size
     */
    public void blit(Image img, Rect rect) {
        delegate.drawImage(img, rect.x, rect.y, rect.width, rect.height, null);
    }

    /**
     * Draws an {@link ImageWrapper} at a given point.
     *
     * @param img the {@link ImageWrapper} containing the image and metadata
     * @param p   the position to draw at
     */
    public void blit(ImageWrapper img, Point p) {
        delegate.drawImage(img.image, p.x, p.y, img.width, img.height, img.observer);
    }

    /**
     * Draws a line between two points with the specified color.
     *
     * @param startx starting x coordinate
     * @param starty starting y coordinate
     * @param endx   ending x coordinate
     * @param endy   ending y coordinate
     * @param color  the {@link Color} of the line
     */
    public void drawLine(int startx, int starty, int endx, int endy, Color color) {
        Color orgcolor = delegate.getColor();
        delegate.setColor(color);
        delegate.drawLine(startx, starty, endx, endy);
        delegate.setColor(orgcolor);
    }

    /**
     * Draws a line between two {@link Point} objects.
     *
     * @param start the start point
     * @param end   the end point
     * @param color the {@link Color} of the line
     */
    public void drawLine(Point start, Point end, Color color) {
        Color orgcolor = delegate.getColor();
        delegate.setColor(color);
        delegate.drawLine(start.x, start.y, end.x, end.y);
        delegate.setColor(orgcolor);
    }

    /**
     * Draws an unfilled circle at a given position and radius.
     *
     * @param pos    the {@link Point} at the center
     * @param radius the circle radius
     * @param color  the {@link Color} of the outline
     */
    public void drawCircle(Point pos, int radius, Color color) {
        Color orgcolor = delegate.getColor();
        delegate.setColor(color);
        delegate.drawOval(pos.x - radius, pos.y - radius, radius * 2, radius * 2);
        delegate.setColor(orgcolor);
    }

    /**
     * Draws a filled circle at a given position and radius.
     *
     * @param pos    the {@link Point} at the center
     * @param radius the circle radius
     * @param color  the {@link Color} to fill the circle
     */
    public void drawFilledCircle(Point pos, int radius, Color color) {
        Color orgcolor = delegate.getColor();
        delegate.setColor(color);
        delegate.fillOval(pos.x - radius, pos.y - radius, radius * 2, radius * 2);
        delegate.setColor(orgcolor);
    }

    /**
     * Draws an unfilled rectangle with the given color.
     *
     * @param rect  the {@link Rect} specifying position and size
     * @param color the {@link Color} of the outline
     */
    public void drawRect(Rect rect, Color color) {
        Color orgcolor = delegate.getColor();
        delegate.setColor(color);
        delegate.drawRect(rect.x, rect.y, rect.width, rect.height);
        delegate.setColor(orgcolor);
    }

    /**
     * Draws a filled rectangle with the given color.
     *
     * @param rect  the {@link Rect} specifying position and size
     * @param color the {@link Color} to fill the rectangle
     */
    public void drawFilledRect(Rect rect, Color color) {
        Color orgcolor = delegate.getColor();
        delegate.setColor(color);
        delegate.fillRect(rect.x, rect.y, rect.width, rect.height);
        delegate.setColor(orgcolor);
    }

    /**
     * Draws an unfilled rounded rectangle.
     *
     * @param rect      the {@link Rect} specifying position and size
     * @param color     the {@link Color} of the outline
     * @param arcWidth  the horizontal diameter of the arc at the corners
     * @param arcHeight the vertical diameter of the arc at the corners
     */
    public void drawRoundedRect(Rect rect, Color color, int arcWidth, int arcHeight) {
        Color orgcolor = delegate.getColor();
        delegate.setColor(color);
        delegate.drawRoundRect(rect.x, rect.y, rect.width, rect.height, arcWidth, arcHeight);
        delegate.setColor(orgcolor);
    }

    /**
     * Draws a filled rounded rectangle.
     *
     * @param rect      the {@link Rect} specifying position and size
     * @param color     the {@link Color} to fill the rectangle
     * @param arcWidth  the horizontal diameter of the arc at the corners
     * @param arcHeight the vertical diameter of the arc at the corners
     */
    public void drawFilledRoundedRect(Rect rect, Color color, int arcWidth, int arcHeight) {
        Color orgcolor = delegate.getColor();
        delegate.setColor(color);
        delegate.fillRoundRect(rect.x, rect.y, rect.width, rect.height, arcWidth, arcHeight);
        delegate.setColor(orgcolor);
    }

    /**
     * Draws a triangle by connecting three points.
     *
     * @param p1    the first vertex
     * @param p2    the second vertex
     * @param p3    the third vertex
     * @param color the {@link Color} of the outline
     */
    public void drawTriangle(Point p1, Point p2, Point p3, Color color) {
        Color orgcolor = delegate.getColor();
        delegate.setColor(color);
        delegate.drawLine(p1.x, p1.y, p2.x, p2.y);
        delegate.drawLine(p2.x, p2.y, p3.x, p3.y);
        delegate.drawLine(p3.x, p3.y, p1.x, p1.y);
        delegate.setColor(orgcolor);
    }

    /**
     * Draws a string of text at a given position with specified color and font.
     *
     * @param s     the string to draw
     * @param p     the position as a {@link Point}
     * @param color the {@link Color} of the text
     * @param font  the {@link Font} to use
     */
    public void drawText(String s, Point p, Color color, Font font) {
        Color orgcolor = delegate.getColor();
        Font orgfont = delegate.getFont();
        delegate.setColor(color);
        delegate.setFont(font);
        delegate.drawString(s, p.x, p.y);
        delegate.setColor(orgcolor);
        delegate.setFont(orgfont);
    }

    /**
     * Draws a string of text at a given position using current color and font.
     *
     * @param s the string to draw
     * @param p the position as a {@link Point}
     */
    public void drawText(String s, Point p) {
        delegate.drawString(s, p.x, p.y);
    }

    /**
     * Draws a string of text at a given position with specified color and font.
     *
     * @param s     the string to draw
     * @param x     the x coordinate
     * @param y     the y coordinate
     * @param color the {@link Color} of the text
     * @param font  the {@link Font} to use
     */
    public void drawText(String s, int x, int y, Color color, Font font) {
        Color orgcolor = delegate.getColor();
        Font orgfont = delegate.getFont();
        delegate.setColor(color);
        delegate.setFont(font);
        delegate.drawString(s, x, y);
        delegate.setColor(orgcolor);
        delegate.setFont(orgfont);
    }

    /**
     * Draws a string of text at a given position using current color and font.
     *
     * @param s the string to draw
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public void drawText(String s, int x, int y) {
        delegate.drawString(s, x, y);
    }
}

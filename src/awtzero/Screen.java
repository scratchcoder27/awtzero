package awtzero;


import java.awt.*;

/**
 * Provides a drawable screen area using double buffering to reduce flickering.
 */
public class Screen extends Canvas {
    private Image offscreenImage;
    private Graphics offscreenGraphics;
    private Rect screenrect;
	
	public Screen() {
	}
	
    /**
     * Update the screen (flip the buffers)
     * @param g The Graphics context to draw onto the screen
     */
	public void update(Graphics g) {
        if (offscreenImage == null ||
            offscreenImage.getWidth(null) != getWidth() ||
            offscreenImage.getHeight(null) != getHeight()) {
            offscreenImage = createImage(getWidth(), getHeight());
            offscreenGraphics = offscreenImage.getGraphics();
        }

        // Clear offscreen
        offscreenGraphics.setColor(getBackground());
        offscreenGraphics.fillRect(0, 0, getWidth(), getHeight());

        // Call paint on offscreen graphics
        paint(offscreenGraphics);

        // Draw the offscreen image onto the screen
        g.drawImage(offscreenImage, 0, 0, null);
    }
    
    /**
     * Get the Rect representing the screen dimensions
     * @return The Rect of the screen
     * @see Rect
     */
    public Rect getScreenRect() {
    	if (screenrect == null) {
    		screenrect = new Rect(0, 0, getWidth(), getHeight());
    	}
    	return this.screenrect;
    }

    /** Get immediate (offscreen) surface
     *  @return A Surface representing the offscreen buffer
     *  @see Surface
     */
    public Surface getImmediateSurface() {
        if (offscreenGraphics == null) {
            offscreenImage = createImage(getWidth(), getHeight());
            offscreenGraphics = offscreenImage.getGraphics();
        }
        return new Surface(offscreenImage);
    }
}

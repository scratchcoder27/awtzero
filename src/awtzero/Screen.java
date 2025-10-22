package awtzero;


import java.awt.*;

public class Screen extends Canvas {
    private Image offscreenImage;
    private Graphics offscreenGraphics;
    private Rect screenrect;
	
	public Screen() {
        
	}
	
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
    
    public Rect getScreenRect() {
    	if (screenrect == null) {
    		screenrect = new Rect(0, 0, getWidth(), getHeight());
    	}
    	return this.screenrect;
    }
}

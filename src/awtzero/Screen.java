package awtzero;


import java.awt.*;

@SuppressWarnings("serial")
public class Screen extends Canvas {
	private Color black;
    private Image offscreenImage;
    private Graphics offscreenGraphics;
    private Rect screenrect;
	
	public Screen() {
		black = new Color(0, 0, 0);
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
	
    public void clearScreen(Graphics g)  {
    	this.setBackground(black);
        g.fillRect(0, 0, getWidth(), getHeight());
    }
    
    public void fill(Graphics g, Color color) {
        g.setColor(color);
        g.fillRect(0, 0, getWidth(), getHeight());
    }
    
    public void fill(Graphics g, int red, int green, int blue) {
        fill(g, new Color(red, green, blue));
    }
    
    public void fill(Graphics g, int color) {
    	fill(g, new Color(color));
    }
    
    public void fill(Graphics g, float red, float green, float blue) {
    	fill(g, new Color(red,green, blue));
    }
    
    public void blit(Graphics g, Image img, Point p) {
    	g.drawImage(img, p.x, p.y, null);
    }
    
    public void blit(Graphics g, Image img, Point p, Component observer) {
    	g.drawImage(img, p.x, p.y, observer);
    }
    
    public void blit(Graphics g, Image img, int x, int y, int w, int h) {
    	g.drawImage(img, x, y, w, h, null);
    }
    
    public void blit(Graphics g, Image img, int x, int y, int w, int h, Component observer) {
    	g.drawImage(img, x, y, w, h, observer);
    }
    
    public void blit(Graphics g, Image img, Rect rect) {
    	g.drawImage(img, rect.x, rect.y, rect.width, rect.height, null);
    }
    
    public void blit(Graphics g, ImageWrapper img, Point p) {
    	g.drawImage(img.image, p.x, p.y, img.width, img.height, img.observer);
    }
    
    public void drawLine(Graphics g, int startx, int starty, int endx, int endy, Color color) {
    	Color orgcolor = g.getColor();
    	g.setColor(color);
    	g.drawLine(startx, starty, endx, endy);
    	g.setColor(orgcolor);
    	
    }
    
    public void drawLine(Graphics g, Point start, Point end, Color color) {
    	Color orgcolor = g.getColor();
    	g.setColor(color);
    	g.drawLine(start.x, start.y, end.x, end.y);
    	g.setColor(orgcolor);
    }
    
    public void drawCircle(Graphics g, Point pos, int radius, Color color) {
    	Color orgcolor = g.getColor();
    	g.setColor(color);
    	g.drawOval(pos.x - radius, pos.y - radius, radius * 2, radius * 2);
    	g.setColor(orgcolor);
    }
    
    public void drawFilledCircle(Graphics g, Point pos, int radius, Color color) {
    	Color orgcolor = g.getColor();
    	g.setColor(color);
    	g.fillOval(pos.x - radius, pos.y - radius, radius * 2, radius * 2);
    	g.setColor(orgcolor);
    }
    
    public void drawRect(Graphics g, Rect rect, Color color) {
    	Color orgcolor = g.getColor();
    	g.setColor(color);
    	g.drawRect(rect.x, rect.y, rect.width, rect.height);
    	g.setColor(orgcolor);
    }
    
    public void drawFilledRect(Graphics g, Rect rect, Color color) {
    	Color orgcolor = g.getColor();
    	g.setColor(color);
    	g.fillRect(rect.x, rect.y, rect.width, rect.height);
    	g.setColor(orgcolor);
    }

    public void drawRoundedect(Graphics g, Rect rect, Color color, int arcWidth, int arcHeight) {
    	Color orgcolor = g.getColor();
    	g.setColor(color);
    	g.drawRoundRect(rect.x, rect.y, rect.width, rect.height, arcWidth, arcHeight);
    	g.setColor(orgcolor);
    }
    
    public void drawFilledRoundedRect(Graphics g, Rect rect, Color color, int arcWidth, int arcHeight) {
    	Color orgcolor = g.getColor();
    	g.setColor(color);
    	g.fillRoundRect(rect.x, rect.y, rect.width, rect.height, arcWidth, arcHeight);
    	g.setColor(orgcolor);
    }

    public void drawText(Graphics g, String s, Point p, Color color, Font font) {
    	Color orgcolor = g.getColor();
    	Font orgfont = g.getFont();
    	
        g.setColor(color);

    	g.setFont(font);
    	
        g.drawString(s, p.x, p.y);
        
    	g.setColor(orgcolor);
    	g.setFont(orgfont);
    }
    
    public void drawText(Graphics g, String s, Point p) {
    	Color orgcolor = g.getColor();
    	Font orgfont = g.getFont();
    	
        g.drawString(s, p.x, p.y);
        
    	g.setColor(orgcolor);
    	g.setFont(orgfont);
    }
    
    public void drawText(Graphics g, String s, int x, int y, Color color, Font font) {
    	Color orgcolor = g.getColor();
    	Font orgfont = g.getFont();
    	
        g.setColor(color);

    	g.setFont(font);
    	
        g.drawString(s, x, y);
        
    	g.setColor(orgcolor);
    	g.setFont(orgfont);
    }
    
    public void drawText(Graphics g, String s, int x, int y) {
    	Color orgcolor = g.getColor();
    	Font orgfont = g.getFont();
    	
        g.drawString(s, x, y);
        
    	g.setColor(orgcolor);
    	g.setFont(orgfont);
    }
    
    public Rect getScreenRect() {
    	if (screenrect == null) {
    		screenrect = new Rect(0, 0, getWidth(), getHeight());
    	}
    	return this.screenrect;
    }
}

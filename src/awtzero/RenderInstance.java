package awtzero;
import java.awt.*;

public class RenderInstance {
    private final Graphics delegate;

    public RenderInstance(Graphics delegate) {
        this.delegate = delegate;
    }

    public void dispose() {
        delegate.dispose();
    }

    // MARK: ADDITIONS

    public void clearScreen(Canvas screen)  {
    	screen.setBackground(Color.black);
        delegate.fillRect(0, 0, screen.getWidth(), screen.getHeight());
    }
    
    public void fillScreen(Canvas screen, Color color) {
        delegate.setColor(color);
        delegate.fillRect(0, 0, screen.getWidth(), screen.getHeight());
    }
    
    public void fillScreen(Canvas screen, int red, int green, int blue) {
        fillScreen(screen, new Color(red, green, blue));
    }
    
    public void fillScreen(Canvas screen, int color) {
    	fillScreen(screen, new Color(color));
    }
    
    public void fillScreen(Canvas screen, float red, float green, float blue) {
    	fillScreen(screen, new Color(red, green, blue));
    }
    
    public void blit(Image img, Point p) {
    	delegate.drawImage(img, p.x, p.y, null);
    }
    
    public void blit(Image img, Point p, Component observer) {
    	delegate.drawImage(img, p.x, p.y, observer);
    }
    
    public void blit(Image img, int x, int y, int w, int h) {
    	delegate.drawImage(img, x, y, w, h, null);
    }
    
    public void blit(Image img, int x, int y, int w, int h, Component observer) {
    	delegate.drawImage(img, x, y, w, h, observer);
    }
    
    public void blit(Image img, Rect rect) {
    	delegate.drawImage(img, rect.x, rect.y, rect.width, rect.height, null);
    }
    
    public void blit(ImageWrapper img, Point p) {
    	delegate.drawImage(img.image, p.x, p.y, img.width, img.height, img.observer);
    }
    
    public void drawLine(int startx, int starty, int endx, int endy, Color color) {
    	Color orgcolor = delegate.getColor();
    	delegate.setColor(color);
    	delegate.drawLine(startx, starty, endx, endy);
    	delegate.setColor(orgcolor);
    	
    }
    
    public void drawLine(Point start, Point end, Color color) {
    	Color orgcolor = delegate.getColor();
    	delegate.setColor(color);
    	delegate.drawLine(start.x, start.y, end.x, end.y);
    	delegate.setColor(orgcolor);
    }
    
    public void drawCircle(Point pos, int radius, Color color) {
    	Color orgcolor = delegate.getColor();
    	delegate.setColor(color);
    	delegate.drawOval(pos.x - radius, pos.y - radius, radius * 2, radius * 2);
    	delegate.setColor(orgcolor);
    }
    
    public void drawFilledCircle(Point pos, int radius, Color color) {
    	Color orgcolor = delegate.getColor();
    	delegate.setColor(color);
    	delegate.fillOval(pos.x - radius, pos.y - radius, radius * 2, radius * 2);
    	delegate.setColor(orgcolor);
    }
    
    public void drawRect(Rect rect, Color color) {
    	Color orgcolor = delegate.getColor();
    	delegate.setColor(color);
    	delegate.drawRect(rect.x, rect.y, rect.width, rect.height);
    	delegate.setColor(orgcolor);
    }
    
    public void drawFilledRect(Rect rect, Color color) {
    	Color orgcolor = delegate.getColor();
    	delegate.setColor(color);
    	delegate.fillRect(rect.x, rect.y, rect.width, rect.height);
    	delegate.setColor(orgcolor);
    }

    public void drawRoundedect(Rect rect, Color color, int arcWidth, int arcHeight) {
    	Color orgcolor = delegate.getColor();
    	delegate.setColor(color);
    	delegate.drawRoundRect(rect.x, rect.y, rect.width, rect.height, arcWidth, arcHeight);
    	delegate.setColor(orgcolor);
    }
    
    public void drawFilledRoundedRect(Rect rect, Color color, int arcWidth, int arcHeight) {
    	Color orgcolor = delegate.getColor();
    	delegate.setColor(color);
    	delegate.fillRoundRect(rect.x, rect.y, rect.width, rect.height, arcWidth, arcHeight);
    	delegate.setColor(orgcolor);
    }

    public void drawText(String s, Point p, Color color, Font font) {
    	Color orgcolor = delegate.getColor();
    	Font orgfont = delegate.getFont();
    	
        delegate.setColor(color);

    	delegate.setFont(font);
    	
        delegate.drawString(s, p.x, p.y);
        
    	delegate.setColor(orgcolor);
    	delegate.setFont(orgfont);
    }
    
    public void drawText(String s, Point p) {
    	Color orgcolor = delegate.getColor();
    	Font orgfont = delegate.getFont();
    	
        delegate.drawString(s, p.x, p.y);
        
    	delegate.setColor(orgcolor);
    	delegate.setFont(orgfont);
    }
    
    public void drawText(String s, int x, int y, Color color, Font font) {
    	Color orgcolor = delegate.getColor();
    	Font orgfont = delegate.getFont();
    	
        delegate.setColor(color);

    	delegate.setFont(font);
    	
        delegate.drawString(s, x, y);
        
    	delegate.setColor(orgcolor);
    	delegate.setFont(orgfont);
    }
    
    public void drawText(String s, int x, int y) {
    	Color orgcolor = delegate.getColor();
    	Font orgfont = delegate.getFont();
    	
        delegate.drawString(s, x, y);
        
    	delegate.setColor(orgcolor);
    	delegate.setFont(orgfont);
    }
}

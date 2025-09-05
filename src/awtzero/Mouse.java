package awtzero;


import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

public class Mouse implements MouseListener, MouseMotionListener {

    private static int mouseX = 0;
    private static int mouseY = 0;
    private static int mouseButton = 0;
    public static Cursor current = Cursor.getDefaultCursor();

    public static Point getMousePos() {
        return new Point(mouseX, mouseY);
    }

    public static int getMouseButton() {
        return mouseButton;
    }

    @Override
    public void mousePressed(MouseEvent e) {
    	/**
         * Returns the currently pressed mouse button.
         * 
         * @return the mouse button code:
         *         <ul>
         *           <li>1 - Left button</li>
         *           <li>2 - Middle button</li>
         *           <li>3 - Right button</li>
         *           <li>0 - No button currently pressed</li>
         *         </ul>
         */
    	
        mouseButton = e.getButton();  // 1 = left, 2 = middle, 3 = right
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseButton = 0; // Reset on release
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }
    
    public static void hideCursor(Component component) {
        BufferedImage cursorImg = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                cursorImg, new java.awt.Point(0, 0), "blank cursor");
        component.setCursor(blankCursor);
      
    }
    public static void setCursorImage(Image cursorimage, Point hotpoint, Component component) {
    	Cursor customCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                cursorimage, new java.awt.Point(hotpoint.x, hotpoint.y), "custom cursor");
    	current = customCursor;
        component.setCursor(customCursor);
    }
    
    public static void showCursor(Component component) {
    	component.setCursor(current);
    }
    
    public static void resetCursor(Component component) {
    	current = Cursor.getDefaultCursor();
    	component.setCursor(current);
    }

    // Unused methods from MouseListener
    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}

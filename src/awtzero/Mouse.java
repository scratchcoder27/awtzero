package awtzero;


import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

/**
 * A static mouse class to get and set mouse and cursor properties.
 * <p>Since it is a static class, there is no need to create an instance of Mouse, however, {@code window.setupMouse();} must be called to initialize mouse tracking.</p>
 * @see MouseListener
 */
public class Mouse implements MouseListener, MouseMotionListener {

    private static int mouseX = 0;
    private static int mouseY = 0;
    private static int mouseButton = 0;
    /** The cursor being rendered */
    public static Cursor current = Cursor.getDefaultCursor();

    /**
     * Returns the current mouse position as a Point object.
     * @return a Point representing the current mouse position
     * @see Point
     */
    public static Point getMousePos() {
        return new Point(mouseX, mouseY);
    }

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
    public static int getMouseButton() {
        return mouseButton;
    }

    @Override
    public void mousePressed(MouseEvent e) {	
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
    
    /**
     * Hides the mouse cursor while it is in the specified component.
     * Use {@link #showCursor(Component)} to show the cursor again.
     * Use {@code Screen} as the component parameter to hide the cursor in the entire screen.
     * @param component the component in which to hide the cursor
     * @see #showCursor(Component)
     */
    public static void hideCursor(Component component) {
        BufferedImage cursorImg = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                cursorImg, new java.awt.Point(0, 0), "blank cursor");
        component.setCursor(blankCursor);
    }

    /**
     * Sets a custom image as the mouse cursor in the specified component.
     * @param cursorimage the {@code Image} to be used as the cursor
     * @param hotpoint the {@code Point} within the image that represents the cursor's active point (where clicks are actually registered)
     * @param component the component in which to set the custom cursor
     */
    public static void setCursorImage(Image cursorimage, Point hotpoint, Component component) {
    	Cursor customCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                cursorimage, new java.awt.Point(hotpoint.x, hotpoint.y), "custom cursor");
    	current = customCursor;
        component.setCursor(customCursor);
    }
    
    /**
     * Shows the mouse cursor again after it has been hidden.
     * Use {@link #hideCursor(Component)} to hide the cursor.
     * @param component the component in which to remove the cursor hide effect
     * @see #hideCursor(Component)
     */
    public static void showCursor(Component component) {
    	component.setCursor(current);
    }
    
    /**
     * Resets the mouse cursor to the default system cursor in the specified component.
     * @param component the component in which to reset the cursor
     */
    public static void resetCursor(Component component) {
    	current = Cursor.getDefaultCursor();
    	component.setCursor(current);
    }

    // Unused methods from MouseListener
    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}

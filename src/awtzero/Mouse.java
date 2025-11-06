package awtzero;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.Point;

/**
 * A static mouse class to get and set mouse and cursor properties.
 */
public class Mouse implements MouseListener, MouseMotionListener {

    private static int mouseX = 0;
    private static int mouseY = 0;
    private static int mouseButton = 0;

    /** relative movement per frame when locked */
    private static int deltaX = 0;
    private static int deltaY = 0;

    /** Cursor currently used */
    public static Cursor current = Cursor.getDefaultCursor();

    /** mouse lock fields */
    public static boolean locked = false;
    private static boolean recentering = false;

    /** The {@link Robot} that is used while Mouse Locking */
    private static Robot robot;
    private static Component lockComponent;

    static {
        try {
            robot = new Robot();
        } catch (Exception ignored) {}
    }

    public static awtzero.Point getMousePos() {
        return new awtzero.Point(mouseX, mouseY);
    }

    /** Returns relative movement when mouse is locked */
    public static awtzero.Point getMouseDelta() {
        return new awtzero.Point(deltaX, deltaY);
    }

    public static int getMouseButton() {
        return mouseButton;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mouseButton = e.getButton();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseButton = 0;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        track(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        track(e);
    }

    /**
     * Internal mouse tracking logic (normal + locked mode)
     */
    private void track(MouseEvent e) {

        if (locked && !recentering) {
            int centerX = lockComponent.getWidth() / 2;
            int centerY = lockComponent.getHeight() / 2;

            deltaX = e.getX() - centerX;
            deltaY = e.getY() - centerY;

            // swallow tiny movements (noise)
            if (deltaX != 0 || deltaY != 0) {
                recentering = true;
                Point screenPos = lockComponent.getLocationOnScreen();
                robot.mouseMove(screenPos.x + centerX, screenPos.y + centerY);
            }
        } else {
            recentering = false;
        }

        mouseX = e.getX();
        mouseY = e.getY();
    }

    // ----------------------------------------------------------------------------------
    // CURSOR FUNCTIONS (unchanged)
    // ----------------------------------------------------------------------------------

    public static void hideCursor(Component component) {
        BufferedImage cursorImg = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                cursorImg, new Point(0, 0), "blank cursor");
        component.setCursor(blankCursor);
    }

    public static void setCursorImage(Image cursorimage, awtzero.Point hotpoint, Component component) {
        Cursor customCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                cursorimage, new Point(hotpoint.x, hotpoint.y), "custom cursor");
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

    // ----------------------------------------------------------------------------------
    // ✅ NEW FUNCTIONS — MOUSE LOCKING SUPPORT
    // ----------------------------------------------------------------------------------

    /**
     * Locks the mouse inside the component and enables relative mouse movement.
     */
    public static void lockMouse(Component component) {
        locked = true;
        lockComponent = component;
        hideCursor(component);

        // Center mouse immediately
        Point screenPos = component.getLocationOnScreen();
        robot.mouseMove(screenPos.x + component.getWidth() / 2, screenPos.y + component.getHeight() / 2);
    }

    /**
     * Unlocks the mouse and returns normal absolute mouse movement.
     */
    public static void unlockMouse(Component component) {
        locked = false;
        recentering = false;
        deltaX = deltaY = 0;
        showCursor(component);
    }

    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}
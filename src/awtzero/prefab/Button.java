package awtzero.prefab;

import java.awt.Color;

import awtzero.Rect;
import awtzero.RenderInstance;
import awtzero.Screen;
import java.awt.Font;

/**
 * A simple Button prefab for AWTZero
 * It consists of a rounded rectangle with a border with text on it
 * with hover and click interactivity
 * <p><strong> The button is rudimentary and may not work perfectly in all cases. Use at your own risk. You are advised to make your own class for production use </strong></p>
 * <p>Usage:
 * <p> {@link #interact()} is called continuously while the button is clicked
 * <p> {@link #interactOnce()} is called once per click
 * These methods should be overridden to define button behavior
 * <p> {@code color1} is the inner color, {@code color2} is the border color, and {@code textColor} is the text color
 * <p> {@code font} defaults to Sans Serif, size 12. Can be changed with {@link #setFont(Font, int)}
 * <p> borderwidth can be changed with {@link #setBorderWidth(int)}
 * <p> border radius can be changed with {@link #setBorderRadius(int, int)}
 */
public class Button {
    int x, y, width, height;
    String text;

    /** The inner color */
    Color color1;
    /** The border color */
    Color color2;
    /** The text color */
    Color textColor;

    Color originalcolor;

    /** The font used for rendering */
    Font font;

    int rx, ry; // border radius x and y
    int borderwidth;

    int stringsize;

    private boolean click_blk;
    private int click_timer;

    /**
     * Constructor for {@link Button}
     * @param x The x position of the button
     * @param y The y position of the button
     * @param width The width of the button
     * @param height The height of the button
     * @param text The text to be displayed on the button
     */
    public Button(int x, int y, int width, int height, String text) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;

        this.color1 = Color.BLACK;
        this.color2 = Color.GREEN;
        this.textColor = Color.WHITE;

        this.originalcolor = color2;

        this.font = new java.awt.Font(java.awt.Font.SANS_SERIF, java.awt.Font.PLAIN, 12);
        this.stringsize = 3;

        this.rx = 30;
        this.ry = 30;

        this.borderwidth = 5;

        click_blk = false;
        click_timer = 0;
    }

    /**
     * Sets the colors of the button
     * @param color1 the inner color
     * @param color2 the border color
     * @param textColor the color of the Text
     */
    public void setColors(Color color1, Color color2, Color textColor) {
        this.color1 = color1;
        this.color2 = color2;
        this.textColor = textColor;

        this.originalcolor = color2;
    }

    /**
     * Sets the font of the button text
     * @param font a {@link Font} object
     * @param size a size in points of type int
     */
    public void setFont(Font font, int size) {
        this.font = font;
        this.stringsize = (size * 3) / 12;
    }

    /**
     * Sets the border radius of the button
     * @param r_x radius in x direction
     * @param r_y radius in y direction
     */
    public void setBorderRadius(int r_x, int r_y) {
        this.rx = r_x;
        this.ry = r_y;
    }

    /**
     * Sets the border width of the button
     * @param borderwidth width in pixels
     */
    public void setBorderWidth(int borderwidth) {
        this.borderwidth = borderwidth;
    }

    /**
     * Draws the button on the given {@link Screen} using the given {@link RenderInstance}
     * <p> Should be called each frame to render the button
     * @param screen The {@link Screen} to draw on
     * @param g The {@link RenderInstance} to use for drawing
     */
    public void draw(Screen screen, RenderInstance g) {
        g.drawFilledRoundedRect(new Rect(x, y, width, height), color2, rx, ry);
        g.drawFilledRoundedRect(new Rect(x+borderwidth, y+borderwidth, width-(2 * borderwidth), height-(2 * borderwidth)), color1, rx, ry);
        g.drawText(text, x + (width / 2) - (text.length() * this.stringsize), y + (height / 2) + (stringsize * 5 / 3), textColor, this.font);
    }

    /**
     * Draws the button using AWT Graphics only
     * @param g AWT Graphics object
     */
    public void draw(java.awt.Graphics g) {
        Color orgcolor = g.getColor();

        g.setColor(this.color2);
    	g.fillRoundRect(this.x, this.y, this.width, this.height, rx, ry);

    	g.setColor(this.color1);
    	g.fillRoundRect(this.x + borderwidth , this.y + borderwidth, this.width - (2 * borderwidth), this.height - (2 * borderwidth), rx, ry);

    	Font orgfont = g.getFont();
    	g.setColor(this.textColor);
    	g.setFont(font);
        g.drawString(this.text, x + (width / 2) - (text.length() * this.stringsize), y + (height / 2) + (stringsize * 5 / 3));
    	g.setColor(orgcolor);
    	g.setFont(orgfont);
    }

    /**
     * Checks if the mouse is over the button
     * <p> The mouse coordinates can be obtained using {@link awtzero.Mouse#getMousePos()}
     * @param mouseX the mouse x position
     * @param mouseY the mouse y position
     * @return true if the mouse is over the button, false otherwise
     */
    public boolean isMouseOver(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    /**
     * <strong>Override this method to define button behavior on continuous click</strong>
     */
    public void interact() {
        //Should be overridden
    }

    /**
     * <strong>Override this method to define button behavior if it is clicked (safer than {@link Button#interact()}</strong>
     */
    public void interactOnce() {
        //Should be overridden
    }

    /**
     * Updates the button state based on mouse position and click status
     * <p> Should be called each frame to update the button state
     * <p> The mouse coordinates can be obtained using {@link awtzero.Mouse#getMousePos()}
     * @param mouseX the mouse x position
     * @param mouseY the mouse y position
     * @param mousePressed if the mouse is currently pressed. Obtained using {@link awtzero.Mouse#getMouseButton()}
     */
    public void update(int mouseX, int mouseY, boolean mousePressed) {
        if (click_timer > 0) {
            click_timer--;
        }

        boolean touching = isMouseOver(mouseX, mouseY);

        if (mousePressed && (!touching)) {
            click_blk = true;
        }

        if (click_blk && (!mousePressed)) {
            click_blk = false;
            click_timer = 5;
        }

        if (touching) {
            this.color2 = Color.LIGHT_GRAY;

            if (mousePressed) {
                this.interact();

                if (!click_blk && click_timer == 0) {
                    this.interactOnce();
                    click_timer = 5;
                }
            }

        } else {
            this.color2 = originalcolor;
        }
    }
}

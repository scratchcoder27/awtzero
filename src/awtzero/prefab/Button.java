package awtzero.prefab;

import java.awt.Color;

import awtzero.Rect;
import awtzero.RenderInstance;
import awtzero.Screen;
import java.awt.Font;

public class Button {
    int x, y, width, height;
    String text;
    Color color1, color2, textColor, originalcolor;
    Font font;

    int rx, ry; // border radius x and y
    int borderwidth;

    int stringsize;

    private boolean click_blk;
    private int click_timer;

    public Button(int x, int y, int width, int height, String text, int fancy_touch) {
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

    public Button(int x, int y, int width, int height, String text) {
        this(x, y, width, height, text, 0);
    }

    public void setColors(Color color1, Color color2, Color textColor) {
        this.color1 = color1;
        this.color2 = color2;
        this.textColor = textColor;

        this.originalcolor = color2;
    }

    public void setFont(Font font, int size) {
        this.font = font;
        this.stringsize = (size * 3) / 12;
    }

    public void setBorderRadius(int r_x, int r_y) {
        this.rx = r_x;
        this.ry = r_y;
    }

    public void setBorderWidth(int borderwidth) {
        this.borderwidth = borderwidth;
    }

    public void draw(Screen screen, RenderInstance g) {
        g.drawFilledRoundedRect(new Rect(x, y, width, height), color2, rx, ry);
        g.drawFilledRoundedRect(new Rect(x+borderwidth, y+borderwidth, width-(2 * borderwidth), height-(2 * borderwidth)), color1, rx, ry);
        g.drawText(text, x + (width / 2) - (text.length() * this.stringsize), y + (height / 2) + (stringsize * 5 / 3), textColor, this.font);
    }

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

    public boolean isMouseOver(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    public void interact() {
        //Should be overridden
    }

    public void interactOnce() {
        //Should be overridden
    }

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

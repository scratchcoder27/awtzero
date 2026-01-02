package awtzero.prefab;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;

import awtzero.Rect;
import awtzero.RenderInstance;
import awtzero.Screen;
import awtzero.Vector2;
import awtzero.Key;
import awtzero.Keyboard;
import awtzero.Point;
import awtzero.Window;

//MARK: CONFIG WINDOW
class ConfigDialog extends Dialog {
    DebugMonitor monitor;
    
    public ConfigDialog(Frame window, DebugMonitor monitor){
        super(window, true);
        this.monitor = monitor;

        setBackground(Color.GRAY);

        setLayout(new BorderLayout());
        Panel panel = new Panel();

        java.awt.Button closeButton = new java.awt.Button("Close");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        panel.add(closeButton);
        add("South", panel);
        setSize(200,200);

        addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent windowEvent){
            dispose();
        }
        });
    }

    public void paint(Graphics g){
        g.setColor(Color.white);
        g.drawString("Edit Settings", 25, 20);
    }
}

//MARK: DEBUGMONITOR
class DebugMonitor {
    public int x, y;
    public Color color;
    public int value;

    public int height;

    public String name;

    public boolean hidden;
    public boolean isSlider;

    public Rect rect;
    public Rect innerRect;
    public Rect textRect;
    public Rect lineRect;
    public Rect sliderInteractRect;
    public Point labelPoint;
    public Point dataPoint;
    public Point lineDataPoint;
    public Font textFont;

    final int WIDTH = 150;
    final int HEIGHT_SLIDER = 50;
    final int HEIGHT = 30;

    final int BORDER_RADIUS = 10;

    public int minValue;
    public int maxValue;

    private boolean isBeingDragged;
    private Vector2 dragDelta;

    private Window window;

    private boolean isShowingSlider;

    public DebugMonitor(String name, int x, int y, Color color, int initValue, Font font, boolean isSlider, Window window) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.color = color;
        this.value = initValue;
        this.textFont = font;
        this.isSlider = isSlider;
        this.window = window;

        this.height = (isSlider) ? HEIGHT_SLIDER : HEIGHT;

        this.lineDataPoint = new Point(0, 0);

        this.minValue = 0;
        this.maxValue = 100;

        this.hidden = false;

        this.isBeingDragged = false;
        this.dragDelta = new Vector2(0, 0);

        this.rect = new Rect(x, y, WIDTH, this.height);
        updatePositions();

        this.isShowingSlider = false;
    }

    public void updatePositions() {
        this.innerRect = rect.inflate(-2, -2);
        this.textRect = new Rect(rect.x + (WIDTH - 55), rect.y + 5, 50,20);
        this.labelPoint = new Point(rect.x + 8, rect.y + 19);
        this.dataPoint = new Point(rect.x + (WIDTH - 50), rect.y + 19);
        this.lineRect = new Rect(rect.x + 5, rect.y + ((int) (this.height * 2.1/3)), WIDTH - 10, 3);
        this.lineDataPoint.y = this.lineRect.y;
        
        this.sliderInteractRect = new Rect(this.rect.x, this.lineRect.y - 6, this.rect.width, 14);
    }

    public void draw(RenderInstance g) {
        if (this.hidden) return;

        g.drawFilledRoundedRect(rect, color, BORDER_RADIUS, BORDER_RADIUS);
        g.drawFilledRoundedRect(innerRect, Color.BLACK, BORDER_RADIUS, BORDER_RADIUS);
        g.drawFilledRect(textRect, Color.DARK_GRAY);

        g.drawText(name, labelPoint, Color.WHITE, this.textFont);
        g.drawText(this.value + "", dataPoint, Color.WHITE, this.textFont);

        if (this.isSlider) {
            g.drawFilledRoundedRect(lineRect, this.color, 1, 2);

            int acceptedValue = Math.max(Math.min(this.maxValue, this.value), this.minValue);
            int drawXOffset = (int) (((((double) acceptedValue) - this.minValue) / this.maxValue) * (WIDTH - 10));
            lineDataPoint.x = this.rect.x + drawXOffset;
            g.drawFilledCircle(lineDataPoint, 7, Color.WHITE);
            g.drawFilledCircle(lineDataPoint, 5, this.color);
        }
    }

    public void update(Point mousePos, int mouse, Keyboard keyboard) {
        boolean touching = this.rect.collidepoint(mousePos.x, mousePos.y);
        if (!touching) return;

        boolean touchingSlider = (this.isSlider) ? (this.sliderInteractRect.collidepoint(mousePos.x, mousePos.y)) : false;

        if (isBeingDragged) {
            if (mouse == 1) {
                Vector2 newPos = mousePos.asVector2().subtract(dragDelta);
                this.rect.x = (int) newPos.x;
                this.rect.y = (int) newPos.y;
                updatePositions();
            } else {
                this.isBeingDragged = false;
                this.dragDelta.x = 0;
                this.dragDelta.y = 0;
            }
        }

        if (touching && keyboard.isKeyDown(Key.E)) {
            ConfigDialog configScreen = new ConfigDialog(this.window.frame, this);
            configScreen.setVisible(true);
        }

        if ((!touchingSlider) && touching) {
            this.isBeingDragged = true;
            this.dragDelta.x = mousePos.x - this.rect.x;
            this.dragDelta.y = mousePos.y - this.rect.y;
        }

        if (touchingSlider && touching) {
            if (mouse == 1) {
                double relativeX = mousePos.x - this.rect.x;
                relativeX = Math.max(0.0, Math.min((WIDTH - 10.0), relativeX));
                double calculatedValue = relativeX / (WIDTH - 10.0);
                calculatedValue = (calculatedValue * this.maxValue) + this.minValue;

                this.value = (int) calculatedValue;
            }
        }
    }
}

// MARK: MONITORS
public class DebugMonitors {
    public static int sX, sY;
    public static HashMap<String, DebugMonitor> monitors;
    public static Font font;
    public static Window window;
    public static Screen screen;
    public static Keyboard keyboard;

    public static void initialize(Window win, String textFontName, Keyboard keys) {
        window = win;
        keyboard = keys;
        screen = win.screen;
        Dimension screenSize = screen.getSize();
        sX = screenSize.width;
        sY = screenSize.height;
        monitors = new HashMap<>();

        font = new Font(textFontName, 1, 12);
    }

    public static void addMonitor(String name, Point position, Color color, int initialValue, boolean isSlider) {
        if (monitors.containsKey(name)) return;

        monitors.put(name, new DebugMonitor(name, position.x, position.y, color, initialValue, font, isSlider, window));
    }

    public static void deleteMonitor(String name) {
        monitors.remove(name);
    }

    public static void drawMonitors(RenderInstance g) {
        DebugMonitor[] monitorList = monitors.values().toArray(new DebugMonitor[monitors.size()]); // to prevent errors from objects getting removed while the loop is running
    
        for (DebugMonitor m : monitorList) {
            m.draw(g);
        }

        monitorList = null;
    }

    public static void updateMonitors(Point mousePos, int mouse) {
        DebugMonitor[] monitorList = monitors.values().toArray(new DebugMonitor[monitors.size()]);
        for (DebugMonitor m : monitorList) {
            m.update(mousePos, mouse, keyboard);
        }

        monitorList = null;
    }

    public static int getValue(String name) {
        DebugMonitor m = monitors.get(name);
        if (m != null) {
            return m.value;
        } else {
            return -1;
        }
    }
}

<h1>AWTZero</h1>
<h3>This is my attempt at building a wrapper for AWT, which allows the easy creation of games on  Java.<br>
<br>
 This is aimed at allowing students to make graphical applications easily, without having to work with a full game engine.</h3>

<h5>The project is heavily inspired by pygame and pygame-zero on python. Javadoc is available, either by running javadoc locally, or at https://scratchcoder27.github.io/awtzero/awtzero/module-summary.html </h5>

## Setting up the library
SJGL is packaged into one .jar file: ```SJGL-{version}.jar```. Head to the release page and install the latest .jar.
1. Launch your preferred IDE (Eclipse IDE is recommended)
2. Create a new Java Project and set the .jar file in the ```Build Path > Classpath``

## QuickStart:
Just copy and paste this code to create a blank window:
```java
import awtzero.*;

public class App {
    public static Window window;
    public static Keyboard keyboard;
    public static Screen screen;
    public static int WIDTH = 800;
    public static int HEIGHT = 600;

    private static void draw(RenderInstance g) {
        g.clearScreen(screen);
    }

    private static void update() {
    }

    public static void main(String[] args) {
        window = new Window("Demo", WIDTH, HEIGHT, false);
        screen = window.screen;

        window.setTargetFPS(60);

        keyboard = new Keyboard();
        window.setKeyboard(keyboard);
        window.setupMouse();

        window.setOnUpdate(() -> {
            update();
        });
        window.setOnDraw(g -> {
            draw(g);
        });

        window.startGameLoop();
    }
}
```

The library allows easy rendering of shapes, primarily through the `RenderInstance` class, which composits an AWT `Graphics` instance.
<br>There is an extensive `Vector2` class, allowing easy vector and mathematical operations, and the powerful `awtzero.transforms.Colors.applyPixelShader()` and `awtzero.transforms.Colors.applyKernelShader()`methods, which allow executing methods on each pixel of an Image or `Surface` parallely *on the CPU*.
### A more extensive demonstration may be seen here:
```java
package awtzero;

import awtzero.prefab.Button;
import java.awt.Color;
import java.awt.Font;

/**
 * This is a demo of AWTZero features including keyboard and mouse input, which also provides an example.
 */
public class Demo {
    public static Window window; // Make a static Window
    public static Keyboard keyboard; // a static Keyboard
    public static Screen screen; // define the Screen to be used for drawing
    public static Rect player_rect = new Rect(0, 0, 0, 0); // defines the player rect
    public static Font gamefont = new Font(Font.MONOSPACED, Font.BOLD, 30);
    public static ExitButton exitButton = new ExitButton(); // Create an instance of ExitButton

    static class ExitButton extends Button { // Can keep this in a separate file if wanted
        public ExitButton() {
            super(570, 500, 200, 50, "Exit"); // Position and size
            this.setBorderWidth(3);
            this.setFont(gamefont, 30);
        }

        @Override
        public void interactOnce() { // Use interactOnce to avoid multiple triggers, interact if continuous input is
                                     // needed
            System.exit(0); // Close the window
        }
    }

    private static void draw(RenderInstance g) { // the method handling drawing

        g.clearScreen(screen);

        exitButton.draw(screen, g); // Draw the exit button

        g.drawText("AWTZero - DEMO", 250, 100, Color.WHITE, gamefont);
        g.drawText("Move with Arrow Keys", 200, 300, Color.WHITE, gamefont);
        g.drawText("Click to move towards mouse", 130, 400, Color.WHITE, gamefont);

        g.drawFilledRect(player_rect, Color.RED);

        g.drawCircle(Mouse.getMousePos(), 5, Color.WHITE);
    }

    private static void update() {
        // the method handling the interactivity and screen updates

        boolean mouse = (Mouse.getMouseButton() == 1); // true if left mouse button is pressed

        Point pos = Mouse.getMousePos(); // get mouse position

        if (!mouse) {
            if (keyboard.isKeyDown(Key.LEFT))
                player_rect.x -= 4; // player_rect.x is player x position
            if (keyboard.isKeyDown(Key.RIGHT))
                player_rect.x += 4; // player_rect.y is player y position
            if (keyboard.isKeyDown(Key.UP))
                player_rect.y -= 4;
            if (keyboard.isKeyDown(Key.DOWN))
                player_rect.y += 4;
        } else {
            player_rect.x = (int) player_rect.x - ((player_rect.x - pos.x) / 10); // glide to mouse position
            player_rect.y = (int) player_rect.y - ((player_rect.y - pos.y) / 10);
        }

        window.setTitle("Demo - FPS: " + window.roundDecimalPlaces(window.getFPS(), 2));
        exitButton.update(pos.x, pos.y, mouse);
    }

    public static void main(String[] args) {
        // make a Window object with title "Keyboard Demo"
        // and width and height of 800 and 600 respectively
        window = new Window("Demo", 800, 600, false);
        screen = window.screen; // sets the screen

        // sets the target FPS, 30 or 60 in most cases
        window.setTargetFPS(60);

        // Makes a Keyboard object and sets it to the window keyhandler
        keyboard = new Keyboard();
        window.setKeyboard(keyboard);

        // sets up the mouse
        // NOTE: Mouse methods are static and a Mouse class does not need to be made
        window.setupMouse();

        // sets update and draw to be called by the window on each tick
        window.setOnUpdate(() -> {
            update();
        });
        window.setOnDraw(g -> {
            draw(g);
        });

        // hides the cursor
        window.hideCursor();

        // defines the player as x = 50, y = 50, width = 20, height = 20
        player_rect = new Rect(50, 50, 20, 20);

        // starts the game(loop)
        window.startGameLoop();
    }
}
``` 
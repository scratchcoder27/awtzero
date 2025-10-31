package awtzero; 

import java.awt.*;
import java.awt.event.*;
import java.util.function.Consumer;

/**
 * Represents a window with a drawable screen area, keyboard input handling,
 * and a game loop for updating and rendering.
 * <p>Composits {@link Frame} to create a windowed application.</p>
 */

public class Window {
    /** Whether the program is running */
	public boolean running;
    /** The AWT Frame which is composited */
	public Frame frame;
    /** Reference to this Window instance */
	public Window me;
    /** The drawable {@link Screen} */
	public Screen screen;
    /** The FPS as an integer */
	public int FPS;
    /** The associated {@link Keyboard} */
	public Keyboard keyboard;
	
    private Consumer<RenderInstance> onDraw;
    private Runnable onUpdate;

    private double fps = 0;  // latest calculated FPS
	
    /**
     * Creates a new Window with the specified title, dimensions, and resizability.
     * @param window_name The title of the window
     * @param WIDTH The width of the window
     * @param HEIGHT The height of the window
     * @param resizable Whether the window will resizable or not (as a boolean)
     */
    public Window(String window_name, int WIDTH, int HEIGHT, boolean resizable)
    {
        frame = new Frame(window_name);
        
        screen = new Screen() {
			private static final long serialVersionUID = 6963058267354657407L;

			@Override
            public void paint(Graphics graphics) {
                if (onDraw != null) {
                    RenderInstance g = new RenderInstance(graphics);
                    onDraw.accept(g);
                }
            }

			@Override
		    public void update(Graphics g) {
		        super.update(g);
		    }
        };
        
        frame.add(screen);
        
        frame.setSize(WIDTH, HEIGHT);
        
        
        frame.setVisible(true);
        frame.setResizable(resizable);
        running = true;
        
        keyboard = new Keyboard();
        
        this.setKeyboard(keyboard);       
        frame.requestFocusInWindow(); // After frame is visible
        

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e)
            {
                System.exit(0);
            }
            

        });
    }

    /**
     * @return The current FPS as a double
     */
    public double getFPS() {
        return fps;
    }

    /**
     * @return The current FPS as an integer
     */
    public int getFPSasInt() {
        return (int) fps;
    }
    
    /*
     * A utility function to round a double to n decimal places
     * @param d The double to be rounded
     * @param n The number of decimal places to round to
     * @return The rounded double
     */
    public double roundDecimalPlaces(double d, int n) {
        double temp = Math.pow(10, n);
    	return Math.round(d * temp) / temp;
    }

    /**
     * Set the onDraw function which handles drawing
     * The function can either be a <a href="https://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html">lambda</a> or a method reference
     * @param onDraw the function to handle drawing
     */
    public void setOnDraw(Consumer<RenderInstance> onDraw) {
        this.onDraw = onDraw;
    }
    
    /**
     * Set the onUpdate function which handles updating (of variables, positions, etc.)
     * <p>The function is called every frame before drawing</p>
     * <p>The function can either be a <a href="https://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html">lambda</a> or a method reference </p>
     * @param onUpdate the function to handle updation
     */
    public void setOnUpdate(Runnable onUpdate) {
        this.onUpdate = onUpdate;
    }
    
    /**
     * Set the target FPS for the game loop
     * <p> Actually achieved FPS may vary based on system performance, both higher and lower </p>
     * @param FPS the target
     * @return the target FPS
     */
    public int setTargetFPS(int FPS) {
    	this.FPS = FPS;
    	return FPS;
    }
    
    /**
     * Causes a draw to the screen
     * Triggers a redraw of the screen by calling repaint on the Screen
     */
    public void draw() {
    	screen.repaint(); // repaint will call paint() and trigger onDraw
    }

    /**
     * Causes an update by calling the onUpdate function
     */
    public void update() {
    	if (onUpdate != null) {
    	    onUpdate.run();
    	}
    }
    
    /**
     * Sets the {@link Keyboard} for this Window
     * @param k
     */
    public void setKeyboard(Keyboard k) {
        screen.addKeyListener(k);
        screen.setFocusable(true);
        screen.requestFocusInWindow();
    }
    
    /**
     * Sets up mouse handling for this Window
     */
    public void setupMouse() {
    	Mouse mouse = new Mouse();
    	screen.addMouseListener(mouse);
    	screen.addMouseMotionListener(mouse);
    }
    
    /**
     * Sets the favicon/icon for this Window
     * @param i accepts an {@link Image} object
     * <p>NOTE: ImageWrapper can be used by getting the property {@code imagewrapperInstance.image }</p>
     * @see ImageWrapper
     */
    public void setIcon(Image i) {
    	frame.setIconImage(i);
    }
    
    /**
     * Sets the favicon/icon for this Window
     * @param path the path to the image file
     */
    public void setIcon(String path) {
    	frame.setIconImage(ImageWrapper.loadImage(null, path));
    }

    /**
     * Sets the title of this Window
     * @param title the title as a {@link String}
     */
    public void setTitle(String title) {
    	frame.setTitle(title);
    }
    
    /**
     * Hides the mouse cursor over this Window
     * Internally uses {@link Mouse#hideCursor(Screen)}
     */
    public void hideCursor() {
    	Mouse.hideCursor(screen);
    }
    
    /**
     * Shows the mouse cursor over this Window
     * Internally uses {@link Mouse#resetCursor(Screen)}
     */
    public void showCursor() {
    	Mouse.resetCursor(screen);
    }
    
    /**
     * Starts the game loop
     * <p>The game loop repeatedly calls the onUpdate and onDraw functions at the target FPS</p>
     * <p> The game loop updations and draws run in a separate thread </p>
     */
    public void startGameLoop() {
        new Thread(() -> {

            long lastTime = System.nanoTime();

            while (running) {

                update();
                draw();

                long now = System.nanoTime();
                double instantaneousFps = 1_000_000_000.0 / (now - lastTime);
                lastTime = now;

                this.fps = instantaneousFps;


                try {
                    Thread.sleep(1000 / FPS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
    }
}

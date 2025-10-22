package awtzero; 

import java.awt.*;
import java.awt.event.*;
import java.util.function.Consumer;

public class Window {
	public boolean running;
	public Frame frame;
	public Window me;
	public Screen screen;
	public int FPS;
	public Keyboard keyboard;
	
    private Consumer<RenderInstance> onDraw;
    private Runnable onUpdate;

    private double fps = 0;  // latest calculated FPS
	
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

    
    public double getFPS() {
        return fps;
    }

    public int getFPSasInt() {
        return (int) fps;
    }
    
    public double roundDecimalPlaces(double d, int n) {
        double temp = Math.pow(10, n);
    	return Math.round(d * temp) / temp;
    }

    public void setOnDraw(Consumer<RenderInstance> onDraw) {
        this.onDraw = onDraw;
    }
    
    public void setOnUpdate(Runnable onUpdate) {
        this.onUpdate = onUpdate;
    }
    
    public int setTargetFPS(int FPS) {
    	this.FPS = FPS;
    	return FPS;
    }
    
    public void draw() {
    	screen.repaint(); // repaint will call paint() and trigger onDraw
    }
    
    public void update() {
    	if (onUpdate != null) {
    	    onUpdate.run();
    	}
    }
    
    public void setKeyboard(Keyboard k) {
        screen.addKeyListener(k);
        screen.setFocusable(true);
        screen.requestFocusInWindow();
    }
    
    public void setupMouse() {
    	Mouse mouse = new Mouse();
    	screen.addMouseListener(mouse);
    	screen.addMouseMotionListener(mouse);
    }
    
    public void setIcon(Image i) {
    	frame.setIconImage(i);
    }
    
    public void setIcon(String path) {
    	frame.setIconImage(ImageWrapper.loadImage(null, path));
    }

    public void setTitle(String title) {
    	frame.setTitle(title);
    }
    
    public void hideCursor() {
    	Mouse.hideCursor(screen);
    }
    
    public void showCursor() {
    	Mouse.resetCursor(screen);
    }
    
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

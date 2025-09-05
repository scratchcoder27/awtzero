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
	
    private Consumer<Graphics> onDraw;
    private Runnable onUpdate;
	
    public Window(String window_name, int WIDTH, int HEIGHT, boolean resizable)
    {
        frame = new Frame(window_name);
        
        screen = new Screen() {
			private static final long serialVersionUID = 6963058267354657407L;

			@Override
            public void paint(Graphics g) {
                if (onDraw != null) {
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
    
    public void setOnDraw(Consumer<Graphics> onDraw) {
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
    
    public void hideCursor() {
    	Mouse.hideCursor(screen);
    }
    
    public void showCursor() {
    	Mouse.resetCursor(screen);
    }
    
    public void startGameLoop() {
        new Thread(() -> {
            while (running) {
                update();
                draw();
                try {
                    Thread.sleep(1000 / FPS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
    }
}

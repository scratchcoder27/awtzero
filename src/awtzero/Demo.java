package awtzero;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Demo {
    public static Window window; //Make a static Window
    public static Keyboard keyboard; //a static Keyboard
    public static Screen screen; //define the Screen to be used for drawing
    public static Rect player_rect = new Rect(0, 0, 0, 0); // defines the player rect
    public static Font gamefont = new Font(Font.MONOSPACED, Font.BOLD, 30);
    
    private static void draw(Graphics g) { //the method handling drawing
    	
        screen.clear_screen(g);
        
        screen.draw_text(g, "AWTZero - DEMO", 250, 100, Color.WHITE, gamefont);
    	screen.draw_text(g, "Move with Arrow Keys", 200, 300, Color.WHITE, gamefont);
    	screen.draw_text(g, "Click to move towards mouse", 130, 400, Color.WHITE, gamefont);
    	
    	
        screen.draw_filled_rect(g, player_rect, Color.GREEN);
        
        screen.draw_circle(g, Mouse.getMousePos(), 5, Color.WHITE);
    }
    
    private static void update() {
    	 // the method handling the interactivity and screen updates
    	
    	boolean mouse = (Mouse.getMouseButton() == 1);
    	Point pos = Mouse.getMousePos();
    	
    	if (!mouse) {
    		if (keyboard.isKeyDown(Key.LEFT)) player_rect.x -= 4;  //player_rect.x is player x position
            if (keyboard.isKeyDown(Key.RIGHT)) player_rect.x += 4; //player_rect.y is player y position
            if (keyboard.isKeyDown(Key.UP)) player_rect.y -= 4;
            if (keyboard.isKeyDown(Key.DOWN)) player_rect.y += 4;
    	} else {
    		player_rect.x = (int) player_rect.x - ((player_rect.x - pos.x) / 10);
    		player_rect.y = (int) player_rect.y - ((player_rect.y - pos.y) / 10);
    	}
    }

    public static void main(String[] args) {
    	// make a Window object with title "Keyboard Demo"
    	// and width and height of 800 and 600 respectively
        window = new Window("Demo", 800, 600, false);
        screen = window.screen; // sets the screen
        
        //sets the target FPS, 30 or 60 in most cases
        window.setTargetFPS(60);

        //Makes a Keyboard object and sets it to the window keyhandler
        keyboard = new Keyboard();
        window.setKeyboard(keyboard);
        
        //sets up the mouse
        //NOTE: Mouse methods are static and a Mouse class does not need to be made
        window.setupMouse();

        //sets update and draw to be called by the window on each tick
        window.setOnUpdate(() -> {update();});
        window.setOnDraw(g -> {draw(g);});
        
        //hides the cursor
        window.hideCursor();
        
        //defines the player as x = 50, y = 50, width = 20, height = 20
        player_rect = new Rect(50, 50, 20, 20);

        //starts the game(loop)
        window.startGameLoop();
    }
}
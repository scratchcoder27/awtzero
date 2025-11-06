/**
 * Provides the core classes for the awtzero graphics and game development library.
 * <p>
 * This package contains fundamental building blocks such as {@link awtzero.Rect}
 * for collision and manipulation, {@link awtzero.Vector2} for 2D math,
 * and core I/O components like {@link awtzero.Keyboard} and {@link awtzero.Mouse}.
 * It aims to simplify graphics programming using the Java AWT toolkit.
 *
 * <h2>Key Classes:</h2>
 * <ul>
 * <li>{@link awtzero.Window}: The main application window and rendering surface.</li>
 * <li>{@link awtzero.Screen}: A simplified interface for drawing graphics.</li>
 * <li>{@link awtzero.Keyboard}: Keyboard input handling.</li>
 * <li>{@link awtzero.Mouse}: Mouse input handling.</li>
 * <li>{@link awtzero.Surface}: A drawable surface similar to pygame Surfaces.</li>
 * <li>{@link awtzero.RenderInstance}: Provides drawing capabilities on Surfaces.</li>
 * <li>{@link awtzero.Rect}: A rectangle class for positioning and collision detection.</li>
 * <li>{@link awtzero.Vector2}: A 2D vector class for mathematical operations.</li>
 * <li>{@link awtzero.ImageWrapper}: A utility class for image loading and manipulation.</li>
 * </ul>
 * 
 * Some important notes:
 * <ul>
 * <li> (0, 0) refers to the top left. Therefore, negative y refers towards the top, and negative x refers to the left
 * <li> For operations involving {@link Vector2}, <strong> -90 degrees refers to the top, 0 to the right, 90 degrees to the bottom, and 180 to the left (clockwise)
 * </ul>
 *
 * This library is inspired by pygame and aims to provide a familiar and easy-to-use API
 * for developers looking to create graphics applications in Java using AWT.
 * @since 1.0
 */

module awtzero {
	requires transitive java.desktop;
    exports awtzero;
    exports awtzero.transforms;
    exports awtzero.prefab;
}
package game;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;

/**
 * Handles all the key presses and the key bindings
 * 
 * @author Alex
 * 
 */
public class KeyInput {

	public static boolean moveForwardPressed = false;
	public static boolean moveBackwardPressed = false;
	public static boolean moveLeftPressed = false;
	public static boolean moveRightPressed = false;

	// Key map logic
	private static int moveForwardKey = Input.KEY_W;
	private static int moveBackwardKey = Input.KEY_S;
	private static int moveLeftKey = Input.KEY_A;
	private static int moveRightKey = Input.KEY_D;

	/**
	 * Checks all the keys that are pressed
	 */
	public static void get(GameContainer gc) {
		Input input = gc.getInput();

		moveForwardPressed = input.isKeyDown(moveForwardKey);
		moveBackwardPressed = input.isKeyDown(moveBackwardKey);
		moveLeftPressed = input.isKeyDown(moveLeftKey);
		moveRightPressed = input.isKeyDown(moveRightKey);
	}
}

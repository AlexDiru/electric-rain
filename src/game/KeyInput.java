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

	public boolean moveForwardPressed = false;
	public boolean moveBackwardPressed = false;
	public boolean moveLeftPressed = false;
	public boolean moveRightPressed = false;

	// Key map logic
	private int moveForwardKey = Input.KEY_W;
	private int moveBackwardKey = Input.KEY_S;
	private int moveLeftKey = Input.KEY_A;
	private int moveRightKey = Input.KEY_D;

	/**
	 * Checks all the keys that are pressed
	 */
	public void get(GameContainer gc) {
		Input input = gc.getInput();

		moveForwardPressed = input.isKeyDown(moveForwardKey);
		moveBackwardPressed = input.isKeyDown(moveBackwardKey);
		moveLeftPressed = input.isKeyDown(moveLeftKey);
		moveRightPressed = input.isKeyDown(moveRightKey);
	}
}

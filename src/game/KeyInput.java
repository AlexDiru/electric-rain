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

	public static boolean moveForwardDown = false;
	public static boolean moveBackwardDown = false;
	public static boolean moveLeftDown = false;
	public static boolean moveRightDown = false;
	public static boolean switchCharacterLeftPressed = false;
	public static boolean switchCharacterRightPressed = false;
	public static boolean switchMapLeftPressed = false;
	public static boolean switchMapRightPressed = false;
	public static boolean moveForwardPressed = false;
	public static boolean moveBackwardPressed = false;
	public static boolean confirmPressed = false;
	public static boolean backPressed = false;
	public static boolean moveRightPressed = false;
	public static boolean inventoryPressed = false;
	public static boolean partyPressed = false;

	// Key map logic
	private static int moveForwardKey = Input.KEY_W;
	private static int moveBackwardKey = Input.KEY_S;
	private static int moveLeftKey = Input.KEY_A;
	private static int moveRightKey = Input.KEY_D;
	private static int switchCharacterLeftKey = Input.KEY_COMMA;
	private static int switchCharacterRightKey = Input.KEY_PERIOD;
	private static int switchMapLeftKey = Input.KEY_Z;
	private static int switchMapRightKey = Input.KEY_X;
	private static int confirmKey = Input.KEY_ENTER;
	private static int backKey = Input.KEY_BACK;
	private static int inventoryKey = Input.KEY_TAB;
	private static int partyKey = Input.KEY_P;

	/**
	 * Checks all the keys that are pressed
	 * @param gc The game container
	 */
	public static void get(GameContainer gc) {
		Input input = gc.getInput();

		moveForwardDown = input.isKeyDown(moveForwardKey);
		moveBackwardDown = input.isKeyDown(moveBackwardKey);
		moveLeftDown = input.isKeyDown(moveLeftKey);
		moveRightDown = input.isKeyDown(moveRightKey);
		switchCharacterLeftPressed = input.isKeyPressed(switchCharacterLeftKey);
		switchCharacterRightPressed = input.isKeyPressed(switchCharacterRightKey);
		switchMapLeftPressed = input.isKeyPressed(switchMapLeftKey);
		switchMapRightPressed = input.isKeyPressed(switchMapRightKey);
		moveForwardPressed = input.isKeyPressed(moveForwardKey);
		moveBackwardPressed = input.isKeyPressed(moveBackwardKey);
		confirmPressed = input.isKeyPressed(confirmKey);
		backPressed = input.isKeyPressed(backKey);
		moveRightPressed = input.isKeyPressed(moveRightKey);
		inventoryPressed = input.isKeyPressed(inventoryKey);
		partyPressed = input.isKeyPressed(partyKey);
	}
}

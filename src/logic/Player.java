package logic;

import items.Inventory;

import java.util.ArrayList;
import java.util.Arrays;

import xml.EntityXMLHelper;
import xml.PlayerXMLHelper;
import xml.XMLHelper;
import entity.Entity;
import game.Game;
import game.KeyInput;
import graphics.GlobalFont;

public class Player extends XMLHelper {
	/**
	 * Enumerating direction
	 * 
	 */
	public enum Direction {
		LEFT, RIGHT, UP, DOWN, NONE
	}
	
	/**
	 * The inventory of the player
	 */
	private Inventory inventory = new Inventory("res/battle/selectPlayerCursor.png");

	/**
	 * The direction the player is facing
	 */
	private Direction facingDirection = Direction.DOWN;

	/**
	 * A list of the characters owned by the player
	 */
	private ArrayList<Entity> entities = new ArrayList<Entity>();

	/**
	 * The ids of the entities the player is currently using (in battle)
	 */
	private int[] entityIdsInParty;

	/**
	 * The ids of the entities the player has unlocked
	 */
	private int[] unlockedEntities;

	/**
	 * The current character being used by the player on the overworld
	 */
	protected int currentCharacterIndex;

	/**
	 * The map the entity is on
	 */
	protected Map map;

	/**
	 * The x world position of the player (not tile x)
	 */
	public int tilePositionX = 0;

	/**
	 * The y world position of the player (not tile y)
	 */
	public int tilePositionY = 0;

	/**
	 * The time delay left until the entity next moves
	 */
	private int movementTimer = 0;

	/**
	 * The time delay between the entity moves
	 */
	private final int movementTimerMax = 160;

	/**
	 * The direction the player is moving in (while the movementTimer > 0)
	 */
	public Direction moved = Direction.NONE;

	/**
	 * Constructor for the player
	 * 
	 * @param map
	 *            The map the player is on
	 * @param tileX
	 *            The tile X start position of the player
	 * @param tileY
	 *            The tile Y start position of the player
	 */
	public Player(Map map, int tileX, int tileY) {
		this.map = map;

		try {
			PlayerXMLHelper.readFromXml(this, "res/data/savedata.xml");
		} catch (Exception ex) {
		}

		currentCharacterIndex = 0;

		// Refine entities to what the player has unlocked
		Entity[] unrefinedEntities = EntityXMLHelper.readEntities("res/data/entities.xml");

		for (int i = 0; i < unrefinedEntities.length; i++)
			for (int j = 0; j < getUnlockedEntities().length; j++)
				if (unrefinedEntities[i].getId() == getUnlockedEntities()[j])
					entities.add(unrefinedEntities[i]);

		tilePositionX = tileX;
		tilePositionY = tileY;
	}
	
	public void renderCharacterInfo() {
		//LHS = characters in party
		int i = 0;
		int j = entityIdsInParty.length;
		for (Entity entity : entities) {
			//If in party
			if (Arrays.binarySearch(entityIdsInParty, entity.getId()) >= 0) {
				entity.renderDefaultFrame(Direction.RIGHT, 0, i * Game.tileSize);
				GlobalFont.getMediumFont().drawString(Game.tileSize, i * Game.tileSize + (Game.tileSize - GlobalFont.getMediumFont().getLineHeight())/2, entity.toFullInfoString());
				i++;
			} else {
				entity.renderDefaultFrame(Direction.DOWN, 0, j * Game.tileSize);
				GlobalFont.getMediumFont().drawString(Game.tileSize, j * Game.tileSize + (Game.tileSize - GlobalFont.getMediumFont().getLineHeight())/2, entity.toFullInfoString());
				j++;
			}
		}
		
		//RHS = characters not in party
	}

	/**
	 * Returns the movement timer with a sign dependent on which direction the
	 * entity moved (to scroll the screen in the correct direction)
	 * 
	 * @return The adjusted timer
	 */
	public float getMovementTimerAdjusted() {
		float ratio = ((float) movementTimer / (float) movementTimerMax);
		float mult = ratio * Game.tileSize;
		return mult * (moved.equals(Direction.LEFT) ? -1 : 1) * (moved.equals(Direction.UP) ? -1 : 1);
	}

	/**
	 * Updates the movement timer by subtracting the current delta off the timer
	 */
	protected void updateMovementTimer() {
		if (movementTimer > 0) {
			movementTimer -= Game.currentDelta;
			getEntityById(getEntityIdsInParty()[currentCharacterIndex]).setFrameAccordingToMovementTimer(movementTimer, movementTimerMax);
			if (movementTimer < 0)
				movementTimer = 0;
		}
	}

	/**
	 * Whether the movement timer allows the player to move
	 * 
	 * @return Movement flag
	 */
	protected boolean timerAllowsMovement() {
		return movementTimer <= 0;
	}

	/**
	 * Resets the movement timer to its maximum value
	 */
	protected void resetMovementTimer() {
		movementTimer = movementTimerMax;
	}

	/**
	 * Renders the current character that is being used
	 */
	public void render() {
		getEntityById(getEntityIdsInParty()[currentCharacterIndex]).render(facingDirection, Game.width / 2, Game.height / 2);
	}

	/**
	 * Renders the specified character at the specified position
	 * 
	 * @param characterIndex
	 *            The character to render
	 * @param x
	 *            The x position to render
	 * @param y
	 *            The y position to render
	 */
	public void render(int characterIndex, int x, int y) {
		getEntityById(getEntityIdsInParty()[characterIndex]).render(Direction.RIGHT, x, y, 0);
	}

	/**
	 * Finds the entity with the id number passed
	 * 
	 * @param id
	 * @return
	 */
	private Entity getEntityById(int id) {
		for (Entity entity : entities)
			if (entity.getId() == id)
				return entity;
		return null;
	}

	/**
	 * Changes the current character the player is using
	 */
	public void increaseCharacterIndex() {
		currentCharacterIndex++;
		if (currentCharacterIndex >= getEntitiesInParty().size())
			currentCharacterIndex = 0;
	}

	/**
	 * Changes the current character the player is using
	 */
	public void decreaseCharacterIndex() {
		currentCharacterIndex--;
		if (currentCharacterIndex < 0)
			currentCharacterIndex = getEntitiesInParty().size() - 1;
	}

	/**
	 * Handles the player's movement according to the key input
	 */
	public void playerControlledMovement() {

		updateMovementTimer();

		if (map.isPlayerMovementFrozen())
			return;

		if (timerAllowsMovement()) {
			moved = Direction.NONE;

			// Move one tile
			int destinationX = ((KeyInput.moveRightDown ? 1 : 0) - (KeyInput.moveLeftDown ? 1 : 0));
			int destinationY = ((KeyInput.moveBackwardDown ? 1 : 0) - (KeyInput.moveForwardDown ? 1 : 0));

			if (destinationX != 0 || destinationY != 0) {

				// Move right
				if (destinationX > 0) {
					facingDirection = Direction.RIGHT;
					if (map.checkPortalCollision(tilePositionX + 1, tilePositionY))
						return;

					if (map.canMove(tilePositionX + 1, tilePositionY)) {
						resetMovementTimer();
						moved = Direction.RIGHT;
						tilePositionX++;
						map.checkWildEncounter(tilePositionX, tilePositionY);
					}
				}
				// Move left
				else if (destinationX < 0) {
					facingDirection = Direction.LEFT;
					if (map.checkPortalCollision(tilePositionX - 1, tilePositionY))
						return;

					if (map.canMove(tilePositionX - 1, tilePositionY)) {
						resetMovementTimer();
						moved = Direction.LEFT;
						tilePositionX--;
						map.checkWildEncounter(tilePositionX, tilePositionY);
					}
				}
				// Move down
				else if (destinationY > 0) {
					facingDirection = Direction.DOWN;
					if (map.checkPortalCollision(tilePositionX, tilePositionY + 1))
						return;

					if (map.canMove(tilePositionX, tilePositionY + 1)) {
						resetMovementTimer();
						moved = Direction.DOWN;
						tilePositionY++;
						map.checkWildEncounter(tilePositionX, tilePositionY);
					}
				}
				// Move up
				else if (destinationY < 0) {
					facingDirection = Direction.UP;
					if (map.checkPortalCollision(tilePositionX, tilePositionY - 1))
						return;

					if (map.canMove(tilePositionX, tilePositionY - 1)) {
						resetMovementTimer();
						moved = Direction.UP;
						tilePositionY--;
						map.checkWildEncounter(tilePositionX, tilePositionY);
					}
				}
			}
		}
	}

	public void setMovementTimerMax(int max) {
		// movementTimerMax = max;
	}

	public void showUnlockedCharacters() {
		for (int i = 0; i < entities.size(); i++)
			System.out.println(entities.get(i).getName());
	}

	/* * * * * * * * * * * */
	/* GETTERS AND SETTERS */
	/* * * * * * * * * * * */

	public ArrayList<Entity> getEntitiesInParty() {
		ArrayList<Entity> entityList = new ArrayList<Entity>();
		for (int i = 0; i < entities.size(); i++)
			for (int j = 0; j < getEntityIdsInParty().length; j++)
				if (entities.get(i).getId() == getEntityIdsInParty()[j])
					entityList.add(entities.get(i));
		return entityList;
	}

	public Direction getFacingDirection() {
		return facingDirection;
	}

	public int[] getEntityIdsInParty() {
		return entityIdsInParty;
	}

	public void setEntityIdsInParty(int[] entityIdsInParty) {
		this.entityIdsInParty = entityIdsInParty;
	}

	public int[] getUnlockedEntities() {
		return unlockedEntities;
	}

	public void setUnlockedEntities(int[] unlockedEntities) {
		this.unlockedEntities = unlockedEntities;
	}
	
	public Inventory getInventory() {
		return inventory;
	}
}

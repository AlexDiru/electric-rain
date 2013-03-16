package entity;

import game.Game;
import graphics.DialogBox;

import java.awt.Point;
import java.util.Random;

import logic.Map;
import logic.Player.Direction;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class NPC extends Entity {
	
	/**
	 * Static - Doesn't move at all
	 * Turn - Turns but stands in the same spot
	 * Dynamic - Moves regularly
	 * @author Alex
	 *
	 */
	public enum MovementType {
		STATIC, TURN, DYNAMIC
	}

	public int tilePositionX;
	public int tilePositionY;
	private boolean dialogActive = false;
	private String dialog;
	private Map map;
	private Direction facingDirection = Direction.DOWN;
	private int movementCooldown = 0;
	private int movementCooldownMax = 300;
	private int movementProbability = 1;
	private int localXOffset = 0;
	private int localYOffset = 0;
	private MovementType movementType;

	public NPC(String spriteFile, String name, String dialog, int frameNumber, int x, int y, MovementType movementType, Direction initialDirection) throws SlickException {
		super(-1, spriteFile, Game.tileSize, Game.tileSize, name, null, null, frameNumber);
		this.dialog = dialog;
		tilePositionX = x;
		tilePositionY = y;
		this.movementType = movementType;
		facingDirection = initialDirection;
	}

	public void toggleDialog() {
		dialogActive = !dialogActive;
		if (dialogActive)
			map.freezePlayerMovement();
		else
			map.unfreezePlayerMovement();
	}

	public void facePlayer(Direction playersFacing) {
		if (playersFacing.equals(Direction.UP))
			facingDirection = Direction.DOWN;
		else if (playersFacing.equals(Direction.DOWN))
			facingDirection = Direction.UP;
		if (playersFacing.equals(Direction.LEFT))
			facingDirection = Direction.RIGHT;
		else if (playersFacing.equals(Direction.RIGHT))
			facingDirection = Direction.LEFT;
	}

	public void renderSelf(int mapOffsetX, int mapOffsetY, int npcOffsetX, int npcOffsetY, int playerTileX, int playerTileY) {
		render(facingDirection, -npcOffsetX + localXOffset + Game.width / 2 + (int) ((float) (tilePositionX - playerTileX) * (float) Game.tileSize * Game.scaleFactor) - Math.abs(mapOffsetX),
				-npcOffsetY + localYOffset + Game.height / 2 + (int) ((float) (tilePositionY - playerTileY) * (float) Game.tileSize * Game.scaleFactor) - Math.abs(mapOffsetY));
	}

	public void renderDialog(Graphics g) {
		DialogBox.draw(g, 0, Game.height - 200, 25, 25, dialog);
	}

	public boolean isDialogActive() {
		return dialogActive;
	}

	private void updateMovementTimer() {
		// Reduce the movement timer and change frame

		currentFrame = Math.round((float) movementCooldown / movementCooldownMax) * (frameNumber - 1);
		if (facingDirection.equals(Direction.UP) || facingDirection.equals(Direction.DOWN)) {
			localYOffset = (int) (((int) ((1 - (float) movementCooldown / movementCooldownMax) * (float) Game.tileSize * Game.scaleFactor)));
			localYOffset -= Game.tileSize * Game.scaleFactor;
			if (facingDirection.equals(Direction.UP)) {
				// localYOffset -= Game.tileSize*Game.scaleFactor;
				localYOffset *= -1;
			}

		} else {
			localXOffset = (int) ((int) ((1 - (float) movementCooldown / movementCooldownMax) * Game.tileSize * Game.scaleFactor));
			localXOffset -= Game.tileSize * Game.scaleFactor;
			if (facingDirection.equals(Direction.LEFT)) {
				// localXOffset -= Game.tileSize*Game.scaleFactor;
				localXOffset *= -1;
			}
		}

		movementCooldown -= Game.currentDelta;

	}

	/**
	 * Moves the NPC in a random open direction
	 * 
	 * @param random
	 *            A random generator
	 */
	public void move(Random random) {
		if (movementType.equals(MovementType.STATIC))
			return;
		
		if (movementCooldown > 0) {
			updateMovementTimer();
			return;
		} else
			localXOffset = localYOffset = 0;

		// Active dialog - don't want to walk away from the player so they are
		// unable to toggle the dialog
		if (dialogActive)
			return;

		if (random.nextInt(100) >= movementProbability)
			return;

		//Get a random direction excluding Direction.NONE
		Direction direction = Direction.values()[random.nextInt(Direction.values().length-1)];

		Point point = Map.getNewPosition(tilePositionX, tilePositionY, direction);

		// If dynamic movement and no objects and player in the way then move
		if (movementType.equals(MovementType.DYNAMIC) && map.canMove(point.x, point.y) && !(map.player.tilePositionX == point.x && map.player.tilePositionY == point.y)) {
			tilePositionX = point.x;
			tilePositionY = point.y;
			movementCooldown = movementCooldownMax + Game.currentDelta;
			facingDirection = direction;

			// Update movement timer before we render again
			updateMovementTimer();
		} else if (movementType.equals(MovementType.TURN))
			facingDirection = direction;
	}

	public void setMap(Map map) {
		this.map = map;
	}

	public void setTilePosition(int x, int y) {
		tilePositionX = x;
		tilePositionY = y;
	}
}

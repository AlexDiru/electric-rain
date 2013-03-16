package graphics;

import game.Game;

import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

/**
 * Cursors for selecting characters in battle
 * Cursor are from Final Fantasy IV http://www.videogamesprites.net/FinalFantasy4/Objects/Menu.html
 * @author Alex
 *
 */
public class BattleCursors {
	
	public enum CursorType {
		PLAYERSELECT, ENEMYSELECT, PLAYERSELECTFLIPPED
	}

	/**
	 * Cursor used to pick a character to attack
	 */
	private SpriteSheet playerSelectCursor;
	
	/**
	 * Cursor used to select an enemy target
	 */
	private SpriteSheet enemySelectCursor;
	
	/**
	 * Cursor used to select a character to buff
	 */
	private SpriteSheet playerSelectFlippedCursor;
	
	/**
	 * Loads all the sprite sheets
	 * @param playerSelectFile The file of the player select cursor image
	 * @param enemySelectFile The file of the enemy select cursor image
	 * @param playerSelectFlippedFile The file of the player buff select cursor image
	 */
	public BattleCursors(String playerSelectFile, String enemySelectFile, String playerSelectFlippedFile) {
		try {
			playerSelectCursor = new SpriteSheet(playerSelectFile, Game.tileSize, Game.tileSize, Color.black);
		} catch (SlickException e) {
			e.printStackTrace();
			System.out.println("ERROR - " + playerSelectFile + " doesn't exist");
		}
		
		try {
			enemySelectCursor = new SpriteSheet(enemySelectFile, Game.tileSize, Game.tileSize, Color.black);
		} catch (SlickException e) {
			e.printStackTrace();
			System.out.println("ERROR - " + enemySelectFile + " doesn't exist");
		}
		
		try {
			playerSelectFlippedCursor = new SpriteSheet(playerSelectFlippedFile, Game.tileSize, Game.tileSize, Color.black);
		} catch (SlickException e) {
			e.printStackTrace();
			System.out.println("ERROR - " + playerSelectFlippedFile + " doesn't exist");
		}
	}
	
	/**
	 * Renders the selected cursor
	 * @param type The type of cursor to render
	 * @param x X position
	 * @param y Y position
	 */
	public void render(CursorType type, int x, int y) {
		switch (type) {
		case PLAYERSELECT:
			playerSelectCursor.getSprite(0, 0).draw(x,y);
			break;
		case ENEMYSELECT:
			enemySelectCursor.getSprite(0, 0).draw(x,y);
			break;
		case PLAYERSELECTFLIPPED:
			playerSelectFlippedCursor.getSprite(0, 0).draw(x, y);
			break;
		}
	}
}

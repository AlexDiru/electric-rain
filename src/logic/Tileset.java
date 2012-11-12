package logic;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public class Tileset {

	private Image image;
	private int tileWidth;
	private int tileHeight;
	private int numberOfTilesAcross;
	
	public int getTileWidth() {
		return tileWidth;
	}
	
	public int getTileHeight() {
		return tileHeight;
	}
	
	public void startImageUse() {
		image.startUse();
	}
	
	public void endImageUse() {
		image.endUse();
	}

	/**
	 * Creates the tileset
	 * 
	 * @param directory
	 *            The directory of the tileset
	 * @param tileWidth
	 *            The width of each tile
	 * @param tileHeight
	 *            The height of each tile
	 */
	public Tileset(String directory, int tileWidth, int tileHeight) {
		this.tileHeight = tileHeight;
		this.tileWidth = tileWidth;
		try {
			image = new Image(directory, false, Image.FILTER_NEAREST);
		} catch (SlickException e) {
			System.out.println("Failed to load image: " + directory);
		}
		numberOfTilesAcross = image.getWidth() / tileWidth;
	}

	/**
	 * Gets a tile given its ID
	 * 
	 * @param id
	 *            The ID of the tile
	 * @return The image of the tile
	 */
	public Image getTile(int id) {
		int mod = id % numberOfTilesAcross;
		return getTile(mod, (id - mod) / numberOfTilesAcross);
	}

	/**
	 * Gets a tile given its position on the tileset image
	 * 
	 * @param numberX
	 *            The x position on the image
	 * @param numberY
	 *            The y position on the image
	 * @return
	 */
	public Image getTile(int numberX, int numberY) {
		return image.getSubImage(numberX * tileWidth, numberY * tileHeight, tileWidth, tileHeight);
	}
}

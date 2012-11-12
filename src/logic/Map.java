package logic;

import game.Game;
import game.Light;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class Map {

	public static boolean useLightmapping = false;
	
	public ArrayList<TileLayer> tileLayers = new ArrayList<TileLayer>();
	public TileLayer collisionMap;
	
	private ArrayList<Light> lights = new ArrayList<Light>();
	public Entity player = new Entity(this);

	/**
	 * Reads the map from an array of strings
	 * @param map The string array
	 * @param tileset The tileset to apply
	 */
	public void parseFromStringList(String[] map, Tileset tileset, boolean isCollisionMap) {
		TileLayer layer = new TileLayer();

		for (String row : map) {
			ArrayList<Integer> tileRow = new ArrayList<Integer>();
			String[] data = row.split(",");
			
			for (String datum : data)
				tileRow.add(Integer.parseInt(datum));
			
			layer.tiles.add(tileRow);
		}

		layer.tileHeight = tileset.getTileHeight();
		layer.tileWidth = tileset.getTileWidth();
		
		if (isCollisionMap) {
			collisionMap = layer;
		}
		else {
			layer.tileset = tileset;
			tileLayers.add(layer);
		}
	}
	
	/**
	 * Whether the pixel at world position (x,y) is open or blocked
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean canMove(int x, int y) {
		int tileX = (int)(x/collisionMap.tileWidth);
		int tileY = (int)(y/collisionMap.tileHeight);
		
		return collisionMap.tiles.get(tileY).get(tileX) == 0;
	}
	
	public void lightDemo() {
		lights.add(new Light(500,500,2f, Color.red));
	}
	
	public void renderWithLights(Graphics g) { 
		for (Light light : lights) {
			int xPos = (int)light.x-(int)player.worldPositionX;
			int yPos = (int)light.y-(int)player.worldPositionY;
			light.render(this,g,xPos,yPos, player);
		}
	}

	/**
	 * 
	 * @param centreX
	 *            World X position of the player
	 * @param centreY
	 *            World Y position of the player
	 */
	public void render() {

		int drawX;
		int drawY;

		for (TileLayer layer : tileLayers)
			for (int y = 0; y < getMapHeight() && (drawY = y * layer.tileset.getTileHeight() - (int) player.worldPositionY) <= Game.height; y++)
				for (int x = 0; x < getMapWidth() && (drawX = x * layer.tileset.getTileWidth() - (int) player.worldPositionX) <= Game.width; x++)
					if (useLightmapping)
						layer.tileset.getTile(layer.tiles.get(y).get(x)).drawEmbedded(drawX, drawY, layer.tileset.getTileWidth(), layer.tileset.getTileHeight());
					else
						layer.tileset.getTile(layer.tiles.get(y).get(x)).draw(drawX, drawY);
		
		player.render();
	}

	public int getMapWidth() {
		try {
			return tileLayers.get(0).tiles.get(0).size();
		} catch (Exception ex) {
			return 0;
		}
	}

	public int getMapHeight() {
		try {
			return tileLayers.get(0).tiles.size();
		} catch (Exception ex) {
			return 0;
		}
	}
}

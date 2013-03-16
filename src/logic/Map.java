package logic;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import logic.Player.Direction;
import managers.MapManager;
import managers.NPCManager;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

import statistics.WildEncounterProperties;
import weather.Weather;
import xml.XMLHelper;
import entity.Enemy;
import entity.NPC;
import game.Game;
import game.Game.GameState;

public class Map {

	/**
	 * A map is made up of multiple tiled maps (so transition into smaller local
	 * areas such as the inners of buildings is already pre loaded
	 */
	public TiledMap tiledMap;

	/**
	 * The current map the player is on
	 */
	private int currentMapId = 4;

	/**
	 * The object to represent the player
	 */
	public Player player = new Player(this, 54, 50);

	/**
	 * A random generator
	 */
	private static Random random = new Random();

	/**
	 * Pixel offset of objects, used for screen scrolling
	 */
	private int npcOffsetX = 0;
	private int npcOffsetY = 0;
	private int mapOffsetX = 0;
	private int mapOffsetY = 0;
	private int mapTileStartX = 0;
	private int mapTileStartY = 0;
	private int mapTileWidth = 0;
	private int mapTileHeight = 0;
	
	public boolean renderInventoryFlag = false;
	public boolean renderPartyFlag = false;

	/**
	 * Whether the player can move or not
	 */
	private boolean playerMovementIsFrozen = false;

	/**
	 * Properties of a wild encounter on the map
	 */
	private WildEncounterProperties wildEncounterProperties = new WildEncounterProperties();

	/**
	 * A battle taking place on the map
	 */
	public Battle battle = new Battle(player);

	/**
	 * Manages the weather
	 */
	private Weather weather = new Weather();

	private ArrayList<NPC> npcs = new ArrayList<NPC>();

	public Weather getWeather() {
		return weather;
	}

	public Map() {
		wildEncounterProperties.setMinimumLevelRange(23);
		wildEncounterProperties.setMaximumLevelRange(27);
		wildEncounterProperties.addEnemy(0);
		wildEncounterProperties.addEnemy(1);
		wildEncounterProperties.addEnemy(2);
		/*
		 * wildEncounterProperties.addEnemy("Ghost");
		 * wildEncounterProperties.addEnemy("Skeleton");
		 * wildEncounterProperties.addEnemy("Giant Rat");
		 * wildEncounterProperties.addEnemy("Dolan");
		 * wildEncounterProperties.addEnemy("Wolf");
		 * wildEncounterProperties.addEnemy("Goblin");
		 * wildEncounterProperties.addEnemy("King Goblin");
		 * wildEncounterProperties.addEnemy(392, "Portal Demon");
		 * wildEncounterProperties.addEnemy(392, "Destroyer of Worlds");
		 */

		addTmxFile(MapManager.mapFilenames.get(currentMapId));

		// Load all NPCs
		NPCManager.initialise();

		// Set positions of NPCs from map data - map property spawnNpc
		// Value of spawnNpc is a tuple, npc id | x | y
		// 1,2,4 = spawn npc #1 at 2,4
		// Tuples are split by *
		// 1,2,4*6,5,3
		String property = tiledMap.getMapProperty("spawnNpc", "");
		if (!property.equals("")) {
			String[] tuples = property.split("\\*");
			for (String _tuple : tuples) {
				String[] tuple = _tuple.split("\\,");
				NPC newNpc = NPCManager.getNPC(XMLHelper.parseInt(tuple[0]), this);
				newNpc.setTilePosition(XMLHelper.parseInt(tuple[1]), XMLHelper.parseInt(tuple[2]));
				npcs.add(newNpc);
			}
		}

		// Clear unused NPCs
		NPCManager.clear();
	}

	public void reset() {
		tiledMap = null;
	}

	public Portal getPortal(int mapIndex, int tileX, int tileY) {
		ArrayList<Portal> subset = MapManager.getPortals().get(mapIndex);
		for (Portal portal : subset)
			if (portal.getPosition().x == tileX && portal.getPosition().y == tileY)
				return portal;
		System.out.println("Error - portal not found at ( " + tileX + " , " + tileY + " )");
		return null;
	}

	public void setStartPosition(int entranceId) {
		Portal entrance = MapManager.getPortals().get(currentMapId).get(entranceId);
		player.tilePositionX = entrance.getPosition().x;
		player.tilePositionY = entrance.getPosition().y;
	}

	/**
	 * Loads a map from a .tmx file (output from Tiled)
	 * 
	 * @param directory
	 */
	public void addTmxFile(String directory) {
		try {
			tiledMap = new TiledMap(directory);
		} catch (SlickException e) {
			e.printStackTrace(System.out);
		}

		// Calculate the 'local' entrances
	}

	/**
	 * Whether the pixel at world position (x,y) is open or blocked
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean canMove(int x, int y) {

		try {
			// Get the current map
			TiledMap map = tiledMap;

			int tileX = x;
			int tileY = y;

			// First check map boundaries
			if (tileX < 0 || tileY < 0 || tileX >= this.getMapWidth() || tileY >= this.getMapHeight())
				return false;

			// Check if the player can move
			for (int layer = 0; layer < map.getLayerCount(); layer++)
				if ("true".equals(map.getTileProperty(map.getTileId(tileX, tileY, layer), "blocked", "false")))
					return false;

			// Check for any NPCs
			if (isNPCAtPosition(tileX, tileY))
				return false;

			return true;
		} catch (Exception ex) {
			return true;
		}
	}

	public static Point getNewPosition(int x, int y, Direction direction) {
		if (direction.equals(Direction.LEFT))
			x--;
		else if (direction.equals(Direction.RIGHT))
			x++;
		else if (direction.equals(Direction.UP))
			y--;
		else if (direction.equals(Direction.DOWN))
			y++;

		return new Point(x, y);
	}

	public void updateNPCs() {
		for (NPC npc : npcs)
			npc.move(random);
	}

	/**
	 * When the player presses space they attempt to action with whatever they
	 * are facing
	 */
	public void action() {
		Point point = getNewPosition(player.tilePositionX, player.tilePositionY, player.getFacingDirection());

		NPC npc;
		if ((npc = getNPCAtPosition(point.x, point.y)) != null) {
			npc.toggleDialog();
			npc.facePlayer(player.getFacingDirection());
		}
	}

	private void renderNPCDialogs(Graphics g) {
		for (NPC npc : npcs)
			if (npc.isDialogActive())
				npc.renderDialog(g);
	}

	private NPC getNPCAtPosition(int x, int y) {
		for (NPC npc : npcs)
			if (npc.tilePositionX == x && npc.tilePositionY == y)
				return npc;
		return null;
	}

	private boolean isNPCAtPosition(int x, int y) {
		for (NPC npc : npcs)
			if (npc.tilePositionX == x && npc.tilePositionY == y)
				return true;
		return false;
	}

	/**
	 * Given the x and y coordinates of an entity, calculates if there is a wild
	 * encounter
	 * 
	 * @param x
	 *            X coordinate
	 * @param y
	 *            Y coordinate
	 */
	public void checkWildEncounter(int x, int y) {
		try {
			// Store the tileX and tileY so the calculations only have to been
			// done once
			int tileX = x;
			int tileY = y;

			// Get the current map
			TiledMap map = tiledMap;

			// Iterate the layers of the map
			for (int layer = 0; layer < map.getLayerCount(); layer++) {
				// Get the tild Id
				int tileId = map.getTileId(tileX, tileY, layer);
				// Grab the percentage encounter rate from the tile
				int percentage = Integer.parseInt(map.getTileProperty(tileId, "encounterrate", "0"));
				if (percentage != 0)
					// If the random occurance is within the encounter rate
					if (random.nextInt(100) < percentage) {
						initialiseBattle(wildEncounterProperties.generateEncounter(tileId));
					}
			}
		} catch (Exception ex) {
		}
	}

	/**
	 * Initialises the battle mode
	 * 
	 * @param enemies
	 *            The enemies the player is fighting against
	 */
	private void initialiseBattle(ArrayList<Enemy> enemies) {
		// Change the game state
		Game.gameState = GameState.BATTLE;

		// Get the tile ids the player is standing on to set the right
		// background for the map
		int[] tiles = new int[tiledMap.getLayerCount()];
		for (int i = 0; i < tiledMap.getLayerCount(); i++)
			tiles[i] = tiledMap.getTileId(player.tilePositionX, player.tilePositionY, i);

		battle.initialise(tiles, enemies);
	}

	/**
	 * 
	 * @param g
	 *            The graphics object
	 */
	public void render(Graphics g) {

		// Setup offsets
		calculateOffsetValues();

		// Get the current map
		TiledMap map = tiledMap;

		// Render the first n-1 layers
		if (map.getLayerCount() > 1)
			for (int layer = 0; layer < map.getLayerCount() - 1; layer++)
				renderLayer(map, layer);
		else
			renderLayer(map, 0);

		player.render();

		// Render NPCs
		for (NPC npc : npcs)
			npc.renderSelf(mapOffsetX, mapOffsetY, npcOffsetX, npcOffsetY, player.tilePositionX, player.tilePositionY);

		// Render the last layer
		if (map.getLayerCount() > 1)
			renderLayer(map, map.getLayerCount() - 1);

		weather.render(g);

		// Render NPC dialog
		renderNPCDialogs(g);
		
		// Render inventory
		if (renderInventoryFlag)
			player.getInventory().render();
		
		if (renderPartyFlag)
			player.renderCharacterInfo();
	}

	/**
	 * Renders a layer of the map
	 * 
	 * @param map
	 *            The map to render
	 * @param layer
	 *            The layer to render
	 */
	private void renderLayer(TiledMap map, int layer) {
		// int mapTileWidth = map.getTileWidth();
		// int mapTileHeight = map.getTileHeight();
		renderTiledMap(map, layer, -Math.abs(mapOffsetX), -Math.abs(mapOffsetY), mapTileStartX, mapTileStartY, mapTileWidth, mapTileHeight);

		// map.render(-Math.abs(offsetx), -Math.abs(offsety), tx, ty, gx, gy,
		// layer, true);
	}

	private void calculateOffsetValues() {

		int mapWidth = getMapTileWidth();
		int mapHeight = getMapTileHeight();

		mapTileWidth = (int) Math.ceil((float) Game.width / (mapWidth * Game.scaleFactor));
		mapTileHeight = (int) Math.ceil((float) Game.height / (mapHeight * Game.scaleFactor));
		int px = (int) (player.tilePositionX * Game.tileSize);
		int py = (int) (player.tilePositionY * Game.tileSize);

		mapOffsetX = npcOffsetX = 0;
		mapOffsetY = npcOffsetY = 0;

		if (player.moved.equals(Direction.LEFT) || player.moved.equals(Direction.RIGHT)) {
			mapOffsetX = (int) ((player.getMovementTimerAdjusted()));
		}
		if (player.moved.equals(Direction.UP) || player.moved.equals(Direction.DOWN))
			mapOffsetY = (int) ((player.getMovementTimerAdjusted()));

		mapTileStartX = (px / mapWidth) - mapTileWidth / 2;
		mapTileStartY = (py / mapHeight) - mapTileHeight / 2;

		if (mapOffsetX > 0) {
			mapTileStartX--;
			npcOffsetX -= Game.tileSize;
			mapOffsetX = Game.tileSize - mapOffsetX;
		}
		if (mapOffsetY > 0) {
			mapTileStartY--;
			npcOffsetY -= Game.tileSize;
			mapOffsetY = Game.tileSize - mapOffsetY;
		}
		if (mapOffsetX != 0)
			mapTileWidth++;
		if (mapOffsetY != 0)
			mapTileHeight++;
	}

	private int getMapTileHeight() {
		try {
			return tiledMap.getTileHeight();
		} catch (Exception ex) {
			return 0;
		}
	}

	private int getMapTileWidth() {
		try {
			return tiledMap.getTileWidth();
		} catch (Exception ex) {
			return 0;
		}
	}

	/**
	 * Renders a layer of a tiled map
	 * 
	 * @param map
	 *            The map to render
	 * @param layer
	 *            The specific layer to render
	 * @param xoffset
	 *            The x pixel offset of the map
	 * @param yoffset
	 *            The y pixel offset of the map
	 * @param tx
	 *            The tile x position that is first rendered
	 * @param ty
	 *            The tile y position that is first rendered
	 * @param gx
	 *            The number of tiles rendered width
	 * @param gy
	 *            The number of tiles rendered height
	 */
	private void renderTiledMap(TiledMap map, int layer, int xoffset, int yoffset, int tx, int ty, int gx, int gy) {
		float scaleFactor = Game.scaleFactor;
		int drawY = 0;
		for (int tileY = ty; tileY < gy + ty; tileY++) {
			int drawX = 0;
			for (int tileX = tx; tileX < gx + tx; tileX++) {
				try {
					Image tile = map.getTileImage(tileX, tileY, layer);
					if (tile != null)
						tile.draw(drawX + xoffset * scaleFactor, drawY + yoffset * scaleFactor, scaleFactor);
				} catch (ArrayIndexOutOfBoundsException ex) {
					// Trying to render a tile which is outside the map - ignore
					// and continue
				}
				drawX += scaleFactor * Game.tileSize;
			}
			drawY += scaleFactor * Game.tileSize;
		}
	}

	/**
	 * Given the position of the player checks whether they are standing on a
	 * portal
	 * 
	 * @param x
	 *            The x world position
	 * @param y
	 *            The y world position
	 * @return Whether the player is on a portal
	 */
	public boolean checkPortalCollision(int x, int y) {
		// Get the current map
		TiledMap map = tiledMap;

		int tileX = x;
		;
		int tileY = y;

		if (tileX < 0 || tileY < 0 || tileX >= getMapWidth() || tileY >= getMapHeight())
			return false;

		for (int i = 0; i < map.getLayerCount(); i++) {
			if ("true".equals(map.getTileProperty(map.getTileId(tileX, tileY, i), "portal", "false"))) {
				Portal portal = getPortal(currentMapId, tileX, tileY);

				// Reset map
				if (portal.getTargetMapId() != -1) {
					reset();
					addTmxFile(MapManager.mapFilenames.get(portal.getTargetMapId()));
					currentMapId = portal.getTargetMapId();
				}
				player.tilePositionX = portal.getTargetPosition().x;
				player.tilePositionY = portal.getTargetPosition().y;

				return true;
			}
		}

		return false;
	}

	/**
	 * Gets the number of tiles across the map width
	 * 
	 * @return Tile number
	 */
	public int getMapWidth() {
		try {
			return tiledMap.getWidth();
		} catch (Exception ex) {
			return 0;
		}
	}

	/**
	 * Gets the number of tiles across the map height
	 * 
	 * @return Tile number
	 */
	public int getMapHeight() {
		try {
			return tiledMap.getHeight();
		} catch (Exception ex) {
			return 0;
		}
	}

	public void freezePlayerMovement() {
		playerMovementIsFrozen = true;
	}

	public void unfreezePlayerMovement() {
		playerMovementIsFrozen = false;
	}

	public boolean isPlayerMovementFrozen() {
		return playerMovementIsFrozen;
	}
}

package managers;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

import logic.Portal;

/**
 * Manages the file IO and portals for the maps
 * 
 * @author Alex
 * 
 */
public class MapManager {

	/**
	 * Maps the map id to the portals it contains
	 */
	private static HashMap<Integer, ArrayList<Portal>> portals = new HashMap<Integer, ArrayList<Portal>>();

	/**
	 * Maps the map id to their tile map filename
	 */
	public static HashMap<Integer, String> mapFilenames = new HashMap<Integer, String>();

	/**
	 * Adds a portal to the map
	 * 
	 * @param mapIndex
	 *            The index of the map the portal belongs to
	 * @param targetMapIndex
	 *            The index of the map the portal leads to
	 * @param initialPosition
	 *            The position of the portal the player has to walk into
	 * @param newPosition
	 *            The position in the new map that the portal leads to
	 */
	public static void addPortal(int mapIndex, int targetMapIndex, Point initialPosition, Point newPosition) {
		// New entry in hashmap if needed
		if (!portals.containsKey(mapIndex))
			portals.put(mapIndex, new ArrayList<Portal>());

		// Add the entrance
		portals.get(mapIndex).add(new Portal(initialPosition, targetMapIndex, newPosition));
	}

	/**
	 * Adds a local portal (portal to a position on the same map)
	 * 
	 * @param mapIndex
	 *            The index of the map the portal belongs to and leads to
	 * @param initialPosition
	 *            The position of the portal the player has to walk into
	 * @param newPosition
	 *            The position the player spawns to
	 */
	public static void addPortal(int mapIndex, Point initialPosition, Point newPosition) {
		// New entry in hashmap if needed
		if (!portals.containsKey(mapIndex))
			portals.put(mapIndex, new ArrayList<Portal>());

		// Add the entrance
		portals.get(mapIndex).add(new Portal(initialPosition, newPosition));
	}

	/**
	 * Gets the portals hash map
	 * 
	 * @return Hash map
	 */
	public static HashMap<Integer, ArrayList<Portal>> getPortals() {
		return portals;
	}

	/**
	 * Loads all of the maps into the map manager
	 */
	public static void initialiseFiles() {
		MapManager.mapFilenames.put(0, "res/map/rpgm.tmx");
		MapManager.mapFilenames.put(1, "res/map/rpgm2.tmx");
		MapManager.mapFilenames.put(2, "res/map/rpgm3.tmx");
		MapManager.mapFilenames.put(3, "res/map/rpgm4.tmx");
		MapManager.mapFilenames.put(4, "res/map/rpgm5.tmx");
	}

	/**
	 * Sets of the portals of all the maps
	 */
	public static void initialisePortals() {
		// Pallet Town -> Oak's Lab
		MapManager.addPortal(0, 1, new Point(17, 15), new Point(4, 6));
		MapManager.addPortal(0, 1, new Point(18, 15), new Point(5, 6));

		// Oak's Lab -> Pallet Town
		MapManager.addPortal(1, 0, new Point(4, 7), new Point(17, 16));
		MapManager.addPortal(1, 0, new Point(5, 7), new Point(18, 16));

		// Pallet Town -> Ash's House
		MapManager.addPortal(0, 2, new Point(6, 9), new Point(5, 6));

		// Ash's House -> Pallet Town
		MapManager.addPortal(2, 0, new Point(5, 7), new Point(6, 10));

		// Ash's House -> Upstairs
		MapManager.addPortal(2, 4, new Point(9, 0), new Point(8, 0));

		// Upstairs -> Ash's House
		MapManager.addPortal(4, 2, new Point(9, 0), new Point(8, 0));

		// Pallet Town -> Gary's House
		MapManager.addPortal(0, 3, new Point(19, 9), new Point(5, 6));

		// Gary's House -> Pallet Town
		MapManager.addPortal(3, 0, new Point(5, 7), new Point(19, 10));
	}
}

package managers;

import java.util.HashMap;

/**
 * Manages the file IO for the battles
 * @author Alex
 *
 */
public class BattleManager {

	/**
	 * Maps a tile id to the filepath of the image representing it for a battle
	 */
	private static HashMap<Integer, String> battleBackground = new HashMap<Integer, String>();
	
	public static void addBackground(int tileId, String imagePath)
	{
		if (!battleBackground.containsKey(tileId))
			battleBackground.put(tileId, imagePath);
	}
	
	public static String getBackgroundFilePath(int tile) {
		try {
			return battleBackground.get(tile);
		} catch (Exception ex) {
			return null;
		}
	}
	
	public static void initialise() {
		BattleManager.addBackground(483, "res/battle/grassBackground.png");
		BattleManager.addBackground(392, "res/battle/portalBackground.png");
	}
}

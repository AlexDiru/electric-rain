package helpers;

import game.Game;


public class ScaleHelper {

	/**
	 * Determines the amount an image should be scaled to fit the screen
	 * @param imageWidth
	 * @param imageHeight
	 * @param screenWidth
	 * @param screenHeight
	 * @return
	 */
	public static float determineScaleFactorOfBattleBackground(int imageWidth, int imageHeight, int screenWidth, int screenHeight) {
		float widthScale = (float)imageWidth/screenWidth;
		float heightScale = (float)imageHeight/screenHeight;
		return 1 / Math.min(widthScale, heightScale);
	}

	public static float determineScaleFactorOfTile() {
		float widthRatio = ((float)Game.tileSize/Game.width) * Game.desiredTilesAcrossScreenWidth;
		float heightRatio = ((float)Game.tileSize/Game.height) * Game.desiredTilesAcrossScreenHeight;
		return 1/Math.max(widthRatio, heightRatio);
		
	}
}

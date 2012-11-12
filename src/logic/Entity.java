package logic;

import game.Game;
import game.KeyInput;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;

public class Entity {

	public SpriteSheet spriteSheet;
	private Map map;
	
	public float worldPositionX = 0;
	public float worldPositionY = 0;
	public float speedFactor = 0.3f;
	
	public Rectangle boundingBox;
	
	public Entity(Map map) {
		this.map = map;
		try {
			spriteSheet = new SpriteSheet("C:/Users/Alex/Desktop/GOOSE.png", 42,70,Color.black);
		} catch (SlickException e) {
			e.printStackTrace();
		}
		
		boundingBox = new Rectangle(0,0,spriteSheet.getWidth(),spriteSheet.getHeight());
	}
	
	//public boolean isWithinBoundingBox(int x, int y) {
	//	return (x <= boundingBox.getMaxX() + worldPositionX && x >= worldPositionX &&
	//		    y <= boundingBox.getMaxY() + worldPositionY && y >= worldPositionY);
	//}
	
	private List<Point> getEdgesOfBoundingBox() {
		ArrayList<Point> edgePoints = new ArrayList<Point>();

		//Top and bottom
		for (int x = (int) worldPositionX; x < boundingBox.getWidth() + worldPositionX; x++) {
			edgePoints.add(new Point(x, (int) worldPositionY));
			edgePoints.add(new Point(x, (int) (worldPositionY + boundingBox.getHeight())));
		}
		
		//Left and right
		for (int y = (int) worldPositionY + 1; y < boundingBox.getHeight() + worldPositionY - 1; y++) {
			edgePoints.add(new Point((int) worldPositionX, y));
			edgePoints.add(new Point((int) (worldPositionX + boundingBox.getWidth()), y)));
		}
		
		return edgePoints;
	}
	
	public void render() {
		if (Map.useLightmapping) {
		}
		else {
			spriteSheet.getSprite(0, 0).draw(Game.width/2, Game.height/2);
		}
	}
	
	public void playerControlledMovement() {
		int destinationX = (int) (map.player.speedFactor * Game.currentDelta * ((KeyInput.moveRightPressed ? 1 : 0) - (KeyInput.moveLeftPressed ? 1 : 0)));
		int destinationY = (int) (map.player.speedFactor * Game.currentDelta * ((KeyInput.moveBackwardPressed ? 1 : 0) - (KeyInput.moveForwardPressed ? 1 : 0)));
		
		/*boolean moveNegativeX = destinationX < 0;
		boolean moveNegativeY = destinationY < 0;
		
		//If move diagonally
		if (destinationX != 0 && destinationY != 0) {
			if (move
		}*/
		
		//Have to test all the pixels in between current position and destination
		List<Point> points = getEdgesOfBoundingBox();
		for (Point point : points)
			if (!
		
	}

}

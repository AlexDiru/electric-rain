package graphics;

import game.Game;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;

import weather.SolidFill;

public abstract class DialogBox {
	
	private static SpriteSheet menuCursor;
	
	public static void initialise() {
		try {
			menuCursor = new SpriteSheet("res/battle/selectPlayerCursor.png", Game.tileSize, Game.tileSize, Color.black);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	public static void draw(Graphics g,int x, int y, int xPadding, int yPadding, String text) {
		g.fill(new Rectangle(x,y, Game.width, Game.height - y), new SolidFill(new Color(70,40,40,140)));
		GlobalFont.getMediumFont().drawString(x + xPadding,y + yPadding,text);
	}
	
	public static void drawMenuCursor(int x, int y) {
		menuCursor.getSprite(0,0).draw(x,y);
	}
}

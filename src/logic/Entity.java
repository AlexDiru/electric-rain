package logic;

import java.awt.Point;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Entity {

	public Image sprite;
	
	public Point worldPosition = new Point();
	
	public Entity() {
		try {
			sprite = new Image("C:/Users/Alex/Desktop/GOOSE.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

}

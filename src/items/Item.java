package items;

import game.Game;

import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public class Item implements Comparable<Item>{
	private int id;
	private String name;
	private String description;
	private SpriteSheet image;
	
	public Item(int id, String name, String description, String imageFile) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		try {
			image = new SpriteSheet(imageFile, Game.tileSize, Game.tileSize, Color.black);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	public void render(int x, int y) {
		image.getSprite(0, 0).draw(x, y);
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}

	@Override
	public int compareTo(Item arg0) {
		return name.compareTo(arg0.getName());
	}
}

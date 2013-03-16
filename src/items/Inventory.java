package items;

import game.Game;
import generics.MutablePair;
import graphics.GlobalFont;

import java.util.Comparator;
import java.util.TreeSet;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public class Inventory {
	/**
	 * Maps an item to its quantity
	 */
	private TreeSet<MutablePair<Item, Integer>> contents = new TreeSet<MutablePair<Item, Integer>>(new Comparator<MutablePair<Item, Integer>>() {
		@Override
		public int compare(MutablePair<Item, Integer> o1, MutablePair<Item, Integer> o2) {
			return o1.getLeft().compareTo(o2.getLeft());
		}
	});
	
	private SpriteSheet cursor;
	private int selectedItemIndex = 0;
	
	public Inventory(String cursorFile) {
		try {
			cursor = new SpriteSheet(cursorFile, Game.tileSize, Game.tileSize, 32);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		for (MutablePair<Item, Integer> content : contents) {
			stringBuilder.append(content.getLeft().getName() + " x " + content.getRight());
		}
		return stringBuilder.toString();
	}
	
	public void add(Item item, int quantity) {
		for (MutablePair<Item, Integer> content : contents)
			if (content.getLeft() == item) {
				//The item is already in the inventory so increase the quantity
				content.setRight(content.getRight() + quantity);
				return;
			}
		
		//The item isn't in the inventory so add in
		contents.add(new MutablePair<Item,Integer>(item, quantity));
	}
	
	/**
	 * Removes an item from the inventory - provides no error checking
	 * @param item
	 * @param quantity
	 */
	public void take(Item item, int quantity) {
		int currentIndex = 0;
		for (MutablePair<Item, Integer> content : contents) {
			if (content.getLeft() == item) {
				content.setRight(content.getRight() - quantity);
				//Remove the item's existance from the inventory
				if (content.getRight() <= 0)
					contents.remove(currentIndex);
				return;
			}
			currentIndex++;
		}
	}
	
	/**
	 * Gets the quantity of an item in the inventory
	 * @param item
	 * @return
	 */
	public int getQuantity(Item item) {
		for (MutablePair<Item, Integer> content : contents)
			if (content.getLeft() == item)
				return content.getRight();
		return 0;
	}
	
	/**
	 * Renders the inventory
	 */
	public void render() {
		int y = 0;
		for (MutablePair<Item, Integer> content : contents) {
			if (y == selectedItemIndex)
				cursor.draw(0, y*Game.tileSize);
			content.getLeft().render(Game.tileSize, y*Game.tileSize);
			GlobalFont.getMediumFont().drawString(2*Game.tileSize + 4, y*Game.tileSize + ((Game.tileSize-GlobalFont.getMediumFont().getLineHeight())/2), content.getLeft().getName() + " x " + content.getRight());
			y++;
		}
	}

	public String[] getAsMenu() {
		String[] menu = new String[contents.size()];
		int i = 0;
		for (MutablePair<Item, Integer> content : contents) 
			menu[i++] = content.getLeft().getName() + " x " + content.getRight();
		
		return menu;
	}
}

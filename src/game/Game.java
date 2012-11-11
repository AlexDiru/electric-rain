package game;

import logic.Entity;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class Game extends BasicGame {

	private KeyInput keyInput = new KeyInput();
	private Entity entity;

	public Game() {
		super("electric-rain");
	}

	public void init(GameContainer gc) throws SlickException {
		entity = new Entity();
	}

	public void update(GameContainer gc, int delta) throws SlickException {
		keyInput.get(gc);
		entity.worldPosition.x += (keyInput.moveRightPressed ? 1 : 0) - (keyInput.moveLeftPressed ? 1 : 0);
		entity.worldPosition.y += (keyInput.moveBackwardPressed ? 1 : 0) - (keyInput.moveForwardPressed ? 1 : 0);
	}

	public void render(GameContainer gc, Graphics g) throws SlickException {
		entity.sprite.draw(entity.worldPosition.x, entity.worldPosition.y);
	}

	public static void main(String[] args) throws SlickException {
		AppGameContainer app = new AppGameContainer(new Game());

		app.setDisplayMode(800, 600, false);
		app.start();
	}
}
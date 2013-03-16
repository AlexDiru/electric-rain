package weather;

import game.Game;

import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.geom.Line;

public class RainDrop {
	private Line line;
	private int x1, y1, x2, y2;

	public RainDrop(int x1, int y1, int x2, int y2) {
		line = new Line(x1, y1, x2, y2);
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
	}

	/**
	 * Updates the raindrop's position
	 * 
	 * @param horizontalSpeed
	 *            The speed to move along the x axis
	 * @param verticalSpeed
	 *            The speed to move along the y axis
	 * @return Whether the raindrop needs to be repositioned above the screen
	 */
	public boolean update(int horizontalSpeed, int verticalSpeed) {
		x1 += horizontalSpeed;
		x2 += horizontalSpeed;
		y1 += verticalSpeed;
		y2 += verticalSpeed;

		// Figure out wheter the raindrop is out of the screen
		if (horizontalSpeed > 0) {
			if (x1 > Game.width)
				return true;
		} else {
			if (x2 < 0)
				return true;
		}

		if (verticalSpeed > 0) {
			if (y2 > Game.height)
				return true;
		} else {
			if (y1 < 0)
				return true;
		}

		return false;
	}

	public void setPosition(int x, int y) {
		int xGap = x1 - x;
		x1 = x;
		x2 -= xGap;
		int yGap = y1 - y;
		y1 = y;
		y2 -= yGap;
	}

	/**
	 * Repositions the raindrop above the screen
	 * 
	 * @param random
	 *            The random generator
	 * @param widthComponent
	 *            The width component
	 */
	public void resetPosition(Random random, int widthComponent) {
		// Only accounts for rain traversing diagonally from top left to bottom
		// right
		int x = random.nextInt(Game.width + widthComponent) - widthComponent;
		int y = random.nextInt(Game.height);
		setPosition(x, -y);
	}

	/**
	 * Renders the raindrop onto the screen
	 * @param g The graphics object
	 * @param color1
	 * @param color2
	 */
	public void render(Graphics g, Color color1, Color color2) {
		line.set(x1, y1, x2, y2);
		g.draw(line, new GradientFill(x1, y1, color1, x2, y2, color2, false));
	}
}

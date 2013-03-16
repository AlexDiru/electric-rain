package weather;

import game.Game;

import java.util.ArrayList;
import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.geom.Rectangle;

public class Rain {
	private ArrayList<RainDrop> raindrops = new ArrayList<RainDrop>();
	private Random random = new Random();
	private Color rainTop;
	private Color rainBottom;
	private Color screenDarkness;
	private int horizontalSpeed;
	private int verticalSpeed;

	/**
	 * The distance which the rain drop moves across the x axis as it descends from the top of the screen to the bottom
	 */
	private int widthComponent;

	/**
	 * Sets up the rain
	 * 
	 * @param density
	 *            Proportional to how many rain drops there are
	 * @param horizontalSize
	 *            The width of the rain
	 * @param verticalSize
	 *            The height of the rain
	 * @param horizontalSizeVariation
	 *            By how many pixels the width of rain varies
	 * @param verticalSizeVariation
	 *            By how many pixels the height of the rain varies
	 * @param horizontalPositionVariation
	 *            The maximum x axis offset of the raindrops
	 * @param verticalPositionVariation
	 *            The maximum y axis offset of the raindrops
	 * @param probability
	 *            The probability a raindrop will be created in a square of the
	 *            map
	 * @param horizontalSpeed
	 *            The speed at which the rain traverses the x axis
	 * @param verticalSpeed
	 *            The speed at which the rain traverses the y axis
	 * @param screenDarkness
	 *            The amount the screen is tinted 0 = full black, 255 = no tint
	 * @param rainTop
	 *            The color of the top of the rain (gradient fill)
	 * @param rainBottom
	 *            The color of the bottom of the rain (gradient fill)
	 */
	public Rain(int density, int horizontalSize, int verticalSize, int horizontalSizeVariation, int verticalSizeVariation, int horizontalPositionVariation, int verticalPositionVariation,
			int probability, int horizontalSpeed, int verticalSpeed, int screenDarkness, Color rainTop, Color rainBottom) {
		this.horizontalSpeed = horizontalSpeed;
		this.verticalSpeed = verticalSpeed;
		this.screenDarkness = new Color(0, 0, 0, screenDarkness);
		this.rainBottom = rainBottom;
		this.rainTop = rainTop;

		// Ratio of screen
		float ratioWidth = (float) Game.width / (Game.height + Game.width);
		float ratioHeight = (float) Game.height / (Game.height + Game.width);

		// Need ratio x more rain
		int nRows = (int) ((float) ratioHeight * Math.sqrt((double) density));
		int nCols = (int) ((float) ratioWidth * Math.sqrt((double) density));

		// Calculate the x movement of a raindrop each screen pass
		widthComponent = (int) ((float) (Game.height) / Math.tan(Math.atan((float) verticalSpeed / horizontalSpeed)));

		for (int i = 0; i < nRows + 2; i++)
			for (int j = 0; j < nCols + 2; j++)
				if (random.nextInt(100) < probability) {
					int hOffset = random.nextInt(horizontalPositionVariation) + random.nextInt(Game.width) /*
																											 * cover
																											 * the
																											 * bottom
																											 * left
																											 */- widthComponent;
					int vOffset = random.nextInt(verticalPositionVariation) + random.nextInt(Game.height);
					raindrops.add(new RainDrop(hOffset + j * (Game.height / nRows), vOffset + i * (Game.width / nCols), hOffset + j * (Game.height / nRows) + horizontalSize
							+ random.nextInt(horizontalSizeVariation), vOffset + i * (Game.width / nCols) + verticalSize + random.nextInt(verticalSizeVariation)));
				}
	}

	/**
	 * Tints the screen with a transparent black
	 * @param g The graphics object
	 */
	private void darkenScreen(Graphics g) {
		if (screenDarkness.getAlpha() < 255)
			g.fill(new Rectangle(0, 0, Game.width, Game.height), new GradientFill(0, 0, screenDarkness, Game.width, Game.height, screenDarkness));
	}

	/**
	 * Updates the position of all the raindrops
	 */
	public void update() {
		for (RainDrop raindrop : raindrops)
			if (raindrop.update(horizontalSpeed, verticalSpeed))
				raindrop.resetPosition(random, widthComponent);
	}

	/**
	 * Renders the rain to the screen
	 * @param g The graphics object
	 */
	public void render(Graphics g) {
		darkenScreen(g);
		for (RainDrop raindrop : raindrops)
			raindrop.render(g, rainTop, rainBottom);

	}
}

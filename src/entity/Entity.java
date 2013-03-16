package entity;

import game.Game;
import graphics.Bar;

import java.util.ArrayList;
import java.util.Arrays;

import logic.Player.Direction;
import managers.AttackManager;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import statistics.Attack.AttackType;
import statistics.EntityStatistics;

public class Entity extends SpriteSheet implements IEntity {

	private int id;
	private String name;
	private EntityStatistics entityStatistics = new EntityStatistics();
	private int[] availableAttacks = new int[] { 1, 2 };
	protected int frameNumber;
	protected int currentFrame;
	private Bar healthBar = new Bar(128,12, new Color(52,178,62), Color.black);
	private Bar spiritBar = new Bar(128,12, new Color(48,149,182), Color.black);

	/**
	 * 
	 * @param spriteFile
	 *            The sprite of the entity
	 * @param width
	 *            The width of the sprite
	 * @param height
	 *            The height of sprite
	 * @param color
	 * @param name
	 *            The name of entity
	 * @param frameNumber
	 *            The number of frames the entity has
	 * @throws SlickException
	 */
	public Entity(String spriteFile, int width, int height, Color color, String name, int frameNumber) throws SlickException {
		super(spriteFile, width, height, color);
		this.name = name;
		this.frameNumber = frameNumber;
	}

	/**
	 * 
	 * @param id 
	 * @param spriteFile 
	 * @param image
	 * @param tw
	 * @param th
	 * @param name
	 * @param entityStatistics
	 * @param availableAttacks
	 * @param frameNumber 
	 * @throws SlickException
	 */
	public Entity(int id, String spriteFile, int tw, int th, String name, EntityStatistics entityStatistics, int[] availableAttacks, int frameNumber) throws SlickException {
		this(spriteFile, tw, th, Color.black, name, frameNumber);
		this.id = id;
		this.entityStatistics = entityStatistics;
		this.availableAttacks = availableAttacks;
	}

	public void updateCurrentFrame() {
		currentFrame++;
		if (currentFrame >= frameNumber)
			currentFrame = 0;
	}

	public String getName() {
		return name;
	}

	public EntityStatistics getStats() {
		return entityStatistics;
	}

	public boolean isDead() {
		return entityStatistics.getCurrentHealth() <= 0;
	}

	public int[] getAvailableAttacks() {
		return availableAttacks;
	}

	public void render(Direction facing, int x, int y) {
		render(facing, x, y, currentFrame);
	}
	
	public void renderDefaultFrame(Direction facing, int x, int y) {
		render(facing, x, y, 0);
	}

	/**
	 * Lets the ratio of movement timer pick the frame to render
	 * 
	 * @param movementTimer
	 * @param movementTimerMax
	 */
	public void setFrameAccordingToMovementTimer(int movementTimer, int movementTimerMax) {
		double ratio = (double) movementTimer / movementTimerMax;
		currentFrame = (int) Math.round(ratio * (frameNumber - 1));
	}

	public void render(Direction facing, int x, int y, int frame) {
		try {
			switch (facing) {
			case DOWN:
				getSprite(frame, 0).draw(x, y, Game.scaleFactor);
				break;
			case LEFT:
				getSprite(frame, 1).draw(x, y, Game.scaleFactor);
				break;
			case RIGHT:
				getSprite(frame, 2).draw(x, y, Game.scaleFactor);
				break;
			case UP:
				getSprite(frame, 3).draw(x, y, Game.scaleFactor);
				break;
			default:
				break;
			}
		} catch (Exception ex) {
			getSprite(0, 0).draw(x, y, Game.scaleFactor);
		}
	}

	/**
	 * Gets a list of the attack names the entity has with a specific attack
	 * type
	 * 
	 * @return The menu
	 */
	public String[] getSpecificAttackTypeAttacksAsMenu(AttackType filter) {
		ArrayList<String> attackList = new ArrayList<String>();
		for (int i = 0; i < availableAttacks.length; i++)
			if (AttackManager.getAttack(availableAttacks[i]).getAttackType().equals(filter))
				attackList.add(AttackManager.getAttack(availableAttacks[i]).getName());
		return attackList.toArray(new String[attackList.size()]);
	}

	public String toBattleDisplayString() {
		return name + " L:" + getStats().getLevel() + " " + getStats().getCurrentHealth() + "/" + getStats().getMaxHealth();
	}
	
	public String toFullInfoString() {
		return name + " L:" + getStats().getLevel() + " HP: " + getStats().getCurrentHealth() + "/" + getStats().getMaxHealth() + " SP: " + getStats().getCurrentSpirit() + "/" + getStats().getMaxSpirit() + " EXP: " + getStats().getExperience() + "/" + getStats().getTotalExperienceForNextLevel();
	}

	public String[] getAllAttackTypesAsMenu() {
		boolean[] attackTypeUsed = new boolean[AttackType.values().length];
		Arrays.fill(attackTypeUsed, false);

		for (int i = 0; i < availableAttacks.length; i++)
			attackTypeUsed[AttackManager.getAttack(availableAttacks[i]).getAttackType().ordinal()] = true;

		ArrayList<String> menu = new ArrayList<String>();
		for (int i = 0; i < attackTypeUsed.length; i++)
			if (attackTypeUsed[i])
				menu.add(AttackType.values()[i].toString());

		// If there is more than one type add an "All option"
		if (menu.size() > 1)
			menu.add("All");

		return menu.toArray(new String[menu.size()]);
	}
	
	public boolean isEnemy() {
		return false;
	}

	public int getId() {
		return id;
	}

	public int getCurrentFrame() {
		return currentFrame;
	}

	public void updateAndRenderHealthBar(Graphics g, int x, int y) {
		healthBar.updateAndRender(getStats().getCurrentHealth(), getStats().getMaxHealth(),g,x,y); 
	}
	
	public Bar getHealthBar() {
		return healthBar;
	}

	public void updateAndRenderSpiritBar(Graphics g, int x, int y) {
		spiritBar.updateAndRender(getStats().getCurrentSpirit(), getStats().getMaxSpirit(), g, x, y);
	}

	public Bar getSpiritBar() {
		return spiritBar;
	}
}

package entity;

import graphics.Bar;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import statistics.Attack.AttackType;
import statistics.EntityStatistics;

public class Enemy implements IEntity {

	private static int idCounter = 1000;

	public static int getIdCounter() {
		return idCounter;
	}

	private int id;
	private String name;
	private String imageFilepath;
	private EntityStatistics stats = new EntityStatistics();
	private SpriteSheet image = null;
	private Bar healthBar = new Bar(64, 12, Color.green, Color.black);
	private Bar spiritBar = new Bar(64, 12, Color.cyan, Color.black);
	
	/**
	 * 
	 * @param name
	 *            Name of the enemy
	 * @param filepath
	 *            Directory of the image
	 */
	public Enemy(String name, String filepath) {
		this.name = name;
		this.imageFilepath = filepath;
		this.id = idCounter++;
	}

	public Enemy clone() {
		return new Enemy(this.name, this.imageFilepath);
	}

	public String getName() {
		return name;
	}

	public void loadImage() {
		try {
			image = new SpriteSheet(imageFilepath, 200, 200, Color.black);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	public void render(int x, int y) {
		image.draw(x, y);
	}

	public boolean isEnemy() {
		return true;
	}

	public void setStats(EntityStatistics stats) {
		this.stats = stats;
	}

	public EntityStatistics getStats() {
		return stats;
	}

	public boolean isDead() {
		return stats.getCurrentHealth() <= 0;
	}

	public SpriteSheet getImage() {
		return image;
	}

	public int[] getAvailableAttacks() {
		return null;
	}

	public String toBattleDisplayString() {
		return name + " L:" + getStats().getLevel() + " " + getStats().getCurrentHealth() + "/" + getStats().getMaxHealth();
	}

	@Override
	public String[] getAllAttackTypesAsMenu() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getSpecificAttackTypeAttacksAsMenu(AttackType filter) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * This function is used when the player is fighting multiple enemies of the
	 * same type i.e. if there are 2 Rhydons, they will be named Rhydon I and
	 * Rhydon II
	 * 
	 * @param index
	 *            The number to convert to roman numerals and add onto the end
	 *            of the name
	 */
	public void setOccurance(int index) {
		name += " ";
		switch (index) {
		case 1:
			name += "I";
			break;
		case 2:
			name += "II";
			break;
		case 3:
			name += "III";
			break;
		case 4:
			name += "IV";
			break;
		default:
			name += "UNKNOWN_INDEX";
			break;
		}
	}

	public int getId() {
		return id;
	}

	public void updateAndRenderHealthBar(Graphics g, int x, int y) {
		healthBar.updateAndRender(getStats().getCurrentHealth(), getStats().getMaxHealth(), g, x, y);
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

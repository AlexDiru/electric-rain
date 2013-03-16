 package entity;

import graphics.Bar;

import org.newdawn.slick.Graphics;

import statistics.Attack.AttackType;
import statistics.EntityStatistics;

public interface IEntity {
	public String getName();
	public EntityStatistics getStats();
	public boolean isDead();
	/**
	 * Gets the ID numbers of every available attack
	 * @return An array of the ID numbers
	 */
	public int[] getAvailableAttacks();
	
	/**
	 * Gets the text to provide additional information in battle
	 * @return
	 */
	public String toBattleDisplayString();
	
	/**
	 * Reads all the attack types and narrows them down
	 * If the attack types are just Physical and Spiritual then the menu will look like:
	 * 	Physical
	 *  Spiritual
	 * @return
	 */
	public String[] getAllAttackTypesAsMenu();
	
	/**
	 * Reads all the attacks of an IEntity and filters them by attack type
	 * @param filter The attacktype to filter to
	 * @return
	 */
	public String[] getSpecificAttackTypeAttacksAsMenu(AttackType filter);
	
	public int getId();
	
	public boolean isEnemy();
	
	public void updateAndRenderHealthBar(Graphics g, int x, int y);
	
	public Bar getHealthBar();
	
	public void updateAndRenderSpiritBar(Graphics g, int x, int y);
	
	public Bar getSpiritBar();
}

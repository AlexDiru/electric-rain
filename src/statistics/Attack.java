package statistics;

public class Attack {
	/**
	 * The type of attack being used
	 * @author Alex
	 *
	 */
	public enum AttackType {
		Physical, Weapon, Spiritual
	}
	
	/**
	 * Regular - Attack is used on the enemy
	 * Buff - Attack is used on friendly characters
	 * @author Alex
	 *
	 */
	public enum AttackUsage {
		Regular, Buff
	}
	
	/**
	 * Singular - Attack is used on a single character
	 * Group - Attack is used on a whole group
	 * @author Alex
	 *
	 */
	public enum AttackTarget {
		Singular, Group
	}
	
	private String name;
	private int baseDamage;
	private int baseHealing;
	private AttackType attackType;
	private AttackTarget attackTarget;
	private AttackUsage attackUsage;
	
	public Attack(String name, int baseDamage, int baseHealing, AttackType attackType, AttackTarget attackTarget, AttackUsage attackUsage) {
		super();
		this.name = name;
		this.baseDamage = baseDamage;
		this.attackType = attackType;
		this.attackTarget = attackTarget;
		this.attackUsage = attackUsage;
		this.baseHealing = baseHealing;
	}

	public String getName() {
		return name;
	}
	
	public int getBaseDamage() {
		return baseDamage;
	}
	
	public int getBaseHealing() {
		return baseHealing;
	}
	
	public AttackType getAttackType() {
		return attackType;
	}

	public AttackTarget getAttackTarget() {
		return attackTarget;
	}

	public AttackUsage getAttackUsage() {
		return attackUsage;
	}
}

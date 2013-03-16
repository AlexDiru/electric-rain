package helpers;

import java.util.Random;

import statistics.Attack.AttackType;
import statistics.EntityStatistics;

public abstract class FormulaeHelper {
	
	private static Random random = new Random();

	public static int calculateDamage(EntityStatistics attacker, EntityStatistics target, AttackType attackType, int attackBaseDamage) {
		if (attackType.equals(AttackType.Spiritual))
			return (int) ((((2.0 * (float)attacker.getLevel() + 10.0)/250.0)*((float)attacker.getSpiritAttack()/(float)target.getSpiritDefense())*(float)attackBaseDamage+2)*calculateModifier());
		else if (attackType.equals(AttackType.Physical))
			return (int) ((((2.0 * (float)attacker.getLevel() + 10.0)/250.0)*((float)attacker.getPhysicalAttack()/(float)target.getPhysicalDefense())*(float)attackBaseDamage+2)*calculateModifier());
		else
			return (int) ((((2.0 * (float)attacker.getLevel() + 10.0)/250.0)*((float)attacker.getWeaponAttack()/(float)target.getWeaponDefense())*(float)attackBaseDamage+2)*calculateModifier());
	}
	
	public static int calculateExperience(EntityStatistics loser) {
		double x = loser.getLevel()*2 + 10;
		return (int) (Math.sqrt(x) * x * x);
	}
	
	/**
	 * Calculates rand 0.85 ... 1.00 inclusive of both
	 * @return The modifier
	 */
	public static float calculateModifier() {
		return ((float)random.nextInt(26) + 85)/100;
	}
}

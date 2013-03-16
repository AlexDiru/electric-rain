package managers;

import java.util.HashMap;
import java.util.Random;
import java.util.Set;

import statistics.Attack;

import xml.AttackXMLHelper;
import xml.XMLHelper;

public class AttackManager extends XMLHelper {

	// Attacks cannot have duplicate names

	private static HashMap<Integer, Attack> attackMap;
	private static Random random = new Random();

	public static void initialise() {
		try {
			attackMap = AttackXMLHelper.readAttacks("res/data/attacks.xml");
		} catch (Exception ex) {
		}
	}

	public static Attack getRandomAttack() {
		Set<Integer> keys = attackMap.keySet();
		return attackMap.get(keys.toArray()[random.nextInt(keys.size())]);
	}

	public static Attack getAttack(int attackId) {
		return attackMap.get(attackId);
	}

	public static Attack getAttackFromName(String name) {
		for (Integer key : attackMap.keySet())
			if (attackMap.get(key).getName().equals(name))
				return attackMap.get(key);
		return null;
	}

}

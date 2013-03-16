package helpers;

import entity.Enemy;
import entity.Entity;
import generics.ImmutablePair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

import managers.AttackManager;

import statistics.AttackAction;

/**
 * Provides functions to help manipulate data in the Battle class
 * 
 * @author Alex
 * 
 */
public class BattleHelper {
	
	private static Random random = new Random();
	
	/**
	 * Converts a hashmap to an list of pairs (int, string)
	 * 
	 * @param map
	 *            The hashmap to convert
	 * @return The list
	 */
	public static ArrayList<ImmutablePair<Integer, ArrayList<AttackAction>>> getList(HashMap<Integer, ArrayList<AttackAction>> map) {
		Set<Integer> keySet = map.keySet();
		Integer[] keys = keySet.toArray(new Integer[keySet.size()]);

		ArrayList<ImmutablePair<Integer, ArrayList<AttackAction>>> list = new ArrayList<ImmutablePair<Integer, ArrayList<AttackAction>>>();

		for (int i = 0; i < keys.length; i++)
			list.add(new ImmutablePair<Integer, ArrayList<AttackAction>>(keys[i], map.get(keys[i])));

		return list;
	}

	public static void sortAttacksBySpeed(ArrayList<ArrayList<AttackAction>> attacks) {
		Collections.sort(attacks, new Comparator<ArrayList<AttackAction>>() {
			public int compare(ArrayList<AttackAction> p, ArrayList<AttackAction> q) {
				AttackAction a = p.get(0);
				AttackAction b = q.get(0);
				return b.getUser().getStats().getSpeed() - a.getUser().getStats().getSpeed();
			}
		});
	}

	public static ArrayList<AttackAction> getAttack(ArrayList<ArrayList<AttackAction>> attacks, int userId, boolean eraseAttack) {
		for (ArrayList<AttackAction> attack : attacks)
			if (attack.size() > 0)
				if (attack.get(0).getUser().getId() == userId) {
					if (eraseAttack)
						attack.clear();

					return attack;
				}

		// Attack doesn't exist yet
		attacks.add(new ArrayList<AttackAction>());
		return attacks.get(attacks.size() - 1);
	}

	public static Enemy getRandomAliveEnemy(ArrayList<Enemy> ientities) {
		ArrayList<Enemy> aliveList = new ArrayList<Enemy>();
		for (Enemy Enemy : ientities)
			if (!Enemy.isDead())
				aliveList.add(Enemy);
		if (aliveList.size() < 1)
			return null;
		return aliveList.get(random.nextInt(aliveList.size()));
	}
	
	public static Entity getRandomAliveEntity(ArrayList<Entity> ientities) {
		ArrayList<Entity> aliveList = new ArrayList<Entity>();
		for (Entity Entity : ientities)
			if (!Entity.isDead())
				aliveList.add(Entity);
		if (aliveList.size() < 1)
			return null;
		return aliveList.get(random.nextInt(aliveList.size()));
	}
	
	/**
	 * Given an entities ID, checks if they have made an attack
	 * @param attackMap The list of attacks made this turn
	 * @param characterId The ID of the character
	 * @return Whether they have made an attack
	 */
	public static boolean hasCharacterMadeAnAttack(ArrayList<ArrayList<AttackAction>> attackMap, int characterId) {
		for (ArrayList<AttackAction> aa : attackMap)
			if (aa.get(0).getUser().getId() == characterId)
				return true;
		return false;
	}
	
	/**
	 * Converts a character's available attacks to a menu
	 * 
	 * @param attacks
	 *            The array of the attack IDs
	 * @return The new menu
	 */
	public static String[] convertToAttackChoiceMenu(int[] attacks) {
		String[] menu = new String[attacks.length];
		for (int i = 0; i < attacks.length; i++)
			menu[i] = AttackManager.getAttack(attacks[i]).getName();
		return menu;
	}

	/**
	 * Gets a count of the number of player characters that are still alive
	 * @param entitiesInParty The characters in the player's party
	 * @return The count of alive characters
	 */
	public static int getNumberOfAlivePlayers(ArrayList<Entity> entitiesInParty) {
		int count = 0;
		for (int i = 0; i < entitiesInParty.size(); i++)
			if (!entitiesInParty.get(i).isDead())
				count++;
		return count;
	}
	
	/**
	 * Whether all of the player characters are dead
	 * @param entitiesInParty The characters in the player's party
	 * @return Whether the characters are all dead
	 */
	public static boolean isAllPlayerDead(ArrayList<Entity> entitiesInParty) {
		for (int i = 0; i < entitiesInParty.size(); i++)
			if (!entitiesInParty.get(i).isDead())
				return false;
		return true;
	}

	/**
	 * Whether all of the enemies are dead
	 * @param enemies The enemies
	 * @return Death flag
	 */
	public static boolean isAllEnemyDead(ArrayList<Enemy> enemies) {
		for (int i = 0; i < enemies.size(); i++)
			if (!enemies.get(i).isDead())
				return false;
		return true;
	}
	
	public static int getNearestIndexOfAPlayerWhoHasntAttacked(ArrayList<ArrayList<AttackAction>> attackMap, int currentIndex, ArrayList<Entity> characters) {
		for (int i = 0; i < characters.size(); i++) { 
			//Pivot around current index
			int pivot = (i + currentIndex) % characters.size();
			
			if (!characters.get(pivot).isDead())
				if (!hasCharacterMadeAnAttack(attackMap, characters.get(pivot).getId()))
					return pivot;
		}
		return 0;
	}
}

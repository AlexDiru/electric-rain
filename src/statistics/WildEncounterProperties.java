package statistics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

import managers.EnemyManager;
import entity.Enemy;

public class WildEncounterProperties {

	private int minimumLevelRange;
	private int maximumLevelRange;

	/**
	 * Enemies located in any 'wild' section of the map
	 */
	private ArrayList<Integer> globalEnemies = new ArrayList<Integer>();

	/**
	 * Enemies located only on a specific tile Maps the tile id to the list of
	 * enemies that are unique to them
	 */
	private HashMap<Integer, ArrayList<Integer>> specificEnemies = new HashMap<Integer, ArrayList<Integer>>();

	private Random random = new Random();

	/**
	 * Given the tile the player is standing on Generates the encounter
	 * 
	 * @param tileId
	 * @return A list of the enemies the player will fight against
	 */
	public ArrayList<Enemy> generateEncounter(int tileId) {
		// Number of enemies 1 - 3 inclusive
		int number = random.nextInt(3) + 1;
		int level;
		int enemyId;

		ArrayList<Enemy> enemyList = new ArrayList<Enemy>();
		
		for (int i = 0; i < number; i++) {
			level = minimumLevelRange + random.nextInt(maximumLevelRange - minimumLevelRange + 1);

			try {
				ArrayList<Integer> concatenatedEnemies = new ArrayList<Integer>();

				// Add the global enemies
				concatenatedEnemies.addAll(globalEnemies);

				// If there are any specific enemies, add them
				if (specificEnemies.containsKey(tileId))
					concatenatedEnemies.addAll(specificEnemies.get(tileId));

				enemyId = concatenatedEnemies.get(random.nextInt(concatenatedEnemies.size()));
			} catch (Exception ex) {
				enemyId = 0;
			}
	
			// Generate the enemy
			Enemy enemy = EnemyManager.getNewEnemy(enemyId);
			enemy.getStats().setPhysicalAttack(50);
			enemy.getStats().setPhysicalDefense(50);
			enemy.getStats().setWeaponAttack(50);
			enemy.getStats().setWeaponDefense(50);
			enemy.getStats().setSpiritualAttack(50);
			enemy.getStats().setSpiritualDefense(50);
			enemy.getStats().setMaxHealth(35);
			enemy.getStats().setCurrentHealth(35);
			enemy.getStats().setLevel(level);
			enemy.getStats().setSpeed(random.nextInt(10));
			enemyList.add(enemy);
		}
		
		//Check enemies of the same name and amend the names with roman numerals if there are duplicate names
		//HashMap to map the name to the number of occurances
		HashMap<String, ArrayList<Enemy>> namesChecked = new HashMap<String, ArrayList<Enemy>>();
		
		for (Enemy enemy : enemyList) {
			if (!namesChecked.containsKey(enemy.getName())) 
				namesChecked.put(enemy.getName(), new ArrayList<Enemy>());
			namesChecked.get(enemy.getName()).add(enemy);
		}

		Set<String> keys = namesChecked.keySet();
		for (String key : keys) {
			//If more than one enemy with the same name
			if (namesChecked.get(key).size() > 1) {
				ArrayList<Enemy> duplicateNames = namesChecked.get(key);
				//Iterate through enemies and amend names
				for (int i = 0; i < duplicateNames.size(); i++)
					duplicateNames.get(i).setOccurance(i + 1);
			}
		}
		
		return enemyList;
	}

	public void addEnemy(int id) {
		globalEnemies.add(id);
	}

	public void addEnemy(int tileId, int enemyId) {
		if (!specificEnemies.containsKey(tileId))
			specificEnemies.put(tileId, new ArrayList<Integer>());

		specificEnemies.get(tileId).add(enemyId);
	}

	public int getMinimumLevelRange() {
		return minimumLevelRange;
	}

	public void setMinimumLevelRange(int minimumLevelRange) {
		this.minimumLevelRange = minimumLevelRange;
	}

	public int getMaximumLevelRange() {
		return maximumLevelRange;
	}

	public void setMaximumLevelRange(int maximumLevelRange) {
		this.maximumLevelRange = maximumLevelRange;
	}

}

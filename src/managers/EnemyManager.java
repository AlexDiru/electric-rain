package managers;

import java.util.HashMap;

import entity.Enemy;

public class EnemyManager {

	private static HashMap<Integer, Enemy> enemyFiles = new HashMap<Integer, Enemy>();

	public static void initialise() {
		enemyFiles.put(0, new Enemy("Warick", "res/enemy/rhydon.png"));
		enemyFiles.put(1, new Enemy("Crowbar", "res/enemy/golbat.png"));
		enemyFiles.put(2, new Enemy("Headcrab", "res/enemy/muk.png"));
	}
	
	public static Enemy getNewEnemy(int id) {
		return enemyFiles.get(id).clone();
	}
	
	public static String getEnemyName(int id) {
		return enemyFiles.get(id).getName();
	}
}

package statistics;



public class EntityStatistics {

	private int physicalAttack;
	private int weaponAttack;
	private int spiritAttack;
	private int physicalDefense;
	private int weaponDefense;
	private int spiritDefense;
	private int speed;
	private int currentHealth;
	private int maxHealth;
	private int currentSpirit;
	private int maxSpirit;
	private int level;
	private int experience;
	
	public EntityStatistics() {
	}
	
	public EntityStatistics(int physicalAttack, int weaponAttack, int spiritAttack, int physicalDefense, int weaponDefense, int spiritDefense, int speed, int currentHealth, int maxHealth,
			int currentSpirit, int maxSpirit, int level, int experience) {
		super();
		this.setPhysicalAttack(physicalAttack);
		this.setWeaponAttack(weaponAttack);
		this.spiritAttack = spiritAttack;
		this.setPhysicalDefense(physicalDefense);
		this.setWeaponDefense(weaponDefense);
		this.spiritDefense = spiritDefense;
		this.speed = speed;
		this.currentHealth = currentHealth;
		this.maxHealth = maxHealth;
		this.currentSpirit = currentSpirit;
		this.maxSpirit = maxSpirit;
		this.level = level;
		this.experience = experience;
	}
	
	public void increaseExperience(int amount) {
		experience += amount;
	}
	
	public void reduceHealth(int amount) {
		currentHealth -= amount;
		
		if (currentHealth < 0)
			currentHealth = 0;
	}
	
	public void increaseHealth(int amount) {
		currentHealth += amount;
		
		if (currentHealth > maxHealth)
			currentHealth = maxHealth;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public void setCurrentHealth(int currentHealth) {
		this.currentHealth = currentHealth;
	}

	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}

	public void setCurrentSpirit(int currentSpirit) {
		this.currentSpirit = currentSpirit;
	}

	public void setMaxSpirit(int maxSpirit) {
		this.maxSpirit = maxSpirit;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
	public void setExperience(int experience) {
		this.experience = experience;
	}

	public int getSpiritAttack() {
		return spiritAttack;
	}

	public int getSpiritDefense() {
		return spiritDefense;
	}

	public int getSpeed() {
		return speed;
	}

	public int getCurrentHealth() {
		return currentHealth;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public int getCurrentSpirit() {
		return currentSpirit;
	}

	public int getMaxSpirit() {
		return maxSpirit;
	}

	public int getLevel() {
		return level;
	}
	
	public int getExperience() {
		return experience;
	}

	public String getTotalExperienceForNextLevel() {
		return null;
	}

	public int getWeaponDefense() {
		return weaponDefense;
	}

	public void setWeaponDefense(int weaponDefense) {
		this.weaponDefense = weaponDefense;
	}

	public int getPhysicalDefense() {
		return physicalDefense;
	}

	public void setPhysicalDefense(int physicalDefense) {
		this.physicalDefense = physicalDefense;
	}

	public int getPhysicalAttack() {
		return physicalAttack;
	}

	public void setPhysicalAttack(int physicalAttack) {
		this.physicalAttack = physicalAttack;
	}

	public int getWeaponAttack() {
		return weaponAttack;
	}

	public void setWeaponAttack(int weaponAttack) {
		this.weaponAttack = weaponAttack;
	}

	public void setSpiritualAttack(int i) {
		this.spiritAttack = i;
	}
	
	public void setSpiritualDefense(int i) {
		this.spiritDefense = i;
	}
}

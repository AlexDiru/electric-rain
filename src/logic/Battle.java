package logic;

import java.util.ArrayList;
import java.util.Random;

import managers.AttackManager;
import managers.BattleManager;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import statistics.Attack;
import statistics.Attack.AttackTarget;
import statistics.Attack.AttackType;
import statistics.Attack.AttackUsage;
import statistics.AttackAction;
import entity.Enemy;
import entity.Entity;
import entity.IEntity;
import game.Game;
import game.Game.GameState;
import game.KeyInput;
import graphics.BattleCursors;
import graphics.BattleCursors.CursorType;
import graphics.DialogBox;
import graphics.GlobalFont;
import helpers.BattleHelper;
import helpers.FormulaeHelper;
import helpers.ScaleHelper;

/**
 * To do: Enemies which the same name are suffixed with roman numerals
 * 
 * @author Alex
 * 
 */
public class Battle {
	/**
	 * The state of the menu
	 */
	private enum MenuState {
		MAINMENU, FIGHTCHARACTERSELECTION, ATTACKTYPEMENU, ATTACKCHOICEMENU, ENEMYSELECTION, BUFFCHARACTERSELECTION, ITEMSELECTION
	}

	/**
	 * The background image of the battle
	 */
	private Image background;

	/**
	 * The list of all the enemies the player is fighting against
	 */
	private ArrayList<Enemy> enemies;

	/**
	 * The player fighting the battle
	 */
	private Player player;

	/**
	 * The current state of the menu
	 */
	private MenuState menuState = MenuState.MAINMENU;

	/**
	 * The options of the main menu
	 */
	private String[] mainMenu = { "Fight", "Item", "Run" };

	/**
	 * The current menu being rendered
	 */
	private String[] currentMenu;

	/**
	 * The index of the menu item selected
	 */
	private int selectedMenuItem = 0;

	/**
	 * The number of characters the player is using in battle
	 */
	private int playerPartySize;

	/**
	 * The index of the character the player has selected
	 */
	private int selectedCharacter = -1;

	/**
	 * The index of the enemy the player has chosen to set their attack
	 * destination to Or in the case of buffs, it represents the index of the
	 * character that the player has chosen to buff
	 */
	private int selectedEnemy = 0;

	/**
	 * A random generator
	 */
	private Random random = new Random();

	/**
	 * The type of attack the player wants to use for their selected character
	 */
	private AttackType selectedAttackType;

	/**
	 * The attack the player has chosen a particular character to use
	 */
	private Attack selectedAttack;

	/**
	 * Maps an attack from it's user
	 */
	private ArrayList<ArrayList<AttackAction>> attackMap = new ArrayList<ArrayList<AttackAction>>();

	/**
	 * The Y positions that the players and enemies are rendered at
	 */
	private static final int[] characterYPositions = new int[] { 45, 175, 305, 435 };

	/**
	 * The pointers to indicate which character is selected
	 */
	private BattleCursors battleCursors = new BattleCursors("res/battle/selectPlayerCursor.png", "res/battle/selectEnemyCursor.png", "res/battle/selectPlayerFlippedCursor.png");

	/**
	 * Constructor
	 * 
	 * @param player
	 *            The player fighting the battle
	 */
	public Battle(Player player) {
		this.player = player;
	}

	/**
	 * Sets the menu state, use this function instead of direct assignment as it
	 * assigns other variables
	 * 
	 * @param menuState
	 *            The new menu state
	 */
	private void setMenuState(MenuState menuState) {
		this.menuState = menuState;

		// Reset the current menu
		currentMenu = null;

		switch (menuState) {

		case MAINMENU:
			currentMenu = mainMenu;
			break;

		case ATTACKTYPEMENU:
			// Current menu is the attack types of the selected character
			currentMenu = player.getEntitiesInParty().get(selectedCharacter).getAllAttackTypesAsMenu();
			break;

		case ATTACKCHOICEMENU:
			if (selectedAttackType != null)
				// Current menu is the attacks of the selected type
				currentMenu = player.getEntitiesInParty().get(selectedCharacter).getSpecificAttackTypeAttacksAsMenu(selectedAttackType);
			else
				// Assume the "All" option was chosen - so show all attacks
				currentMenu = BattleHelper.convertToAttackChoiceMenu(player.getEntitiesInParty().get(selectedCharacter).getAvailableAttacks());
			break;
			
		case FIGHTCHARACTERSELECTION:
			selectedCharacter = BattleHelper.getNearestIndexOfAPlayerWhoHasntAttacked(attackMap, selectedCharacter, player.getEntitiesInParty());
			break;

		case ENEMYSELECTION:
			// Make sure selectedEnemy variable doesn't throw an OOB exception
			// on the number of enemies
			if (selectedEnemy >= enemies.size())
				selectedEnemy = enemies.size() - 1;
			break;

		case BUFFCHARACTERSELECTION:
			// Make sure selectedEnemy variable doesn't throw an OOB exception
			// on the number of characters
			if (selectedEnemy >= player.getEntitiesInParty().size())
				selectedEnemy = player.getEntitiesInParty().size() - 1;
			break;

		case ITEMSELECTION:
			currentMenu = player.getInventory().getAsMenu();
			break;

		default:
			break;
		}
	}

	/**
	 * Initialise the battle
	 * 
	 * @param tiles
	 *            An array of all the tile ids the player is standing on
	 * @param enemies
	 *            The list of the enemies that are being fought
	 */
	public void initialise(int[] tiles, ArrayList<Enemy> enemies) {
		playerPartySize = player.getEntitiesInParty().size();
		selectedMenuItem = 0;
		selectedCharacter = -1;
		selectedEnemy = 0;
		setMenuState(MenuState.MAINMENU);

		this.enemies = enemies;
		for (Enemy enemy : enemies)
			enemy.loadImage();

		// Iterate through the array until a valid background is found
		String filename = "";
		for (int i = 0; i < tiles.length; i++)
			if ((filename = BattleManager.getBackgroundFilePath(tiles[i])) != null)
				break;

		if (filename == null) {
			System.out.println("Tile doesn't have a battle background image associated with it");
			throw new NullPointerException();
		}

		setBackground(filename);
	}

	/**
	 * Sets the background of the battle
	 * 
	 * @param filename
	 *            The file of the image
	 */
	private void setBackground(String filename) {
		try {
			background = new Image(filename);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Renders the given menu
	 * 
	 * @param g
	 *            The graphics object
	 * @param menuItems
	 *            The list of text on the menu
	 */
	private void drawMenu(Graphics g, String[] menuItems) {
		// No items - render nothing
		if (menuItems == null)
			return;

		// Add menu title
		String text = menuState.toString() + "\n";

		int menuCursorHeight = 0;

		// Add menu items
		int i = 0;
		for (String item : menuItems) {
			String menuItem = "";

			if (i++ == selectedMenuItem) {
				// Set the height of menu cursor
				menuCursorHeight = Game.height - 175 + i * GlobalFont.getMediumFont().getLineHeight();
			}

			menuItem += item;
			text += menuItem + "\n";
		}

		// Draw the dialog box for the menu
		DialogBox.draw(g, 0, Game.height - 200, 50, 25, text);
		DialogBox.drawMenuCursor(18, menuCursorHeight);
	}

	/**
	 * Cleans up the data used for a battle and changes the game state
	 */
	private void endBattle() {
		this.enemies.clear();
		try {
			this.background.destroy();
		} catch (SlickException e) {
			e.printStackTrace();
		}
		Game.gameState = GameState.WORLD;
	}

	/**
	 * Updates the battle - checks what keys were pressed and updates
	 * accordingly
	 */
	public void update() {

		// Common menu functions
		if (menuState.equals(MenuState.MAINMENU) || menuState.equals(MenuState.ATTACKCHOICEMENU) || menuState.equals(MenuState.ATTACKTYPEMENU)) {
			if (KeyInput.moveBackwardPressed)
				increaseSelectedMenuItem();

			if (KeyInput.moveForwardPressed)
				decreaseSelectedMenuItem();
		}

		switch (menuState) {
		case MAINMENU:
			updateMainMenu();
			break;

		case FIGHTCHARACTERSELECTION:
			updateFightCharacterSelectionMenu();
			break;

		case ATTACKTYPEMENU:
			updateAttackTypeMenu();
			break;

		case ATTACKCHOICEMENU:
			updateAttackChoiceMenu();
			break;

		case ENEMYSELECTION:
			updateEnemySelectionMenu();
			break;

		case BUFFCHARACTERSELECTION:
			updateBuffCharacterSelectionMenu();
			break;
		default:
			break;
		}

	}

	private void updateMainMenu() {
		if (KeyInput.confirmPressed) {
			switch (getSelectedMenuItem()) {
			case "Fight":
				// Set the selected character to zero or high in case they
				// are dead
				selectedCharacter = -1;
				increaseSelectedCharacter();
				// Set the selected enemy to one who isn't dead
				if (selectedEnemy >= enemies.size())
					selectedEnemy = 0;

				if (enemies.get(selectedEnemy).isDead())
					increaseSelectedEnemy();
				setMenuState(MenuState.FIGHTCHARACTERSELECTION);
				break;

			case "Item":
				setMenuState(MenuState.ITEMSELECTION);
				break;

			case "Run":
				endBattle();
				break;
			}
		}
	}

	private void updateFightCharacterSelectionMenu() {
		if (KeyInput.moveBackwardPressed)
			increaseSelectedCharacter();

		if (KeyInput.moveForwardPressed)
			decreaseSelectedCharacter();

		if (KeyInput.confirmPressed)
			setMenuState(MenuState.ATTACKTYPEMENU);

		if (KeyInput.backPressed)
			setMenuState(MenuState.MAINMENU);

		if (KeyInput.moveRightPressed) {
			// AI attack
			generateAIAttacks();
			calculateAttacksInATurn();

			if (BattleHelper.isAllEnemyDead(enemies)) {
				calculateExperience();
				endBattle();
			} else if (BattleHelper.isAllPlayerDead(player.getEntitiesInParty()))
				System.out.println("GAME OVER");
			setMenuState(MenuState.MAINMENU);
		}
	}

	private void updateAttackTypeMenu() {
		if (KeyInput.backPressed)
			setMenuState(MenuState.FIGHTCHARACTERSELECTION);

		if (KeyInput.confirmPressed) {
			// Set the attack type by converting the string on the menu
			// display to the enum value
			try {
				selectedAttackType = AttackType.valueOf(getSelectedMenuItem());
			} catch (IllegalArgumentException ex) {
				// Assume the "All" option was picked
				selectedAttackType = null;
			}

			setMenuState(MenuState.ATTACKCHOICEMENU);
			selectedMenuItem = 0;
		}
	}

	private void updateAttackChoiceMenu() {
		if (KeyInput.backPressed)
			setMenuState(MenuState.ATTACKTYPEMENU);

		if (KeyInput.confirmPressed) {
			selectedAttack = AttackManager.getAttackFromName(getSelectedMenuItem());

			if (selectedAttack.getAttackTarget().equals(AttackTarget.Group)) {
				// Group Target Attack
				if (selectedAttack.getAttackUsage().equals(AttackUsage.Regular))
					// Apply to all enemies
					for (int i = 0; i < enemies.size(); i++)
						addAttack(player.getEntitiesInParty().get(selectedCharacter), new AttackAction(player.getEntitiesInParty().get(selectedCharacter), enemies.get(i), selectedAttack), true);
				else
					// Apply to all allies
					for (int i = 0; i < playerPartySize; i++)
						addAttack(player.getEntitiesInParty().get(selectedCharacter), new AttackAction(player.getEntitiesInParty().get(selectedCharacter), player.getEntitiesInParty().get(i),
								selectedAttack), true);

				setMenuState(MenuState.FIGHTCHARACTERSELECTION);
			} else {
				// Singular Target Attack
				if (selectedAttack.getAttackUsage().equals(AttackUsage.Regular))
					setMenuState(MenuState.ENEMYSELECTION);
				else if (selectedAttack.getAttackUsage().equals(AttackUsage.Buff))
					setMenuState(MenuState.BUFFCHARACTERSELECTION);
			}
		}
	}

	private void updateEnemySelectionMenu() {
		if (KeyInput.moveBackwardPressed)
			increaseSelectedEnemy();

		if (KeyInput.moveForwardPressed)
			decreaseSelectedEnemy();

		if (KeyInput.backPressed)
			setMenuState(MenuState.ATTACKCHOICEMENU);

		if (KeyInput.confirmPressed) {
			// Add the attack to the attack map
			addAttack(player.getEntitiesInParty().get(selectedCharacter), new AttackAction(player.getEntitiesInParty().get(selectedCharacter), enemies.get(selectedEnemy), selectedAttack), false);

			// Reset the menu state to character selection
			setMenuState(MenuState.FIGHTCHARACTERSELECTION);
		}
	}

	private void updateBuffCharacterSelectionMenu() {
		// In this menu, selectedEnemy holds the value of the selected
		// character
		if (KeyInput.moveBackwardPressed)
			increaseSelectedBuffCharacter();

		if (KeyInput.moveForwardPressed)
			decreaseSelectedBuffCharacter();

		if (KeyInput.backPressed)
			setMenuState(MenuState.ATTACKCHOICEMENU);

		if (KeyInput.confirmPressed) {
			addAttack(player.getEntitiesInParty().get(selectedCharacter), new AttackAction(player.getEntitiesInParty().get(selectedCharacter), player.getEntitiesInParty().get(selectedEnemy),
					selectedAttack), false);
			setMenuState(MenuState.FIGHTCHARACTERSELECTION);
		}

	}

	private void generateAIAttacks() {
		// Filter the players to those who are alive
		Entity[] aliveEntities = new Entity[BattleHelper.getNumberOfAlivePlayers(player.getEntitiesInParty())];
		int arrayIndex = 0;
		for (int j = 0; j < playerPartySize; j++)
			// If the character isn't dead
			if (!player.getEntitiesInParty().get(j).isDead())
				// Add them to the array
				aliveEntities[arrayIndex++] = player.getEntitiesInParty().get(j);

		// Iterate enemies
		for (int i = 0; i < enemies.size(); i++)
			// Dead enemies can't attack
			if (!enemies.get(i).isDead())
				// Random attack type and target
				addAttack(enemies.get(i), new AttackAction(enemies.get(i), aliveEntities[random.nextInt(aliveEntities.length)], AttackManager.getRandomAttack()), false);
	}

	private void calculateAttacksInATurn() {
		BattleHelper.sortAttacksBySpeed(attackMap);
		for (ArrayList<AttackAction> attack : attackMap) {
			for (AttackAction attackAction : attack) {
				if (attackAction.getUser().isDead())
					System.out.println(attackAction.getUser().getName() + " attempted to attack but is dead");
				else if (!attackAction.getRecipient().isDead())
					// If recipient isn't dead
					calculateAttack(attackAction.getUser(), attackAction.getRecipient(), attackAction.getAttack());
				else {
					// Redirect attack to a different recipient
					System.out.print(attackAction.getUser().getName() + " attack redirected from " + attackAction.getRecipient().getName() + " to ");

					// If the recipient is a player or an enemy
					if (attackAction.getRecipient().isEnemy()) {
						// Get a random alive enemy
						Enemy randomAliveEnemy = BattleHelper.getRandomAliveEnemy(enemies);

						// Attempt to use attack on a random alive enemy
						if (randomAliveEnemy != null) {
							System.out.print(randomAliveEnemy.getName() + "\n");
							calculateAttack(attackAction.getUser(), randomAliveEnemy, attackAction.getAttack());
						} else
							// No alive enemies to redirect to
							System.out.print("nowhere\n");

					} else {
						// Get a random alive player
						Entity randomAliveEntity = BattleHelper.getRandomAliveEntity(player.getEntitiesInParty());

						// Attempt to use attack on a random alive player
						if (randomAliveEntity != null) {
							System.out.print(randomAliveEntity.getName() + "\n");
							calculateAttack(attackAction.getUser(), randomAliveEntity, attackAction.getAttack());
						} else
							// No alive players to redirect to
							System.out.print("nowhere\n");
					}
				}
			}
		}

		// Attack Map is no longer needed
		attackMap.clear();
	}

	private void calculateAttack(IEntity user, IEntity target, Attack attack) {
		// Calculate Damage
		if (attack.getBaseDamage() > 0) {
			int damage = FormulaeHelper.calculateDamage(user.getStats(), target.getStats(), attack.getAttackType(), attack.getBaseDamage());
			target.getStats().reduceHealth(damage);
			System.out.println(user.getName() + "(speed " + user.getStats().getSpeed() + ") inflicted " + damage + " points of damage on " + target.getName() + " using " + attack.getName());
		}

		// Calculate Health Increase
		if (attack.getBaseHealing() > 0) {
			target.getStats().increaseHealth(attack.getBaseHealing());
			System.out.println(user.getName() + "(speed " + user.getStats().getSpeed() + ") healed " + attack.getBaseHealing() + " points of health on " + target.getName() + " using "
					+ attack.getName());
		}
	}

	private void calculateExperience() {
		int totalExperience = 0;
		for (Enemy enemy : enemies)
			totalExperience += FormulaeHelper.calculateExperience(enemy.getStats());

		// Divide the experience between alive characters
		int experiencePerCharacter = totalExperience / BattleHelper.getNumberOfAlivePlayers(player.getEntitiesInParty());

		// Apply experience to alive characters
		for (int i = 0; i < playerPartySize; i++)
			if (!player.getEntitiesInParty().get(i).isDead()) {
				player.getEntitiesInParty().get(i).getStats().increaseExperience(experiencePerCharacter);
				System.out.println(player.getEntitiesInParty().get(i).getName() + " experience increased by " + experiencePerCharacter);
			}
	}

	/**
	 * Adds an attack to the attack map
	 * 
	 * @param user
	 *            The user of the attack
	 * @param attack
	 *            The attack the user is using
	 * @param groupAttack
	 *            Whether the attack targets a whole group
	 */
	private void addAttack(IEntity user, AttackAction attack, boolean groupAttack) {
		BattleHelper.getAttack(attackMap, user.getId(), !groupAttack).add(attack);
	}

	/**
	 * Gets a name of the current menu item that is selected
	 * 
	 * @return The name of the menu item
	 */
	private String getSelectedMenuItem() {
		return currentMenu[selectedMenuItem];
	}

	/**
	 * Increases the index of the selected menu item
	 */
	private void increaseSelectedMenuItem() {
		selectedMenuItem++;
		if (selectedMenuItem >= currentMenu.length)
			selectedMenuItem = 0;
	}

	/**
	 * Decreases the index of the selected menu item
	 */
	private void decreaseSelectedMenuItem() {
		selectedMenuItem--;
		if (selectedMenuItem < 0)
			selectedMenuItem = currentMenu.length - 1;
	}

	private void increaseSelectedCharacter() {
		selectedCharacter++;
		if (selectedCharacter >= playerPartySize)
			selectedCharacter = 0;

		// Infinite loop may happen
		if (player.getEntitiesInParty().get(selectedCharacter).isDead())
			increaseSelectedCharacter();
	}

	private void decreaseSelectedCharacter() {
		selectedCharacter--;
		if (selectedCharacter < 0)
			selectedCharacter = playerPartySize - 1;

		if (player.getEntitiesInParty().get(selectedCharacter).isDead())
			decreaseSelectedCharacter();
	}

	private void increaseSelectedEnemy() {
		selectedEnemy++;
		if (selectedEnemy >= enemies.size())
			selectedEnemy = 0;

		if (enemies.get(selectedEnemy).isDead())
			increaseSelectedEnemy();
	}

	private void decreaseSelectedEnemy() {
		selectedEnemy--;
		if (selectedEnemy < 0)
			selectedEnemy = enemies.size() - 1;

		if (enemies.get(selectedEnemy).isDead())
			decreaseSelectedEnemy();
	}

	private void increaseSelectedBuffCharacter() {
		selectedEnemy++;
		if (selectedEnemy >= player.getEntitiesInParty().size())
			selectedEnemy = 0;

		if (player.getEntitiesInParty().get(selectedEnemy).isDead())
			increaseSelectedEnemy();
	}

	private void decreaseSelectedBuffCharacter() {
		selectedEnemy--;
		if (selectedEnemy < 0)
			selectedEnemy = player.getEntitiesInParty().size() - 1;

		if (player.getEntitiesInParty().get(selectedEnemy).isDead())
			decreaseSelectedEnemy();
	}

	public void render(Graphics g) {
		background.draw(0, 0, ScaleHelper.determineScaleFactorOfBattleBackground(background.getWidth(), background.getHeight(), Game.width, Game.height));

		// Draw enemies
		for (int i = 0; i < enemies.size(); i++)
			if (!enemies.get(i).isDead())
				enemies.get(i).render(Game.width - 100 - enemies.get(i).getImage().getWidth(), characterYPositions[i]);

		// Draw player's characters
		for (int i = 0; i < playerPartySize; i++)
			if (!player.getEntitiesInParty().get(i).isDead()) {
				int id = player.getEntitiesInParty().get(i).getId();

				// If the character has made an attack
				if (BattleHelper.hasCharacterMadeAnAttack(attackMap, id))
					player.render(i, 150, characterYPositions[i]);
				else
					player.render(i, 100, characterYPositions[i]);
			}

		// Draw selected player cursor
		if (menuState.equals(MenuState.FIGHTCHARACTERSELECTION) || menuState.equals(MenuState.ATTACKCHOICEMENU) || menuState.equals(MenuState.ENEMYSELECTION)
				|| menuState.equals(MenuState.BUFFCHARACTERSELECTION) || menuState.equals(MenuState.ATTACKTYPEMENU))
			battleCursors.render(CursorType.PLAYERSELECT, 30, characterYPositions[selectedCharacter]);

		// Draw player buff selector
		if (menuState.equals(MenuState.BUFFCHARACTERSELECTION))
			battleCursors.render(CursorType.PLAYERSELECTFLIPPED, 166, characterYPositions[selectedEnemy]);

		// Draw selected enemy cursor
		if (menuState.equals(MenuState.ENEMYSELECTION))
			battleCursors.render(CursorType.ENEMYSELECT, Game.width - 202, characterYPositions[selectedEnemy]);

		// Draw health for player's characters
		int i = 0;
		for (IEntity iEntity : player.getEntitiesInParty()) {
			if (!iEntity.isDead()) {
				String text = iEntity.toBattleDisplayString();
				GlobalFont.getMediumFont().drawString(45, characterYPositions[i] - GlobalFont.getMediumFont().getLineHeight(), text);
				iEntity.updateAndRenderHealthBar(g, 45, characterYPositions[i] + Game.tileSize);
				iEntity.updateAndRenderSpiritBar(g, 45, characterYPositions[i] + Game.tileSize + 12);
			}
			i++;
		}

		// Draw health for enemies
		i = 0;
		for (IEntity iEntity : enemies) {
			if (!iEntity.isDead()) {
				String text = iEntity.toBattleDisplayString();
				GlobalFont.getSmallFont().drawString(Game.width - 45 - GlobalFont.getMediumFont().getWidth(text), characterYPositions[i] - GlobalFont.getMediumFont().getLineHeight(), text);
				iEntity.updateAndRenderHealthBar(g, Game.width - 45 - iEntity.getHealthBar().getWidth(), characterYPositions[i] + Game.tileSize);
			}
			i++;
		}

		// Draw dialog box
		if (menuState.equals(MenuState.ATTACKCHOICEMENU) || menuState.equals(MenuState.MAINMENU) || menuState.equals(MenuState.ATTACKTYPEMENU) || menuState.equals(MenuState.ITEMSELECTION)) {
			drawMenu(g, currentMenu);
		}
	}
}
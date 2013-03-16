package game;
	

import graphics.DialogBox;
import helpers.ScaleHelper;
import logic.Map;
import managers.AttackManager;
import managers.BattleManager;
import managers.EnemyManager;
import managers.ItemManager;
import managers.MapManager;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import weather.Weather.WeatherType;

/**
 * Tiles and players from http://www.tekepon.net/
 * Items from http://pousse.rapiere.free.fr/tome/index.htm
 *
 */

public class Game extends BasicGame {

	public enum GameState {
		WORLD, BATTLE
	}

	public static GameState gameState = GameState.WORLD;

	public static int width = 1024;
	public static int height = 768;
	public static int currentDelta;
	public static final int tileSize = 32;
	
	public static final int desiredTilesAcrossScreenHeight = 768/32;
	public static final int desiredTilesAcrossScreenWidth = 1024/32;
	
	public static final float scaleFactor = ScaleHelper.determineScaleFactorOfTile();

	private Map map;

	// Frame rate parameters
	private int elapsedTime;
	private static final int delay = 1000 / 60;

	public Game() {
		super("electric-rain");
	}

	public void init(GameContainer gc) throws SlickException {

		BattleManager.initialise();
		MapManager.initialiseFiles();
		MapManager.initialisePortals();
		EnemyManager.initialise();
		AttackManager.initialise();
		DialogBox.initialise();
		ItemManager.initialise();
		
		map = new Map(); 
		
		//Add some default items to the inventory
		map.player.getInventory().add(ItemManager.getItem(1), 2);
		map.player.getInventory().add(ItemManager.getItem(3), 4);
		map.player.getInventory().add(ItemManager.getItem(2), 5);
	}

	public void update(GameContainer gc, int delta) throws SlickException {
		currentDelta = delta;
		elapsedTime += delta;
		if (elapsedTime >= delay) {
			elapsedTime = 0;
			KeyInput.get(gc);

			if (gameState.equals(GameState.WORLD)) {
				map.player.playerControlledMovement();
				map.updateNPCs();

				if (KeyInput.switchCharacterLeftPressed)
					map.player.decreaseCharacterIndex();
				if (KeyInput.switchCharacterRightPressed)
					map.player.increaseCharacterIndex();

				if (gc.getInput().isKeyPressed(Input.KEY_Q)) {
					System.out.println("Player position: " + map.player.tilePositionX  + ", " + map.player.tilePositionY);
					for (int i = 0; i < map.tiledMap.getLayerCount(); i++)
						System.out.println("Layer " + i + " | Tile ID: " + map.tiledMap.getTileId(map.player.tilePositionX, map.player.tilePositionY, i));
					System.out.println(map.player.getFacingDirection());
				}
				
				if (gc.getInput().isKeyPressed(Input.KEY_1))
					map.getWeather().setWeatherType(WeatherType.NONE);
				else if (gc.getInput().isKeyPressed(Input.KEY_2))
					map.getWeather().setWeatherType(WeatherType.RAIN);
				else if (gc.getInput().isKeyPressed(Input.KEY_3))
					map.getWeather().setWeatherType(WeatherType.HEAVYRAIN);
				
				map.getWeather().update();
				
				//Run
				if (gc.getInput().isKeyDown(Input.KEY_LSHIFT))
					map.player.setMovementTimerMax(80);
				else 
					map.player.setMovementTimerMax(160);
				
				//Action
				if (gc.getInput().isKeyPressed(Input.KEY_SPACE))
					map.action();
				
				//Inventory
				if (KeyInput.inventoryPressed)
					//Toggle
					map.renderInventoryFlag = !map.renderInventoryFlag;
				
				//Part
				if (KeyInput.partyPressed)
					//Toggle
					map.renderPartyFlag = !map.renderPartyFlag;
				
			} else if (gameState.equals(GameState.BATTLE)) {
				map.battle.update();
				if (gc.getInput().isKeyPressed(Input.KEY_Q))
					gameState = GameState.WORLD;
			}
		}
	}

	public void render(GameContainer gc, Graphics g) throws SlickException {
		if (gameState.equals(GameState.WORLD)) {
			map.render(g);
		}
		else if (gameState.equals(GameState.BATTLE))
			map.battle.render(g);
	}

	public static void main(String[] args) throws SlickException {
		
		AppGameContainer app = new AppGameContainer(new Game());
		app.setShowFPS(false);
		//app.setVSync(true);
		app.setDisplayMode(width, height, false);
		app.setTargetFrameRate(60);
		try {
		app.start();
		} catch (SlickException ex) {
			ex.printStackTrace();
		}
	}
}
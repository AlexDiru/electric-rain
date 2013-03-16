package weather;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class Weather {
	public enum WeatherType {
		NONE, RAIN, HEAVYRAIN
	}
	
	private WeatherType weatherType = WeatherType.NONE;
	private Rain rain;
	
	public void setWeatherType(WeatherType weatherType) {
		this.weatherType = weatherType;
		
		switch (weatherType) {
		case RAIN:
			rain = new Rain(500,10,50,8,14,230,200,50,10,30,100,new Color(255,255,255, 210), new Color(255,255,255, 210));
			break;
		case HEAVYRAIN:
			rain = new Rain(1200,10,50,8,14,230,200,50,10,30,200, new Color(120,120,120, 210), new Color(120,120,120,210));
			break;
		default:
			break;
		};
	}
	
	public void update() {
		switch (weatherType) {
		case RAIN:
		case HEAVYRAIN:
			rain.update();
			break;
		default:
			break;
		}
	}
	
	public void render(Graphics g) {
		switch (weatherType) {
		case RAIN:
		case HEAVYRAIN:
			rain.render(g);
			break;
		default:
			break;
		}
	}
}

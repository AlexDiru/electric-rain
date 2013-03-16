package graphics;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import weather.SolidFill;

/**
 * Needs a better name
 * Produces a bar for stats such as health
 * @author Alex
 *
 */
public class Bar {
	
	private int width;
	private int height;
	private SolidFill background;
	private SolidFill foreground;
	private int arg1;
	private int arg2;
	
	public Bar(int width, int height, Color foreground, Color background) {
		super();
		this.width = width;
		this.height = height;
		this.background = new SolidFill(background);
		this.foreground = new SolidFill(foreground);
	}

	public void update(int arg1, int arg2) {
		this.arg1 = arg1;
		this.arg2 = arg2;
	}
	
	public void render(Graphics g, int x, int y) {
		//Draw foreground
		int leftWidth = (int) (((float)arg1/(float)arg2) * (float)width);
		g.fill(new Rectangle(x, y, leftWidth, height), foreground);
		
		if (leftWidth == width)
			return;
		
		//Draw background
		g.fill(new Rectangle(x + leftWidth, y, width - leftWidth, height), background);
	}
	
	public void updateAndRender(int arg1, int arg2, Graphics g, int x, int y) {
		update(arg1, arg2);
		render(g, x, y);
	}
	
	public int getWidth() {
		return width;
	}
}

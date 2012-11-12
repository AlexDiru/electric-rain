package game;

import logic.Entity;
import logic.Map;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Light {
	private static Color sharedColor = new Color(1f,1f,1f,1f);
	private static Image alphaMap;
	public float x;
	public float y;
	public Color tint;
	public float alpha;
	public float scale;
	
	public Light(float x, float y, float scale, Color tint) {
		try {
			alphaMap = new Image("C:/Users/Alex/Desktop/lighting_sprites.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
		this.x = x;
		this.y = y;
		this.scale = scale;
		alpha = 1f;
		this.tint = tint;
	}
	
	public Light(float x, float y, float scale) {
		this(x,y,scale, Color.white);
	}
	
	public void render(Map m, Graphics g, int x, int y, Entity entity) {
		preRender(m,g,x,y);
		
        m.tileLayers.get(0).tileset.startImageUse();
        
        tint.bind();
       
        m.render();
		entity.spriteSheet.getSprite(0, 0).drawEmbedded(Game.width / 2, Game.height / 2, 16,16);

        m.tileLayers.get(0).tileset.endImageUse();
        
		postRender(g);
	}
	
	public void preRender(Map m, Graphics g, int x, int y) {
		g.setDrawMode(Graphics.MODE_ALPHA_MAP);
		g.clearAlphaMap();
		

        int alphaW = (int)(alphaMap.getWidth() * scale);
        int alphaH = (int)(alphaMap.getHeight() * scale);
        int alphaX = (int)(x - alphaW/2f);
        int alphaY = (int)(y - alphaH/2f);
        
        sharedColor.a = alpha;
        
        alphaMap.draw(alphaX, alphaY, alphaW, alphaH, sharedColor);
        
        g.setDrawMode(Graphics.MODE_ALPHA_BLEND);
        g.setClip(alphaX, alphaY, alphaW, alphaH);
	}
	
	public void postRender(Graphics g) {
        g.clearClip();
        g.setDrawMode(Graphics.MODE_NORMAL);
	}
}

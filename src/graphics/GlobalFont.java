package graphics;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Image;

public class GlobalFont {
	
	private static AngelCodeFont createFont(String file1, String file2) {
		try {
			return new AngelCodeFont(file1,new Image(file2));
		} catch (Exception e) {
			System.out.println(file1 + " failed or " + file2 + " failed");
			return null;
		}
	}
	
	private static AngelCodeFont mediumFont = createFont("res/font/font.fnt", "res/font/font_0.png");
	private static AngelCodeFont smallFont = createFont("res/font/smallFont.fnt", "res/font/smallFont_0.png");
	
	public static AngelCodeFont getMediumFont() {
		return mediumFont;
	}
	
	public static AngelCodeFont getSmallFont() {
		return smallFont;
	}
}

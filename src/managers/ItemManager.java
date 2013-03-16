package managers;

import items.Item;

import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import xml.ItemXMLHelper;

public class ItemManager {

	private static HashMap<Integer, Item> itemMap;
	
	public static void initialise() {
		try {
			itemMap = ItemXMLHelper.readItems("res/data/items.xml");
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
	}

	public static Item getItem(int i) {
		return itemMap.get(i);
	}
	
}

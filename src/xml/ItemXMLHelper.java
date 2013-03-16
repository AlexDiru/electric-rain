package xml;

import items.Item;

import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public abstract class ItemXMLHelper extends XMLHelper {
	public static HashMap<Integer, Item> readItems(String xmlFile) throws ParserConfigurationException, SAXException, IOException {
		HashMap<Integer, Item> itemMap = new HashMap<Integer, Item>();
		
		// Setup XML
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder;
		builder = factory.newDocumentBuilder();
		Document doc = builder.parse(xmlFile);

		// Get nodes
		NodeList nodes = doc.getElementsByTagName("item");
		
		// Iterate entities
		for (int i = 0; i < nodes.getLength(); i++) {
			
			//Entity fields
			int id = -1;
			String name = "";
			String desc = "";
			String image = "";
			
			NodeList children = nodes.item(i).getChildNodes();
			
			for (int j = 0; j < children.getLength(); j++) {
				
				String nodeText = children.item(j).getTextContent();
				switch (children.item(j).getNodeName()) {
				case "id":
					id = parseInt(nodeText);
					break;
				case "name":
					name = nodeText;
					break;
				case "desc":
					desc = nodeText;
					break;
				case "image":
					image = nodeText;
					break;
				}
			}
			//Create attack
			try {
				itemMap.put(id, new Item(id, name, desc, image));
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Duplicate error");
			}
		}
		return itemMap;
	}
}
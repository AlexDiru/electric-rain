package xml;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


import org.newdawn.slick.SlickException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import entity.Entity;

import statistics.EntityStatistics;


/**
 * Reads entities from an XML file
 * 
 * @author Alex
 * 
 */
public abstract class EntityXMLHelper extends XMLHelper {

	/**
	 * Reads the entities from the XML file and handles exceptions
	 * @param xmlFile The XML file to read
	 * @return A list of the entities found
	 */
	public static Entity[] readEntities(String xmlFile) {
		try {
			return readEntitiesThrowsGarbage(xmlFile);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Reads the entities from the XML file
	 * @param xmlFile The XML file to read
	 * @return A list of the entities found
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	private static Entity[] readEntitiesThrowsGarbage(String xmlFile) throws ParserConfigurationException, SAXException, IOException {
		// Setup XML
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder;
		builder = factory.newDocumentBuilder();
		Document doc = builder.parse(xmlFile);

		// Get nodes
		NodeList nodes = doc.getElementsByTagName("entity");

		// Setup return val
		Entity[] entities = new Entity[nodes.getLength()];

		// Iterate entities
		for (int i = 0; i < nodes.getLength(); i++) {
			
			//Entity fields
			String name = "NONAME";
			String imageFile = "NOFILE";
			int level = -1;
			int currentHealth = -1;
			int maxHealth = -1;
			int currentSpirit = -1;
			int maxSpirit = -1;
			int attack1 = -1;
			int attack2 = -1;
			int attack3 = -1;
			int defense1 = -1;
			int defense2 = -1;
			int defense3 = -1;
			int experience = -1;
			int speed = -1;
			int id = -1;
			int[] availableAttacks = null;
			int framenumber = 1;
			
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
				case "image":
					imageFile = nodeText;
					break;
				case "availableAttacks":
					availableAttacks = parseCSVIntString(nodeText);
					break;
				case "frames":
					framenumber = parseInt(nodeText);
					break;
				case "stats":
					NodeList statsChildren = children.item(j).getChildNodes();
					for (int k = 0; k < statsChildren.getLength(); k++) {
						nodeText = statsChildren.item(k).getTextContent();
						switch (statsChildren.item(k).getNodeName()) {
						case "level":
							level = parseInt(nodeText);
							break;
						case "currentHealth":
							currentHealth = parseInt(nodeText);
							break;
						case "maxHealth":
							maxHealth = parseInt(nodeText);
							break;
						case "currentSpirit":
							currentSpirit = parseInt(nodeText);
							break;
						case "maxSpirit":
							maxSpirit = parseInt(nodeText);
							break;
						case "physicalAttack":
							attack1 = parseInt(nodeText);
							break;
						case "weaponAttack":
							attack2 = parseInt(nodeText);
							break;
						case "spiritualAttack":
							attack3 = parseInt(nodeText);
							break;
						case "physicalDefense":
							defense1 = parseInt(nodeText);
							break;
						case "weaponDefense":
							defense2 = parseInt(nodeText);
							break;
						case "spiritualDefense":
							defense3 = parseInt(nodeText);
							break;
						case "experience":
							experience = parseInt(nodeText);
							break;
						case "speed":
							speed = parseInt(nodeText);
							break;
						}
					}
					break;
				}
			}
			//Create entity
			try {
				entities[i] = new Entity(id, imageFile, 32, 32, name, new EntityStatistics(attack1, attack2, attack3, defense1, defense2, defense3, speed, currentHealth, maxHealth, currentSpirit, maxSpirit, level, experience), availableAttacks, framenumber);
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}

		return entities;
	}
}

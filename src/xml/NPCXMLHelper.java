package xml;

import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import logic.Player.Direction;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import entity.NPC;
import entity.NPC.MovementType;


public abstract class NPCXMLHelper extends XMLHelper {

	
	public static HashMap<Integer, NPC> readNPCs(String xmlFile) throws ParserConfigurationException, SAXException, IOException {
		HashMap<Integer, NPC> npcMap = new HashMap<Integer, NPC>();
		
		// Setup XML
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder;
		builder = factory.newDocumentBuilder();
		Document doc = builder.parse(xmlFile);

		// Get nodes
		NodeList nodes = doc.getElementsByTagName("npc");
		
		// Iterate entities
		for (int i = 0; i < nodes.getLength(); i++) {
			
			//Entity fields
			int id = -1;
			String name = "";
			String dialog = "";
			int framenumber = -1;
			String imageFile = "";
			MovementType movementType = MovementType.DYNAMIC;
			Direction initialDirection = Direction.DOWN;
			
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
				case "dialog":
					dialog = nodeText;
					break;
				case "frames":
					framenumber = parseInt(nodeText);
					break;
				case "image":
					imageFile = nodeText;
					break;
				case "movementType":
					movementType = MovementType.valueOf(nodeText);
					break;
				case "initialDirection":
					initialDirection = Direction.valueOf(nodeText);
					break;
				}
			}
			//Create attack
			try {
				npcMap.put(id, new NPC(imageFile, name, dialog, framenumber, 50, 50, movementType, initialDirection));
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Duplicate error");
			}
		}
		return npcMap;
	}
}

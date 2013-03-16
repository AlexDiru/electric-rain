package xml;

import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import statistics.Attack;
import statistics.Attack.AttackTarget;
import statistics.Attack.AttackType;
import statistics.Attack.AttackUsage;

public abstract class AttackXMLHelper extends XMLHelper {
	/**
	 * Reads the attacks from the xml file
	 * @param xmlFile The XML file to read the attacks from
	 * @return The ID of an attack mapped to the attack itself
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static HashMap<Integer, Attack> readAttacks(String xmlFile) throws ParserConfigurationException, SAXException, IOException {
		HashMap<Integer, Attack> attackMap = new HashMap<Integer, Attack>();
		
		// Setup XML
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder;
		builder = factory.newDocumentBuilder();
		Document doc = builder.parse(xmlFile);

		// Get nodes
		NodeList nodes = doc.getElementsByTagName("attack");
		
		// Iterate entities
		for (int i = 0; i < nodes.getLength(); i++) {
			
			//Entity fields
			int id = -1;
			String name = "";
			AttackType type = AttackType.Physical;
			AttackUsage usage = AttackUsage.Buff;
			AttackTarget target = AttackTarget.Singular;
			int basedamage = -1;
			int basehealing = -1;
			
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
				case "type":
					type = AttackType.valueOf(nodeText);
					break;
				case "usage":
					usage = AttackUsage.valueOf(nodeText);
					break;
				case "target":
					target = AttackTarget.valueOf(nodeText);
					break;
				case "basedamage":
					basedamage = parseInt(nodeText);
					break;
				case "basehealing":
					basehealing = parseInt(nodeText);
					break;
				}
			}
			//Create attack
			try {
				attackMap.put(id, new Attack(name, basedamage, basehealing, type, target, usage));
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Duplicate error");
			}
		}

		return attackMap;
	}
}

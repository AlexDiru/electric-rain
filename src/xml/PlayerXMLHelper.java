package xml;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import logic.Player;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public abstract class PlayerXMLHelper extends XMLHelper {
	public static void readFromXml(Player player, String xmlFile) throws SAXException, IOException, ParserConfigurationException {
		// Setup XML
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder;
		builder = factory.newDocumentBuilder();
		Document doc = builder.parse(xmlFile);

		// Get nodes
		NodeList nodes = doc.getElementsByTagName("save");

		for (int i = 0; i < nodes.getLength(); i++) {
			NodeList children = nodes.item(i).getChildNodes();
			for (int j = 0; j < children.getLength(); j++) {
				String nodeText = children.item(j).getTextContent();
				switch (children.item(j).getNodeName()) {
				case "charactersInParty":
					player.setEntityIdsInParty(parseCSVIntString(nodeText));
					break;
				case "charactersUnlocked":
					player.setUnlockedEntities(parseCSVIntString(nodeText));
					break;
				}
			}
		}
	}
}

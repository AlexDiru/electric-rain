package managers;

import java.util.HashMap;

import logic.Map;

import entity.NPC;

import xml.NPCXMLHelper;

public class NPCManager {

	public static HashMap<Integer, NPC> npcMap = new HashMap<Integer, NPC>();
	
	public static void initialise() {
		try {
			npcMap = NPCXMLHelper.readNPCs("res/data/npcs.xml");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static NPC getNPC(int id, Map map) {
		NPC npc = npcMap.get(id);
		npc.setMap(map);
		return npc;
	}
	
	public static void clear() {
		npcMap.clear();
	}
}

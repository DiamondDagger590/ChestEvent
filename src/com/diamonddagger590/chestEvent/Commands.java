package com.diamonddagger590.chestEvent;

import java.awt.List;
import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Commands {
	
	public boolean registerChest;
	public static ArrayList<String> usernames = new ArrayList<String>();
	
	public boolean registerChest(Player sender){
		
		String username = sender.getName().toString();
		usernames.add(username);
		
		return true;
	}
}

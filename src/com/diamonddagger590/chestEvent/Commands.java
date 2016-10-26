package com.diamonddagger590.chestEvent;

import java.awt.List;
import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Commands {
	
	public boolean registerChest;
	public static ArrayList<String> chestRegister = new ArrayList<String>();
	public static ArrayList<String> chestUnregister = new ArrayList<String>();
	
	public static boolean registerChest(Player sender){
		//command format: /ce register {Name}
		//allows player to register chest
		String username = sender.getName().toString();
		chestRegister.add(username);
		
		//write to file the name of the chest.
		
		
		return true;
	}
	public static boolean unregisterChest(Player sender){
		String username = sender.getName().toString();
		chestUnregister.add(username);
		
		return true;
	}
}

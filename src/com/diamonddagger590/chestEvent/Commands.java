package com.diamonddagger590.chestEvent;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class Commands {
	
	public static boolean registerChest(String name, Location chestLocation, int cooldown, String itemset){
		//get the chests location
		int x = chestLocation.getBlockX();
		int z = chestLocation.getBlockZ();
		int y = chestLocation.getBlockY();
		String world = chestLocation.getWorld().getName();
		//register each variable
		Main.listHandler.getChestLocation().set("Locations." + name +".x", x);
		Main.listHandler.getChestLocation().set("Locations." + name +".z", z);
		Main.listHandler.getChestLocation().set("Locations." + name +".y", y);
		Main.listHandler.getChestLocation().set("Locations." + name +".world", world);
		Main.listHandler.getChestLocation().set("Locations." + name + ".cooldown" , cooldown);
		Main.listHandler.getChestLocation().set("Locations." + name + ".ItemSet", itemset);
		Main.listHandler.saveChestLocation();
		return true;
	}
	public static boolean unregisterChest(String name){
		//delete the chest
		Main.listHandler.getChestLocation().set("Locations." + name, null);
		Main.listHandler.saveChestLocation();
		return true;
	}
	public static void teleportToCrate(Player sender, String name){
		if(Main.listHandler.getChestLocation().contains("Locations." + name)){
			//teleport to the crate
			int x = Main.listHandler.getChestLocation().getInt("Locations." + name + ".x");
			int y = Main.listHandler.getChestLocation().getInt("Locations." + name + ".y");
			int z = Main.listHandler.getChestLocation().getInt("Locations." + name + ".z");
			World w = Bukkit.getWorld(Main.listHandler.getChestLocation().getString("Locations." + name + ".world"));
			Location loc1 = new Location(w, x, y + 1, z);
			Location loc2 = new Location(w, x, y + 2, z);
			if(loc1.getBlock()==null && loc2.getBlock()==null){
				sender.sendMessage(Main.color("&bTeleporting you to " + name));
				sender.teleport(loc1);
			}
			//if there are blocks above the chest
			else{
				sender.sendMessage(Main.color("&cThere are blocks above the chest. A better teleportation system will be implemented later"));
			}
		}
		//error msg
		else{
			sender.sendMessage(Main.color("&3The chest " + name + " is not registered. If you believe this to be an error please contact an admin or dev"));
		}
	}

}

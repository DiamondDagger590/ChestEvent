package com.diamonddagger590.chestEvent;

import java.awt.List;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class Commands {
	
	public static boolean registerChest(String name, Location chestLocation, int cooldown, String itemset){
		//command format: /ce register {Name}
		int x = chestLocation.getBlockX();
		int z = chestLocation.getBlockZ();
		int y = chestLocation.getBlockY();
		String world = chestLocation.getWorld().getName();
		
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
		Main.listHandler.getChestLocation().set("Locations." + name, null);
		Main.listHandler.saveChestLocation();
		return true;
	}
	public static void teleportToCrate(Player sender, String name){
		if(Main.listHandler.getChestLocation().contains(name)){
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
			else{
				sender.sendMessage(Main.color("&cThere are blocks above the chest. A better teleportation system will be implemented later"));
			}
		}
		else{
			sender.sendMessage(Main.color("&3The chest " + name + " is not registered. If you believe this to be an error please contact an admin or dev"));
		}
	}

}

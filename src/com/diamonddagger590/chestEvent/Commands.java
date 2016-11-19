package com.diamonddagger590.chestEvent;

import java.util.Calendar;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import com.diamonddagger590.chestEvent.Main;


public class Commands {
	
	public static void registerChest(String name, Location chestLocation, int cooldown, String itemset){
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
		return;
	}
	public static void unregisterChest(String name){
		//delete the chest
		Main.listHandler.getChestLocation().set("Locations." + name, null);
		Main.listHandler.saveChestLocation();
		return;
	}
	public static void listCrates(Player sender){
		if(Main.listHandler.getChestLocation().getConfigurationSection("Locations").getKeys(false) == null){
			sender.sendMessage(Main.color("&cThere are no chests registered"));
			return;
		}
		else{
			for(String name: Main.listHandler.getChestLocation().getConfigurationSection("Locations").getKeys(false)){
				sender.sendMessage(Main.color("&3&lThe chests you are allowed to open are:"));
				if(sender.hasPermission("ce.chestopen.*") || sender.hasPermission("ce.*") || sender.hasPermission("ce.chestopen." + name)){
					sender.sendMessage(Main.color("&e----------------------------"));
					sender.sendMessage(Main.color("&3    " + name));
				}
			}
			return;
		}
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
			if(loc1.getBlock().getType()==Material.AIR && loc2.getBlock().getType()==Material.AIR){
				sender.sendMessage(Main.color("&bTeleporting you to " + name));
				sender.teleport(loc1);
				return;
			}
			loc1 = new Location(w, x + 1, y, z);
			loc2 = new Location(w, x + 1, y + 1, z);
			if(loc1.getBlock().getType()==Material.AIR && loc2.getBlock().getType()==Material.AIR){
				sender.sendMessage(Main.color("&bTeleporting you to " + name));
				sender.teleport(loc1);
				return;
			}
			loc1 = new Location(w, x - 1, y, z);
			loc2 = new Location(w, x - 1, y + 1, z);
			if(loc1.getBlock().getType()==Material.AIR && loc2.getBlock().getType()==Material.AIR){
				sender.sendMessage(Main.color("&bTeleporting you to " + name));
				sender.teleport(loc1);
				return;
			}
			loc1 = new Location(w, x, y, z + 1);
			loc2 = new Location(w, x, y + 1, z + 1);
			if(loc1.getBlock().getType()==Material.AIR && loc2.getBlock().getType()==Material.AIR){
				sender.sendMessage(Main.color("&bTeleporting you to " + name));
				sender.teleport(loc1);
				return;
			}
			loc1 = new Location(w, x, y, z - 1);
			loc2 = new Location(w, x, y + 1, z - 1);
			if(loc1.getBlock().getType()==Material.AIR && loc2.getBlock().getType()==Material.AIR){
				sender.sendMessage(Main.color("&bTeleporting you to " + name));
				sender.teleport(loc1);
				return;
			}
			//if there are blocks above the chest
			else{
				sender.sendMessage(Main.color("&cThere are blocks blocking the chest."));
				sender.sendMessage(Main.color("&cHere are the coords."));
				sender.sendMessage(Main.color("&e--------------------------------------"));
				sender.sendMessage(Main.color("&eX: &a" + x));
				sender.sendMessage(Main.color("&eY: &a" + y));
				sender.sendMessage(Main.color("&eZ: &a" + z));
				sender.sendMessage(Main.color("&eWorld: &a" + w));
				sender.sendMessage(Main.color("&e--------------------------------------"));
				return;
			}
		}
		//error msg
		else{
			sender.sendMessage(Main.color("&3The chest " + name + " is not registered. If you believe this to be an error please contact an admin or dev"));
			return;
		}
	}
	@SuppressWarnings("deprecation")
	public static void cooldownSet(Player sender, String target, String cooldown, String chest){
		if(!(sender.hasPermission("ce.cooldowns"))){
			sender.sendMessage(Main.color("&cYou do not have permission to run this command"));
			return;
		}
		else if((Bukkit.getPlayer(target)) == null){
			sender.sendMessage(Main.color("&cThat player doesn't exist or is not online."));
			return;
		}
		Player player = Bukkit.getPlayer(target);
		if(Main.listHandler.getChestLocation().getConfigurationSection("Locations").getKeys(false) == null){
			sender.sendMessage(Main.color("&cThere are no registered chests."));
			return;
		}
		else if(!Main.listHandler.getPlayers().contains("Players." + player.getUniqueId())){
			sender.sendMessage(Main.color("That player is not registered or does not exist"));
			return;
		}
		else if(Main.listHandler.getPlayers().contains("Players." + player.getUniqueId())){
			if(Main.listHandler.getPlayers().contains("Players." + player.getUniqueId() + "." + chest + ".cooldownExpire")){
				if(!Main.isInt(cooldown)){
					sender.sendMessage(Main.color("&cPlease use the format of /ce cooldown <target> <cooldown> <chest>"));
					return;
				}
				else{
					int cool = Integer.parseInt(cooldown);
					Calendar current = Calendar.getInstance();
					Calendar cal = Calendar.getInstance();
					cal.add(Calendar.SECOND, cool);
					int timeLeft = (int) (cal.getTimeInMillis()/1000) - ((int) (current.getTimeInMillis()/1000));
					sender.sendMessage(Main.color("&a" + target + "s cooldown has been set to &e" + timeLeft + "&a from now."));
					Main.listHandler.getPlayers().set("Players." + player.getUniqueId() + "." + chest + ".cooldownExpire", cal.getTimeInMillis());
					Main.listHandler.savePlayers();
					return;
				}
			}
			else{
				sender.sendMessage(Main.color("&e" + chest + "&c is not registered for &e" + target));
				return;
			}
		}
		else{
			sender.sendMessage(Main.color("&cWell... if you made it to this point I really screwed up"));
			return;
		}
		
	}

}

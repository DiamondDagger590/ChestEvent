package com.diamonddagger590.chestEvent;

import java.io.File;
import java.util.HashSet;
import java.util.Random;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.Calendar;

public class Main extends JavaPlugin{
	public Main instance = this;
	public File pluginFolder = instance.getDataFolder();
	public static ListHandler listHandler = ListHandler.getInstance();
	@Override
	//when server boots up
	public void onEnable(){
		//setup list handler class
		listHandler.setup(this);
		PluginDescriptionFile pdfFile = getDescription();
		//standard logging
		Logger logger = Logger.getLogger("Minecraft");
		logger.info(pdfFile.getName() + " Version " + pdfFile.getVersion() + " has been enabled.");
		Bukkit.getServer().getPluginManager().registerEvents(new playerInteractEvent(), this);

	}
	
	public void onDisable(){
		//standard logging
		PluginDescriptionFile pdfFile = getDescription();
		Logger logger = Logger.getLogger("Minecraft");
		logger.info(pdfFile.getName() + " Version " + pdfFile.getVersion() + " has been disabled.");
	}
	//method to check if a string is an integer
	boolean isInt(String s) {
		  try {
		    Integer.parseInt(s);
		  } catch (NumberFormatException nfe) {
		    return false;
		  }
		  return true;
		}
	//method to return a random number
	public static int randomNumber (int random){
		Random ran = new Random();
		int itemNumber = ran.nextInt(random) + 1;
		return itemNumber;
	}
	//method for cleaner message code
	public static String color(String msg){
		  return ChatColor.translateAlternateColorCodes('&', msg);
		}
	//A calendar conversion method
	public static int convertToTime(long time){
		Calendar currentTime = Calendar.getInstance();
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		int total = ((int) (cal.getTimeInMillis()/1000)-(int) (currentTime.getTimeInMillis()/1000));
		return total;
	}
@SuppressWarnings("deprecation")
public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
	switch (cmd.getName()){
	//if user types /ce
		case "ce":
			//if user types 
			if (args.length >= 1){
				//set sub command to all lowercase
				String arg0 = args[0].toLowerCase();
					switch(arg0){
					//if user types /ce reload...
						case "reload":
							//if user doesnt have permission ce.reload, send them a message
							if(!sender.hasPermission("ce.reload")){
								sender.sendMessage(Main.color("&cYou do not have permissions to run that command"));
								return true;
							}
							//else reload all files and send a message
							else{
								Main.listHandler.reloadChestLocation();
								Main.listHandler.reloadItems();
								Main.listHandler.reloadPlayers();
								sender.sendMessage(Main.color("&aAll ChestEvent files are now reloaded!"));
								return true;
							}
						//if user types /ce register...
						case "register":
						//if the user doesn't have permission ce.register
							if(!sender.hasPermission("ce.register")){
								sender.sendMessage(Main.color("&cYou do not have permission to run that command"));
								return true;
							}
							//else
							else{
								//if there are less than 3 arguments, send a prompt message with correct format
								if (args.length < 3){
									sender.sendMessage(Main.color("&cPlease use the format /ce register <name> <cooldown> <ItemSet>"));
									sender.sendMessage(Main.color("&cPlease register an item set before a chest to avoid errors!"));
									return true;
								}
							
								//get the block the player is looking at
								Block b = ((Player) sender).getTargetBlock((HashSet<Byte>)null, 7);
								//if the second argument isn't an int
								if(!isInt(args[2])){
									sender.sendMessage(Main.color("&cPlease use the format of &4/ce register <name> <cooldown> <ItemSet> &cwhere cooldown is an int."));
									return true;
								}
								//if there is no block
								if(b.isEmpty()){
									sender.sendMessage(Main.color("&cYou need to be looking at a block at most 7 blocks away"));
									return true;
								}
								//if the user isnt looking at a chest
								if(b.getType() != Material.CHEST){
									sender.sendMessage(Main.color("&cYou need to be looking at a chest"));
									return true;
								}
								//if the chest name has already been used
								if(Main.listHandler.getChestLocation().contains("Locations." + args[1])){
									sender.sendMessage(Main.color("&cThe name " + Main.color("&e" + args[1]) + Main.color(" &chas already been used. Please use a different name")));
									return true;
								}
								//if a chest has already been registered at that location
								for(String name : Main.listHandler.getChestLocation().getConfigurationSection("Locations").getKeys(false)){
									//obtain the x, y, z, and world for each chest
									int x = Main.listHandler.getChestLocation().getInt("Locations." + name + ".x");
									int y = Main.listHandler.getChestLocation().getInt("Locations." + name + ".y");
									int z = Main.listHandler.getChestLocation().getInt("Locations." + name + ".z");
									World w = Bukkit.getWorld(Main.listHandler.getChestLocation().getString("Locations." + name + ".world"));
									//if chest is in the file send a message
									if(b.getLocation().equals(new Location(w, x, y, z))){
										sender.sendMessage(Main.color("&cA chest has already been registered at that location, please try elsewhere."));
										return true;
									}
								}
								//register the chest
								Commands.registerChest(args[1], b.getLocation(), Integer.parseInt(args[2]), args[3]);
								//tell the user the chest was registered
								sender.sendMessage(Main.color("&aCongrats, &e" + args[1] + " &ahas been registered"));
								return true;
							}
							//TODO 
							//if user types /ce items
						case "items":
							//if user doesn't have permission ce.items...
							if(!sender.hasPermission("ce.items")){
								sender.sendMessage(Main.color("&cYou do not have permission to run that command"));
							}
							//TODO

							//if(args.length < 3){
								//sender.sendMessage(Main.color("&cPlease use the format of /ce items register <ItemSetName>"));
								//return true;
							//}
							else{
								//create set with the itemset name as the itemset name... Kinda redundant eh?
								InventoryManager.createItemSet((Player) sender, args[1]);
								return true;
							}
						//if user types /ce unregister
						case "unregister":
							//if user doesn't have the permission ce.unregister...
							if(!sender.hasPermission("ce.unregister")){
								sender.sendMessage(Main.color("&cYou do not have permission to run that command"));
								return true;
							}
							else{

								Commands.unregisterChest(args[1]);
								sender.sendMessage(Main.color("&aCongrats, &e" + args[1] + " &ahas been unregistered"));
								return true;
							}
						//if user types /ce teleport...
						case "teleport":
							//if user doesn't have permission ce.teleport
							if(!sender.hasPermission("ce.teleport")){
								sender.sendMessage(Main.color("&cYou do not have permission to run that command"));
								return true;
							}
							
							else{
								//CASE SENSITIVE
							Commands.teleportToCrate((Player) sender, args[1]);
							return true;
							}
						//if user types /ce help
						case "help":
							//if user doesn't have permission ce.help...
							if(!sender.hasPermission("ce.help")){
								sender.sendMessage(Main.color("&cYou do not have permission to run that command"));
								return true;
							}
							//send user the help guide
							else{
								sender.sendMessage(Main.color("&3Register Command: /ce register <name> <cooldown> <ItemSet>"));
								sender.sendMessage(Main.color("&3    -Registers the chest you are looking at with the name, cooldown, and itemset provided"));
								sender.sendMessage(Main.color("&3Unregister Command: /ce unregister <name>"));
								sender.sendMessage(Main.color("&3    -Unregisters the specified chest"));
								sender.sendMessage(Main.color("&3Register Items: /ce <setName>"));
								sender.sendMessage(Main.color("&3    -Register items in your inventory as a set specified as the name provided"));
								sender.sendMessage(Main.color("&3Teleport: /ce teleport <chestName>"));
								sender.sendMessage(Main.color("&3    -Teleports you to the specified chest, names ARE case sensitive"));
								sender.sendMessage(Main.color("&3Reload: /ce reload"));
								sender.sendMessage(Main.color("&3    -Reloads all files for CE"));
							}
					}
				}
			else{
				//prompt the user
				sender.sendMessage("&3Do /ce help for commands");
				return true;
			}
		}
		return false;
	}


}
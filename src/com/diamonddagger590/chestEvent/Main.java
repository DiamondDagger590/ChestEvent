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
		Bukkit.getServer().getPluginManager().registerEvents(new InventoryEvents(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new InventoryMoveEvents(), this);

	}
	
	public void onDisable(){
		//standard logging
		PluginDescriptionFile pdfFile = getDescription();
		Logger logger = Logger.getLogger("Minecraft");
		logger.info(pdfFile.getName() + " Version " + pdfFile.getVersion() + " has been disabled.");
	}
	//method to check if a string is an integer
	static boolean isInt(String s) {
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
								sender.sendMessage(Main.color(Main.listHandler.getConfig().getString("PluginPrefix") + Main.listHandler.getConfig().getString("Messages.NoPerms")));
								return true;
							}
							//else reload all files and send a message
							else{
								Main.listHandler.reloadChestLocation();
								Main.listHandler.reloadItems();
								Main.listHandler.reloadPlayers();
								sender.sendMessage(Main.color(Main.listHandler.getConfig().getString("PluginPrefix") + Main.listHandler.getConfig().getString("Messages.Reload")));
								return true;
							}
						//if user types /ce register...
						case "register":
							if(!(sender instanceof Player)){
								sender.sendMessage(Main.color(Main.listHandler.getConfig().getString("PluginPrefix") + Main.listHandler.getConfig().getString("Messages.OnlyPlayer")));
								return true;
							}
							if((sender.hasPermission("ce.register") || sender.hasPermission("ce.*"))){
								//if there are less than 3 arguments, send a prompt message with correct format
								if (args.length < 3){
									sender.sendMessage(Main.color(Main.listHandler.getConfig().getString("PluginPrefix") + Main.listHandler.getConfig().getString("Messages.RegisterPrompt1")));
									sender.sendMessage(Main.color(Main.listHandler.getConfig().getString("PluginPrefix") + Main.listHandler.getConfig().getString("Messages.RegisterPrompt2")));
									return true;
								}
							
								//get the block the player is looking at
								Block b = ((Player) sender).getTargetBlock((HashSet<Byte>)null, Main.listHandler.getConfig().getInt("Config.MaxRegisterDistance"));
								//if the second argument isn't an int
								if(!isInt(args[2])){
									sender.sendMessage(Main.color(Main.listHandler.getConfig().getString("PluginPrefix") + Main.listHandler.getConfig().getString("Messages.NotAnInt")));
									return true;
								}
								//if there is no block
								if(b.isEmpty()){
									sender.sendMessage(Main.color(Main.listHandler.getConfig().getString("PluginPrefix") + Main.listHandler.getConfig().getString("Messages.TooFar").replaceAll("%Distance%", Main.listHandler.getConfig().getString("Config.MaxRegisterDistance"))));
									return true;
								}
								//if the user isnt looking at a chest
								if(b.getType() != Material.CHEST){
									sender.sendMessage(Main.color(Main.listHandler.getConfig().getString("PluginPrefix") + Main.listHandler.getConfig().getString("Messages.NotAChest")));
									return true;
								}
								//if the chest name has already been used
								if(Main.listHandler.getChestLocation().contains("Locations." + args[1])){
									sender.sendMessage(Main.color(Main.listHandler.getConfig().getString("PluginPrefix") + Main.listHandler.getConfig().getString("Messages.ChestNameAlreadyUsed").replaceAll("%ChestName%", args[1])));
									return true;
								}
								//if a chest has already been registered at that location
								if(Main.listHandler.getChestLocation().getConfigurationSection("Locations").getKeys(false)!= null){
									for(String name : Main.listHandler.getChestLocation().getConfigurationSection("Locations").getKeys(false)){
										//obtain the x, y, z, and world for each chest
										int x = Main.listHandler.getChestLocation().getInt("Locations." + name + ".x");
										int y = Main.listHandler.getChestLocation().getInt("Locations." + name + ".y");
										int z = Main.listHandler.getChestLocation().getInt("Locations." + name + ".z");
										World w = Bukkit.getWorld(Main.listHandler.getChestLocation().getString("Locations." + name + ".world"));
										//if chest is in the file send a message
										if(b.getLocation().equals(new Location(w, x, y, z))){
											sender.sendMessage(Main.color(Main.listHandler.getConfig().getString("PluginPrefix") + Main.listHandler.getConfig().getString("Messages.LocationAlreadyUsed")));
											return true;
										}
									}
								}
								
								//register the chest
								Commands.registerChest(args[1], b.getLocation(), Integer.parseInt(args[2]), args[3]);
								//tell the user the chest was registered
								sender.sendMessage(Main.color(Main.listHandler.getConfig().getString("PluginPrefix") + Main.listHandler.getConfig().getString("Messages.SuccessfulRegister").replaceAll("%ChestName%", args[1])));
								return true;
							}
							else{
								sender.sendMessage(Main.color(Main.listHandler.getConfig().getString("PluginPrefix") + Main.listHandler.getConfig().getString("Messages.NoPerms")));
							}
							//TODO 
							//if user types /ce items
						case "items":
							if(!(sender instanceof Player)){
								sender.sendMessage(Main.color(Main.listHandler.getConfig().getString("PluginPrefix") + Main.listHandler.getConfig().getString("Messages.OnlyPlayer")));
								return true;
							}
							if(args[1].equalsIgnoreCase("register")){
								if(sender.hasPermission("ce.items.register") || sender.hasPermission("ce.items.*") || sender.hasPermission("ce.*"))
								//create set with the itemset name as the itemset name... Kinda redundant eh?
									if(args.length < 3){
										sender.sendMessage(Main.color(Main.listHandler.getConfig().getString("PluginPrefix") + Main.listHandler.getConfig().getString("Messages.ItemsPrompt1")));
										sender.sendMessage(Main.color(Main.listHandler.getConfig().getString("PluginPrefix") + Main.listHandler.getConfig().getString("Messages.ItemPrompt2")));
										return true;
									}
									else{
										InventoryManager.createItemSet((Player) sender, args[2]);
										return true;
									}
								else{
									sender.sendMessage(Main.color(Main.listHandler.getConfig().getString("PluginPrefix") + Main.listHandler.getConfig().getString("Messages.NoPerms")));
									return true;
								}
							}
							if(args[1].equalsIgnoreCase("unregister")){
								if(sender.hasPermission("ce.items.unregister") || sender.hasPermission("ce.items.*") || sender.hasPermission("ce.*")){
									if(args.length < 3){
										sender.sendMessage(Main.color(Main.listHandler.getConfig().getString("PluginPrefix") + Main.listHandler.getConfig().getString("Messages.ItemsPrompt1")));
										sender.sendMessage(Main.color(Main.listHandler.getConfig().getString("PluginPrefix") + Main.listHandler.getConfig().getString("Messages.ItemPrompt2")));
										return true;
									}
									else{
										Main.listHandler.getItems().set("Items." + args[2], null);
										sender.sendMessage(Main.color(Main.listHandler.getConfig().getString("PluginPrefix") + Main.listHandler.getConfig().getString("Messages.SuccessfulItemRegister")));
										Main.listHandler.saveItems();
										return true;
									}
								}
							
							}
							if(args[1].equalsIgnoreCase("view")){
								if(sender.hasPermission("ce.items.view") || sender.hasPermission("ce.items.*") || sender.hasPermission("ce.*")){
									if(args.length < 3){
										sender.sendMessage(Main.color(Main.listHandler.getConfig().getString("PluginPrefix") + Main.listHandler.getConfig().getString("Messages.ItemsPrompt1")));
										sender.sendMessage(Main.color(Main.listHandler.getConfig().getString("PluginPrefix") + Main.listHandler.getConfig().getString("Main.ItemPrompt2")));
										return true;
									}
									else{
										InventoryManager.viewItemSet((Player) sender, args[2]);
										return true;
									}
								}
								else{
									sender.sendMessage(Main.color(Main.listHandler.getConfig().getString("PluginPrefix") + Main.listHandler.getConfig().getString("Messages.NoPerms")));
								}
							}
							if(args[1].equalsIgnoreCase("edit")){
								if(sender.hasPermission("ce.items.view") || sender.hasPermission("ce.items.*") || sender.hasPermission("ce.*")){
									if(args.length < 3){
										sender.sendMessage(Main.color(Main.listHandler.getConfig().getString("PluginPrefix") + Main.listHandler.getConfig().getString("Messages.ItemsPrompt1")));
										sender.sendMessage(Main.color(Main.listHandler.getConfig().getString("PluginPrefix") + Main.listHandler.getConfig().getString("Main.ItemPrompt2")));
										return true;
									}
									else{
										InventoryManager.editItemSet((Player) sender, args[2]);
										return true;
									}
								}
							}
							
						//if user types /ce unregister
						case "unregister":
							//if user doesn't have the permission ce.unregister...
							if(sender.hasPermission("ce.unregister") || sender.hasPermission("ce.*")){
								if(!(Main.listHandler.getChestLocation().contains("Locations." + args[1]))){
									sender.sendMessage(Main.color(Main.listHandler.getConfig().getString("PluginPrefix") + Main.listHandler.getConfig().getString("Messages.NotRegisteredChest")));
									return true;
								}
								else{
									Commands.unregisterChest(args[1]);
									sender.sendMessage(Main.color("&7[&1ChestEvent&7]&6>>&aCongrats, &e" + args[1] + " &ahas been unregistered"));
									return true;
								}
							}
							else{
								sender.sendMessage(Main.color(Main.listHandler.getConfig().getString("PluginPrefix") + Main.listHandler.getConfig().getString("Messages.NoPerms")));
								return true;
								
							}
						//if user types /ce teleport...
						case "teleport":
							if(!(sender instanceof Player)){
								sender.sendMessage(Main.color(Main.listHandler.getConfig().getString("PluginPrefix") + Main.listHandler.getConfig().getString("Messages.OnlyPlayer")));
								return true;
							}
							//if user doesn't have permission ce.teleport

							else if(sender.hasPermission("ce.teleport.*") || sender.hasPermission("ce.teleport." + args[1]) || sender.hasPermission("ce.*")){
								//CASE SENSITIVE
							Commands.teleportToCrate((Player) sender, args[1]);
							return true;
							}
							else{
								sender.sendMessage(Main.color(Main.listHandler.getConfig().getString("PluginPrefix") + Main.listHandler.getConfig().getString("Messages.NoPerms")));
								return true;
							}
							
						//if user types /ce help
						case "help":
							//if user has permission ce.help...
							if(sender.hasPermission("ce.help") || sender.hasPermission("ce.*")){
								if(args.length == 1){
									sender.sendMessage(Main.color("&e--------------------------"));
									sender.sendMessage(Main.color("&7[&6Register Command&7]&3 /ce register <name> <cooldown> <ItemSet>"));
									sender.sendMessage(Main.color("&3    -Registers the chest you are looking at with the name,   cooldown, and itemset provided"));
									sender.sendMessage(Main.color("&7[&6Unregister Command&7]&3 /ce unregister <name>"));
									sender.sendMessage(Main.color("&3    -Unregisters the specified chest"));
									sender.sendMessage(Main.color("&7[&6Teleport&7]&3 /ce teleport <chestName>"));
									sender.sendMessage(Main.color("&3    -Teleports you to the specified chest, names ARE case   sensitive"));
									sender.sendMessage(Main.color("&7[&6Reload Command&7]&3 /ce reload"));
									sender.sendMessage(Main.color("&3    -Reloads all files related to CE"));
									sender.sendMessage(Main.color("&e/ce help 2 for more"));
									sender.sendMessage(Main.color("&e---------------------------"));
									return true;
								}
								
								if(args[1].equalsIgnoreCase("2")){
									sender.sendMessage(Main.color("&e---------------------------"));
									sender.sendMessage(Main.color("&7[&6Register Items&7]&3 /ce items register <setName>"));
									sender.sendMessage(Main.color("&3    -Creates a GUI for item registration"));
									sender.sendMessage(Main.color("&7[&6Unregister Items&7]&3 /ce items unregister <setName>"));
									sender.sendMessage(Main.color("&3    -Removes the specified itemset"));
									sender.sendMessage(Main.color("&7[&6View Items&7]&3 /ce items view <setName>"));
									sender.sendMessage(Main.color("&3    -Creates a GUI that shows all items in the itemset"));
									sender.sendMessage(Main.color("&7[&6Register Items&7]&3 /ce items edit <setName>"));
									sender.sendMessage(Main.color("&3    -Creates a GUI that allows you to edit the set"));
									sender.sendMessage(Main.color("&7[&6List locations&7]&3 /ce list"));
									sender.sendMessage(Main.color("&3    -Lists all locations you are allowed to open"));
									sender.sendMessage(Main.color("&7[&6Cooldown&7]&3 /ce cooldown <target> <cooldown> <chest>"));
									sender.sendMessage(Main.color("&3    -Change the targets cooldown for specified chest by the amount inputed in seconds from now"));
									sender.sendMessage(Main.color("&e---------------------------"));
									return true;
								

							}
							}
							else{
								sender.sendMessage(Main.color(Main.listHandler.getConfig().getString("PluginPrefix") + Main.listHandler.getConfig().getString("Messages.NoPerms")));
								return true;

							}
						case "list":
							Commands.listCrates((Player) sender);
							return true;
						case "cooldown":
							if(!(sender instanceof Player)){
								sender.sendMessage(Main.color(Main.listHandler.getConfig().getString("PluginPrefix") + Main.listHandler.getConfig().getString("Messages.OnlyPlayer")));
								return true;
							}
							else{
								Commands.cooldownSet((Player) sender, args[1], args[2], args[3]);
								return true;
							}
							
					}
				}
			else{
				//prompt the user
				sender.sendMessage(Main.color("&7[&1ChestEvent&7]&6>>&3Do /ce help for commands"));
				return true;
			}
		}
		return false;
	}


}
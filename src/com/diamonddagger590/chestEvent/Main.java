package com.diamonddagger590.chestEvent;

import java.io.File;
import java.util.HashSet;
import java.util.List;
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
	public void onEnable(){
		listHandler.setup(this);
		PluginDescriptionFile pdfFile = getDescription();
		Logger logger = Logger.getLogger("Minecraft");
		logger.info(pdfFile.getName() + " Version " + pdfFile.getVersion() + " has been enabled.");
		Bukkit.getServer().getPluginManager().registerEvents(new playerInteractEvent(), this);

	}
	
	public void onDisable(){
		PluginDescriptionFile pdfFile = getDescription();
		Logger logger = Logger.getLogger("Minecraft");
		logger.info(pdfFile.getName() + " Version " + pdfFile.getVersion() + " has been disabled.");
	}
	boolean isInt(String s) {
		  try {
		    Integer.parseInt(s);
		  } catch (NumberFormatException nfe) {
		    return false;
		  }
		  return true;
		}
	public static int randomNumber (int random){
		Random ran = new Random();
		int itemNumber = ran.nextInt(random) + 1;
		return itemNumber;
	}
	public static String color(String msg){
		  return ChatColor.translateAlternateColorCodes('&', msg);
		}
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
		case "ce":
				
			if (args.length >= 1){
					switch(args[0]){
						case "reload":
							if(sender.hasPermission("ce.reload")){
								Main.listHandler.reloadChestLocation();
								Main.listHandler.reloadItems();
								Main.listHandler.reloadPlayers();
								sender.sendMessage(Main.color("&aAll ChestEvent files are now reloaded!"));
								return true;
							}
							else{
								sender.sendMessage(Main.color("&cYou do not have permissions to run this command"));
								return true;
							}
						case "register":
						//if the user typed an incomplete command
							if(sender.hasPermission("ce.register")){
								if (args.length < 3){
									sender.sendMessage(Main.color("&4Please use the format /ce register <name> <cooldown>"));
									return true;
								}
							
								//get the block the player is looking at
								Block b = ((Player) sender).getTargetBlock((HashSet<Byte>)null, 7);
								//if the second argument doesnt exist or if the third argument is not an integer/doesnt exist
								if(!isInt(args[2])){
									sender.sendMessage(ChatColor.RED + "Please use the format of " + ChatColor.AQUA + "/ce register <name> <cooldown>" + ChatColor.RED +  "where cooldown is an int.");
									return true;
								}
								//if there is no block
								if(b.isEmpty()){
									sender.sendMessage(ChatColor.RED + "You need to be looking at a block at most 7 blocks away");
									return true;
								}
								//if the user isnt looking at a chest
								if(b.getType() != Material.CHEST){
									sender.sendMessage(ChatColor.RED + "You need to be looking at a chest");
									return true;
								}
								//if the chest name has already been used
								if(Main.listHandler.getChestLocation().contains("Locations." + args[1])){
									sender.sendMessage(Main.color("&4The name " + Main.color("&e" + args[1]) + Main.color(" &4has already been used. Please use a different name")));
									return true;
								}
								//if a chest has already been registered at that location
								for(String name : Main.listHandler.getChestLocation().getConfigurationSection("Locations").getKeys(false)){
									//obtain the x, y, z, and world for each chest
									int x = Main.listHandler.getChestLocation().getInt("Locations." + name + ".x");
									int y = Main.listHandler.getChestLocation().getInt("Locations." + name + ".y");
									int z = Main.listHandler.getChestLocation().getInt("Locations." + name + ".z");
									World w = Bukkit.getWorld(Main.listHandler.getChestLocation().getString("Locations." + name + ".world"));
									if(b.getLocation().equals(new Location(w, x, y, z))){
										sender.sendMessage(Main.color("&7A chest has already been registered at that location, please try elsewhere."));
										return true;
									}
								}
								//register the chest
								Commands.registerChest(args[1], b.getLocation(), Integer.parseInt(args[2]), args[3]);
								//tell the user the chest was registered
								sender.sendMessage(Main.color("&aCongrats, &e" + args[1] + " &ahas been registered"));
								return true;
							}
							else{
								sender.sendMessage(Main.color("&cYou do not have permission to run this command"));
								return true;
							}
						case "items":
							//if(!sender.hasPermission("ce.items")){

								//if(args.length < 3){
									//sender.sendMessage(Main.color("&cPlease use the format of /ce items register <ItemSetName>"));
									//return true;
								//}
								
									InventoryManager.createItemSet((Player) sender, args[1]);
									return true;
								
							//}
							//else{
								//sender.sendMessage(Main.color("&cYou do not have permission to run this command"));
								//return true;
							//}
						case "unregister":
							if(sender.hasPermission("ce.unregister")){
								Commands.unregisterChest(args[1]);
								sender.sendMessage(Main.color("&aCongrats, &e" + args[1] + " &ahas been unregistered"));
								return true;
							}
							else{
								sender.sendMessage(Main.color("&cYou do not have permission to run this command"));
								return true;
							}
						case "teleport":
							Commands.teleportToCrate((Player) sender, args[1]);
							return true;
					}
				}	
		}
		return false;
	}

public static List<String> color(List<String> lore) {
	// TODO Auto-generated method stub
	return null;
}

}
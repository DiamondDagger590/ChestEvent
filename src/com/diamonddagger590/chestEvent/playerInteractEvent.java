package com.diamonddagger590.chestEvent;
import java.util.Calendar;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import com.diamonddagger590.chestEvent.Main;



public class playerInteractEvent implements Listener{
	public static int itemNumber;
	@EventHandler

	public void PlayerInteractEvent(PlayerInteractEvent e){
		//get the users UUID
		String UUID = e.getPlayer().getUniqueId().toString();
		//if the players file doesnt have the UUID registered, register it
		if(!Main.listHandler.getPlayers().contains("Players." + UUID)){
			//register players name as well for ease of admins
			Main.listHandler.getPlayers().set("Players.PlayerName", e.getPlayer().getDisplayName());
			Main.listHandler.getPlayers().set("Players." + UUID + ".Reset", null);
			Main.listHandler.savePlayers();
		}
		//if they dont click anything
		if(e.getClickedBlock() == null){
			return;
		}
		//if the user clicks a chest
		if(e.getClickedBlock().getType() == Material.CHEST){
			//save block to a variable
			Block chest = e.getClickedBlock();
			//for all chests in the chest file
			for(String name : Main.listHandler.getChestLocation().getConfigurationSection("Locations").getKeys(false)){
				//obtain the x, y, z, and world for each chest
				int x = Main.listHandler.getChestLocation().getInt("Locations." + name + ".x");
				int y = Main.listHandler.getChestLocation().getInt("Locations." + name + ".y");
				int z = Main.listHandler.getChestLocation().getInt("Locations." + name + ".z");
				World w = Bukkit.getWorld(Main.listHandler.getChestLocation().getString("Locations." + name + ".world"));
				//get the cooldown
				int cooldown = Main.listHandler.getChestLocation().getInt("Locations." + name+ ".cooldown");
				//save the chest location to a variable
				Location loc = new Location(w, x, y, z);
				//if a chest in the file matches the clicked chest...
				if(chest.getLocation().equals(loc)){
					//register current time
					Calendar cal = Calendar.getInstance();
					//if players file doesnt have the chest registered for that player
					if(!Main.listHandler.getPlayers().contains("Players." + UUID + "." + name)){
						//set the player
						Main.listHandler.getPlayers().set("Players." + UUID + "." + name + ".cooldownExpire", cal.getTimeInMillis()-1);
					}
					//set the cooldownExpire time
					Calendar cooldownExpire = Calendar.getInstance();
					cooldownExpire.setTimeInMillis(Main.listHandler.getPlayers().getLong("Players." + UUID + "." + name + ".cooldownExpire"));
					//if the user's cooldown has expired
					if(cal.after(cooldownExpire)){ 
						//code to get the random item
						String itemSet = Main.listHandler.getChestLocation().getString("Locations." + name + ".ItemSet");
						int totalItems = Main.listHandler.getItems().getInt("Items." + itemSet + ".TotalItems");
						int itemNumber = Main.randomNumber(totalItems);
						ItemStack newItem = Main.listHandler.getItems().getItemStack("Items." + itemSet + ".ItemNumber_" + itemNumber);
						
						//stop player from opening chest
						e.setCancelled(true);
						//send user the inventory
						Inventory inv = Bukkit.createInventory(null, 27, Main.color("&5" + e.getPlayer().getName() + "s Item"));
						inv.setItem(13, newItem);
						e.getPlayer().openInventory(inv);


                        cal.add(Calendar.SECOND, cooldown);
						Main.listHandler.getPlayers().set("Players." + UUID + "." + name + ".cooldownExpire", cal.getTimeInMillis());
						e.getPlayer().sendMessage(Main.color("&aEnjoy your item!"));
						Main.listHandler.savePlayers();
						return;
						
					}
					else{
						//cancel event
						e.setCancelled(true);
						int timeLeft = (int) (cooldownExpire.getTimeInMillis()/1000) - ((int) (cal.getTimeInMillis()/1000));
						e.getPlayer().sendMessage(Main.color("&cYou can not open that chest until &a" + timeLeft + " &cseconds"));
						return;
					}
				}
			}
		}

	}
}
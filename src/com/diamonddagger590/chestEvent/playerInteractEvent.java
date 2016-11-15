package com.diamonddagger590.chestEvent;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.diamonddagger590.chestEvent.Main;



public class playerInteractEvent implements Listener{
	public static int itemNumber;

	@SuppressWarnings({"deprecation", "null" })
	@EventHandler

	public void PlayerInteractEvent(PlayerInteractEvent e){

		String UUID = e.getPlayer().getUniqueId().toString();
		if(!Main.listHandler.getPlayers().contains("Players." + UUID)){
			Main.listHandler.getPlayers().set("Players." + UUID + ".Reset", null);
			Main.listHandler.saveChestLocation();
			Main.listHandler.savePlayers();
		}
		if(e.getClickedBlock() == null){
			return;
		}
		
		if(e.getClickedBlock().getType() == Material.CHEST){
			Block chest = e.getClickedBlock();
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
					Calendar cal = Calendar.getInstance();		
					if(!Main.listHandler.getPlayers().contains("Players." + UUID + "." + name)){
						Main.listHandler.getPlayers().set("Players." + UUID + "." + name + ".cooldownExpire", cal.getTimeInMillis());
					}
					Calendar cooldownExpire = Calendar.getInstance();
					cooldownExpire.setTimeInMillis(Main.listHandler.getPlayers().getLong("Players." + UUID + "." + name + ".cooldownExpire"));
					if(cal.after(cooldownExpire)){
						String itemSet = Main.listHandler.getChestLocation().getString("Locations." + name + ".ItemSet");
						int totalItems = Main.listHandler.getItems().getInt("Items." + itemSet + ".TotalItems");
						if(!(totalItems == 1)){
							 int itemNumber = Main.randomNumber(totalItems);
						
						
						ItemStack newItem = null;
						//get the item
						newItem.setTypeId(Main.listHandler.getItems().getInt("Items." + itemSet + ".ItemNumber_" + itemNumber + ".ItemID"));
						int amount = Main.listHandler.getItems().getInt("Items." + itemSet + ".ItemNumber_" + itemNumber + ".amount");
						newItem.setAmount(amount);
						//set durability
						newItem.setDurability( (short) Main.listHandler.getItems().getInt("Items." + itemSet + ".ItemNumber_" + itemNumber + ".Durability"));
						//get and set lore
						List<String> lore = null;
						int loreLines = Main.listHandler.getItems().getInt("Items." + itemSet + ".LoreLines");
						if(!(loreLines == 0)){
							for(int counter=1; counter<loreLines; counter++){
								lore.add(Main.listHandler.getItems().getString("Items." + itemSet + ".ItemNumber_" + itemNumber + ".LoreLine" + counter));
							}
						}
						ItemMeta meta = null;
						meta.setLore(Main.color(lore));
						//get and set enchants
						int enchants = Main.listHandler.getItems().getInt("Items." + itemSet + ".ItemNumber_" + itemNumber + ".NumberOfEnchants");
						if(!(enchants==0)){
							for(int counter = 1; counter < enchants; counter++){
								int enchantID = Main.listHandler.getItems().getInt("Items." + name + ".ItemNumber_" + itemNumber + ".Enchant_" + counter);
								int enchantLevel = Main.listHandler.getItems().getInt("Items." + name + ".ItemNumber_" + itemNumber + ".Enchant_" + counter  + " level");
								meta.addEnchant(Enchantment.getById(enchantID), enchantLevel, true);						
							}
						}
						String display = Main.listHandler.getItems().getString("Items." + itemSet + ".ItemNumber_" + itemNumber + ".displayName");
						meta.setDisplayName(Main.color(display));
						newItem.setItemMeta(meta);
						Inventory inv = Bukkit.createInventory(null, 27, Main.color("&5" + e.getPlayer().getName() + "s Item"));
						inv.setItem(15, newItem);
						e.getPlayer().openInventory(inv);

						//run chest event 	
                        cal.add(Calendar.SECOND, cooldown);
						Main.listHandler.getPlayers().set("Players." + UUID + "." + name + ".cooldownExpire", cal.getTimeInMillis());
						e.getPlayer().sendMessage(Main.color("&aEnjoy your item!"));
						Main.listHandler.saveChestLocation();
						Main.listHandler.savePlayers();
						return;
						}
					}
					else{
						int timeLeft = (int) (cooldownExpire.getTimeInMillis()/1000) - ((int) (cal.getTimeInMillis()/1000));
						e.getPlayer().sendMessage(Main.color("&cYou can not open that chest until &a" + timeLeft + " &cseconds"));
						return;
					}
				}
			}
		}

	}
}
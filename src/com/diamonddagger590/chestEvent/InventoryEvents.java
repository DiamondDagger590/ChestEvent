package com.diamonddagger590.chestEvent;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

public class InventoryEvents implements Listener{
	public int times = 0;
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e){
		Player player = (Player) e.getPlayer();
		Inventory inv = e.getInventory();
		if(inv.getTitle() != null){
			for(String itemSet : Main.listHandler.getItems().getConfigurationSection("Items").getKeys(false)){
				if(ChatColor.stripColor(inv.getTitle()).equalsIgnoreCase("Creating ItemSet: " + itemSet)){
					player.setCanPickupItems(true);
					for(ItemStack i : e.getInventory().getContents()){
						
						if((i!=null)){
							
							times++;
							Main.listHandler.getItems().set("Items." + itemSet + ".ItemNumber_" + times, i);
						}
						else{
							continue;
						}
					}
					Main.listHandler.getItems().set("Items." + itemSet + ".Reset", null);
					Main.listHandler.getItems().set("Items." + itemSet + ".TotalItems", times);
					times = 0;
					Main.listHandler.saveItems();
					Inventory playerinv = e.getPlayer().getInventory();
					int totalItems = Main.listHandler.getItems().getInt("Items." + itemSet + ".TotalItems");
					
					for(String itemNumber : Main.listHandler.getItems().getConfigurationSection("Items." + itemSet).getKeys(true)){
						ItemStack i = Main.listHandler.getItems().getItemStack("Items." + itemSet + "." + itemNumber);
						int emptyslot = playerinv.firstEmpty();
						playerinv.setItem(emptyslot, i);
							
						}
					
					e.getPlayer().sendMessage(Main.color("&aItem file saved!"));
				} 
				if(ChatColor.stripColor(inv.getTitle()).equalsIgnoreCase("Editing ItemSet: " + itemSet)){
					player.setCanPickupItems(true);
					for(ItemStack i : e.getInventory().getContents()){
						
						if((i!=null)){
							
							times++;
							Main.listHandler.getItems().set("Items." + itemSet + ".ItemNumber_" + times, i);
						}
						else{
							continue;
						}
					}
					Main.listHandler.getItems().set("Items." + itemSet + ".Reset", null);
					Main.listHandler.getItems().set("Items." + itemSet + ".TotalItems", times);
					times = 0;
					Main.listHandler.saveItems();					
					e.getPlayer().sendMessage(Main.color("&aItem file saved!"));
					} 
				else{
					continue;
				}
			}
			
		}
		
		else{
			return;
			}
		}
	}
	


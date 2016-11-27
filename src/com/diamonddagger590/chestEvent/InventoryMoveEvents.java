package com.diamonddagger590.chestEvent;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class InventoryMoveEvents implements Listener{
	@SuppressWarnings("deprecation")
	@EventHandler
	    public void onInventoryClick(InventoryClickEvent e) {
	      Player player = (Player) e.getWhoClicked();
	      Inventory inv = e.getInventory();
	      for(String itemSet : Main.listHandler.getItems().getConfigurationSection("Items").getKeys(false))
	        if(ChatColor.stripColor(inv.getTitle()).equalsIgnoreCase(Main.color(Main.listHandler.getConfig().getString("Titles.Viewing").replaceAll("%ItemSet%", itemSet)))) {
	                          e.setCancelled(true);
	                          player.updateInventory();
	                             
	            }
	        }
	    }



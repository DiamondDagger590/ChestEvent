package com.diamonddagger590.chestEvent;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class InventoryEvents implements Listener{
	public int times = 0;
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e){
		Player player = (Player) e.getPlayer();
		Inventory inv = e.getInventory();
		if(inv.getTitle() != null){
			if(ChatColor.stripColor(inv.getTitle()).equalsIgnoreCase(e.getPlayer().getName() + "s Item")){
				for(int itemnumber = 0; itemnumber < inv.getSize(); itemnumber++){
					if(!(inv.getItem(itemnumber) == null)){
						ItemStack item = inv.getItem(itemnumber);
						int empty = player.getInventory().firstEmpty();
						if(empty == -1){
							Location loc = player.getLocation();
							World w = loc.getWorld();
							w.dropItemNaturally(loc, item);
							break;
						}
						else{
						player.getInventory().setItem(empty, item);
						}
					}
					else{
						continue;
					}
				}
				return;
			}
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
					for(String itemNumber : Main.listHandler.getItems().getConfigurationSection("Items." + itemSet).getKeys(true)){
						ItemStack i = Main.listHandler.getItems().getItemStack("Items." + itemSet + "." + itemNumber);
						int emptyslot = playerinv.firstEmpty();
						if((emptyslot == -1)){
							Location loc = player.getLocation();
							World w = loc.getWorld();
							w.dropItemNaturally(loc, i);			
						}
						else{			
							playerinv.setItem(emptyslot, i);
							continue;
						}
					}
					
					e.getPlayer().sendMessage(Main.color("&7[&1ChestEvent&7]&6>>&aItem file saved!"));
				} 
				if(ChatColor.stripColor(inv.getTitle()).equalsIgnoreCase("Editing ItemSet: " + itemSet)){
					player.setCanPickupItems(true);
					Main.listHandler.getItems().set("Items." + itemSet, null);
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
					e.getPlayer().sendMessage(Main.color("&7[&1ChestEvent&7]&6>>&aItem file saved!"));
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
	


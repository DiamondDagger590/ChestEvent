package com.diamonddagger590.chestEvent;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class InventoryManager extends BukkitRunnable{
	public static int times = 0;
	public static int loreLines = 0;
	public static int enchantlines = 0;
	private final static Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("ChestEvent");
	public static void createItemSet(Player sender, String name){
		//get users inv 
		Inventory inv = Bukkit.createInventory(null, 27, Main.color("&a&lCreating ItemSet: " + name));
		Main.listHandler.getItems().set("Items." + name + ".Reset", 0);
		Main.listHandler.saveItems();
		sender.sendMessage(Main.color("&aCreating the inventory now... Put items you want into the inventory then close it"));

		Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable(){
			public void run(){
				sender.openInventory(inv);
			}
		}, 10);
		sender.setCanPickupItems(false);

		
	}
	public static void viewItemSet(Player sender, String name){
		if(!(Main.listHandler.getItems().contains("Items." + name))){
			sender.sendMessage(Main.color("&cThat ItemSet doesn't exist"));
			return;
		}
		else{
			Inventory inv = Bukkit.createInventory(null, 27, Main.color("&a&lViewing ItemSet: " + name));
			for(String itemNumber : Main.listHandler.getItems().getConfigurationSection("Items." + name).getKeys(true)){
				ItemStack i = Main.listHandler.getItems().getItemStack("Items." + name + "." + itemNumber);
				int emptyslot = inv.firstEmpty();
				inv.setItem(emptyslot, i);
				}
			sender.openInventory(inv);
		}
	}
	public static void editItemSet(Player sender, String name){
		if(!(Main.listHandler.getItems().contains("Items." + name))){
			sender.sendMessage(Main.color("&cThat ItemSet doesn't exist"));
			return;
		}
		else{
			Inventory inv = Bukkit.createInventory(null, 27, Main.color("&a&lEditing ItemSet: " + name));
			for(String itemNumber : Main.listHandler.getItems().getConfigurationSection("Items." + name).getKeys(true)){
				ItemStack i = Main.listHandler.getItems().getItemStack("Items." + name + "." + itemNumber);
				int emptyslot = inv.firstEmpty();
				inv.setItem(emptyslot, i);
				}
			sender.openInventory(inv);
		}
	}
}


package com.diamonddagger590.chestEvent;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryManager {
	public static int times = 0;
	public static int loreLines = 0;
	public static int enchantlines = 0;
	public static void createItemSet(Player sender, String name){
		//get users inv
		Inventory inv = sender.getInventory();
		//for items in the inv
		for(ItemStack i : inv.getContents()){
		//if item isnt air
			if((i!=null)){
				//register item
				times++;
				Main.listHandler.getItems().set("Items." + name + ".ItemNumber_" + times, i);


			}
		}
		//register total items
		Main.listHandler.getItems().set("Items." + name + ".TotalItems", times);
		times = 0;
		Main.listHandler.saveItems();
		sender.sendMessage(Main.color("&aItem set registered as &e" + name));
	}
}


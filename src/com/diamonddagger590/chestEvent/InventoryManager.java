package com.diamonddagger590.chestEvent;

import java.util.List;
import java.util.Map;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryManager {
	public static int times = 0;
	public static int loreLines = 0;
	public static int enchantlines = 0;
	@SuppressWarnings("deprecation")
	public static void createItemSet(Player sender, String name){
		Inventory inv = sender.getInventory();
	
		//debug code
		sender.sendMessage("1");
		for(ItemStack i : inv.getContents()){
		
			if((i!=null)){

				//debug code
		
			sender.sendMessage("2");
			times++;


			int item = i.getType().getId();
			int amount = i.getAmount();
			Short durability = i.getDurability();
			String displayName = i.getItemMeta().getDisplayName();
			List<String> lore = i.getItemMeta().getLore();
			if(i.hasItemMeta()){
				if(i.getItemMeta().hasLore()){
						for(String l : lore){
							loreLines++;
							Main.listHandler.getItems().set("Items." + name + ".ItemNumber_" + times + ".LoreLine" + loreLines, l);
						}
				}
			}
			Main.listHandler.getItems().set("Items." + name + ".ItemNumber_" + times + ".LoreLines", loreLines);
			loreLines = 0;
			Map<Enchantment, Integer> enchants = i.getEnchantments();

			if(i.hasItemMeta()){
				if(i.getItemMeta().hasEnchants()){
					Enchantment[] enchantslist = Enchantment.values();
					for(Enchantment e : enchantslist){
						if(enchants.containsKey(e)){
							enchantlines++;
							int enchantID = e.getId();
							int enchantlevel = enchants.get(e);
							Main.listHandler.getItems().set("Items." + name + ".ItemNumber_" + times + ".Enchant_" + enchantlines, enchantID);
							Main.listHandler.getItems().set("Items." + name + ".ItemNumber_" + times + ".Enchant_" + enchantlines  + " level" , enchantlevel);
						}
					}
				}
			}
			Main.listHandler.getItems().set("Items." + name + ".ItemNumber_" + times + ".NumberOfEnchants", enchantlines);
			enchantlines = 0;
			Main.listHandler.getItems().set("Items." + name + ".ItemNumber_" + times + ".ItemID" , item);
			Main.listHandler.getItems().set("Items." + name + ".ItemNumber_" + times + ".amount", amount);
			Main.listHandler.getItems().set("Items." + name + ".ItemNumber_" + times + ".Durability", durability);
			Main.listHandler.getItems().set("Items." + name + ".ItemNumber_" + times + ".displayName", displayName);

			}
		}
		Main.listHandler.getItems().set("Items." + name + ".TotalItems", times);
		times = 0;
		Main.listHandler.saveItems();
		sender.sendMessage(Main.color("&aItem set registered as &e" + name));
	}
}


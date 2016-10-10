package com.diamonddagger590.chestEvent;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class eventhandlerClass implements Listener{
	private String username;
	
	@EventHandler
	
	public void onInventoryOpenEvent(InventoryOpenEvent e){
        if (e.getInventory().getHolder() instanceof Chest){
            username = e.getPlayer().getName().toString();
            if (Commands.usernames.contains(username)){
            	//run code
            	
            	Commands.usernames.remove(username);
            	Player sender = (Player) e.getPlayer();
            	sender.sendMessage(ChatColor.GREEN + "Chest is now registered");
            	
            }
        }
        else if (e.getInventory().getHolder() instanceof DoubleChest){
        	Player sender = (Player) e.getPlayer();
        	sender.sendMessage(ChatColor.RED + "Please open a single chest.");
        	
        }
    }
	public void onBlockBreakEvent(Block block, Player player) {
		//get the broken blocks chunk, if in the registry, check for coords, if in the registry, disable breaking and send error message
	}
	
	
}

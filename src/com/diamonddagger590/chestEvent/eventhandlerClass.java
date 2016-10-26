package com.diamonddagger590.chestEvent;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class eventhandlerClass implements Listener{
	private String username;
	
	@EventHandler
	
	public void onInventoryOpenEvent(InventoryOpenEvent e){
		//if the opened inventory is a chest
		
        if (e.getInventory().getHolder() instanceof Chest){
        	
        	//username = the players name
            username = e.getPlayer().getName().toString();
            
            //if the Array of usernames active by the command contains the users username
            if (Commands.chestRegister.contains(username)){
            	
            	Location chestLocation = e.getInventory().getLocation();
            	int chestx = chestLocation.getBlockX();
            	int chesty = chestLocation.getBlockY();
            	int chestz = chestLocation.getBlockZ();
            	
            	
            	//run code
            	//remove username from the array
            	Commands.chestRegister.remove(username);
            	
            	//create a player variable
            	Player sender = (Player) e.getPlayer();
            	
            	//send the player a success message
            	sender.sendMessage(ChatColor.GREEN + "Chest is now registered");
            }
            else if (Commands.chestUnregister.contains(username)){
            	
            	//run code
            	//remove username from the array
            	Commands.chestUnregister.remove(username);
            	
            	//create a player variable
            	Player sender = (Player) e.getPlayer();
            	
            	//send the player a success message
            	sender.sendMessage(ChatColor.GREEN + "Chest is now unregistered");
            	
            	
            }
        }
        //else, if the player opens a double chest
        else if (e.getInventory().getHolder() instanceof DoubleChest){
        	
        	//username = players name
        	username = e.getPlayer().getName().toString();
        	
        	//if username is contained in the array of active user names
        	if (Commands.chestRegister.contains(username)){
        		
            	//remove the user name from array
            	Commands.chestRegister.remove(username);
            	
            	//create a player variable
            	Player sender = (Player) e.getPlayer();
            	
            	//send the player an error message 
            	sender.sendMessage(ChatColor.RED + "Please reenter the commmand and open a single chest.");
        	}
        	else if (Commands.chestUnregister.contains(username)){
        		
            	//remove the user name from array        		
            	Commands.chestUnregister.remove(username);
            	
            	//create a player variable
            	Player sender = (Player) e.getPlayer();
            	
            	//send the player an error message 
            	sender.sendMessage(ChatColor.RED + "Please reenter the commmand and open a single chest.");
        	
        	}
        	
        }
    }
	public void onBlockBreakEvent(Block block, Player player) {
		//get the broken blocks chunk, if in the registry, check for coords, if in the registry, disable breaking and send error message
	}
	
	
}

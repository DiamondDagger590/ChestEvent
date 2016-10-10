package com.diamonddagger590.chestEvent;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class ChestRegister {
	
	//create a boolean to see if the player can register a chest
	private boolean registerAble;
	
	//create a string to test the first argument of the command
	private String register;
	
	//command
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		//if user types /ce
		if (cmd.getName().equalsIgnoreCase("ce")){
			
			//set register equal to the first arg
			register = args[0];
			
			//if command is issued by console
			if (!(sender instanceof Player)){
				
				//send the console a message
				sender.sendMessage("This command can only be ran by a player");
				
				return false;
			}
			//if user types /ce register
			if (register.equalsIgnoreCase(register)){
				
				return true;
			}
			else{
				
				return true;
			}
			
		}
		return false;
	}
}

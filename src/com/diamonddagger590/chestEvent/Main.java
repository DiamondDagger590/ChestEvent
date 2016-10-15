package com.diamonddagger590.chestEvent;

import java.io.File;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{
	public Main instance = this;
	public File pluginFolder = instance.getDataFolder();
	@Override
	
	public void onEnable(){
		
		if(!pluginFolder.exists()){
			pluginFolder.mkdirs();
		}
	}
	
	public void onDisable(){
		
	}
	
public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
	switch (cmd.getName()){
		case "ce":
			if (args.length >= 1){
				switch(args[0]){
					case "register":
						Commands.registerChest((Player) sender);
						return true;
						
					case "unregister":
						return true;
				}
			}
	
	}
		
			
		
		return false;
	}

}

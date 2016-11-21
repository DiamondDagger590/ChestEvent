package com.diamonddagger590.chestEvent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

public class ListHandler { 
	//create the listhandler info
	static ListHandler instance = new ListHandler();

	public static ListHandler getInstance() {
		return instance;
	}
	//create files

	Plugin p;
	FileConfiguration config;
	File configfile;

	FileConfiguration ChestLocation;
	File cfile;

	FileConfiguration items;

	File ifile;
	
	FileConfiguration players;
	File pfile;
	//setup called in on enable
	public void setup(Plugin p) {
		//create a datafolder if it doesnt exist
		if (!p.getDataFolder().exists()) {
			p.getDataFolder().mkdir();
		}
		//create cfile if it doesnt exist
		cfile = new File(p.getDataFolder(), "ChestLocation.yml");
		if (!cfile.exists()) {
			try{
        		File en = new File(p.getDataFolder(), "/ChestLocation.yml");
         		InputStream E = getClass().getResourceAsStream("/ChestLocation.yml");
         		copyFile(E, en);
         	}catch (Exception e) {
         		e.printStackTrace();
         	}
		}
		//save cfile to fileconfig type
		ChestLocation = YamlConfiguration.loadConfiguration(cfile);
		configfile = new File(p.getDataFolder(), "config.yml");
		if (!cfile.exists()) {
			try{
        		File en = new File(p.getDataFolder(), "/config.yml");
         		InputStream E = getClass().getResourceAsStream("/config.yml");
         		copyFile(E, en);
         	}catch (Exception e) {
         		e.printStackTrace();
         	}
		}
		config = YamlConfiguration.loadConfiguration(configfile);
		//create pfile if it doesnt exist
		pfile = new File(p.getDataFolder(), "Players.yml");
		if (!pfile.exists()) {
			try{
        		File en = new File(p.getDataFolder(), "/Players.yml");
         		InputStream E = getClass().getResourceAsStream("/Players.yml");
         		copyFile(E, en);
         	}catch (Exception e) {
         		e.printStackTrace();
         	}
		}
		//save pfile to a fileconfig type
		players = YamlConfiguration.loadConfiguration(pfile);
		//create ifile if it doesnt exist
		ifile = new File(p.getDataFolder(), "Items.yml");
		if (!ifile.exists()) {
			try{
        		File en = new File(p.getDataFolder(), "/Items.yml");
         		InputStream E = getClass().getResourceAsStream("Items.yml");
         		copyFile(E, en);
         	}catch (Exception e) {
         		e.printStackTrace();
         	}
		}
		//save ifile to a fileconfig type
		items = YamlConfiguration.loadConfiguration(ifile);
		
	}
	//create methods to get each file
	public FileConfiguration getConfig(){
		return config;
	}
	public FileConfiguration getItems() {
		return items;
	}
	public FileConfiguration getChestLocation() {
		return ChestLocation;
	}
	public FileConfiguration getPlayers() {
		return players;
	}
	//create methods to save each file
	public void saveConfig(){
		try {
			config.save(configfile);
		} catch (IOException e) {
			Bukkit.getServer().getLogger()
					.severe(ChatColor.RED + "Could not save config.yml!");
		}
	}
	
	public void saveChestLocation() {
		try {
			ChestLocation.save(cfile);
		} catch (IOException e) {
			Bukkit.getServer().getLogger()
					.severe(ChatColor.RED + "Could not save ChestLocation.yml!");
		}
	}
	public void saveItems() {
		try {
			items.save(ifile);
		} catch (IOException e) {
			Bukkit.getServer().getLogger()
					.severe(ChatColor.RED + "Could not save Items.yml!");
		}
	}
	public void savePlayers() {
		try {
			players.save(pfile);
		} catch (IOException e) {
			Bukkit.getServer().getLogger()
					.severe(ChatColor.RED + "Could not save Players.yml!");
		}
	}
	//create methods to reload fies
	public void reloadConfig(){
		config = YamlConfiguration.loadConfiguration(configfile);
	}
	public void reloadChestLocation() {
		ChestLocation = YamlConfiguration.loadConfiguration(cfile);
	}
	public void reloadPlayers() {
		players = YamlConfiguration.loadConfiguration(pfile);
	}
	public void reloadItems() {
		items = YamlConfiguration.loadConfiguration(ifile);
	}
	//code BadBones69 told me to have or else it breaks xD
	public PluginDescriptionFile getDesc() {
		return p.getDescription();
	}
	public static void copyFile(InputStream in, File out) throws Exception { // https://bukkit.org/threads/extracting-file-from-jar.16962/
        InputStream fis = in;
        FileOutputStream fos = new FileOutputStream(out);
        try {
            byte[] buf = new byte[1024];
            int i = 0;
            while ((i = fis.read(buf)) != -1) {
                fos.write(buf, 0, i);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (fis != null) {
                fis.close();
            }
            if (fos != null) {
                fos.close();
            }
        }
    }
}


package com.keuin.kbackup;

import java.io.IOException;

import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginLogger;
import org.bukkit.plugin.java.JavaPlugin;

public final class KBackup extends JavaPlugin implements Listener {
	
	private String backupDirectory;
	FileConfiguration config;
	PluginLogger logger;
	
    // Fired when plugin is first enabled
    @Override
    public void onEnable() {
    	// Initialize plugin logger
    	logger = new PluginLogger(this);
    	// Initialize config file
    	config = this.getConfig();
    	config.addDefault("backupDirectory", "./backups"); // default backups reside in the subdir "backups" of your server root dir
    	this.backupDirectory = config.getString("backupDirectoory");
    	
    	config.options().copyDefaults(true);
    	saveConfig();
    	
    	logger.info("KBackup loaded successfully.");
    }
    
//    @EventHandler
//    public void onPlayerJoin(PlayerJoinEvent event) {
//    	
//    }
    
    // Fired when plugin is disabled
    @Override
    public void onDisable() {
    	// Save all config
    	config.options().copyDefaults(true);
    	saveConfig();
    }
    
    private boolean backupWorld(World world) {
    	logger.info(String.format("Backing up world %s ...", world.getName()));
    	boolean previousAutoSaveSwitch = world.isAutoSave(); // if this world is set to be auto saved
    	
    	try {
        	logger.info("Saving world data to disk...");
        	world.save();
        	logger.info("World saved.");
        	if(previousAutoSaveSwitch) {
        		logger.info("Disabling auto-save temporarily to prevent data inconsistency.");
        		world.setAutoSave(false); // Disable auto saving temporarily to prevent world data inconsistency
        	}
        	
        	logger.info(String.format("Compressing world data for world %s. Please DO NOT save manually during backup process.",world.getName()));
        	// TODO: Do some zip operation
        	ZipUtil.zip(world.getWorldFolder(), zipPath, zipFileName);
    	} catch(ZipUtilException exception) {
    		
    	} catch(IOException exception) {
    		
    	} finally {
    		
    	}
    	
    	if(previousAutoSaveSwitch)
    		world.setAutoSave(true);
    	return true;
    }
    
    
}

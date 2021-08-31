package net.pgfmc.teams.blockData;

import java.io.File;
import java.io.IOException;
import java.time.LocalTime;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import net.pgfmc.pgfessentials.EssentialsMain;
import net.pgfmc.teams.teamscore.TeamsCore;

public class SurvivalManager {
	
	public static void updateBlock(Block block, OfflinePlayer player, boolean isPlaced) {
		
		
		
		// loads file
		// if file for the corresponding Chunk is not found, a new one is created.
		
		Chunk chunk = block.getChunk();
		File file = new File(TeamsCore.plugin.getDataFolder() + File.separator + "EABlockData" + File.separator + "x" + String.valueOf(chunk.getX()) + "z" + String.valueOf(chunk.getZ()) + ".yml");
				
		try {
			if (file.createNewFile()) {
				System.out.println("New chunk file created!");
						
			}
		} catch (IOException e) {
			e.printStackTrace();
					
		}
		FileConfiguration database = YamlConfiguration.loadConfiguration(file);
		
		String data = String.valueOf(block.getX()) + "-" + String.valueOf(block.getY()) + "-" + String.valueOf(block.getX());
		
		
		// saves previous block data to deep archive
		
		if (database.getConfigurationSection(data) != null) {
			saveDeepBlock(database.getConfigurationSection(data), chunk, data);
			database.set(data, null);
		}
		
		// sets data
		ConfigurationSection configSec = database.createSection(data);
		configSec.set("Player", player.getUniqueId().toString());
		configSec.set("isPlaced?", isPlaced);
		configSec.set("time", LocalTime.now().toString());
		configSec.set("block", block.getType().toString());
				
		database.set(data, configSec);
		
		// saves file
		
		try {
			database.save(file);
			
		} catch (IOException e) {
			e.printStackTrace();
			
		}
	}
	
	public static void saveDeepBlock(ConfigurationSection data, Chunk chunk, String name) { // saves Block data to deep Archive
		
		// loads file
		// if file for the corresponding Chunk is not found, a new one is created.
		
		File file = new File(TeamsCore.plugin.getDataFolder() + File.separator + "DeepBlockData" + File.separator + "x" + String.valueOf(chunk.getX()) + "z" + String.valueOf(chunk.getZ()) + ".yml");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
		}
		FileConfiguration database = YamlConfiguration.loadConfiguration(file);
		
		// saves data
		
		if (database.get(name) != null) {
			ConfigurationSection configSec = database.getConfigurationSection(name);
			configSec.set(String.valueOf(configSec.getKeys(false).size() + 1), data);
			database.set(name, configSec);
			
		} else {
			ConfigurationSection configSec = database.createSection(name);
			configSec.set("1", data);
			database.set(name, configSec);
			
		}
		
		try {
			database.save(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String locToString(Location location) { // converts location to string.
		
		return "w" + String.valueOf(EssentialsMain.worldToInt(location.getWorld())) + "x" + String.valueOf(location.getBlockX()) + "y" + String.valueOf(location.getBlockY()) + "z" + String.valueOf(location.getBlockZ());
		
	}
	
	
	
	
	
	
	
	
	
	

}

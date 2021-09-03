package net.pgfmc.teams.blockData;

import java.io.File;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import net.pgfmc.teams.blockData.containers.BlockContainer;
import net.pgfmc.teams.teamscore.TeamsCore;

/*
Written by CrimsonDart

-----------------------------------

manages inspector data.

-----------------------------------
 */

public class CreativeManager {
	
	// Moderation tools below ( slow access ) VV

	public static void outputBlockData(Block block, Player player) {
		
		// loads file
		Chunk chunk = block.getChunk();
		File file = new File(TeamsCore.plugin.getDataFolder() + File.separator + "EABlockData" + File.separator + "x" + String.valueOf(chunk.getX()) + "z" + String.valueOf(chunk.getZ()) + ".yml");
		if (!file.exists()) {
			player.sendMessage("No data on that block was found!");
			return;
		}
		FileConfiguration database = YamlConfiguration.loadConfiguration(file);
		
		String data = String.valueOf(block.getX()) + "-" + String.valueOf(block.getY()) + "-" + String.valueOf(block.getX());
		if (database.getConfigurationSection(data) != null) {
			if (database.getConfigurationSection(data).getBoolean("isPlaced?")) {
				player.sendMessage("The block at " + block.getX() + ", " + block.getY() + ", " + block.getZ() + " was Placed at time " + 
			database.getConfigurationSection(data).get("time") + " by " + Bukkit.getPlayer(UUID.fromString(database.getConfigurationSection(data).getString("Player"))).getName() + ".");
				
				Boolean lock = BlockContainer.getContainer(block).isLocked();
				if (lock != null) {
					
					if (lock) {
						player.sendMessage("This container is Locked!");
					} else {
						player.sendMessage("This container isn't Locked!");
					}
					
				}
			} else {
				player.sendMessage("The block at " + block.getX() + ", " + block.getY() + ", " + block.getZ() + " was Broken at time " + 
			database.getConfigurationSection(data).get("time") + " by " + Bukkit.getPlayer(UUID.fromString(database.getConfigurationSection(data).getString("Player"))).getName() + ".");
			}
			
			
			
		} else {
			player.sendMessage("No data on that block was found!");
		}
	}
}
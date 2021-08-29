package net.pgfmc.teams.blockData;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Beacon;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.mojang.datafixers.util.Pair;

import net.pgfmc.pgfessentials.EssentialsMain;
import net.pgfmc.teams.teamscore.TeamsCore;

public class CreativeManager {
	
	private static File file = new File(TeamsCore.plugin.getDataFolder() + File.separator + "containers.yml"); // Creates a File object
	private static FileConfiguration database = YamlConfiguration.loadConfiguration(file); // Turns the File object into YAML and loads data
	
	public static Pair<OfflinePlayer, Boolean> getContainerData(Block block) { // retruns who placed a stored block.
		
		Location location = block.getLocation();
		ConfigurationSection blocc = database.getConfigurationSection("x" + String.valueOf(location.getBlockX()) + "y" + String.valueOf(location.getBlockY()) + "z" + String.valueOf(location.getBlockZ()));
		if (blocc == null) {
			System.out.println("x" + String.valueOf(location.getBlockX()) + "y" + String.valueOf(location.getBlockY()) + "z" + String.valueOf(location.getBlockZ()) + " not found in Blocks");
			return null;
		}
		
		String uuid = blocc.getString("player");
		boolean bool = blocc.getBoolean("isLocked");
		
		return new Pair<OfflinePlayer, Boolean>(Bukkit.getOfflinePlayer(UUID.fromString(uuid)), bool);
	}
	
	
	
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
				
				Boolean lock = ContainerManager.getLocked(block);
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
	

	public static List<Beacon> getBeacons() {
		
		List<Beacon> beacons = new ArrayList<Beacon>();
		
		for (String key : database.getKeys(false)) {
			
			
			key = key.replace("x", "");
			
			String[] sting = key.split("y");
			
			String[] sting2 = sting[1].split("z");
			
			
			
			Block block = EssentialsMain.survivalWorld.getBlockAt(new Location(EssentialsMain.survivalWorld, Double.parseDouble(sting[0]), Double.parseDouble(sting2[0]), Double.parseDouble(sting2[1])));
			
			if (block.getType() == Material.BEACON && block.getState() instanceof Beacon) {
				beacons.add((Beacon) block.getState());
				System.out.println("Added " + key + " to beacons!");
			}
			
		}
		
		return beacons;
		
	}
}
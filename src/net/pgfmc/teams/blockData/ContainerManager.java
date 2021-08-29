package net.pgfmc.teams.blockData;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Container;
import org.bukkit.block.DoubleChest;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import net.pgfmc.pgfessentials.EssentialsMain;
import net.pgfmc.teams.teamscore.TeamsCore;

public class ContainerManager {
	
	private static File file = new File(TeamsCore.plugin.getDataFolder() + File.separator + "containers.yml"); // Creates a File object
	private static FileConfiguration database = YamlConfiguration.loadConfiguration(file); // Turns the File object into YAML and loads data
	
	public static void saveContainer(Block block, OfflinePlayer player) { // saves the container in its own file for easy access
		
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(TeamsCore.plugin, new Runnable() {
			@Override
			public void run() {
				
				Location location = block.getLocation();
				
				// if location is not found, a new one is created.
				ConfigurationSection blocc = database.getConfigurationSection(SurvivalManager.locToString(location));
				if (blocc == null) {
					blocc = database.createSection(SurvivalManager.locToString(location));
				}
				
				blocc.set("player", player.getUniqueId().toString());
				blocc.set("isLocked", true);
				database.set(SurvivalManager.locToString(location), blocc);
				
				Block bloke = getOtherSide(block);
				
				if (bloke != null) { // saves the other side of the double chest (if the container was a double chest)
					
					location = bloke.getLocation();
					
					blocc = database.getConfigurationSection(SurvivalManager.locToString(location));
					if (blocc == null) {
						blocc = database.createSection(SurvivalManager.locToString(location));
					}
					
					blocc.set("player", player.getUniqueId().toString());
					blocc.set("isLocked", true);
					
					database.set(SurvivalManager.locToString(location), blocc);
				}
				
				// saves data.
				try {
					database.save(file);
					System.out.println("Container location saved!");
					
				} catch (IOException e) {
					e.printStackTrace();
					
				}
			}
		}, 1);
		
	}
	
	public static void deleteContainer(Block block, OfflinePlayer player) { // deletes a container from the database.
		
		Location location = block.getLocation();
		database.set("x" + String.valueOf(location.getBlockX()) + "y" + String.valueOf(location.getBlockY()) + "z" + String.valueOf(location.getBlockZ()), null);
		
		// saves data
		
		try {
			database.save(file);
			System.out.println("Container location deleted!");
			
		} catch (IOException e) {
			e.printStackTrace();
			
		}
	}
	
	public static Block getOtherSide(Block chest) { // gets the other side of a double chest after one side is placed.
		
		if (chest.getType() == Material.CHEST && ((Container) chest.getState()).getInventory().getHolder() instanceof DoubleChest) {
			
			DoubleChest inv = (DoubleChest) ((Chest) chest.getState()).getInventory().getHolder();
			
			Set<Block> blocks = new HashSet<Block>();
			
			blocks.add(EssentialsMain.survivalWorld.getBlockAt(chest.getLocation().add(1, 0, 0)));
			blocks.add(EssentialsMain.survivalWorld.getBlockAt(chest.getLocation().add(-1, 0, 0)));
			blocks.add(EssentialsMain.survivalWorld.getBlockAt(chest.getLocation().add(0, 0, 1)));
			blocks.add(EssentialsMain.survivalWorld.getBlockAt(chest.getLocation().add(0, 0, -1)));
			
			for (Block block : blocks) {
				if (block != null && block.getType() == Material.CHEST && ((Chest) block.getState()).getInventory().getHolder() instanceof DoubleChest && 
						((DoubleChest) ((Chest) block.getState()).getInventory().getHolder()).getRightSide().toString().equals(inv.getRightSide().toString())) {
					return block;
				}
			}
		}
		return null;
	}
	
	public static OfflinePlayer getPlacer(Block block) { //returns the player who placed a block
		
		ConfigurationSection gamer = database.getConfigurationSection(SurvivalManager.locToString(block.getLocation()));
		
		if (gamer != null) {
			return Bukkit.getOfflinePlayer(UUID.fromString(gamer.getString("player")));
			
		}
		return null;
	}
	
	public static Boolean getLocked(Block block) { // returns wether or not a block is locked
		
		ConfigurationSection gamer = database.getConfigurationSection(SurvivalManager.locToString(block.getLocation()));
		
		if (gamer != null) {
			return gamer.getBoolean("isLocked");
		}
		return null;
	}
	
	public static void setLocked(Block block, boolean lock) { // locks a container
		
		Location location = block.getLocation();
		ConfigurationSection blocc = database.getConfigurationSection("x" + String.valueOf(location.getBlockX()) + "y" + String.valueOf(location.getBlockY()) + "z" + String.valueOf(location.getBlockZ()));
		if (blocc == null) {
			System.out.println("Failed to change lock status of a block!");
			return;
		}
		
		blocc.set("isLocked", lock);
		database.set("x" + String.valueOf(location.getBlockX()) + "y" + String.valueOf(location.getBlockY()) + "z" + String.valueOf(location.getBlockZ()), blocc);
		
		try {
			database.save(file);
			System.out.println("Lock status changed successfully!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

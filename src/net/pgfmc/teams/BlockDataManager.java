package net.pgfmc.teams;

import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
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
import org.bukkit.entity.Player;

import com.mojang.datafixers.util.Pair;

public class BlockDataManager {
	
	static File file = new File(Main.plugin.getDataFolder() + File.separator + "containers.yml"); // Creates a File object
	static FileConfiguration database = YamlConfiguration.loadConfiguration(file); // Turns the File object into YAML and loads data
	
	public static void deleteContainerLocation(Block block) { // deletes saved block data
		
		Location location = block.getLocation();
		database.set("x" + String.valueOf(location.getBlockX()) + "y" + String.valueOf(location.getBlockY()) + "z" + String.valueOf(location.getBlockZ()), null);
		
		try {
			database.save(file);
			System.out.println("Container location deleted!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Pair<OfflinePlayer, Boolean> getContainerData(Block block) { // retruns who placed a stored block.
		
		Location location = block.getLocation();
		ConfigurationSection blocc = database.getConfigurationSection("x" + String.valueOf(location.getBlockX()) + "y" + String.valueOf(location.getBlockY()) + "z" + String.valueOf(location.getBlockZ()));
		if (blocc == null) {
			System.out.println("x" + String.valueOf(location.getBlockX()) + "y" + String.valueOf(location.getBlockY()) + "z" + String.valueOf(location.getBlockZ()) + " not found in Blocks");
			return new Pair<OfflinePlayer, Boolean>(null, null);
		}
		
		String uuid = blocc.getString("player");
		boolean bool = blocc.getBoolean("isLocked");
		
		return new Pair<OfflinePlayer, Boolean>(Bukkit.getOfflinePlayer(UUID.fromString(uuid)), bool);
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
	
	// Moderation tools below ( slow access ) VV
	
	public static void updateBlock(Block block, OfflinePlayer player, boolean isPlaced) { // saves association with location, block editor, 
	// loads file
		Chunk chunk = block.getChunk();
		File file = new File(Main.plugin.getDataFolder() + File.separator + "EABlockData" + File.separator + "x" + String.valueOf(chunk.getX()) + "z" + String.valueOf(chunk.getZ()) + ".yml");
		
		try {
			if (file.createNewFile()) {
				System.out.println("New chunk file created!");
			}
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		FileConfiguration database = YamlConfiguration.loadConfiguration(file);
		String data = String.valueOf(block.getX()) + "-" + String.valueOf(block.getY()) + "-" + String.valueOf(block.getX());
		
		if (database.getConfigurationSection(data) != null) {
			saveDeepBlock(database.getConfigurationSection(data), chunk, data);
			database.set(data, null);
		}
		
		ConfigurationSection configSec = database.createSection(data);
		configSec.set("Player", player.getUniqueId().toString());
		configSec.set("isPlaced?", isPlaced);
		configSec.set("time", LocalTime.now().toString());
		configSec.set("block", block.getType().toString());
		
		database.set(data, configSec);
		
		
		// checks to see if the block placed is a container
		// if so, the location is recorded, along with the player who placed it, and automatically locks the container.
		
		Material mat = block.getType();
		if (mat == Material.CHEST || mat == Material.BEACON || mat == Material.FURNACE || mat == Material.BLAST_FURNACE || mat == Material.SMOKER || mat == Material.DISPENSER || mat == Material.DROPPER || mat == Material.TRAPPED_CHEST || mat == Material.BARREL || mat == Material.CAMPFIRE || mat == Material.SOUL_CAMPFIRE || mat == Material.SHULKER_BOX || mat == Material.JUKEBOX || mat == Material.LECTERN || mat == Material.HOPPER || mat == Material.BREWING_STAND) {
			if (isPlaced) {
				
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
					@Override
					public void run() {
						
						Location location = block.getLocation();
						// saves the container in a seperate file for easy access
						
						ConfigurationSection blocc = BlockDataManager.database.getConfigurationSection("x" + String.valueOf(location.getBlockX()) + "y" + String.valueOf(location.getBlockY()) + "z" + String.valueOf(location.getBlockZ()));
						if (blocc == null) {
							blocc = BlockDataManager.database.createSection("x" + String.valueOf(location.getBlockX()) + "y" + String.valueOf(location.getBlockY()) + "z" + String.valueOf(location.getBlockZ()));
						}
						
						blocc.set("player", player.getUniqueId().toString());
						blocc.set("isLocked", true);
						
						BlockDataManager.database.set("x" + String.valueOf(location.getBlockX()) + "y" + String.valueOf(location.getBlockY()) + "z" + String.valueOf(location.getBlockZ()), blocc);
						
						Block bloke = getOtherSide(block);
						
						if (bloke != null) {
							
							location = bloke.getLocation();
							// saves the container in a seperate file for easy access
							
							blocc = BlockDataManager.database.getConfigurationSection("x" + String.valueOf(location.getBlockX()) + "y" + String.valueOf(location.getBlockY()) + "z" + String.valueOf(location.getBlockZ()));
							if (blocc == null) {
								blocc = BlockDataManager.database.createSection("x" + String.valueOf(location.getBlockX()) + "y" + String.valueOf(location.getBlockY()) + "z" + String.valueOf(location.getBlockZ()));
							}
							
							blocc.set("player", player.getUniqueId().toString());
							blocc.set("isLocked", true);
							
							BlockDataManager.database.set("x" + String.valueOf(location.getBlockX()) + "y" + String.valueOf(location.getBlockY()) + "z" + String.valueOf(location.getBlockZ()), blocc);
						}
						
						
						try {
							BlockDataManager.database.save(BlockDataManager.file);
							System.out.println("Container location saved!");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, 1);
			} else {
				deleteContainerLocation(block);
			}
		}
		
		// saves the file.
		try {
			database.save(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void returnBlockData(Block block, Player player) {
		
		// loads file
		Chunk chunk = block.getChunk();
		File file = new File(Main.plugin.getDataFolder() + File.separator + "EABlockData" + File.separator + "x" + String.valueOf(chunk.getX()) + "z" + String.valueOf(chunk.getZ()) + ".yml");
		if (!file.exists()) {
			player.sendMessage("No data on that block was found!");
			return;
		}
		FileConfiguration database = YamlConfiguration.loadConfiguration(file);
		
		String data = String.valueOf(block.getX()) + "-" + String.valueOf(block.getY()) + "-" + String.valueOf(block.getX());
		if (database.getConfigurationSection(data) != null) {
			if (database.getConfigurationSection(data).getBoolean("isPlaced?")) {
				player.sendMessage("The block at " + block.getX() + ", " + block.getY() + ", " + block.getZ() + " was Placed at time " + database.getConfigurationSection(data).get("time") + " by " + Bukkit.getPlayer(UUID.fromString(database.getConfigurationSection(data).getString("Player"))).getName() + ".");
			} else {
				player.sendMessage("The block at " + block.getX() + ", " + block.getY() + ", " + block.getZ() + " was Broken at time " + database.getConfigurationSection(data).get("time") + " by " + Bukkit.getPlayer(UUID.fromString(database.getConfigurationSection(data).getString("Player"))).getName() + ".");
			}
		} else {
			player.sendMessage("No data on that block was found!");
		}
	}
	
	// stores data from Surface block data and moves it into deep block data, so keep 
	public static void saveDeepBlock(ConfigurationSection data, Chunk chunk, String name) {
		File file = new File(Main.plugin.getDataFolder() + File.separator + "DeepBlockData" + File.separator + "x" + String.valueOf(chunk.getX()) + "z" + String.valueOf(chunk.getZ()) + ".yml");
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
	
	public static Block getOtherSide(Block chest) {
		if (chest.getType() == Material.CHEST && ((Container) chest.getState()).getInventory().getHolder() instanceof DoubleChest) {
			
			DoubleChest inv = (DoubleChest) ((Chest) chest.getState()).getInventory().getHolder();
			
			Set<Block> blocks = new HashSet<Block>();
			
			blocks.add(Main.survivalWorld.getBlockAt(chest.getLocation().add(1, 0, 0)));
			blocks.add(Main.survivalWorld.getBlockAt(chest.getLocation().add(-1, 0, 0)));
			blocks.add(Main.survivalWorld.getBlockAt(chest.getLocation().add(0, 0, 1)));
			blocks.add(Main.survivalWorld.getBlockAt(chest.getLocation().add(0, 0, -1)));
			
			for (Block block : blocks) {
				if (block != null && block.getType() == Material.CHEST && ((Chest) block.getState()).getInventory().getHolder() instanceof DoubleChest && 
						((DoubleChest) ((Chest) block.getState()).getInventory().getHolder()).getRightSide().toString().equals(inv.getRightSide().toString())) {
					return block;
				}
			}
		}
		return null;
	}
}
package net.pgfmc.teams.data.containers;

import java.io.File;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;
import net.pgfmc.teams.data.containers.Ownable.Lock;
import net.pgfmc.teams.teamscore.TeamsCore;
import net.pgfmc.teams.teamscore.Utility;

/*
Written by CrimsonDart

-----------------------------------

Handles saving and loading containers.

-----------------------------------
 */

public class ContainerDatabase {
	
	
	public static void loadContainers() {
		
		File file = new File(TeamsCore.getPlugin().getDataFolder() + File.separator + "BlockContainers.yml"); // Creates a File object
		FileConfiguration database = YamlConfiguration.loadConfiguration(file); // Turns the File object into YAML and loads data
		
		
		if (database != null) {
			for (String key : database.getKeys(false)) {
				
				ConfigurationSection configSec = database.getConfigurationSection(key);
				
				Location loc = Utility.StringtoLoc(key);
				
				PlayerData pd = PlayerData.getPlayerData(UUID.fromString(configSec.getString("player")));
				
				
				Lock lock;
				
				if (configSec.getString("Lock") == null) {
					if (configSec.getBoolean("isLocked")) {
						lock = Lock.FRIENDS_ONLY;
					} else {
						lock = Lock.UNLOCKED;
					}
				} else {
					lock = Lock.valueOf(configSec.getString("Lock"));
				}
				
				OwnableBlock.createBlockContainer(pd, lock, loc.getWorld().getBlockAt(loc));
				
			}
		}
		
		// Entity Containers
		
		file = new File(TeamsCore.getPlugin().getDataFolder() + File.separator + "EntityContainers.yml"); // Creates a File object
		database = YamlConfiguration.loadConfiguration(file); // Turns the File object into YAML and loads data
		
		if (database != null) {
			for (String key : database.getKeys(false)) {
				
				ConfigurationSection configSec = database.getConfigurationSection(key);
				
				UUID uuid = UUID.fromString(key);
				
				PlayerData pd = PlayerData.getPlayerData(UUID.fromString(configSec.getString("player")));
				
				Lock lock = Lock.valueOf(configSec.getString("Lock"));
				
				new OwnableEntity(pd, lock, uuid);
			}
		}
	}
	
	public static void saveContainers() {
		
		File file = new File(TeamsCore.getPlugin().getDataFolder() + File.separator + "BlockContainers.yml"); // Creates a File object
		FileConfiguration database = YamlConfiguration.loadConfiguration(file); // Turns the File object into YAML and loads data
		
		for (Entry<Block, OwnableBlock> blocke : OwnableBlock.containers.entrySet()) { // for all BlockContainers and beacons.
			
			Block block = blocke.getKey();
			OwnableBlock cont = blocke.getValue();
			
			Location location = block.getLocation();
			PlayerData player = cont.getPlayer();
			
			// if location is not found, a new one is created.
			ConfigurationSection blocc = database.getConfigurationSection(Utility.locToString(location));
			if (blocc == null) {
				blocc = database.createSection(Utility.locToString(location));
			}
			
			blocc.set("isLocked", null);
			blocc.set("player", player.getUniqueId().toString());
			blocc.set("Lock", cont.getLock().toString());
			
			database.set(Utility.locToString(location), blocc);
			
			// saves data.
			try {
				database.save(file);
				System.out.println("Container location saved!");
				
			} catch (IOException e) {
				e.printStackTrace();
				
			}
			
		} // ------------------------------------ end loop
		
		// Entity Containers
		
		file = new File(TeamsCore.getPlugin().getDataFolder() + File.separator + "EntityContainers.yml"); // Creates a File object
		database = YamlConfiguration.loadConfiguration(file); // Turns the File object into YAML and loads data
		
		for (UUID entity : OwnableEntity.getContainers().keySet()) { // for all BlockContainers and beacons.
			
			OwnableEntity ent = OwnableEntity.getContainer(entity);
			PlayerData player = ent.getPlayer();
			
			// if location is not found, a new one is created.
			ConfigurationSection blocc = database.getConfigurationSection(entity.toString());
			if (blocc == null) {
				blocc = database.createSection(entity.toString());
			}
			
			blocc.set("player", player.getUniqueId().toString());
			blocc.set("Lock", ent.getLock().toString());
			database.set(entity.toString(), blocc);
			
			// saves data.
			try {
				database.save(file);
				System.out.println("Container location saved!");
				
			} catch (IOException e) {
				e.printStackTrace();
				
			}
		}
	}
}

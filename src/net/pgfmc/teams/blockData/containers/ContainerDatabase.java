package net.pgfmc.teams.blockData.containers;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
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
import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;
import net.pgfmc.teams.blockData.SurvivalManager;
import net.pgfmc.teams.teamscore.Team;
import net.pgfmc.teams.teamscore.TeamsCore;

/*
Written by CrimsonDart

-----------------------------------

Handles saving and loading containers.

-----------------------------------
 */

public class ContainerDatabase {
	
	
	public static void loadContainers() {
		
		File file = new File(TeamsCore.plugin.getDataFolder() + File.separator + "BlockContainers.yml"); // Creates a File object
		FileConfiguration database = YamlConfiguration.loadConfiguration(file); // Turns the File object into YAML and loads data
		
		
		if (database != null) {
			for (String key : database.getKeys(false)) {
				
				ConfigurationSection configSec = database.getConfigurationSection(key);
				
				Location loc = StringtoLoc(key);
				
				OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(configSec.getString("player")));
				try {
					Team team = Team.findID(UUID.fromString(configSec.getString("team")));
					BlockContainer.createBlockContainer(player, configSec.getBoolean("isLocked"), loc.getWorld().getBlockAt(loc), team);
				} catch(Exception e) {
					
					Team team = (Team) PlayerData.getData(player, "team");
					BlockContainer.createBlockContainer(player, configSec.getBoolean("isLocked"), loc.getWorld().getBlockAt(loc), team);
				}
			}
		}
	}
	
	public static void saveContainers() {
		
		File file = new File(TeamsCore.plugin.getDataFolder() + File.separator + "BlockContainers.yml"); // Creates a File object
		FileConfiguration database = YamlConfiguration.loadConfiguration(file); // Turns the File object into YAML and loads data
		
		
		for (Block block : BlockContainer.containers.keySet()) { // for all BlockContainers and beacons.
			
			Location location = block.getLocation();
			BlockContainer cont = BlockContainer.getContainer(block);
			OfflinePlayer player = cont.getPlayer();
			
			// if location is not found, a new one is created.
			ConfigurationSection blocc = database.getConfigurationSection(SurvivalManager.locToString(location));
			if (blocc == null) {
				blocc = database.createSection(SurvivalManager.locToString(location));
			}
			
			blocc.set("player", player.getUniqueId().toString());
			blocc.set("isLocked", cont.isLocked());
			
			if (cont.getTeam() != null) {
				blocc.set("team", cont.getTeam().getUniqueId().toString());
			} else {
				blocc.set("team", null);
			}
			
			database.set(SurvivalManager.locToString(location), blocc);
			
			/*
			Block bloke = getOtherSide(block);
			 
			
			
			// ------------------------------------ FIX HERE
			
			
			if (bloke != null) { // saves the other side of the double chest (if the container was a double chest)
				
				location = bloke.getLocation();
				
				blocc = database.getConfigurationSection(SurvivalManager.locToString(location));
				if (blocc == null) {
					blocc = database.createSection(SurvivalManager.locToString(location));
				}
				
				blocc.set("player", player.getUniqueId().toString());
				blocc.set("isLocked", cont.isLocked());
				
				database.set(SurvivalManager.locToString(location), blocc);
			}
			*/
			
			// ------------------------------------- FIX HERE // implement into BlockContainer.java
			
			// saves data.
			try {
				database.save(file);
				System.out.println("Container location saved!");
				
			} catch (IOException e) {
				e.printStackTrace();
				
			}
			
		} // ------------------------------------ end loop
		
	}
	
	@SuppressWarnings("unused")
	@Deprecated
	private static Block getOtherSide(Block chest) { // gets the other side of a double chest after one side is placed.
		
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
	
	
	private static Location StringtoLoc(String string) { // converts a specific string to a location object
		
		char[] chars = string.toCharArray();
		
		int stage = 0;
		String w = null;
		String x = "";
		String y = "";
		String z = "";
		
		LinkedList<Character> Chars = new LinkedList<>();
		
		for (char character : chars) {
			Chars.add(character);
		}
		
		w = Chars.get(1).toString();
		Chars.remove(0); // w
		Chars.remove(0); // int
		Chars.remove(0); // x
		
		stage = 1;
		
		for (char c : Chars) {
			if (stage == 1) {
				if (c != "y".charAt(0)) {
					x = x + c;
				} else {
					stage = 2;
				}
				
			} else if (stage == 2) {
				
				if (c != "z".charAt(0)) {
					y = y + c;
				} else {
					stage = 3;
				}
				
				
			} else if (stage == 3) {
				
				z = z + c;
			}
		}
		
		return new Location(EssentialsMain.intToWorld(Integer.valueOf(w)), Integer.valueOf(x), Integer.valueOf(y), Integer.valueOf(z));
		
		
	}
}

package net.pgfmc.teams.teamscore;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Beacon;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import net.pgfmc.pgfessentials.EssentialsMain;
import net.pgfmc.teams.blockData.BBEvent;
import net.pgfmc.teams.blockData.BPE;
import net.pgfmc.teams.blockData.BlockInteractEvent;
import net.pgfmc.teams.blockData.InspectCommand;
import net.pgfmc.teams.blockData.containers.ContainerDatabase;
import net.pgfmc.teams.playerLogistics.AttackEvent;
import net.pgfmc.teams.playerLogistics.InviteCommand;
import net.pgfmc.teams.playerLogistics.LeaveTeamCommand;
import net.pgfmc.teams.playerLogistics.LeaveTeamConfirmCommand;
import net.pgfmc.teams.playerLogistics.TeamAccept;

public class TeamsCore extends JavaPlugin {
	
	public static TeamsCore plugin;
	public static List<Beacon> beacons;
	
	
	@Override
	public void onEnable() {
		
		plugin = this;
		
		plugin.getDataFolder().mkdirs();
		
		File file = new File(plugin.getDataFolder() + "\\database.yml"); // Creates a File object
		try {
			if (file.createNewFile()) {
				System.out.println("database.yml created!");
			} else {
				System.out.println("database.yml already Exists!");
				
				Database.loadTeams();
				
				((EssentialsMain) Bukkit.getPluginManager().getPlugin("PGF-Essentials")).ActivateListener(new Database(), false);
			}
			
		} catch (IOException e) {
			System.out.println("database.yml Couldn't be created!");
			e.printStackTrace();
		}
		
		file = new File(plugin.getDataFolder() + "\\BlockContainers.yml"); // Creates a File object
		try {
			if (file.createNewFile()) {
				System.out.println("BlockContainers.yml created!");
			} else {
				System.out.println("BlockContainers.yml already Exists!");
				
				Database.loadTeams();
				
				((EssentialsMain) Bukkit.getPluginManager().getPlugin("PGF-Essentials")).ActivateListener(new Database(), false);
			}
			
		} catch (IOException e) {
			System.out.println("BlockContainers.yml Couldn't be created!");
			e.printStackTrace();
		}
		
		file = new File(plugin.getDataFolder() + "\\EABlockData");
		if (file.mkdirs()) {
			System.out.println("EABlockData folder created!");
		} else {
			System.out.println("EABlockData already Exists!");
		}
		
		file = new File(plugin.getDataFolder() + "\\DeepBlockData");
		if (file.mkdirs()) {
			System.out.println("DeepBlockData folder created!");
		} else {
			System.out.println("DeepBlockData already Exists!");
		}
		
		
		ContainerDatabase.loadContainers();
		
		//beacons = BlockDataManager.getBeacons();
		
		getServer().getPluginManager().registerEvents(new InventoryEvents(), this);
		getServer().getPluginManager().registerEvents(new BlockInteractEvent(), this);
		getServer().getPluginManager().registerEvents(new AttackEvent(), this);
		getServer().getPluginManager().registerEvents(new BBEvent(), this);
		getServer().getPluginManager().registerEvents(new BPE(), this);
		getServer().getPluginManager().registerEvents(new MessageEvent(), this);
		getServer().getPluginManager().registerEvents(new InspectCommand(), this);
		
		getCommand("team").setExecutor(new TeamCommand());
		getCommand("Invite").setExecutor(new InviteCommand());
		getCommand("teamAccept").setExecutor(new TeamAccept());
		getCommand("leaveTeam").setExecutor(new LeaveTeamCommand());
		getCommand("leaveTeamConfirm").setExecutor(new LeaveTeamConfirmCommand());
		getCommand("inspector").setExecutor(new InspectCommand());
	}
	
	@Override
	public void onDisable() {
		Database.saveTeams();
		ContainerDatabase.saveContainers();
		((EssentialsMain) Bukkit.getPluginManager().getPlugin("PGF-Essentials")).ActivateListener(new Database(), true);
	}
	
	public static ItemStack createItem(String name, Material mat, List<String> lore)
	{
		ItemStack item = new ItemStack(mat, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public static String makePossesive(String name) {
		if (name.endsWith("s")) {
			return (name + "'");
		} else {
			return (name + "'s");
		}
	}
	
	public static ItemStack createItem(String name, Material mat) // function for creating an item with a custom name
	{
		ItemStack item = new ItemStack(mat, 1);
		ItemMeta meta = item.getItemMeta();
		
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		return item;
	}
	
	@Deprecated
	public static boolean playerInForcefield(Player player) {
		
		//for (Beacon beacon : beacons) {
			
			//Collection<LivingEntity> playersList = beacon.getEntitiesInRange();
			
			//Block block = beacon.getBlock();
			
			//Team DEF = Team.getTeam(BlockDataManager.getContainerData(block).getFirst());
			
			//if (Team.getTeam(player) != DEF && playersList.contains(player)) {
			//	return true;
			//}
		//}
		return false;
		
	}
}

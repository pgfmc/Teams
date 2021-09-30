package net.pgfmc.teams.teamscore;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import net.pgfmc.pgfessentials.EssentialsMain;
import net.pgfmc.teams.data.BBEvent;
import net.pgfmc.teams.data.BPE;
import net.pgfmc.teams.data.BlockInteractEvent;
import net.pgfmc.teams.data.InspectCommand;
import net.pgfmc.teams.data.containers.ContainerDataOutputCommand;
import net.pgfmc.teams.data.containers.ContainerDatabase;
import net.pgfmc.teams.data.entities.DeathEvent;
import net.pgfmc.teams.data.entities.EntityClick;
import net.pgfmc.teams.data.entities.InvOpenEvent;
import net.pgfmc.teams.data.entities.TameEvent;
import net.pgfmc.teams.inventories.InventoryEvents;
import net.pgfmc.teams.playerLogistics.AttackEvent;
import net.pgfmc.teams.playerLogistics.InviteCommand;
import net.pgfmc.teams.playerLogistics.LeaveTeamCommand;
import net.pgfmc.teams.playerLogistics.LeaveTeamConfirmCommand;
import net.pgfmc.teams.playerLogistics.TeamAccept;

public class TeamsCore extends JavaPlugin {
	
	private static TeamsCore plugin;
	
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
				
				TeamsDatabase.loadTeams();
				
				((EssentialsMain) Bukkit.getPluginManager().getPlugin("PGF-Essentials")).ActivateListener(new TeamsDatabase(), false);
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
				
				TeamsDatabase.loadTeams();
			}
			
		} catch (IOException e) {
			System.out.println("BlockContainers.yml Couldn't be created!");
			e.printStackTrace();
		}
		
		file = new File(plugin.getDataFolder() + "\\EntityContainers.yml"); // Creates a File object
		try {
			if (file.createNewFile()) {
				System.out.println("EntityContainers.yml created!");
			} else {
				System.out.println("EntityContainers.yml already Exists!");
				
				TeamsDatabase.loadTeams();
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
		
		getServer().getPluginManager().registerEvents(new InventoryEvents(), this);
		getServer().getPluginManager().registerEvents(new BlockInteractEvent(), this);
		getServer().getPluginManager().registerEvents(new AttackEvent(), this);
		getServer().getPluginManager().registerEvents(new BBEvent(), this);
		getServer().getPluginManager().registerEvents(new BPE(), this);
		getServer().getPluginManager().registerEvents(new MessageEvent(), this);
		getServer().getPluginManager().registerEvents(new InspectCommand(), this);
		getServer().getPluginManager().registerEvents(new EntityClick(), this);
		getServer().getPluginManager().registerEvents(new TameEvent(), this);
		getServer().getPluginManager().registerEvents(new DeathEvent(), this);
		getServer().getPluginManager().registerEvents(new InvOpenEvent(), this);
		
		getCommand("team").setExecutor(new TeamCommand());
		getCommand("Invite").setExecutor(new InviteCommand());
		getCommand("teamAccept").setExecutor(new TeamAccept());
		getCommand("leaveTeam").setExecutor(new LeaveTeamCommand());
		getCommand("leaveTeamConfirm").setExecutor(new LeaveTeamConfirmCommand());
		getCommand("inspector").setExecutor(new InspectCommand());
		getCommand("containerDump").setExecutor(new ContainerDataOutputCommand());
	}
	
	@Override
	public void onDisable() {
		TeamsDatabase.saveTeams();
		ContainerDatabase.saveContainers();
		((EssentialsMain) Bukkit.getPluginManager().getPlugin("PGF-Essentials")).ActivateListener(new TeamsDatabase(), true);
	}
	
	public static JavaPlugin getPlugin() {
		return plugin;
	}
}

package net.pgfmc.teams.teamscore;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import net.pgfmc.pgfessentials.Mixins;
import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;
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
import net.pgfmc.teams.playerLogistics.AttackEvent;
import net.pgfmc.teams.playerLogistics.InviteCommand;
import net.pgfmc.teams.playerLogistics.LeaveTeamCommand;
import net.pgfmc.teams.playerLogistics.LeaveTeamConfirmCommand;
import net.pgfmc.teams.playerLogistics.TeamAccept;

public class TeamsCore extends JavaPlugin {
	
	// all relevant file paths.
	public static final String databasePath = "plugins\\Teams\\database.yml";
	public static final String BlockContainersPath = "plugins\\Teams\\BlockContainers.yml";
	public static final String EntityContainersPath = "plugins\\Teams\\EntityContainers.yml";
	public static final String EasyAccessDataPath = "plugins\\Teams\\EABlockData";
	public static final String DeepBlockDataPath = "plugins\\Teams\\DeepBlockData";
	
	private static TeamsCore plugin;
	private transient static boolean loaded = true;
	
	@Override
	public void onEnable() {
		
		if (Bukkit.getServer().getPluginManager().getPlugin("PGF-Essentials") == null) {
			System.out.println("PGF-Essentials isnt loaded; Disabling Teams!");
			loaded = false;
			Bukkit.getServer().getPluginManager().disablePlugin(this);
			return;
		}
		
		plugin = this;
		
		// loads files.
		Mixins.getFile(databasePath);
		Mixins.getFile(BlockContainersPath);
		Mixins.getFile(EntityContainersPath);
		new File(EasyAccessDataPath).mkdirs();
		new File(DeepBlockDataPath).mkdirs();
		
		// loads container data from files.
		ContainerDatabase.loadContainers();
		
		// initializes all Event Listeners and Command Executors.
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
		
		PlayerData.ActivateListener(new TeamsDatabase(), false);
		
	}
	
	@Override
	public void onDisable() {
		if (loaded) {
			Team.settleTeams();
			TeamsDatabase.saveTeams();
			ContainerDatabase.saveContainers();
			PlayerData.ActivateListener(new TeamsDatabase(), true);
		}
	}
	
	public static JavaPlugin getPlugin() {
		return plugin;
	}
}

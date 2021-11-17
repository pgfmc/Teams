package net.pgfmc.teams.teamscore;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import net.pgfmc.pgfessentials.Mixins;
import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;
import net.pgfmc.pgfessentials.playerdataAPI.PlayerDataListener;
import net.pgfmc.teams.data.BBEvent;
import net.pgfmc.teams.data.BPE;
import net.pgfmc.teams.data.BlockInteractEvent;
import net.pgfmc.teams.data.Ownable.Lock;
import net.pgfmc.teams.data.blocks.ContainerDataOutputCommand;
import net.pgfmc.teams.data.blocks.ContainerDatabase;
import net.pgfmc.teams.data.entities.DeathEvent;
import net.pgfmc.teams.data.entities.EntityClick;
import net.pgfmc.teams.data.entities.InvOpenEvent;
import net.pgfmc.teams.data.entities.TameEvent;
import net.pgfmc.teams.friends.FriendAcceptCommand;
import net.pgfmc.teams.friends.FriendRequestCommand;
import net.pgfmc.teams.friends.Friends;
import net.pgfmc.teams.friends.UnfriendCommand;

public class Main extends JavaPlugin {
	
	// all relevant file paths.
	public static final String databasePath = "plugins\\Teams\\database.yml";
	public static final String BlockContainersPath = "plugins\\Teams\\BlockContainers.yml";
	public static final String EntityContainersPath = "plugins\\Teams\\EntityContainers.yml";
	
	public static Main plugin;
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
		
		PlayerData.ActivateListener(new PlayerDataListener() {

			@Override
			public void OfflinePlayerDataInitialization(PlayerData pd) {
				pd.setData("lockMode", Lock.FRIENDS_ONLY);
				
			}

			@Override
			public void OfflinePlayerDataDeInitialization(PlayerData pd) {}
			
		}, false);
		
		// loads container data from files.
		ContainerDatabase.loadContainers();
		
		// initializes all Event Listeners and Command Executors.
		getServer().getPluginManager().registerEvents(new BlockInteractEvent(), this);
		getServer().getPluginManager().registerEvents(new AttackEvent(), this);
		getServer().getPluginManager().registerEvents(new BBEvent(), this);
		getServer().getPluginManager().registerEvents(new BPE(), this);
		getServer().getPluginManager().registerEvents(new EntityClick(), this);
		getServer().getPluginManager().registerEvents(new TameEvent(), this);
		getServer().getPluginManager().registerEvents(new DeathEvent(), this);
		getServer().getPluginManager().registerEvents(new InvOpenEvent(), this);
		getServer().getPluginManager().registerEvents(new ItemProtect(), this);
		getServer().getPluginManager().registerEvents(new AttackEvent(), this);
		
		getCommand("containerDump").setExecutor(new ContainerDataOutputCommand());
		getCommand("friendRequest").setExecutor(new FriendRequestCommand());
		getCommand("friendAccept").setExecutor(new FriendAcceptCommand());
		getCommand("unfriend").setExecutor(new UnfriendCommand());
	}
	
	@Override
	public void onDisable() {
		if (loaded) {
			ContainerDatabase.saveContainers();
			Friends.save();
		}
	}
	
	public static JavaPlugin getPlugin() {
		return plugin;
	}
}

package net.pgfmc.teams.main;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import net.pgfmc.pgfessentials.Mixins;
import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;
import net.pgfmc.pgfessentials.playerdataAPI.PlayerDataManager;
import net.pgfmc.teams.duel.DuelEvents;
import net.pgfmc.teams.friends.FavoriteCommand;
import net.pgfmc.teams.friends.FriendAcceptCommand;
import net.pgfmc.teams.friends.FriendRequestCommand;
import net.pgfmc.teams.friends.Friends;
import net.pgfmc.teams.friends.Friends.Relation;
import net.pgfmc.teams.friends.FriendsListCommand;
import net.pgfmc.teams.friends.UnfavoriteCommand;
import net.pgfmc.teams.friends.UnfriendCommand;
import net.pgfmc.teams.ownable.Ownable.Lock;
import net.pgfmc.teams.ownable.OwnableFile;
import net.pgfmc.teams.ownable.block.BlockManager;
import net.pgfmc.teams.ownable.block.events.BBEvent;
import net.pgfmc.teams.ownable.block.events.BPE;
import net.pgfmc.teams.ownable.block.events.BlockInteractEvent;
import net.pgfmc.teams.ownable.entities.DeathEvent;
import net.pgfmc.teams.ownable.entities.EntityClick;
import net.pgfmc.teams.ownable.entities.InvOpenEvent;
import net.pgfmc.teams.ownable.entities.TameEvent;

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
		
		PlayerDataManager.setInit(x -> x.setData("lockMode", Lock.FRIENDS_ONLY));
		PlayerDataManager.setInit(x -> x.setData("friends", new HashMap<PlayerData, Relation>()));
		
		PlayerDataManager.setPostLoad((x) -> Friends.load());
		PlayerDataManager.setPostLoad((x) -> OwnableFile.loadContainers());
		
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
		getServer().getPluginManager().registerEvents(new DuelEvents(), this);
		getServer().getPluginManager().registerEvents(Friends.DEFAULT, this);
		//getServer().getPluginManager().registerEvents(new BExEvent(), this);
		getServer().getPluginManager().registerEvents(new BlockManager(), this);
		
		getCommand("friendRequest").setExecutor(new FriendRequestCommand());
		getCommand("friendAccept").setExecutor(new FriendAcceptCommand());
		getCommand("unfriend").setExecutor(new UnfriendCommand());
		getCommand("friendlist").setExecutor(new FriendsListCommand());
		getCommand("favorite").setExecutor(new FavoriteCommand());
		getCommand("unfavorite").setExecutor(new UnfavoriteCommand());
	}
	
	@Override
	public void onDisable() {
		if (loaded) {
			OwnableFile.saveContainers();
			Friends.save();
		}
	}
	
	public static JavaPlugin getPlugin() {
		return plugin;
	}
}

package net.pgfmc.teams.playerLogistics;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.pgfmc.pgfessentials.Mixins;
import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;
import net.pgfmc.pgfessentials.request.Requester;
import net.pgfmc.teams.teamscore.TeamsCore;

public class Friends extends Requester implements Listener {
	
	public static final Friends DEFAULT = new Friends();
	
	public enum Relation {
		SELF,
		NONE,
		FRIEND,
		FAVORITE
	}
	
	/**
	 * HOW FRIENDS DATA IS STORED;
	 * 
	 * Friends data is stored as a table, like this:
	 * 
	 * XXX p1, p2, p3, p4
	 * p1/ S , R , R , R
	 * p2/ R , S , R , R
	 * p3/ R , R , S , R
	 * p4/ R , R , R , S
	 * 
	 * top row is gotten first, then gets the data.
	 * 
	 */
	private static HashMap<PlayerData, HashMap<PlayerData, Relation>> friends = new HashMap<>();
	
	
	private Friends() {
		super("Friend", 120, (x, y) -> {
			
			setRelation(x, Relation.FRIEND, y, Relation.FRIEND);
			
			return true;
		});
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		
	}
	
	/**
	 * Stores data for friends functionality
	 * @param p1 Player 1; 
	 * @param r12 the relationship between p1 and p2 from p1's point of view
	 * @param p2 Player 2;
	 * @param r21 the relationship between p1 and p2 from p2's point of view
	 */
	public static void setRelation(PlayerData p1, Relation r12, PlayerData p2, Relation r21) {
		
		if (p1 == p2) { return; } // if <player> and <friend> are equal
		
		if (friends.get(p1) == null) {
			
			HashMap<PlayerData, Relation> E = new HashMap<>();
			E.put(p2, r12);
			friends.put(p1, E);
		} else {
			
			HashMap<PlayerData, Relation> E = friends.get(p1);
			E.put(p2, r12);
			friends.put(p1, E);
		}
		
		if (friends.get(p2) == null) {
			
			HashMap<PlayerData, Relation> E = new HashMap<>();
			E.put(p1, r21);
			friends.put(p2, E);
		} else {
			
			HashMap<PlayerData, Relation> E = friends.get(p2);
			E.put(p1, r21);
			friends.put(p2, E);
		}
	}
	
	public static void setRelation(PlayerData POV, PlayerData friend, Relation relate) {
		HashMap<PlayerData, Relation> AE = friends.get(POV);
		if (AE == null) {
			
			HashMap<PlayerData, Relation> E = new HashMap<>();
			E.put(friend, relate);
			friends.put(POV, E);
			return;
		} 
		
		AE.put(friend, relate);
		return;
	}
	
	public static Relation getRelation(PlayerData POV, PlayerData friend) {
		HashMap<PlayerData, Relation> AE = friends.get(POV);
		if (AE != null) {
			return AE.get(friend);
		}
		return Relation.NONE;
	}
	
	public static void save() {
		FileConfiguration database = Mixins.getDatabase(TeamsCore.databasePath);
		
		for ( Entry<PlayerData, HashMap<PlayerData, Relation>> entry : friends.entrySet()) {
			
			ConfigurationSection conf = verifyConf(verifyConf(database, "friends"), entry.getKey().getUniqueId().toString());
			
			for (Entry<PlayerData, Relation> val : entry.getValue().entrySet()) {
				
				if (val.getKey() == entry.getKey()) {
					continue;
				}
				
				if (val.getValue() == Relation.NONE || val.getValue() == Relation.SELF) {
					conf.set(val.getKey().getUniqueId().toString(), null);
					continue;
				}
				
				conf.set(val.getKey().getUniqueId().toString(), val.getValue().toString());
			}
		}
		Mixins.saveDatabase(database, TeamsCore.databasePath);
	}
	
	private static ConfigurationSection verifyConf(ConfigurationSection conf, String path) {
		ConfigurationSection out = conf.getConfigurationSection(path);
		
		if (out == null) {
			out = conf.createSection(path);
		}
		
		return out;
		
		
		
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		FileConfiguration database = Mixins.getDatabase(TeamsCore.databasePath);
		PlayerData POV = PlayerData.getPlayerData(e.getPlayer());
		
		ConfigurationSection conf = verifyConf(verifyConf(database, "friends"), POV.getUniqueId().toString());
		
		
		
		
		
		List<PlayerData> list = new LinkedList<>();
		
		
		
		
		
		
		
		//setRelation()
	}
	
	
	
	
}
package net.pgfmc.teams.friends;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import net.pgfmc.pgfessentials.Mixins;
import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;
import net.pgfmc.pgfessentials.requestAPI.Requester;
import net.pgfmc.teams.teamscore.Main;

public class Friends extends Requester implements Listener {
	
	public static final Friends DEFAULT = new Friends();
	
	public enum Relation {
		SELF,
		NONE,
		FRIEND,
		FAVORITE;
		
		public static boolean isFriend(Relation r) {
			return (r == FAVORITE || r == FRIEND);
		}
		
		public boolean isFriend() {
			return isFriend(this);
		}
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
	 * in database.yml, the data is stored in a single column:
	 * 
	 * p1 [ 
	 * 	p2 : R
	 *  p3 : R
	 *  p4 : R
	 * ]
	 * p2 [
	 * 	p1 : R
	 * 	p3 : R
	 * 	.
	 * 	.
	 * 	.
	 * 	.
	 * 
	 * all inferable relations are excluded, such as SELF and NONE
	 * 
	 * 
	 */
	private static Table<String, String, Relation> friends = HashBasedTable.create();
	
	
	
	
	private Friends() {
		super("Friend", 120, (x, y) -> {
			
			setRelation(x.getUniqueId(), Relation.FRIEND, y.getUniqueId(), Relation.FRIEND);
			x.playSound(Sound.BLOCK_AMETHYST_BLOCK_HIT);
			x.playSound(Sound.BLOCK_AMETHYST_BLOCK_HIT);
			
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
	public static void setRelation(UUID p1, Relation r12, UUID p2, Relation r21) {
		
		if (p1 == p2) { return; } // if <player> and <friend> are equal
		
		friends.put(p1.toString(), p2.toString(), r12);
		friends.put(p2.toString(), p1.toString(), r21);
	}
	
	public static void setRelation(UUID POV, UUID friend, Relation relate) {
		friends.put(POV.toString(), friend.toString(), relate);
	}
	
	public static Relation getRelation(UUID POV, UUID friend) {
		if (POV.toString().equals(friend.toString())) {
			return Relation.SELF;
		}
		
		Relation r = friends.get(POV.toString(), friend.toString());
		if (r == null) {
			r = Relation.NONE;
		}
		return r;
	}
	
	public static List<PlayerData> getFriendsList(UUID pd) {
		if (friends.containsColumn(pd.toString())) {
			return friends.column(pd.toString()).entrySet().stream()
					.filter(x -> {
						return (x.getValue() == Relation.FRIEND || x.getValue() == Relation.FAVORITE) ;	
					})
					.map(x -> {
						return PlayerData.getPlayerData(UUID.fromString(x.getKey()));
					})
					.collect(Collectors.toList());
		}
		return new ArrayList<>();
	}
	
	/**
	 * 
	 * Saves all friends to database.yml
	 * 
	 * for each playerdata, it stores a list of all other playerdatas with their statuses.
	 * NONE and SELF statuses are not stored
	 * 
	 * 
	 */
	public static void save() {
		FileConfiguration database = Mixins.getDatabase(Main.databasePath);
		
		for ( Entry<String, Map<String, Relation>> entry : friends.columnMap().entrySet()) {
			
			ConfigurationSection conf = verifyConf(verifyConf(database, "friends"), entry.getKey());
			
			for (Entry<String, Relation> val : entry.getValue().entrySet()) {
				
				if (val.getKey() == entry.getKey()) {
					continue;
				}
				
				if (val.getValue() == Relation.NONE || val.getValue() == Relation.SELF) {
					conf.set(val.getKey(), null);
					continue;
				}
				
				conf.set(val.getKey(), val.getValue().toString());
			}
		}
		Mixins.saveDatabase(database, Main.databasePath);
	}
	
	private static ConfigurationSection verifyConf(ConfigurationSection conf, String path) {
		ConfigurationSection out = conf.getConfigurationSection(path);
		
		if (out == null) {
			out = conf.createSection(path);
		}
		
		return out;
	}
	
	/**
	 * loads all friends
	 */
	public static void load() {
		FileConfiguration database = Mixins.getDatabase(Main.databasePath);
		
		ConfigurationSection friends = verifyConf(database, "friends");
		
		for (PlayerData pd : PlayerData.getPlayerDataList()) {
			
			ConfigurationSection config = friends.getConfigurationSection(pd.getUniqueId().toString());
			if (config == null) {
				continue;
			}
			
			config.getKeys(false).stream()
			.forEach(x-> {
				setRelation(pd.getUniqueId(), UUID.fromString(x), Relation.valueOf(config.getString(x)));
			});
		}
	}
	
	
	
	
	
}
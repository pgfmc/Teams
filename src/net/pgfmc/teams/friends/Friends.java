package net.pgfmc.teams.friends;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import net.pgfmc.pgfessentials.Mixins;
import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;
import net.pgfmc.pgfessentials.request.Requester;
import net.pgfmc.teams.teamscore.Main;
import oshi.util.tuples.Pair;

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
			return friends.columnKeySet().stream()
					.map(x -> {
						return PlayerData.getPlayerData(UUID.fromString(x));
					})
					.collect(Collectors.toList());
		}
		return null;
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
	 * holy crap this is insane
	 * probably a good idea to run this async, because it looks pretty processor heavy
	 * @param e
	 */
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		FileConfiguration database = Mixins.getDatabase(Main.databasePath);
		String POV = e.getPlayer().getUniqueId().toString();
		
		ConfigurationSection friends = verifyConf(database, "friends");
		
		verifyConf(friends, POV).getKeys(false).stream()
		.map(x -> {
			return new Pair<String, Relation>(x, Relation.valueOf(verifyConf(friends, POV).getString(x)));
		})
		.filter(x -> {
			if (x.getA() == null || x.getB() == null || x.getB() == Relation.NONE || x.getB() == Relation.SELF) {
				return false;
			}
			return true;
		})
		.forEach(x -> {
			
			ConfigurationSection friend = friends.getConfigurationSection(x.getA());
			
			Relation FPOV;
			if (friend != null) {
				FPOV = Relation.valueOf(friend.getString(POV));
			} else {
				if (x.getB() == Relation.FRIEND || x.getB() == Relation.FAVORITE) {
					FPOV = Relation.FRIEND;
				} else {
					FPOV = Relation.NONE;
				}
			}
			
			setRelation(UUID.fromString(POV), x.getB(), UUID.fromString(x.getA()), FPOV); // FOIL be like
			return;
		});
	}
	
	
}
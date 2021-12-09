package net.pgfmc.teams.friends;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;

import net.pgfmc.pgfessentials.Mixins;
import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;
import net.pgfmc.pgfessentials.requestAPI.Requester;
import net.pgfmc.teams.main.Main;

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
	
	private Friends() {
		super("Friend", 120, (x, y) -> {
			
			setRelation(x, Relation.FRIEND, y, Relation.FRIEND);
			x.playSound(Sound.BLOCK_AMETHYST_BLOCK_HIT);
			x.playSound(Sound.BLOCK_AMETHYST_BLOCK_HIT);
			
			return true;
		});
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
		
		getFriendsMap(p1).put(p2, r12);
		getFriendsMap(p2).put(p1, r21);
		
		save();
	}
	
	public static void setRelation(PlayerData POV, PlayerData friend, Relation relate) {
		
		if (POV != null && friend != null && relate != null) {
			getFriendsMap(POV).put(friend, relate);
			save();
		}
	}
	
	public static Relation getRelation(PlayerData POV, PlayerData friend) {
		if (POV.toString().equals(friend.toString())) {
			return Relation.SELF;
		}
		HashMap<PlayerData, Relation> list = POV.getData("friends");
		
		Relation r = list.get(friend);
		if (r == null) {
			r = Relation.NONE;
		}
		return r;
	}
	
	public static HashMap<PlayerData, Relation> getFriendsMap(PlayerData pd) {
		
		return pd.getData("friends");
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
	@SuppressWarnings("unchecked")
	public static void save() {
		FileConfiguration database = Mixins.getDatabase(Main.databasePath);
		
		for (PlayerData pd: PlayerData.getPlayerDataSet()) {
			
			ConfigurationSection conf = verifyConf(verifyConf(database, "friends"), pd.getUniqueId().toString());
			
			for (Entry<PlayerData, Relation> val : ((HashMap<PlayerData, Relation>) pd.getData("friends")).entrySet()) {
				
				if (val.getValue() == Relation.NONE || val.getValue() == Relation.SELF) {
					conf.set(val.getKey().getUniqueId().toString(), null);
					continue;
				}
				
				conf.set(val.getKey().getUniqueId().toString(), val.getValue().toString());
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
		
		for (PlayerData pd : PlayerData.getPlayerDataSet()) {
			ConfigurationSection config = friends.getConfigurationSection(pd.getUniqueId().toString());
			if (config == null) {
				return;
			}
			
			config.getKeys(false).stream()
			.forEach(x-> {
				setRelation(pd, PlayerData.getPlayerData(UUID.fromString(x)), Relation.valueOf(config.getString(x)));
			});
		}
	}
}
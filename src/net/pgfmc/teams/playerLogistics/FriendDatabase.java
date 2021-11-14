package net.pgfmc.teams.playerLogistics;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.configuration.file.FileConfiguration;

import net.pgfmc.pgfessentials.Mixins;
import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;
import net.pgfmc.pgfessentials.playerdataAPI.PlayerDataListener;
import net.pgfmc.teams.data.containers.Ownable.Lock;
import net.pgfmc.teams.teamscore.TeamsCore;

public class FriendDatabase implements PlayerDataListener {
	
	@Override
	public void OfflinePlayerDataDeInitialization(PlayerData pd) {
		
		//fixFriends();
		
		FileConfiguration database = Mixins.getDatabase(TeamsCore.databasePath);
		
		List<PlayerData> lst = pd.getData("friends");
		
		if (lst == null) {
			return;
		}
		
		database.set(pd.getUniqueId().toString(), lst
				.stream()
				.map(x -> {
					return x.getUniqueId().toString();
				}).collect(Collectors.toList()));
		
		Mixins.saveDatabase(database, TeamsCore.databasePath);
	}

	@Override
	public void OfflinePlayerDataInitialization(PlayerData pd) {
		
		FileConfiguration database = Mixins.getDatabase(TeamsCore.databasePath);
		
		@SuppressWarnings("unchecked")
		List<String> lst = (List<String>) database.getList(pd.getUniqueId().toString());
		
		if (lst != null) {
			pd.setData("friends", lst.stream().map((x) -> {
				return PlayerData.getPlayerData(UUID.fromString(x));
			}).collect(Collectors.toList()));
		} else {
			pd.setData("friends", new LinkedList<PlayerData>());
		}
		
		pd.setData("lockMode", Lock.FRIENDS_ONLY);
		
		
		
	}
}

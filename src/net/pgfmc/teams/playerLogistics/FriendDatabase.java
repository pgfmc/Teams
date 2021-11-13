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
	
	
	/*
	private class Friend {
		
		PlayerData f1;
		PlayerData f2;
		private boolean confirmation = false;
		
		public Friend(PlayerData f1, PlayerData f2) {
			this.f1 = f1;
			this.f2 = f2;
		}
		
		public void setConfirm() {
			confirmation = true;
		}
		
		public boolean isConfirm() {
			return confirmation;
		}
	}
	
	private static List<Friend> list = new LinkedList<Friend>();
	
	private Friend findFriends(PlayerData fx, PlayerData fy) {
		
		for (Friend f : list) {
			
			if (((f.f1 == fx && f.f2 == fy) || (f.f2 == fx && f.f1 == fy)) && !f.isConfirm()) {
				return f;
			}
		}
		
		return null;
		
	}
	
	public void fixFriends() {
		
		PlayerData.stream()
		.forEach(x -> {
			
			List<PlayerData> fList = x.getData("friends");
			
			for (PlayerData pd : fList) {
				
				Friend F = findFriends(x, pd);
				if (F == null) {
					list.add(new Friend(x, pd));
				} else {
					F.setConfirm();
				}
			}
		});
		
		for (Friend F : list) {
			
			if (F.isConfirm()) {
				
				List<PlayerData> lizt = F.f1.getData("friends");
				lizt.add(F.f2);
				
				lizt = F.f2.getData("friends");
				lizt.add(F.f1);
			}
		}
	}
	*/
	
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

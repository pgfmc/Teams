package net.pgfmc.teams.inventory;

import org.bukkit.Material;

import net.pgfmc.pgfessentials.inventoryAPI.InteractableInventory;
import net.pgfmc.pgfessentials.inventoryAPI.PagedInventory;
import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;
import net.pgfmc.teams.friends.Friends;

public class FriendsInventory {
	
	/**
	 * Friends List inventory
	 * Opens the player's friends list
	 * @author CrimsonDart
	 *
	 */
	public static class FriendsList extends PagedInventory<PlayerData> {
		
		public FriendsList(PlayerData player) {
			super(SizeData.SMALL, "Friends List", Friends.getFriendsList(player.getUniqueId()), (x) -> {
				return createButton(Material.PAPER, "§o" + x.getRankedName(), null, (p, e) -> {
					
					p.openInventory(new FriendOptions(player, x).getInventory());
					
				});
			});
		}
	}
	
	/**
	 * Friend Options inventory
	 * allows Unfriend, Favorite (or Unfavorite), block, report ..../????
	 * 
	 * 
	 * @author james
	 *
	 */
	public static class FriendOptions extends InteractableInventory {

		public FriendOptions(PlayerData player, PlayerData friend) {
			super(27, "Options for §o" + friend.getRankedName());
			
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
}

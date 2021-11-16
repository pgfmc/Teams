package net.pgfmc.teams.inventory;

import org.bukkit.Material;

import net.pgfmc.pgfessentials.inventoryAPI.PagedInventory;
import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;
import net.pgfmc.teams.friends.Friends;

public class FriendsInventory extends PagedInventory<PlayerData> {

	public FriendsInventory(PlayerData player) {
		super(SizeData.SMALL, "Friends List", Friends.getFriendsList(player.getUniqueId()), (x) -> {
			return createButton(Material.PAPER, "§o" + x.getRankedName(), null, (p, e) -> {
				
				// insert new inventory here
				
			});
		});
	}
}

package net.pgfmc.teams.inventory;

import org.bukkit.Material;

import net.pgfmc.pgfessentials.inventoryAPI.PagedInventory;
import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;

public class FriendsInventory extends PagedInventory<PlayerData> {

	public FriendsInventory(PlayerData player) {
		super(SizeData.SMALL, "Friends List", player.getData("friends"), (x) -> {
			return createButton(Material.PAPER, "§o" + x.getRankedName(), null, (p, e) -> {
				
				// insert new inventory here
				
			});
		});
	}
}

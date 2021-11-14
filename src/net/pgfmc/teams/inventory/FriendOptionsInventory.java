package net.pgfmc.teams.inventory;

import net.pgfmc.pgfessentials.inventoryAPI.InteractableInventory;
import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;


public class FriendOptionsInventory extends InteractableInventory {

	public FriendOptionsInventory(PlayerData player, PlayerData friend) {
		super(27, "Options for §o" + friend.getRankedName());
		
		//createButton(Material.)
		
		
	}

}

package net.pgfmc.teams.inventory;

import org.bukkit.Material;
import org.bukkit.Sound;

import net.pgfmc.pgfessentials.inventoryAPI.InteractableInventory;
import net.pgfmc.pgfessentials.inventoryAPI.PagedInventory;
import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;
import net.pgfmc.teams.friends.Friends;
import net.pgfmc.teams.friends.Friends.Relation;

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
			super(27, "Options for " + friend.getRankedName());
			
			createButton(Material.ARROW, 12, "Unfriend", (p, e) -> {
				Friends.setRelation(player.getUniqueId(), Relation.NONE, friend.getUniqueId(), Relation.NONE);
				player.sendMessage("§6You have Unfriended " + friend.getName() + ".");
				player.playSound(Sound.BLOCK_CALCITE_HIT);
				player.getPlayer().closeInventory();
			});
			
			Relation r = Friends.getRelation(player.getUniqueId(), friend.getUniqueId());
			
			if (r == Relation.FRIEND) {
				createButton(Material.NETHER_STAR, 14, "Favorite", (p, e) -> {
					
					Friends.setRelation(player.getUniqueId(), friend.getUniqueId(), Relation.FAVORITE);
					player.sendMessage(friend.getName() + " is now a favorite!");
					player.playSound(Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
					player.getPlayer().closeInventory();
					
				});
			} else if (r == Relation.FAVORITE) {
				createButton(Material.NETHER_STAR, 14, "Unfavorite", (p, e) -> {
					
					Friends.setRelation(player.getUniqueId(), friend.getUniqueId(), Relation.FRIEND);
					player.sendMessage(friend.getName() + " has Been unfavorited!");
					player.playSound(Sound.BLOCK_CALCITE_HIT);
					player.getPlayer().closeInventory();
					
				});
			}
			
			
			
			
			
			
			
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
}

package net.pgfmc.teams.friends;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;
import net.pgfmc.teams.inventory.FriendsInventory;

public class FriendsListCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!(sender instanceof Player)) {
			return true;
		}
		
		((Player) sender).openInventory(new FriendsInventory.FriendsList(PlayerData.getPlayerData((Player) sender)).getInventory());
		
		return true;
	}

}

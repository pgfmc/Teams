package net.pgfmc.teams.friends;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;
import net.pgfmc.teams.friends.Friends.Relation;

public class UnfavoriteCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!(sender instanceof Player)) {
			return true;
		}
		
		if (args == null || args[0] == null) {
			return true;
		}
		
		PlayerData friend = PlayerData.getPlayerData(args[0]);
		
		if (friend == null) {
			return true;
		}
		
		Friends.setRelation(((Player) sender).getUniqueId(), friend.getUniqueId(), Relation.FRIEND);
		sender.sendMessage(args[0] + " is Unfavorited!");
		
		return true;
	}
}

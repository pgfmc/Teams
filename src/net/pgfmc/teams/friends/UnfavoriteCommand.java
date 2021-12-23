package net.pgfmc.teams.friends;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.teams.friends.Friends.Relation;

/**
 * Command to Unfavorite a favorited player.
 * 
 * @author CrimsonDart
 * @since 1.2.0	
 */
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
		
		Friends.setRelation(PlayerData.getPlayerData((OfflinePlayer) sender), friend, Relation.FRIEND);
		sender.sendMessage("§cUnfavorited §n" + friend.getRankedName() + "§r§c.");
		
		return true;
	}
}

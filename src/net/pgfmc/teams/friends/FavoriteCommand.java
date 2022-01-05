package net.pgfmc.teams.friends;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.teams.friends.Friends.Relation;
/**
 * Command for Favoriting a Friend.
 * 
 * @author CrimsonDart
 * @since 1.2.0	
 * @version 4.0.3
 */
public class FavoriteCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!(sender instanceof Player)) {
			sender.sendMessage("§cOnly players can use thsi command!");
			return true;
		}
			
		if (args == null || args[0].isBlank()) {
			return false;
		}
		
		PlayerData friend = PlayerData.getPlayerData(args[0]);
		
		if (friend == null) {
			sender.sendMessage("§cCouldn't find player " + args[0] + ".");
			return true;
		}
		
		PlayerData player = PlayerData.getPlayerData((Player) sender);
		
		switch(Friends.getRelation(player, friend)) {
		case FAVORITE:
			sender.sendMessage("§n" + friend.getRankedName() + "§r§6 is already favorited!");
			break;
			
		case FRIEND:
			Friends.setRelation(player, friend, Relation.FAVORITE);
			sender.sendMessage("§aFavorited §n" + friend.getRankedName() + "§r§c.");
			break;
			
		case NONE:
			sender.sendMessage("§n" + friend.getRankedName() + "§r§c isn't your friend.");
			break;
			
		case SELF:
			sender.sendMessage("§6You can't favorite yourself!");
			break;
		}
		
		return true;
	}
}
package net.pgfmc.teams.friends;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.teams.friends.Friends.Relation;

/**
 * Command to unfriend a Friend. lots a cheques lool
 * 
 * @author CrimsonDart
 * @since 1.2.0
 * @version 4.0.3
 */
public class UnfriendCommand implements CommandExecutor {
	
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
			Friends.setRelation(player, Relation.NONE, friend, Relation.NONE);
			sender.sendMessage("§cUnfriended §n" + friend.getRankedName() + "§r§c.");
			player.playSound(Sound.BLOCK_CALCITE_HIT);
			break;
			
		case FRIEND:
			Friends.setRelation(player, Relation.NONE, friend, Relation.NONE);
			sender.sendMessage("§cUnfriended §n" + friend.getRankedName() + "§r§c.");
			player.playSound(Sound.BLOCK_CALCITE_HIT);
			break;
			
		case NONE:
			sender.sendMessage("§n" + friend.getRankedName() + "§r§c isn't your friend.");
			break;
			
		case SELF:
			sender.sendMessage("§6 You can't unfriend yourself!");
			break;
		}
		
		return true;
	}
}
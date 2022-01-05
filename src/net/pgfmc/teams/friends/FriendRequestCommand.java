package net.pgfmc.teams.friends;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.core.requestAPI.Request;

/**
 * Command for sending a Friend Request.
 * 
 * @author CrimsonDart
 * @since 1.2.0	
 */
public class FriendRequestCommand implements CommandExecutor {
	
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
		
		Request r = Friends.DEFAULT.findRequest((Player) sender, (Player) sender);
		
		
		
		
		
		
		switch(Friends.getRelation(player, friend)) {
		case FAVORITE:
			sender.sendMessage("§n" + friend.getRankedName() + "§r§6 is already your friend!");
			break;
			
		case FRIEND:
			sender.sendMessage("§n" + friend.getRankedName() + "§r§6 is already your friend!");
			break;
			
		case NONE:
			Friends.DEFAULT.createRequest((Player) sender, Bukkit.getPlayer(args[0]));
			sender.sendMessage("§aFriend Request send to §n" + friend.getRankedName() + "§r§a.");
			break;
			
		case SELF:
			sender.sendMessage("§r§6You can't friend yourself!");
			break;
		}
		
		
		
		
		return true;
	}
}

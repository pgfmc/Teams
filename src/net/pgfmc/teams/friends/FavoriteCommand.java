<<<<<<< HEAD
package net.pgfmc.teams.friends;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.teams.friends.Friends.Relation;

public class FavoriteCommand implements CommandExecutor {

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
		
		Friends.setRelation(PlayerData.getPlayerData((Player) sender), friend, Relation.FAVORITE);
		sender.sendMessage("§n" + friend.getRankedName() + "§r§a is now a favorite!");
		
		return true;
	}
	
}
=======
package net.pgfmc.teams.friends;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;
import net.pgfmc.teams.friends.Friends.Relation;

public class FavoriteCommand implements CommandExecutor {

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
		
		Friends.setRelation(PlayerData.getPlayerData((Player) sender), friend, Relation.FAVORITE);
		sender.sendMessage("§n" + friend.getRankedName() + "§r§a is now a favorite!");
		
		return true;
	}
	
}
>>>>>>> parent of ec489d7 (k)

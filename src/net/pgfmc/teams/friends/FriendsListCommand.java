<<<<<<< HEAD
package net.pgfmc.teams.friends;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pgfmc.core.playerdataAPI.PlayerData;

public class FriendsListCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!(sender instanceof Player)) {
			return true;
		}
		
		for (PlayerData pd : Friends.getFriendsMap(PlayerData.getPlayerData((Player) sender)).keySet()) {
			sender.sendMessage("§n" + pd.getRankedName());
		}
		
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

public class FriendsListCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!(sender instanceof Player)) {
			return true;
		}
		
		for (PlayerData pd : Friends.getFriendsMap(PlayerData.getPlayerData((Player) sender)).keySet()) {
			sender.sendMessage("§n" + pd.getRankedName());
		}
		
		return true;
	}

}
>>>>>>> parent of ec489d7 (k)

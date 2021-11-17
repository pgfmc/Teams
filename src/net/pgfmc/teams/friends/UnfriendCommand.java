package net.pgfmc.teams.friends;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;

public class UnfriendCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		
		PlayerData p1 = PlayerData.getPlayerData((OfflinePlayer) sender);
		PlayerData p2;
		
		if (!(sender instanceof Player))
		{
			sender.sendMessage("§oThis isnt a player.");
			return false;
		} else if (p1 == null) {
			
			sender.sendMessage("§oThis isnt a player.");
			return true;
		} else if (args[0].length() == 0) { // bk w here
			return false;
		} else if (Bukkit.getPlayer(args[0]) == null) {
			sender.sendMessage("§oEnter a valid player.");
			return true;
		} if (!Friends.getFriendsList(p1.getUniqueId()).contains(PlayerData.getPlayerData(Bukkit.getPlayer(args[0])))) { // and he
			sender.sendMessage("§o" + args[0] + " is not in your friends list!");
			return true;
		}
		
		p2 = PlayerData.getPlayerData(Bukkit.getPlayer(args[0])); // & ere
		
		List<PlayerData> list = p1.getData("friends");
		list.remove(p2);
		
		list = p2.getData("friends");
		list.remove(p1);
		p1.sendMessage("§6You have Unfriended " + args[0] + ".");
		
		return true;
	}
}
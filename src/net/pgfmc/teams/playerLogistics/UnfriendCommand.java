package net.pgfmc.teams.playerLogistics;

import java.util.List;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;

public class UnfriendCommand implements CommandExecutor {
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		
		PlayerData p1 = PlayerData.getPlayerData((OfflinePlayer) sender);
		PlayerData p2 = PlayerData.getPlayerData(args[0]);
		
		if (!(sender instanceof Player))
		{
			sender.sendMessage("�oThis isnt a player.");
			return false;
		} else if (p1 == null) {
			
			sender.sendMessage("�oThis isnt a player.");
			return true;
		} else if (p2 == null) {
			sender.sendMessage("�oEnter a valid player.");
			return true;
		} else if (!((List<PlayerData>) p1.getData("friends")).contains(p2)) {
			sender.sendMessage("�o" + args[0] + " is not in your friends list!");
			return true;
		}
		
		List<PlayerData> list = p1.getData("friends");
		list.remove(p2);
		
		list = p2.getData("friends");
		list.remove(p1);
		p1.sendMessage("�6You have Unfriended " + args[0] + ".");
		
		return true;
	}
}
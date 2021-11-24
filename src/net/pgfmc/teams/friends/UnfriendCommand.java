package net.pgfmc.teams.friends;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;
import net.pgfmc.teams.friends.Friends.Relation;

public class UnfriendCommand implements CommandExecutor, TabCompleter {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		PlayerData p1 = PlayerData.getPlayerData((OfflinePlayer) sender);
		OfflinePlayer target = null;
		
		if (!(sender instanceof Player)) {
			sender.sendMessage("§oThis isnt a player.");
			return false;
		}
		
		if (p1 == null) {
			sender.sendMessage("§oThis isnt a player.");
			return true;
		}
		
		if (args[0].length() == 0) { // bk w here
			return false;
		} 
		
		for (OfflinePlayer op : Bukkit.getOfflinePlayers()) {
			if (op.getName().equals(args[0])) {
				target = op;
				break;
			}
		}
		if (target == null) {
			sender.sendMessage("Enter a valid player!");
			return true;
		}
		
		if (!Friends.getFriendsList(p1.getUniqueId()).contains(PlayerData.getPlayerData(target))) { // and he
			sender.sendMessage("§o" + args[0] + " is not in your friends list!");
			return true;
		}
		
		Friends.setRelation(p1.getUniqueId(), Relation.NONE, target.getUniqueId(), Relation.NONE);
		p1.sendMessage("§6You have Unfriended " + args[0] + ".");
		p1.playSound(Sound.BLOCK_CALCITE_HIT);
		
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player)) {
			return null;
		}
		
		PlayerData p1 = PlayerData.getPlayerData((OfflinePlayer) sender);
		
		if (p1 == null) {
			return null;
		}
		
		if (!(args == null || args[0] == null)) {
			return null;
		}
		
		return Friends.getFriendsList(p1.getUniqueId()).stream()
				.map(x -> {
					return x.getName();
				})
				.collect(Collectors.toList());
	}
}
package net.pgfmc.teams.friends;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;
import net.pgfmc.teams.friends.Friends.Relation;

public class UnfriendCommand implements CommandExecutor{
	
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
			sender.sendMessage("§n" + PlayerData.getPlayerData(target).getRankedName() + "§r§c is not in your friends list!");
			return true;
		}
		
		Friends.setRelation(p1.getUniqueId(), Relation.NONE, target.getUniqueId(), Relation.NONE);
		p1.sendMessage("§cYou have Unfriended §n" + PlayerData.getPlayerData(target).getRankedName() + "§r§c.");
		p1.playSound(Sound.BLOCK_CALCITE_HIT);
		
		return true;
	}
}
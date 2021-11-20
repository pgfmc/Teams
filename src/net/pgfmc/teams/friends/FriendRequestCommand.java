package net.pgfmc.teams.friends;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FriendRequestCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player)) {
			
			sender.sendMessage("Data output to the console.");
			return false;
		} else if (Bukkit.getPlayer(args[0]) == null) {
			sender.sendMessage("Enter in a valid player!");
			return true;
		} else if (((Player) sender).getUniqueId().equals(Bukkit.getPlayer(args[0]).getUniqueId())) {
			sender.sendMessage("You can't friend yourself!");
			return true;
		} else if (Friends.getRelation(((Player) sender).getUniqueId(), Bukkit.getPlayer(args[0]).getUniqueId()).isFriend()) {
			sender.sendMessage("They are already your friend!");
			return true;
		}
		
		Friends.DEFAULT.createRequest((Player) sender, Bukkit.getPlayer(args[0]));
		
		return true;
	}
}
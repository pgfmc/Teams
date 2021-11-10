package net.pgfmc.teams.playerLogistics;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FriendRequestCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (!(sender instanceof Player))
		{
			sender.sendMessage("Data output to the console.");
		} else if (Bukkit.getPlayer(args[0]) == null) {
			sender.sendMessage("Enter in a valid player!");
		}
		
		FriendRequest.DEFAULT.createRequest((Player) sender, Bukkit.getPlayer(args[0]));
		
		return true;
	}
}

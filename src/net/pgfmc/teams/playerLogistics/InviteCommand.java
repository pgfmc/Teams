package net.pgfmc.teams.playerLogistics;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InviteCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!(sender instanceof Player))
		{
			sender.sendMessage("You must be in game to run this command.");
			return true;
		}
		
		if (Bukkit.getPlayer(args[0]) != null) {
			PendingRequest.requestHandler((Player) sender, (Player) Bukkit.getPlayer(args[0]));
		}
		
		return true;
	}
}
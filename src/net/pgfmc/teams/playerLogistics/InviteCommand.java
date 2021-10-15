package net.pgfmc.teams.playerLogistics;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/*
Written by CrimsonDart

-----------------------------------

Invite Command (for inviting people to your team)

-----------------------------------
 */

public class InviteCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!(sender instanceof Player))
		{
			sender.sendMessage("You must be in game to run this command.");
			return true;
		}
		
		if (args.length != 0) {
			PendingRequest.requestHandler((Player) sender, (Player) Bukkit.getPlayer(args[0]));
		}
		
		return true;
	}
}
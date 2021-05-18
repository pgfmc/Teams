package net.pgfmc.teams.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pgfmc.teams.Main;

public class TeamAccept implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		
		if (sender instanceof Player) {
			Main.requestHandler((Player) sender);
		}
		
		return false;
	}

}

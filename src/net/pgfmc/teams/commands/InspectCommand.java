package net.pgfmc.teams.commands;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pgfmc.teams.PlayerData;

/*
Command to Enable and Disable Inspector mode (only in creative mode)
in PlayerEvents there is a function setup that automatically disables inspector when leaving creative mode WIP

Written By CrimsonDart
 */

public class InspectCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (!(sender instanceof Player)) {
			sender.sendMessage("You must be in game to run this command.");
			return true;
		} else if (((Player) sender).getGameMode() != GameMode.CREATIVE) {
			sender.sendMessage("You must be in Creative mode to run this command.");
			return true;
		}
		
		PlayerData PD = PlayerData.findPlayerData((Player) sender);
		if (PD.getDebug()) {
			PD.setDebug(false);
			sender.sendMessage("Disabled Inspect Mode.");
		} else {
			PD.setDebug(true);
			sender.sendMessage("Enabled Inspect Mode.");
		}
		
		return true;
	}
}

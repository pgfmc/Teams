package net.pgfmc.teams.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pgfmc.teams.TeamObj;
import net.pgfmc.teams.inventories.VoteInventory;

/*
Command that opens the vote inventory

Written by CrimsonDart
 */



public class VoteCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!(sender instanceof Player))
		{
			sender.sendMessage("You must be in game to run this command.");
			return true;
		}
		
		((Player) sender).openInventory(new VoteInventory(TeamObj.findPlayer(((Player) sender)).getVote()).getInventory());
		
		return false;
	}
}

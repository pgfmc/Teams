package net.pgfmc.teams.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pgfmc.teams.TeamObj;
import net.pgfmc.teams.Vote;
import net.pgfmc.teams.Vote.VoteCases;

public class VoteKick implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!(sender instanceof Player))
		{
			sender.sendMessage("You must be in game to run this command.");
			return true;
		}
		
		TeamObj team = TeamObj.getTeam((Player) sender);
		
		if (Bukkit.getPlayer(args[0]) != null && TeamObj.getTeam(Bukkit.getPlayer(args[0])) == team) {
			new Vote(team.getMembers(), team, Bukkit.getPlayer(args[0]).getUniqueId(), VoteCases.KICKPLAYER).vote((Player) sender, 1);
		}
		
		return true;
	}
}
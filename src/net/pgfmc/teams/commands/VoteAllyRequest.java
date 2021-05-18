package net.pgfmc.teams.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pgfmc.teams.TeamObj;
import net.pgfmc.voting.Vote;
import net.pgfmc.voting.Vote.VoteCases;

public class VoteAllyRequest implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!(sender instanceof Player))
		{
			sender.sendMessage("You must be in game to run this command.");
			return true;
		}
		
		TeamObj team = TeamObj.findPlayer((Player) sender);
		
		if (TeamObj.findID(UUID.fromString(args[0])) != null && TeamObj.findID(UUID.fromString(args[0])) != team) {
			new Vote(team.getMembers(), team, Bukkit.getPlayer(args[0]).getUniqueId(), VoteCases.ALLYTEAMREQUEST).vote((Player) sender, 1);
		}
		
		return false;
	}
	
	
	
	
	
}

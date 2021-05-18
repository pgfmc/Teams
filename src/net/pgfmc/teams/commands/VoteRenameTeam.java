package net.pgfmc.teams.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pgfmc.teams.TeamObj;
import net.pgfmc.voting.Vote;
import net.pgfmc.voting.Vote.VoteCases;

public class VoteRenameTeam implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!(sender instanceof Player))
		{
			sender.sendMessage("You must be in game to run this command.");
			return true;
		}
		
		TeamObj team = TeamObj.findPlayer((Player) sender);
		
		if (team.getName() != args[0]) {
			new Vote(team.getMembers(), team, args[0], VoteCases.RENAMETEAM).vote((Player) sender, 1);
		}
		
		return false;
	}
	
	
	
}

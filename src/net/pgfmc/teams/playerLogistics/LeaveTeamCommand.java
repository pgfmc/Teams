package net.pgfmc.teams.playerLogistics;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveTeamCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (!(sender instanceof Player))
		{
			sender.sendMessage("You must be in game to run this command.");
			return true;
		}
		
		Player p = (Player) sender;
		
		p.sendMessage("§dAre you sure you want to leave your team?");
		p.sendMessage("§dIf you leave your team, you can still join back.");
		p.sendMessage("§dIf you wish to leave, then type §b/leaveTeamConfirm §dor §b/ltc.");
		return true;
	}
}
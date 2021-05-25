package net.pgfmc.teams.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pgfmc.teams.PlayerData;

public class LeaveTeamConfirmCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (!(sender instanceof Player))
		{
			sender.sendMessage("You must be in game to run this command.");
			return true;
		}
		
		Player p = (Player) sender;
		
		PlayerData playerData = PlayerData.findPlayerData(p);
		playerData.getTeam().removePlayer(p);
		playerData.setTeam(null);
		p.sendMessage("You have left " + playerData.getTeam().getName() + ".");
		return true;
	}
}
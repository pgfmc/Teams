package net.pgfmc.teams.playerLogistics;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;
import net.pgfmc.teams.teamscore.Team;

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
		
		PlayerData playerData = PlayerData.getPlayerData(p);
		if (playerData.getData("team") != null) {
			((Team) playerData.getData("team")).removePlayer(p);
			playerData.setData("team", null);
			p.sendMessage("You have left " + ((Team) playerData.getData("team")).getName() + ".");
			return true;
		} else {
			p.sendMessage("You aren't in a team!");
			return true;
		}
	}
}
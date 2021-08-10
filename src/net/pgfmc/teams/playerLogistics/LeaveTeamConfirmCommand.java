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
			p.sendMessage("§dYou have left §a§l" + ((Team) playerData.getData("team")).getName() + "§r§d.");
			((Team) playerData.getData("team")).removePlayer(p);
			playerData.setData("team", null);
			return true;
		} else {
			p.sendMessage("§cYou aren't in a team!");
			return true;
		}
	}
}
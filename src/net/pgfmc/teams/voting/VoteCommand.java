package net.pgfmc.teams.voting;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pgfmc.teams.inventories.VoteBaseInv;
import net.pgfmc.teams.teamscore.Team;

public class VoteCommand implements CommandExecutor {
	

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] arg3) {

		if (!(sender instanceof Player) || ((Player) sender).getGameMode() != GameMode.SURVIVAL)
		{
			sender.sendMessage("§cYou cannot execute this command."); // lol
			return true;
		}
		
		Player p = (Player) sender; // gets Base (all of the listings) and then opens the gui for them
		
		VoteBaseInv gui = new VoteBaseInv(p, Team.getTeam(p));
		p.openInventory(gui.getInventory());
		
		return true;
	}
}
package net.pgfmc.teams.data;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import net.pgfmc.pgfessentials.EssentialsMain;
import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;


/*
Command to Enable and Disable Inspector mode (only in creative mode)
in PlayerEvents there is a function setup that automatically disables inspector when leaving creative mode WIP

Written By CrimsonDart
 */

public class InspectCommand implements CommandExecutor, Listener {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (!(sender instanceof Player)) {
			sender.sendMessage("You must be in game to run this command.");
			return true;
		} else if (((Player) sender).getGameMode() != GameMode.CREATIVE) {
			sender.sendMessage("�cYou must be in Creative mode to run this command.");
			return true;
		}
		
		PlayerData PD = PlayerData.getPlayerData((Player) sender);
		if (PD.getData("debug") != null) {
			PD.setData("debug", null);
			sender.sendMessage("�bDisabled Inspect Mode.");
		} else {
			PD.setData("debug", true);
			sender.sendMessage("�bEnabled Inspect Mode.");
		}
		
		return true;
	}

	@EventHandler
	public void gamemodeChange(PlayerGameModeChangeEvent e) { // if gamemode changes from 
		
		if (e.getPlayer().getGameMode() == GameMode.CREATIVE) {
			PlayerData.setData(e.getPlayer(), "debug", null);
		}
	}
	
	
	@EventHandler
	public void move(PlayerMoveEvent e) { // if player moves from survival world to anywhere else, disables inspect mode.
		
		if (EssentialsMain.isSurvivalWorld(e.getPlayer().getLocation().getWorld()) && !EssentialsMain.isSurvivalWorld(e.getTo().getWorld())) {
			PlayerData.setData(e.getPlayer(), "debug", null);
		}
	}
}
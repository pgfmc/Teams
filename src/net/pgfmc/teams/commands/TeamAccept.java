package net.pgfmc.teams.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pgfmc.teams.Main;
import net.pgfmc.teams.PendingRequest;
import net.pgfmc.teams.PlayerData;
import net.pgfmc.teams.TeamObj;

public class TeamAccept implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		
		if (sender instanceof Player) {
			// variable declaration
			
			Player attacker = (Player) sender;
			
			TeamObj ATK = TeamObj.findPlayer(attacker);
			PlayerData ATKP = PlayerData.findPlayerData(attacker);
			
			if (ATKP.getRequest() != null) {
				Player target = ATKP.getRequest().getAttacker();
				TeamObj DEF = TeamObj.findPlayer(target);
				PlayerData DEFP = PlayerData.findPlayerData(target);
				
				if (ATKP.getRequest() != null && ATKP.getRequest() == DEFP.getRequest()) {
					
					PendingRequest PR = ATKP.getRequest();
					
					if (ATK != null && DEF == null) { // if the attacker isnt in a team, but the target is
						for (UUID uuid : ATK.getMembers()) {
							if (Bukkit.getPlayer(uuid) != null) {
								Bukkit.getPlayer(uuid).sendMessage(attacker.getCustomName() + " has joined your team!");
							}
						}
						PR.acceptRequest(false);
						attacker.sendMessage("You have joined " + ATK.getName() + "!");
						
					
					} else if (ATK == null && DEF == null) { // if both players arent on a team
						PR.createTeamRequestAccept();
						attacker.sendMessage("You have joined " + Main.makePossesive(target.getCustomName()) + " team!");
						target.sendMessage(attacker.getCustomName() + " has joined your team!");
						
					
					} else if (ATK == null && DEF != null) { // if the attacker is in a team, but the target isnt
						for (UUID uuid : DEF.getMembers()) {
							if (Bukkit.getPlayer(uuid) != null) {
								Bukkit.getPlayer(uuid).sendMessage(attacker.getCustomName() + " has joined your team!");
							}
						}
						PR.acceptRequest(true);
						attacker.sendMessage("You have joined the team " + DEF.getName() + "!");
					}
				} else {
					attacker.sendMessage("There is no Team Request to Accept!");
				}
			}
		}
		
		return true;
	}

}

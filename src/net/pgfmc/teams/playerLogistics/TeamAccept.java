package net.pgfmc.teams.playerLogistics;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;
import net.pgfmc.teams.teamscore.Team;
import net.pgfmc.teams.teamscore.TeamsCore;

public class TeamAccept implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		
		if (sender instanceof Player) {
			// variable declaration
			
			Player attacker = (Player) sender;
			
			Team ATK = Team.getTeam(attacker);
			PlayerData ATKP = PlayerData.getPlayerData(attacker);
			
			if (ATKP.getData("request") != null) {
				Player target = ((PendingRequest) ATKP.getData("request")).getAttacker();
				Team DEF = Team.getTeam(target);
				PlayerData DEFP = PlayerData.getPlayerData(target);
				
				if (ATKP.getData("request") != null && ATKP.getData("request") == DEFP.getData("request")) {
					
					PendingRequest PR = ((PendingRequest) ATKP.getData("request"));
					
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
						attacker.sendMessage("You have joined " + TeamsCore.makePossesive(target.getCustomName()) + " team!");
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

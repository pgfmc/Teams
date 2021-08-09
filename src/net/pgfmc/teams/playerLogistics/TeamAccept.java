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
			
			Team ATK = (Team) PlayerData.getData(attacker, "team");
			PlayerData ATKP = PlayerData.getPlayerData(attacker);
			
			if (ATKP.getData("request") != null) {
				Player target = (Player) ((PendingRequest) ATKP.getData("request")).getInvitor();
				Team DEF = Team.getTeam(target);
				PlayerData DEFP = PlayerData.getPlayerData(target);
				
				if (ATKP.getData("request") != null && ATKP.getData("request") == DEFP.getData("request")) {
					
					PendingRequest PR = ((PendingRequest) ATKP.getData("request"));
					
					if (ATK != null && DEF == null) { // if the attacker isnt in a team, but the target is
						for (UUID uuid : ATK.getMembers()) {
							if (Bukkit.getPlayer(uuid) != null) {
								Bukkit.getPlayer(uuid).sendMessage(attacker.getName() + " has joined your team!");
								attacker.sendMessage("§dYou have joined §a§l" + ATK.getName() + "§r§d!");
								PR.acceptRequest(false);
							} else {
								target.sendMessage("§9" + attacker.getName() + " §dis already in your team!");
								attacker.sendMessage("§dCouldn't join §a§l" + ATK.getName() + "§r§d; you are already in it!");
								Bukkit.getServer().broadcastMessage("§cthis message shouldn't appear. if it does, report it in #feedback! exception #weed");
							}
						}
						
					} else if (ATK == null && DEF == null) { // if both players arent on a team
						PR.createTeamRequestAccept();
						attacker.sendMessage("§dYou have joined §9" + TeamsCore.makePossesive(target.getName()) + " §dteam!");
						target.sendMessage("§9" + attacker.getName() + " §dhas joined your team!");
						
					
					} else if (ATK == null && DEF != null) { // if the attacker is in a team, but the target isnt
						for (UUID uuid : DEF.getMembers()) {
							if (Bukkit.getPlayer(uuid) != null) {
								Bukkit.getPlayer(uuid).sendMessage("§9" + attacker.getName() + " §dhas joined your team!");
								attacker.sendMessage("§dYou have joined the team §a§l" + DEF.getName() + "§r§d!");
								PR.acceptRequest(true);
							} else {
								target.sendMessage("§dCouldn't join §a§l" + DEF.getName() + "§r§d; you are already in it!");
								attacker.sendMessage("§9" + attacker.getName() + " §dis already in your team!");
								Bukkit.getServer().broadcastMessage("§cthis message shouldn't appear. if it does, report it in #feedback! exception #nice");
								return true;
							}
						}
					}
				} else {
					attacker.sendMessage("§cThere is no Team Request to Accept!");
				}
			}
			return true;
		}
		return false;
		
	}

}

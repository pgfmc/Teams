package net.pgfmc.teams.playerLogistics;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;
import net.pgfmc.teams.teamscore.Team;
import net.pgfmc.teams.teamscore.Utility;

/*
Written by CrimsonDart

-----------------------------------

Command for accepting requests

-----------------------------------
 */

public class TeamAccept implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		System.out.println("Team Accept Command ran!");
		
		
		
		if (sender instanceof Player) {
			// variable declaration
			
			Player attacker = (Player) sender;
			
			Team ATK = (Team) PlayerData.getData(attacker, "team");
			PlayerData ATKP = PlayerData.getPlayerData(attacker);
			
			if (ATKP.getData("request") != null) {
				
				if (((PendingRequest) ATKP.getData("request")).getInvitor().getPlayer() == null) { // ok so this makes it to where if the invitor isnt online, then it doesnt send messages to them (CRAZY!?!?!)
					OfflinePlayer target = ((PendingRequest) ATKP.getData("request")).getInvitor();
					
					Team DEF = Team.getTeam(target);
					PlayerData DEFP = PlayerData.getPlayerData(target);
					
					if (ATKP.getData("request") != null && ATKP.getData("request") == DEFP.getData("request")) {
						
						PendingRequest PR = ((PendingRequest) ATKP.getData("request"));
						
						if (ATK != null && DEF == null) { // if the attacker isnt in a team, but the target is
							attacker.sendMessage("§9" + attacker.getName() + " §dhas joined your team!");
							PR.acceptRequest(true);
							
							System.out.println("Out 1");
							
							return true;
							
						} else if (ATK == null && DEF == null) { // if both players arent on a team
							attacker.sendMessage("§dYou have joined §9" + Utility.makePossesive(target.getName()) + " §dteam!");
							PR.createTeamRequestAccept();
							
							System.out.println("Out 2");
							
							return true;
							
						
						} else if (ATK == null && DEF != null) { // if the attacker is in a team, but the target isnt
							attacker.sendMessage("§dYou have joined the team §a§l" + DEF.getName() + "§r§d!");
							PR.acceptRequest(true);
							
							System.out.println("Out 3");
							
							return true;
						}
						
						System.out.println("Out 4....?");
						
						return true;
						
						
					} else {
						System.out.println("Out 5");
						attacker.sendMessage("§cThere is no Team Request to Accept!");
						return true;
					}
				}
				
				Player target = ((PendingRequest) ATKP.getData("request")).getInvitor().getPlayer();
				Team DEF = Team.getTeam(target);
				PlayerData DEFP = PlayerData.getPlayerData(target);
				
				if (ATKP.getData("request") != null && ATKP.getData("request") == DEFP.getData("request")) {
					
					PendingRequest PR = ((PendingRequest) ATKP.getData("request"));
					
					if (ATK != null && DEF == null) { // if the attacker isnt in a team, but the target is
						attacker.sendMessage("§9" + attacker.getName() + " §dhas joined your team!");
						target.sendMessage("§dYou have joined the team §a§l" + ATK.getName() + "§r§d!");
						PR.acceptRequest(true);
						
						System.out.println("Out 6");
						
						return true;
						
					} else if (ATK == null && DEF == null) { // if both players arent on a team
						attacker.sendMessage("§dYou have joined §9" + Utility.makePossesive(target.getName()) + " §dteam!");
						target.sendMessage("§9" + attacker.getName() + " §dhas joined your team!");
						PR.createTeamRequestAccept();
						
						System.out.println("Out 7");
						
						return true;
						
					
					} else if (ATK == null && DEF != null) { // if the attacker is in a team, but the target isnt
						attacker.sendMessage("§dYou have joined the team §a§l" + DEF.getName() + "§r§d!");
						target.sendMessage("§9" + attacker.getName() + " §dhas joined your team!");
						PR.acceptRequest(true);
						
						System.out.println("Out 8");
						
						return true;
					}
					
					System.out.println("Out 9");
					
				} else {
					attacker.sendMessage("§cThere is no Team Request to Accept!");
					System.out.println("Out 10");
					return true;
				}
			}
			return true;
		}
		return false;
	}

}

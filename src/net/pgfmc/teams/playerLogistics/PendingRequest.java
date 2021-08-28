package net.pgfmc.teams.playerLogistics;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;
import net.pgfmc.teams.teamscore.Team;
import net.pgfmc.teams.teamscore.TeamsCore;

/*

Class used to process a pending request
upon the target accepting the request, this class will be deleted, and the person without a team will be added to the person with a team
if neither player is in a team, then a new team will be created, named by the person who sent the request.

written by CrimsonDart

 */

public class PendingRequest {
	
	// variable declaration
	
	Team team;
	PlayerData INV;
	PlayerData JOI;
	
	private PendingRequest(Player attacker, Player target, Team team) { // constructor
		
		this.team = team;
		
		INV = PlayerData.getPlayerData(attacker);
		JOI = PlayerData.getPlayerData(target);
		INV.setData("request", this);
		JOI.setData("request", this);
		
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(TeamsCore.plugin, new Runnable() {
            
			@Override
            public void run() // 60 second long cooldown, in which the plugin will wait for 
            {
            	INV.setData("request", null);;
        		JOI.setData("request", null);;
            }
        }, 1200);
	}
	
	public void acceptRequest(boolean isInvitor) { // accepts the request; makes both players on the same team.
		INV.setData("team", team);
		JOI.setData("team", team);
		
		if (isInvitor) {
			team.addMember(JOI.getPlayer());
		} else {
			team.addMember(INV.getPlayer());
		}
		
		INV.setData("request", null);
		JOI.setData("request", null);
	}
	
	public void createTeamRequestAccept() { // creates a new team for when 
		List<UUID> list = new ArrayList<>();
		list.add(INV.getPlayer().getUniqueId());
		list.add(JOI.getPlayer().getUniqueId());
		Team team = new Team(list);
		
		INV.setData("team", team);
		JOI.setData("team", team);
		INV.setData("request", null);
		JOI.setData("request", null);
		team.renameBegin(INV);
	}
	
	public OfflinePlayer getInvitor() {
		return INV.getPlayer();
	}
	
	public static void requestHandler(Player attacker, Player target) {
		
		PlayerData ATKP = PlayerData.getPlayerData(attacker);
		PlayerData DEFP = PlayerData.getPlayerData(target);
		Team ATK = (Team) ATKP.getData("team");
		Team DEF = (Team) DEFP.getData("team");
		
		if (ATKP.getData("request") == null && DEFP.getData("request") == null) {
			
			if (ATK != null && ATK == DEF) { // if both players are on the same team // denies request
				attacker.sendMessage("§cYou are already on the same team as §9" + target.getName() + "§c!");
				return;
			
			} else if (ATK != null && DEF != null && ATK != DEF) { // if both players are on different teams // denies request
				attacker.sendMessage("§9" + target.getName() + " §cis already in a team!");
				attacker.sendMessage("§cIf you want to join §9" + TeamsCore.makePossesive(target.getName())  +  " §cteam, leave your current team and ask for another request!");
				return;
			
			} else if (ATK == null && DEF != null) { // if the attacker isnt in a team, but the target is
				attacker.sendMessage("§dRequest sent to §9" + target.getName() + " §dto join their team, " + DEF.getName());
				new PendingRequest(attacker, target, DEF);
				target.sendMessage("§9" + attacker.getName() + " §dhas sent you a request to join your team!");
				target.sendMessage("§dHit them with a flower, or type §b/tma §dto accept!");
				return;
			
			} else if (ATK == null && DEF == null) { // if both players arent on a team
				attacker.sendMessage("§dRequest sent to §9" + target.getName() + "§d.");
				attacker.sendMessage("§dA new Team will be created upon §9" + target.getName() + " §daccepting the Request.");
				new PendingRequest(attacker, target, null);
				target.sendMessage("§9" + attacker.getName() + " §dhas sent you a request to join their team!");
				target.sendMessage("§dHit them with a flower, or type §b/tma §dto accept!");
				return;
				
			} else if (ATK != null && DEF == null) { // if the attacker is in a team, but the target isnt
				attacker.sendMessage("§dInvite sent to §9" + target.getName() + " §dto join your team.");
				new PendingRequest(attacker, target, ATK);
				target.sendMessage("§9" + attacker.getName() + " §dhas invited you to their team, §a§l" + ATK.getName() + "§r§d.");
				target.sendMessage("§dHit them with a flower, or type §b/tma §dto accept!");
				return;
			}
			
		} else if (ATKP.getData("request") != null && ATKP.getData("request") == DEFP.getData("request")) { // if both requests are the same.
			PendingRequest PR = (PendingRequest) ATKP.getData("request");
			
			if (ATK != null && DEF == null) { // if the invitor isnt in a team, but the joiner is
				attacker.sendMessage("§9" + attacker.getName() + " §dhas joined your team!");
				((PendingRequest) PlayerData.getData(attacker, "request")).getInvitor().getPlayer().sendMessage("§dYou have joined the team §a§l" + ATK.getName() + "§r§d!");
				PR.acceptRequest(true);
				
			} else if (ATK == null && DEF == null) { // if both players arent on a team
				attacker.sendMessage("§dYou have joined §9" + TeamsCore.makePossesive(target.getName()) + " §dnew team!");
				target.sendMessage("§9" + attacker.getName() + " §dhas joined your team!");
				PR.createTeamRequestAccept();
				return;
				
			} else if (ATK == null && DEF != null) { // if the attacker is in a team, but the target isnt
				attacker.sendMessage("§dYou have joined the team §a§l" + DEF.getName() + "§r§d!");
				((PendingRequest) PlayerData.getData(attacker, "request")).getInvitor().getPlayer().sendMessage("§9" + attacker.getName() + " §dhas joined your team!");
				PR.acceptRequest(true);
			}
		}
	}
}
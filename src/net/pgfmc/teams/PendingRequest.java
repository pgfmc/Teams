package net.pgfmc.teams;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/*

Class used to process a pending request
upon the target accepting the request, this class will be deleted, and the person without a team will be added to the person with a team
if neither player is in a team, then a new team will be created, named by the person who sent the request.

written by CrimsonDart

 */

public class PendingRequest {
	
	// variable declaration
	
	TeamObj team;
	Player attacker;
	Player target;
	PlayerData ATK;
	PlayerData DEF;
	static List<PendingRequest> instances = new ArrayList<>();
	
	public PendingRequest(Player attacker, Player target, TeamObj team) { // constructor
		
		this.team = team;
		this.attacker = attacker;
		this.target = target;
		instances.add(this);
		ATK = PlayerData.findPlayerData(attacker);
		DEF = PlayerData.findPlayerData(target);
		
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
            
            @SuppressWarnings("unlikely-arg-type")
			@Override
            public void run() // 60 second long cooldown, in which the plugin will wait for 
            {
            	instances.remove(this);
            }
        }, 1200);
	}
	
	public void acceptRequest(boolean isAttacker) { // accepts the request; makes both players on the same team.
		ATK.setTeam(team);
		DEF.setTeam(team);
		
		if (isAttacker) {
			team.addMember(target);
		} else {
			team.addMember(attacker);
		}
	}
	
	public void createTeamRequestAccept() { // creates a new team for when 
		List<UUID> list = new ArrayList<>();
		list.add(attacker.getUniqueId());
		list.add(target.getUniqueId());
		TeamObj team = new TeamObj(list);
		
		ATK.setTeam(team);
		DEF.setTeam(team);
	}
	
	public void deleteRequest() { // deletes the request for one reason or another.
		instances.remove(this);
	}
	
	public Player getAttacker() {
		return attacker;
	}
}
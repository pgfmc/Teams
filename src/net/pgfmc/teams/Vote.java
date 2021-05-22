package net.pgfmc.teams;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/*

Class to manage voting

stores all players who are voting, as well as the  

Written by CrimsonDart
 */


public class Vote {
	
	Map<UUID, Integer> votes; // 0 = indifferent, 1 = suport, -1 = against
	Object subject;
	UUID voteID;
	TeamObj team;
	
	public enum VoteCases {
		KICKPLAYER,
		BANPLAYER,
		ALLYTEAMREQUEST,
		ALLYTEAMACCEPT,
		RENAMETEAM,
		ALLYTEAMCANCEL,
		CHANGEGOVERNMENT
	}
	
	VoteCases voteCase;
	static List<Vote> instances = new ArrayList<>();
	
	public Vote(List<UUID> members, TeamObj team, Object subject, VoteCases voteCase) { // new vote constructor
		if (team.getVote() == null) {
			for (UUID uuid : members) {
				this.votes.put(uuid, 2);
			}
			
			this.team = team;
			this.subject = subject;
			this.voteCase = voteCase;
			voteID = UUID.randomUUID();
		} else {
			System.out.println("New Vote prevented from being created because of invalid Team");
		}
		
		instances.add(this);
	}
	
	public Vote(Map<UUID, Integer> members, TeamObj votable, Object subject, VoteCases voteCase, UUID voteID) { // loaded vote constructor
		
		votes = members;
		
		this.team = votable;
		this.subject = subject;
		this.voteCase = voteCase;
		this.voteID = voteID;
		instances.add(this);
	}
	
	public void vote(Player p, int position) {
		for (UUID uuid : votes.keySet()) {
			if (p.getUniqueId() == uuid) {
				if (votes.get(uuid) != 2) {
					p.sendMessage("You have already voted!");
					return;
				} else {
					votes.put(uuid, position);
					
					String str = "";
					
					switch (position) {
					
					case -1: str = str + "You voted Against ";
					case 0: str = str + "You voted Indifferent to ";
					case 1: str = str + "You voted for ";
					}
					
					switch (voteCase) {
					
					case KICKPLAYER: str = str + ("Kicking " + Bukkit.getPlayer((UUID) subject).getCustomName() + " from your team.");
					case BANPLAYER: str = str + ("Banning " + Bukkit.getPlayer((UUID) subject).getCustomName() + " from your team.");
					case ALLYTEAMACCEPT: str = str + ("Accepting the Ally request from team " + TeamObj.findID((UUID) subject).getName() + ".");
					case ALLYTEAMREQUEST: str = str + ("Sending an Ally request to team " + TeamObj.findID((UUID) subject).getName() + ".");
					case ALLYTEAMCANCEL: str = str + ("Ending the Allyship with team " + TeamObj.findID((UUID) subject).getName() + ".");
					case RENAMETEAM: str = str + ("Renaming the team to " + (String) subject + ".");
					case CHANGEGOVERNMENT: str = str + ("Making the leader of the team " + Bukkit.getPlayer((UUID) subject).getCustomName() + ".");
					}
					p.sendMessage(str);
					
					// we need to add support for deleted teams; if a team mentioned in a vote is deleted, then all votes mentioning the team should be ended, and all members of the vote should be notified.
					
				}
			}
		}
	}
	
	// --------------------------------------------------------------------- getters and setters
	
	public VoteCases getCase() {
		return voteCase;
	}
	
	public Object getSubject() {
		return subject;
	}
	
	public UUID getID() {
		return voteID;
	}
	
	public Map<UUID, Integer> getVotes() {
		return votes;
	}
	
	public TeamObj getTeam() {
		return team;
	}
	
	// --------------------------------------------------------------------- static methods
	
	public static Vote findInVote(UUID uuid) {
		for (Vote vote : instances) {
			if (vote.getID().equals(uuid)) {
				return vote;
			}
		}
		return null;
	}
	
	public static List<Vote> getAllVotes() {
		return instances;
	}
	
	
}

package net.pgfmc.teams.teamscore;


import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;
import net.pgfmc.teams.data.containers.BlockContainer;

/*
Object Class for Teams; a new object will be created upon the creation of a new team.

Written by Bk and CrimsonDart

-------------------------------------

TeamObj.java functionality:

create a new team
load an old team upon loading of the plugin
multiple getters and setters for local variables
a rename feature
ability to find a team based on a contained player or UUID
ability to choose between a leader or democracy decision-making system

External Functionality:
hell no im not doing this

future functionality:

ability to disband team

 */

public class Team {
	
	private String name = "New Team";
	private List<UUID> members = new LinkedList<>();
	private static IdentityHashMap<UUID, Team> instances = new IdentityHashMap<UUID, Team>();
	private UUID ID;
	private UUID leader;
	//private Vote<?> vote;
	
	// ------------------------------------------------------------------------------------ constructors
	
	public Team(String name, List<UUID> members, UUID ID, UUID leader)
	{
		this.name = name;
		this.members = members;
		this.ID = ID;
		instances.put(this.ID, this);
		this.leader = leader;
	}
	
	public Team(UUID member) {
		
		this.members.add(member);
		
		leader = member;
		
		ID = UUID.randomUUID();
		
		instances.put(ID, this);
		
		PlayerData.setData(Bukkit.getOfflinePlayer(member), "team", this);
		
		Player p = (Player) getLeader();
		PlayerData pData = PlayerData.getPlayerData(p);
		p.sendMessage("§dFor the next 4 minutes, you can change your ");
		p.sendMessage("§dteam's name by typing into the chat box!");
		pData.setData("naming", true);
		
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(TeamsCore.getPlugin(), new Runnable() {
            
			@Override
            public void run() // 60 second long cooldown, in which the plugin will wait for 
            {
				if (pData.getData("naming") != null) {
					pData.setData("naming", null);
	            	p.sendMessage("§cTime has run out to rename your team.");
				}
            }
        }, 2400);
	}
	
	// ------------------------------------------------------------------------------------ getters and setters

	public List<OfflinePlayer> getMembers() {
		return members.stream().map((x) -> Bukkit.getOfflinePlayer(x)).collect(Collectors.toList());
	}
	
	public List<UUID> getMemberUUIDs() {
		return members;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String string) {
		name = string;
	}
	
	public OfflinePlayer getLeader() {
		return Bukkit.getOfflinePlayer(leader);
	}
	
	public void setLeader(OfflinePlayer leader) {
		this.leader = leader.getUniqueId();
	}
	
	public void addMember(OfflinePlayer offlinePlayer) {
		
		if (members.contains(offlinePlayer.getUniqueId())) {
			System.out.println("Player was already in team!");
			return;
		}
		
		System.out.println("Player added to team!");
		members.add(offlinePlayer.getUniqueId());
		PlayerData.setData(offlinePlayer, "team", this);
		return;
	}
	
	public UUID getUniqueId() {
		return ID;
	}
	
	
	public boolean removePlayer(OfflinePlayer p) {
		
		System.out.println(p.getName());
		
		PlayerData pd = PlayerData.getPlayerData(p);
		
		if (pd.getData("naming") == null && pd.getData("request") == null) {
			
			System.out.println(getMembers().toString());
			
			
			// removes the player using a stream, checking OfflinePlayer p's UUID against all the Team's stored UUIDs.
			members = members.stream().filter((x) -> {
				if (x.equals(p.getUniqueId())) {
					return false;
				}
				return true;
			}).collect(Collectors.toList());
			
			System.out.println("Player Removed from team!");
			
			//members.remove(p.getUniqueId());
			if (members.size() == 0) {
				instances.remove(this.getUniqueId());
			}
			
			BlockContainer.updateTeams();
			
			PlayerData.setData(p, "team", null);
			return true;
			
		} else {
			return false;
		}
	}
	
	// ------------------------------------------------------------------------------------ Renaming function
	
	public void renameBegin(Player p) { // initializes naming mode for player p.getPlayer.
		if (p.getPlayer().getPlayer() != null) {
			PlayerData pData = PlayerData.getPlayerData(p);
			p.sendMessage("§dFor the next 4 minutes, you can change your ");
			p.sendMessage("§dteam's name by typing into the chat box!");
			p.sendMessage("§dType \"c\" to §ccancel§d.");
			pData.setData("naming", true);
			
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(TeamsCore.getPlugin(), new Runnable() {
	            
				@Override
	            public void run() // 60 second long cooldown, in which the plugin will wait for 
	            {
					if (pData.getData("naming") != null) {
						pData.setData("naming", null);
		            	p.sendMessage("§cTime has run out to rename your team.");
					}
	            }
	        }, 2400);
		}
	}
	
	// ------------------------------------------------------------------------------------ instances manager
	
	public static Team getTeam(OfflinePlayer p) { // searches for a given Player p, and returns the team the player is in
		if (p != null) {
			return (Team) PlayerData.getData(p, "team");
			
		}
		return null;
	}
	
	public static Team findID(UUID ID) { // searches for the team with the given ID
		
		if (ID != null) {
			
			for (UUID uuid : instances.keySet()) {
				
				if (ID.equals(uuid)) {
					return instances.get(uuid);
				}
			}
		}
		return null;
	}
	
	public static void settleTeams() {
		
		IdentityHashMap<UUID, Team> teams = new IdentityHashMap<>();
		
		PlayerData.stream().map((x) -> (Team) x.getData("team"))
		.distinct()
		.filter((x) -> {
			if (x == null) {
				return false;
			}
			return true;
		})
		.forEach((x) -> {
			teams.put(x.getUniqueId(), x);
		});;
		
		instances = teams;
	}
	
	public static IdentityHashMap<UUID, Team> getTeams() {
		return instances;
	}
}

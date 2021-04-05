package net.pgfmc.teams;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class TeamObj {	
	String name;
	UUID leader;
	List<UUID> members;
	List<TeamObj> allies = null; // Defaults to null if no allies
	int creationPt;
	
	List<UUID> requests = new ArrayList<>();
	
	public TeamObj(String name, UUID leader, List<UUID> members, List<TeamObj> allies, int creationPt)
	{
		this.name = name;
		this.leader = leader;
		this.members = members;
		this.allies = allies;
		this.creationPt = creationPt;
	}
	
	
	
	public TeamObj(String name, UUID leader, List<UUID> members, int creationPt)
	{
		this.name = name;
		this.leader = leader;
		this.members = members;
		
		this.creationPt = creationPt;
	}
	
	
	
	
	public static TeamObj findTeam(String name, FileConfiguration db, File file)
	{
		
		List<TeamObj> teamList = Database.getTeams(db, file);
		
		for (TeamObj team : teamList)
		{
			if (team.getName().equals(name)) { return team; }
		}
		
		
		return null; // null = no team found
	}
	
	
	
	public List<UUID> getRequests()
	{
		return requests;
	}
	
	public void addRequest(Player p)
	{
		requests.add(p.getUniqueId());
	}
	
	public List<UUID> getMembers()
	{
		return members;
	}
	
	public int getCreation()
	{
		return creationPt;
	}
	
	
	public Player getLeader()
	{
		return Bukkit.getPlayer(leader);
	}
	
	public List<TeamObj> getAllies()
	{
		return allies;
	}
	
	public String getName()
	{
		return name;
	}
	
	public boolean kickMember(Player p)
	{
		if (p.getUniqueId().equals(leader)) { return false; } // You cannot remove the owner!
		
		members.remove(p.getUniqueId());
		return true;
	}
	
	public boolean addMember(Player p)
	{
		if (members.contains(p.getUniqueId())) { return false; } // You cannot add an existing member to the team
		
		members.add(p.getUniqueId());
		return true;
	}
	
	
	public boolean disband(Player sender)
	{
		if (!sender.getUniqueId().equals(leader)) { return false; } // You cannot disband a team if you aren't the owner!
		// TODO delete from save file
		
		// return true if save works.
		
		return false; // Something bad happened and it didn't disband properly
	}
}

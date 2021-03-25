package net.pgfmc.teams;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TeamObj { 
					   /* teams class
							*notes here*
					   */ 
	
	UUID owner;
	List<UUID> members;
	List<TeamObj> allies = null; // Defaults to null if no allies
	
	public TeamObj(UUID owner, List<UUID> members, List<TeamObj> allies)
	{
		this.members = members;
		this.allies = allies;
	}
	
	
	
	public TeamObj(UUID owner, List<UUID> members)
	{
		this.owner = owner;
		this.members = members;
	}
	
	
	
	
	
	
	
	
	public List<UUID> getMembers()
	{
		return members;
	}
	
	
	public Player getOwner()
	{
		return Bukkit.getPlayer(owner);
	}
	
	
	public boolean kickMember(Player p)
	{
		if (p.getUniqueId().equals(owner)) { return false; } // You cannot remove the owner!
		
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
		if (!sender.getUniqueId().equals(owner)) { return false; } // You cannot disband a team if you aren't the owner!
		// TODO delete from save file
		
		// return true if save works.
		
		return false; // Something bad happened and it didn't disband properly
	}
}

package net.pgfmc.teams;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Database {
	
	@SuppressWarnings("unchecked") // Don't worry about it :))
	public static TeamObj getTeam(Player p, FileConfiguration db, File file) // Can be used to check if a player is in a team, too (null = no team)
	{
		if (db.get("teams") == null) { return null; } // if there are no teams
		
		
		
		
		List<TeamObj> teams = (List<TeamObj>) db.get("teams"); // gets the List<TeamObj> from the database file
		
		for (TeamObj team : teams) // for every team in the list
		{
			if (team.getMembers().contains(p.getUniqueId())) // Checks to see if Player p is in that team
			{
				return team; // return the team the player is in
			}
		}
		
		return null; // null if the player isn't in a team
	}

	@SuppressWarnings("unchecked")
	public static List<TeamObj> getTeams(FileConfiguration db, File file) { // Returns the list of teams
		if (db.get("teams") == null) { return null; }
		
		
		return (List<TeamObj>) db.get("teams"); // gets the List<TeamObj> from the database file
	}

	public static int getNextCreation(FileConfiguration db, File file) { // This will be used to determine priority. ????>???? I actually don't know why this is here, ignore until I figure it out
		return db.getInt("creation") + 1;
	}
	
	public static void saveTeam(TeamObj team, FileConfiguration db, File file) // Saves a newly created team
	{
		List<TeamObj> teams = new ArrayList<>();
		
		if (getTeams(db, file) == null) // If there are no teams
		{
			teams.add(team);
			db.set("teams", teams);
			return;
		}
		
		// If there are teams
		teams = getTeams(db, file);
		db.set("teams", teams);
	}

}

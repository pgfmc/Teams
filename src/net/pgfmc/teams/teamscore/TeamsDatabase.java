package net.pgfmc.teams.teamscore;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;
import net.pgfmc.pgfessentials.playerdataAPI.PlayerDataListener;

/*
Written by CrimsonDart

-----------------------------------

Handles saving and loading teams.

-----------------------------------
 */

public class TeamsDatabase implements PlayerDataListener {
	
	
	static File file = new File(TeamsCore.getPlugin().getDataFolder() + File.separator + "database.yml"); // Creates a File object
	static FileConfiguration database = YamlConfiguration.loadConfiguration(file); // Turns the File object into YAML and loads data
	
	static boolean playerInit = false;
	
	public static void saveTeams() { // ----------------- saves all teams to "teams"
		
		database.set("teams", null);
		database.createSection("teams");
		ConfigurationSection configSec = database.getConfigurationSection("teams");
		
		for (UUID key : Team.getTeams().keySet()) {
			Team team = Team.getTeams().get(key);
			
			if (team.getMembers().size() < 1) {
				break;
			}
			
			ConfigurationSection teamSection = configSec.getConfigurationSection(team.getUniqueId().toString()); // saves the team data to the team's UUID.
			if (teamSection == null) {
				teamSection = configSec.createSection(team.getUniqueId().toString());
			}
			
			teamSection.set("name", team.getName()); // saves team name
			
			List<String> strings = new ArrayList<>(); // saves Members
			for (UUID uuid : team.getMembers()) {
				strings.add(uuid.toString());
			}
			teamSection.set("Members", strings);
			
			if (team.getVote() == null) { // saves Vote
				teamSection.set("Vote", null);
			} else {
				teamSection.set("Vote", team.getVote().getUniqueID().toString());
			}
			
			configSec.set(team.getUniqueId().toString(), teamSection);
		}
		database.set("teams", configSec);
		
		try {
			database.save(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void loadTeams() { // ----------- loads all teams
		
		ConfigurationSection configSec = database.getConfigurationSection("teams");
		
		if (configSec == null) {
			return;
		}
		
		for (String key : configSec.getKeys(false)) {
			
			ConfigurationSection teamSection = configSec.getConfigurationSection(key); // saves the team data to the team's UUID.
			
			if (teamSection == null) {
				return;
			}
			
			String name = teamSection.getString("name"); // gets team name
			
			List<UUID> members = new ArrayList<>();
			for (Object string : teamSection.getList("Members")) { // gets all members
				members.add(UUID.fromString((String) string));
			}
			
			
			UUID vote;
			if (teamSection.isSet("Vote")) {
				vote = UUID.fromString(teamSection.getString("Vote"));
			} else {
				vote = null;
			}
			
			new Team(name, members, UUID.fromString(key), vote);
		}
	}

	@Override
	public void OfflinePlayerDataDeInitialization(PlayerData playerData) {
		
		ConfigurationSection playerDataList = database.getConfigurationSection("playerData");
		
		if (!playerInit) {return;}
		
		
		if (playerDataList != null) {
			if (playerData.getData("team") != null) {
				playerDataList.set(playerData.getPlayer().getUniqueId().toString(), ((Team) playerData.getData("team")).getUniqueId().toString());
			} else {
				playerDataList.set(playerData.getPlayer().getUniqueId().toString(), null);
			}
			
		} else {
			
			playerDataList = database.createSection("playerData");
			
			if (playerData.getData("team") != null) {
				playerDataList.set(playerData.getPlayer().getUniqueId().toString(), ((Team) playerData.getData("team")).getUniqueId().toString());
			} else {
				playerDataList.set(playerData.getPlayer().getUniqueId().toString(), null);
			}
			database.set("playerData", playerDataList);
		}
		
		try {
			database.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		playerInit = false;
	}

	@Override
	public void OfflinePlayerDataInitialization(PlayerData playerData) {
		
		ConfigurationSection playerDataList = database.getConfigurationSection("playerData");
		
		if (playerDataList == null) {
			System.out.println("No PlayerData to load!");
			playerData.setData("team", null);
			return;
		}
		
		String uuid = (String) playerDataList.get(playerData.getPlayer().getUniqueId().toString());
		
		if (uuid != null) {
			
			Team team = Team.findID(UUID.fromString(uuid));
			playerData.setData("team", team);
			System.out.println("Team loaded!");
			
		} else {
			playerData.setData("team", null);
			System.out.println("no team to load!");
			
		}
		playerInit = true;
	}
}
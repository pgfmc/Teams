package net.pgfmc.teams;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Database {
	
	public static void saveTeams() { // ----------------- saves all teams to "teams"
		File file = new File(Main.plugin.getDataFolder() + File.separator + "database.yml"); // Creates a File object
		FileConfiguration database = YamlConfiguration.loadConfiguration(file); // Turns the File object into YAML and loads data
		
		
		database.set("teams", null);
		database.createSection("teams");
		ConfigurationSection configSec = database.getConfigurationSection("teams");
		
		for (TeamObj team : TeamObj.getTeams()) {
			
			ConfigurationSection teamSection = configSec.getConfigurationSection(team.getID().toString()); // saves the team data to the team's UUID.
			
			
			
			teamSection.set("name", team.getName()); // saves team name
			
			List<String> strings = new ArrayList<>(); // saves Members
			for (UUID uuid : team.getMembers()) {
				strings.add(uuid.toString());
			}
			teamSection.set("Members", strings);
			
			strings = new ArrayList<>(); // saves team Allies
			for (UUID uuid : team.allies) {
				strings.add(uuid.toString());
			}
			teamSection.set("Allies", strings);
			
			if (team.currentVote == null) { // saves Current vote for the team
				teamSection.set("Vote", null);
			} else {
				teamSection.set("Vote", team.currentVote.getID().toString());
			}
			
			if (team.leader == null) { // saves Leader for the team
				teamSection.set("Leader", null);
			} else {
				teamSection.set("Leader", team.leader.toString());
			}
		}
		
		try {
			database.save(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void loadTeams() {// ----------- loads all teams
		File file = new File(Main.plugin.getDataFolder() + File.separator + "database.yml"); // Creates a File object
		FileConfiguration database = YamlConfiguration.loadConfiguration(file); // Turns the File object into YAML and loads data
		
		ConfigurationSection configSec = database.getConfigurationSection("teams");
		
		for (String key : configSec.getKeys(false)) {
			
			ConfigurationSection teamSection = configSec.getConfigurationSection(key); // saves the team data to the team's UUID.
			
			String name = teamSection.getString("name"); // gets team name
			
			List<UUID> members = new ArrayList<>();
			for (Object string : teamSection.getList("Members")) { // gets all members
				members.add(UUID.fromString((String) string));
			}
			
			List<UUID> allies = new ArrayList<>();
			for (Object string : teamSection.getList("Allies")) { // gets all allies
				allies.add(UUID.fromString((String) string));
			}
			
			UUID vote = null;
			if (teamSection.getString("Vote") != null) { // gets Current vote for the team
				vote = UUID.fromString(teamSection.getString("Vote"));
			}
			
			UUID leader = null;
			if (teamSection.getString("Leader") != null) { // gets Current vote for the team
				vote = UUID.fromString(teamSection.getString("Leader"));
			}
			
			
			new TeamObj(name, members, allies, UUID.fromString(key), leader, vote);
		}
	}
	
	public static void savePlayerData() { // saves all playerdata
		File file = new File(Main.plugin.getDataFolder() + File.separator + "database.yml"); // Creates a File object
		FileConfiguration database = YamlConfiguration.loadConfiguration(file); // Turns the File object into YAML and loads data
		
		database.set("playerData", null);
		database.createSection("playerData");
		ConfigurationSection configSec = database.getConfigurationSection("playerData");
		
		Map<String, String> list = PlayerData.getAllRawPlayerData();
		
		for (String string : list.keySet()) {
			configSec.set(string, list.get(string));
		}
		
		try {
			database.save(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void loadPlayerData() { // -------------------------------------------------------------------------------------- Loads all playerData stored in database.yml
		File file = new File(Main.plugin.getDataFolder() + File.separator + "database.yml"); // Creates a File object
		FileConfiguration database = YamlConfiguration.loadConfiguration(file); // Turns the File object into YAML and loads data
		
		ConfigurationSection configSec = database.getConfigurationSection("playerData");
		for (String key : configSec.getKeys(false)) {
			
			UUID thimg;
			
			System.out.println(configSec.get(key));
			
			try {
				thimg = UUID.fromString((String) configSec.getString(key));
			} catch (IllegalArgumentException e) {
				thimg = null;
			}
			
			new PlayerData(UUID.fromString(key), thimg);
		}
	}
}

package net.pgfmc.teams;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import net.pgfmc.teams.Vote.VoteCases;

public class Database {
	
	static File file = new File(Main.plugin.getDataFolder() + File.separator + "database.yml"); // Creates a File object
	static FileConfiguration database = YamlConfiguration.loadConfiguration(file); // Turns the File object into YAML and loads data
	
	public static void saveTeams() { // ----------------- saves all teams to "teams"
		
		database.set("teams", null);
		database.createSection("teams");
		ConfigurationSection configSec = database.getConfigurationSection("teams");
		
		for (TeamObj team : TeamObj.getTeams()) {
			
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
			
			if (team.allies != null) {
				strings = new ArrayList<>(); // saves team Allies
				for (UUID uuid : team.allies) {
					strings.add(uuid.toString());
				}
				teamSection.set("Allies", strings);
			}
			
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
	
	public static void loadTeams() {// ----------- loads all teams
		
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
			
			List<UUID> allies = null;
			if (teamSection.get("Allies") != null) {
				allies = new ArrayList<>();
				for (Object string : teamSection.getList("Allies")) { // gets all allies
					allies.add(UUID.fromString((String) string));
				}
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
		
		ConfigurationSection configSec = database.getConfigurationSection("playerData");
		if (configSec == null) {
			return;
		}
		
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
	
	public static void saveVotes() {
		
		database.set("Votes", null);
		ConfigurationSection configSec = database.createSection("Votes");
		
		for (Vote vote : Vote.getAllVotes()) {
			
			ConfigurationSection saveVote = configSec.getConfigurationSection(vote.getID().toString());
			if (saveVote == null) {
				saveVote = configSec.createSection(vote.getID().toString());
			}
			
			List<String> uuidList = new ArrayList<>(); // saves each player, along with their decision in voting.
			List<Integer> intList = new ArrayList<>();
			for (UUID uuid : vote.getVotes().keySet()) {
				uuidList.add(uuid.toString());
				intList.add(vote.getVotes().get(uuid));
			}
			saveVote.set("Members", uuidList);
			saveVote.set("MemberVotes", intList);
			
			saveVote.set("Team", vote.getTeam().getUniqueId().toString());
			
			VoteCases voteCase = vote.getCase();
			saveVote.set("VoteCase", voteCase.toString());
			
			switch(voteCase) { // for the Subject of the vote.
			case ALLYTEAMACCEPT:
				saveVote.set("Subject", ((TeamObj) vote.getSubject()).getUniqueId());
			case ALLYTEAMCANCEL:
				saveVote.set("Subject", ((TeamObj) vote.getSubject()).getUniqueId());
			case ALLYTEAMREQUEST:
				saveVote.set("Subject", ((TeamObj) vote.getSubject()).getUniqueId());
			case BANPLAYER:
				saveVote.set("Subject", ((OfflinePlayer) vote.getSubject()).getUniqueId());
			case CHANGEGOVERNMENT:
				saveVote.set("Subject", ((OfflinePlayer) vote.getSubject()).getUniqueId());
			case KICKPLAYER:
				saveVote.set("Subject", ((OfflinePlayer) vote.getSubject()).getUniqueId());
			case RENAMETEAM:
				saveVote.set("Subject", (String) vote.getSubject());
			}
		}
		
		try {
			database.save(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void loadVotes() {
		
		ConfigurationSection configSec = database.getConfigurationSection("Votes");
		
		if (configSec == null) {
			return;
		}
		
		for (String key : configSec.getKeys(false)) {
			
			ConfigurationSection saveVote = configSec.getConfigurationSection(key);
			
			Map<UUID, Integer> members = new HashMap<>();
			for (Object uuidString : saveVote.getList("Members")) {
				members.put(UUID.fromString((String) uuidString), (Integer) saveVote.getList("MemberVotes").get(saveVote.getList("Members").indexOf(uuidString)));
			}
			
			UUID team = UUID.fromString(saveVote.getString("Team"));
			VoteCases voteCase = VoteCases.valueOf(saveVote.getString("VoteCase:"));
			
			Object subject;
			
			switch(voteCase) { // for the Subject of the vote.
			case RENAMETEAM:
				subject = saveVote.getString("Subject");
			default:
				subject = UUID.fromString(saveVote.getString("Subject"));
			}
			
			new Vote(members, TeamObj.findID(team), subject, voteCase, UUID.fromString(key));
		}
	}
	
	public static void saveBlockLocation(Block block, OfflinePlayer player) { // saves who placed a block
		
		
		if (block.getWorld() == Bukkit.getWorld("Survival")) { 
			
			ConfigurationSection configSec = database.getConfigurationSection("Blocks");
			if (configSec == null) {
				configSec = database.createSection("Blocks");
			}
			
			Location location = block.getLocation();
			configSec.set(String.valueOf(location.getBlockX() + "-" + String.valueOf(location.getBlockY()) + "-" + String.valueOf(location.getBlockZ())), player.getUniqueId().toString());
			database.set("Blocks", configSec);
			
			try {
				database.save(file);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void deleteBlockLocation(Block block) { // deletes saved block data
		
		ConfigurationSection configSec = database.getConfigurationSection("Blocks");
		if (configSec == null) {
			return;
		}
		
		Location location = block.getLocation();
		configSec.set(String.valueOf(location.getBlockX() + "-" + String.valueOf(location.getBlockY()) + "-" + String.valueOf(location.getBlockZ())), null);
		database.set("Blocks", configSec);
	}
	
	public static OfflinePlayer getBlockPlacer(Block block) { // retruns who placed a stored block.
		ConfigurationSection configSec = database.getConfigurationSection("Blocks");
		if (configSec == null) {
			return null;
		}
		
		Location location = block.getLocation();
		configSec.getString(String.valueOf(location.getBlockX() + "-" + String.valueOf(location.getBlockY()) + "-" + String.valueOf(location.getBlockZ())));
		return Bukkit.getOfflinePlayer(UUID.fromString(configSec.getString(String.valueOf(location.getBlockX() + "-" + String.valueOf(location.getBlockY()) + "-" + String.valueOf(location.getBlockZ())))));
		
	}
}
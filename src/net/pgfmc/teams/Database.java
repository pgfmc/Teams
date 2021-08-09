package net.pgfmc.teams;

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

public class Database implements PlayerDataListener {
	
	
	static File file = new File(Main.plugin.getDataFolder() + File.separator + "database.yml"); // Creates a File object
	static FileConfiguration database = YamlConfiguration.loadConfiguration(file); // Turns the File object into YAML and loads data
	static ConfigurationSection playerDataList = database.getConfigurationSection("playerData");
	static boolean playerInit = false;
	
	public static void saveTeams() { // ----------------- saves all teams to "teams"
		
		database.set("teams", null);
		database.createSection("teams");
		ConfigurationSection configSec = database.getConfigurationSection("teams");
		
		for (UUID key : TeamObj.getTeams().keySet()) {
			TeamObj team = TeamObj.getTeams().get(key);
			
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
			
			if (team.allies != null) {
				strings = new ArrayList<>(); // saves team Allies
				for (UUID uuid : team.allies) {
					strings.add(uuid.toString());
				}
				teamSection.set("Allies", strings);
			}
			
			/*
			if (team.currentVote == null) { // saves Current vote for the team
				teamSection.set("Vote", null);
			} else {
				teamSection.set("Vote", team.currentVote.getID().toString());
			}*/
			
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
	
	/*
	@Deprecated
	public static void savePlayerData() { // saves all playerdata

		database.set("playerData", null);
		database.createSection("playerData");
		
		Map<String, String> list = OldPlayerData.getAllRawPlayerData();
		
		for (String string : list.keySet()) {
			playerDataList.set(string, list.get(string));
		}
		
		try {
			database.save(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Deprecated
	public static void loadPlayerData() { // -------------------------------------------------------------------------------------- Loads all playerData stored in database.yml
		
		if (playerDataList == null) {
			return;
		}
		
		for (String key : playerDataList.getKeys(false)) {
			
			UUID thimg;
			
			System.out.println(playerDataList.get(key));
			
			try {
				thimg = UUID.fromString((String) playerDataList.getString(key));
			} catch (IllegalArgumentException e) {
				thimg = null;
			}
			
			new OldPlayerData(UUID.fromString(key), thimg);
		}
	}
	@Deprecated
	public static void saveVotes() {
		
		database.set("Votes", null);
		ConfigurationSection configSec = database.createSection("Votes");
		
		if (Vote.getAllVotes() == null) {
			return;
		}
		
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
	
	@Deprecated
	public static void loadVotes() {
		
		ConfigurationSection configSec = database.getConfigurationSection("Votes");
		
		if (configSec == null) {
			return;
		}
		
		for (String key : configSec.getKeys(false)) {
			
			ConfigurationSection saveVote = configSec.getConfigurationSection(key);
			
			HashMap<UUID, Integer> members = new HashMap<>();
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
	
	*/

	@Override
	public void OfflinePlayerDataDeInitialization(PlayerData playerData) {
		
		if (!playerInit) {return;}
		
		
		if (playerDataList != null) {
			if (playerData.getData("team") != null) {
				playerDataList.set(playerData.getPlayer().getUniqueId().toString(), ((TeamObj) playerData.getData("team")).getUniqueId().toString());
			} else {
				playerDataList.set(playerData.getPlayer().getUniqueId().toString(), null);
			}
			
		} else {
			
			ConfigurationSection playerDataList = database.createSection("playerData");
			
			if (playerData.getData("team") != null) {
				playerDataList.set(playerData.getPlayer().getUniqueId().toString(), ((TeamObj) playerData.getData("team")).getUniqueId().toString());
			} else {
				playerDataList.set(playerData.getPlayer().getUniqueId().toString(), null);
			}
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
		
		if (playerDataList == null) {
			System.out.println("No PlayerData to load!");
			return;
		}
		
		System.out.println(playerDataList.get(playerData.getPlayer().getUniqueId().toString()));
		System.out.println("Attempting to load teams data... ");
		
		String uuid = (String) playerDataList.get(playerData.getPlayer().getUniqueId().toString());
		
		if (uuid != null) {
			
			TeamObj team = TeamObj.findID(UUID.fromString(uuid));
			System.out.println(UUID.fromString(uuid));
			System.out.println(team);
			playerData.setData("team", team);
			System.out.println(uuid);
			System.out.println("Team loaded!");
			
		} else {
			playerData.setData("team", null);
			System.out.println("no team to load!");
			
		}
		playerInit = true;
	}
}
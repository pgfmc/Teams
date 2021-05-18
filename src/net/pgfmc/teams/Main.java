package net.pgfmc.teams;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import net.pgfmc.teams.commands.Team;
import net.pgfmc.teams.commands.TeamAccept;
import net.pgfmc.teams.commands.TeamRequest;
import net.pgfmc.teams.commands.VoteAllyRequest;
import net.pgfmc.teams.commands.VoteBan;
import net.pgfmc.teams.commands.VoteCommand;
import net.pgfmc.teams.commands.VoteKick;
import net.pgfmc.teams.commands.VoteRenameTeam;
import net.pgfmc.teams.events.InventoryEvents;
import net.pgfmc.teams.events.PlayerEvents;

public class Main extends JavaPlugin {
	
	public static Main plugin;
	
	@Override
	public void onEnable() {
		
		plugin = this;
		
		File file = new File(plugin.getDataFolder() + "\\database.yml"); // Creates a File object
		
		try {
			if (file.createNewFile()) {
				System.out.println("database.yml created!");
			} else {
				System.out.println("database.yml already Exists!");
				
				Database.loadPlayerData();
				//Database.loadTeams();
			}
			
		
		} catch (IOException e) {
			System.out.println("database.yml Couldn't be created!");
			e.printStackTrace();
		} 
		
		getServer().getPluginManager().registerEvents(new InventoryEvents(), this);
		getServer().getPluginManager().registerEvents(new PlayerEvents(), this);
		
		
		getCommand("team").setExecutor(new Team());
		getCommand("teamRequest").setExecutor(new TeamRequest());
		getCommand("teamAccept").setExecutor(new TeamAccept());
		getCommand("vote").setExecutor(new VoteCommand());
		getCommand("voteKick").setExecutor(new VoteKick());
		getCommand("voteBan").setExecutor(new VoteBan());
		getCommand("voteAllyRequest").setExecutor(new VoteAllyRequest());
		getCommand("voteRenameTeam").setExecutor(new VoteRenameTeam());
	}
	
	@Override
	public void onDisable() {
		Database.savePlayerData();
		Database.saveTeams();
	}
	
	public static ItemStack createItem(String name, Material mat, List<String> lore)
	{
		ItemStack item = new ItemStack(mat, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public static String makePossesive(String name) {
		if (name.endsWith("s")) {
			return (name + "'");
		} else {
			return (name + "'s");
		}
	}
	
	public static void requestHandler(Player attacker, Player target) {
		
		TeamObj ATK = TeamObj.findPlayer(attacker);
		TeamObj DEF = TeamObj.findPlayer(target);
		PlayerData ATKP = PlayerData.findPlayerData(attacker);
		PlayerData DEFP = PlayerData.findPlayerData(target);
		
		if (ATKP.getRequest() == null && DEFP.getRequest() == null) {
		
			if (ATK != null && ATK == DEF) { // if both players are on the same team // denies request
				attacker.sendMessage("You are already on the same team as them!");
			
			} else if (ATK != null && DEF != null && ATK != DEF) { // if both players are on different teams // denies request
				attacker.sendMessage("They are already in a team!");
				attacker.sendMessage("If you want to join their team, leave your current team and ask for another request!");
			
			} else if (ATK == null && DEF != null) { // if the attacker isnt in a team, but the target is
				attacker.sendMessage("Request sent to <name> to join <DEF>");
				new PendingRequest(attacker, target, DEF);
				target.sendMessage(attacker.getCustomName() + " has sent you a request to join your team!");
				target.sendMessage("Hit them with a flower, or type /TA or /teamAccept to accept!");
			
			} else if (ATK == null && DEF == null) { // if both players arent on a team
				attacker.sendMessage("request sent to " + target.getCustomName() + ".");
				attacker.sendMessage("A new Team will be created upon " + target.getCustomName() + " accepting the Request.");
				new PendingRequest(attacker, target, null);
				target.sendMessage(attacker.getCustomName() + " has sent you a request to join your team!");
				target.sendMessage("Hit them with a flower, or type /TA or /teamAccept to accept!");
				
			} else if (ATK != null && DEF == null) { // if the attacker is in a team, but the target isnt
				attacker.sendMessage("Invite sent to " + target.getCustomName() + " to join your team.");
				new PendingRequest(attacker, target, ATK);
				target.sendMessage(attacker.getCustomName() + " has invited you to their team, " + ATK.getName() + ".");
				target.sendMessage("Hit them with a flower, or type /TA or /teamAccept to accept!");
			}
			
		} else if (ATKP.getRequest() != null && ATKP.getRequest() == DEFP.getRequest()) {
			PendingRequest PR = ATKP.getRequest();
			
			if (ATK != null && DEF == null) { // if the attacker isnt in a team, but the target is
				for (UUID uuid : ATK.getMembers()) {
					if (Bukkit.getPlayer(uuid) != null) {
						Bukkit.getPlayer(uuid).sendMessage(attacker.getCustomName() + " has joined your team!");
					}
				}
				PR.acceptRequest(false);
				attacker.sendMessage("You have joined " + ATK.getName() + "!");
				
			
			} else if (ATK == null && DEF == null) { // if both players arent on a team
				PR.createTeamRequestAccept();
				attacker.sendMessage("You have joined " + Main.makePossesive(target.getCustomName()) + " team!");
				target.sendMessage(attacker.getCustomName() + " has joined your team!");
				
			
			} else if (ATK == null && DEF != null) { // if the attacker is in a team, but the target isnt
				for (UUID uuid : DEF.getMembers()) {
					if (Bukkit.getPlayer(uuid) != null) {
						Bukkit.getPlayer(uuid).sendMessage(attacker.getCustomName() + " has joined your team!");
					}
				}
				PR.acceptRequest(true);
				attacker.sendMessage("You have joined the team " + DEF.getName() + "!");
			}
		}
	}
	
	public static void requestHandler(Player attacker) {
		
		// variable declaration
		
		TeamObj ATK = TeamObj.findPlayer(attacker);
		PlayerData ATKP = PlayerData.findPlayerData(attacker);
		
		if (ATKP.getRequest() == null) {
			return;
		}
		
		Player target = ATKP.getRequest().getAttacker();
		TeamObj DEF = TeamObj.findPlayer(target);
		PlayerData DEFP = PlayerData.findPlayerData(target);
		
		if (ATKP.getRequest() != null && ATKP.getRequest() == DEFP.getRequest()) {
			
			PendingRequest PR = ATKP.getRequest();
			
			if (ATK != null && DEF == null) { // if the attacker isnt in a team, but the target is
				for (UUID uuid : ATK.getMembers()) {
					if (Bukkit.getPlayer(uuid) != null) {
						Bukkit.getPlayer(uuid).sendMessage(attacker.getCustomName() + " has joined your team!");
					}
				}
				PR.acceptRequest(false);
				attacker.sendMessage("You have joined " + ATK.getName() + "!");
				
			
			} else if (ATK == null && DEF == null) { // if both players arent on a team
				PR.createTeamRequestAccept();
				attacker.sendMessage("You have joined " + Main.makePossesive(target.getCustomName()) + " team!");
				target.sendMessage(attacker.getCustomName() + " has joined your team!");
				
			
			} else if (ATK == null && DEF != null) { // if the attacker is in a team, but the target isnt
				for (UUID uuid : DEF.getMembers()) {
					if (Bukkit.getPlayer(uuid) != null) {
						Bukkit.getPlayer(uuid).sendMessage(attacker.getCustomName() + " has joined your team!");
					}
				}
				PR.acceptRequest(true);
				attacker.sendMessage("You have joined the team " + DEF.getName() + "!");
			}
		} else {
			attacker.sendMessage("There is no Team Request to Accept!");
		}
	}
	
	public static ItemStack createItem(String name, Material mat) // function for creating an item with a custom name
	{
		ItemStack item = new ItemStack(mat, 1);
		ItemMeta meta = item.getItemMeta();
		
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		return item;
	}
}

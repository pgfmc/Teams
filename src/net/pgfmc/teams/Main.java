package net.pgfmc.teams;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Beacon;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import com.onarandombox.MultiverseCore.MultiverseCore;

import net.pgfmc.pgfessentials.EssentialsMain;
import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;
import net.pgfmc.teams.commands.InspectCommand;
import net.pgfmc.teams.commands.LeaveTeamCommand;
import net.pgfmc.teams.commands.LeaveTeamConfirmCommand;
import net.pgfmc.teams.commands.Team;
import net.pgfmc.teams.commands.TeamAccept;
import net.pgfmc.teams.commands.TeamRequest;
import net.pgfmc.teams.events.AttackEvent;
import net.pgfmc.teams.events.BBEvent;
import net.pgfmc.teams.events.BPE;
import net.pgfmc.teams.events.BlockInteractEvent;
import net.pgfmc.teams.events.InventoryEvents;
import net.pgfmc.teams.events.MessageEvent;

public class Main extends JavaPlugin {
	
	public static Main plugin;
	public static World survivalWorld;
	public static List<Beacon> beacons;
	
	
	@Override
	public void onEnable() {
		
		plugin = this;
		
		plugin.getDataFolder().mkdirs();
		
		survivalWorld = ((MultiverseCore) Bukkit.getPluginManager().getPlugin("Multiverse-Core")).getMVWorldManager().getMVWorld("survival").getCBWorld();
		
		
		File file = new File(plugin.getDataFolder() + "\\database.yml"); // Creates a File object
		try {
			if (file.createNewFile()) {
				System.out.println("database.yml created!");
			} else {
				System.out.println("database.yml already Exists!");
				
				Database.loadTeams();
				
				((EssentialsMain) Bukkit.getPluginManager().getPlugin("PGF-Essentials")).ActivateListener(new Database(), false);
			}
			
		} catch (IOException e) {
			System.out.println("database.yml Couldn't be created!");
			e.printStackTrace();
		}
		
		file = new File(plugin.getDataFolder() + "\\EABlockData");
		if (file.mkdirs()) {
			System.out.println("EABlockData folder created!");
		} else {
			System.out.println("EABlockData already Exists!");
		}
		
		file = new File(plugin.getDataFolder() + "\\DeepBlockData");
		if (file.mkdirs()) {
			System.out.println("DeepBlockData folder created!");
		} else {
			System.out.println("DeepBlockData already Exists!");
		}
		
		beacons = BlockDataManager.getBeacons();
		
		getServer().getPluginManager().registerEvents(new InventoryEvents(), this);
		getServer().getPluginManager().registerEvents(new BlockInteractEvent(), this);
		getServer().getPluginManager().registerEvents(new AttackEvent(), this);
		getServer().getPluginManager().registerEvents(new BBEvent(), this);
		getServer().getPluginManager().registerEvents(new BPE(), this);
		getServer().getPluginManager().registerEvents(new MessageEvent(), this);
		
		getCommand("team").setExecutor(new Team());
		getCommand("teamRequest").setExecutor(new TeamRequest());
		getCommand("teamAccept").setExecutor(new TeamAccept());
		getCommand("leaveTeam").setExecutor(new LeaveTeamCommand());
		getCommand("leaveTeamConfirm").setExecutor(new LeaveTeamConfirmCommand());
		getCommand("inspector").setExecutor(new InspectCommand());
		
		
	}
	
	@Override
	public void onDisable() {
		Database.saveTeams();
		((EssentialsMain) Bukkit.getPluginManager().getPlugin("PGF-Essentials")).ActivateListener(new Database(), true);
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
		
		if (attacker.getGameMode() == GameMode.SURVIVAL && target.getGameMode() == GameMode.SURVIVAL) {
			
			TeamObj ATK = TeamObj.getTeam(attacker);
			TeamObj DEF = TeamObj.getTeam(target);
			PlayerData ATKP = PlayerData.getPlayerData(attacker);
			PlayerData DEFP = PlayerData.getPlayerData(target);
			
			if (ATKP.getData("request") == null && DEFP.getData("request") == null) {
				
			
				if (ATK != null && ATK == DEF) { // if both players are on the same team // denies request
					attacker.sendMessage("You are already on the same team as them!");
				
				} else if (ATK != null && DEF != null && ATK != DEF) { // if both players are on different teams // denies request
					attacker.sendMessage("They are already in a team!");
					attacker.sendMessage("If you want to join their team, leave your current team and ask for another request!");
				
				} else if (ATK == null && DEF != null) { // if the attacker isnt in a team, but the target is
					attacker.sendMessage("Request sent to <name> to join <DEF>");
					new PendingRequest(attacker, target, DEF);
					target.sendMessage(attacker.getDisplayName() + " has sent you a request to join your team!");
					target.sendMessage("Hit them with a flower, or type /TA or /teamAccept to accept!");
				
				} else if (ATK == null && DEF == null) { // if both players arent on a team
					attacker.sendMessage("request sent to " + target.getDisplayName() + ".");
					attacker.sendMessage("A new Team will be created upon " + target.getDisplayName() + " accepting the Request.");
					new PendingRequest(attacker, target, null);
					target.sendMessage(attacker.getDisplayName() + " has sent you a request to join your team!");
					target.sendMessage("Hit them with a flower, or type /TA or /teamAccept to accept!");
					
				} else if (ATK != null && DEF == null) { // if the attacker is in a team, but the target isnt
					attacker.sendMessage("Invite sent to " + target.getDisplayName() + " to join your team.");
					new PendingRequest(attacker, target, ATK);
					target.sendMessage(attacker.getDisplayName() + " has invited you to their team, " + ATK.getName() + ".");
					target.sendMessage("Hit them with a flower, or type /TA or /teamAccept to accept!");
				}
				
			} else if (ATKP.getData("request") != null && ATKP.getData("request") == DEFP.getData("request")) {
				PendingRequest PR = (PendingRequest) ATKP.getData("request");
				
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
					attacker.sendMessage("You have joined " + Main.makePossesive(target.getDisplayName()) + " team!");
					target.sendMessage(attacker.getCustomName() + " has joined your team!");
					
				} else if (ATK == null && DEF != null) { // if the attacker is in a team, but the target isnt
					for (UUID uuid : DEF.getMembers()) {
						if (Bukkit.getPlayer(uuid) != null) {
							Bukkit.getPlayer(uuid).sendMessage(attacker.getDisplayName() + " has joined your team!");
						}
					}
					PR.acceptRequest(true);
					attacker.sendMessage("You have joined the team " + DEF.getName() + "!");
				}
			}
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
	
	public static boolean playerInForcefield(Player player) {
		
		for (Beacon beacon : beacons) {
			
			Collection<LivingEntity> playersList = beacon.getEntitiesInRange();
			
			Block block = beacon.getBlock();
			
			TeamObj DEF = TeamObj.getTeam(BlockDataManager.getContainerData(block).getFirst());
			
			if (TeamObj.getTeam(player) != DEF && playersList.contains(player)) {
				return true;
			}
		}
		return false;
		
	}
}

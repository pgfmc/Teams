package net.pgfmc.teams.duel;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;

import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;
import net.pgfmc.teams.teamscore.Main;

public class Duel {
	
	/*
	private Player initiator;
	
	private Player acceptor;
	*/
	
	private World world;
	
	private Map<PlayerData, PlayerState> Players = new HashMap<>();
	
	private static Set<Duel> instances = new HashSet<>();
	
	private DuelState state = DuelState.BATTLEPENDING;
	
	/**
	 * An enum to represent each stage in a duel;
	 * 
	 * <REQUESTPENDING> is when the duel challenge hasn't been accepted yet.
	 * <BATTLEPENDING> is when the duel has been accepted and the duel is about to start
	 * <INBATTLE> is when the duel has started!
	 * <TIMEOUT> idk
	 */
	public enum DuelState {
		BATTLEPENDING,
		INBATTLE,
		END
	}
	
	public enum PlayerState {
		BYSTANDER,
		JOINING,
		DUELING,
		KILLED
	}
	
	/**
	 * creates a new duel, where PR and CH are who begins the duel.
	 * @param PR player PR's PlayerData
	 * @param CH player CH's PlayerData
	 */
	public Duel(PlayerData PR, PlayerData CH) {
		
		world = PR.getPlayer().getWorld();
		PR.setData("duel", this);
		CH.setData("duel", this);
		Players.put(PR, PlayerState.JOINING);
		Players.put(CH, PlayerState.JOINING);
		/*
		initiator = PR.getPlayer();
		acceptor = CH.getPlayer();
		*/
		
		PR.sendMessage(CH.getName() + " §6has accepted your Challenge to §cDuel!");
		CH.sendMessage("§6You have accepted the Challenge!");
		//Bukkit.broadcastMessage(acceptor.getDisplayName() + " and " + initiator.getDisplayName() + " are beginning to duel!!");
		
		
	}
	
	public DuelState getState() { // returns the state of the duel
		return(state);
	}
	
	public void setState(DuelState gimmer) { // Changes the state
		state = gimmer;
	}
	
	public Map<PlayerData, PlayerState> getPlayers() {
		return(Players);
	}
	
	public World getWorld() {
		return(world);
	}
	
	/*
	public static void duelRequest(Player attacker, Player target) { // ----------------------------------------------------------------- Duel Requester
		attacker.sendRawMessage("§cDuel §6Request sent! Request will expire in 15 seconds."); //  sent to the sender
		target.sendRawMessage(attacker.getDisplayName() + " §6has Challenged you to a §cDuel!!"); // message sent to the target
		target.sendRawMessage("§6To accept the Challenge, hit them back!");
		target.sendRawMessage("§6The Challenge will expire in 15 seconds.");
		
		PlayerData PR = PlayerData.getPlayerData(attacker);
		PlayerData CH = PlayerData.getPlayerData(target);
		
		Duel Grequest = new Duel(PR, CH); // ----------------------------------- creates a new Duel instance
		instances.add(Grequest);
		
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
            
            @Override
            public void run() // 60 second long cooldown, in which the plugin will wait for 
            {
            	if (Grequest.getState() == DuelState.REQUESTPENDING) {
            		
            		instances.remove(Grequest);
            		attacker.sendRawMessage("§6The Challenge has expired!");
            		
            		for (PlayerData planar : Grequest.getPlayers().keySet()) {
            			planar.setData("duel", null);
            			Grequest.getPlayers().put(planar, PlayerState.INGAME);
            		}
            	}
            }
        }, 300);
	}
	
	public void duelAccept() { // Duel Acceptor
		
		initiator.sendRawMessage(initiator.getName() + " §6has accepted your Challenge to §cDuel!");
		acceptor.sendRawMessage("§6You have accepted the Challenge!");
		Bukkit.broadcastMessage(acceptor.getDisplayName() + " and " + initiator.getDisplayName() + " are beginning to duel!!");
		
		duelStart(initiator);
		duelStart(acceptor);
		
		Duel billNye = this; // ---------------disables attack damage
		billNye.setState(DuelState.BATTLEPENDING);
		
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
			@Override
			public void run() {
				billNye.setState(DuelState.INBATTLE);
			}
		}, 60);
	}*/
	
	public void beginDuel() {
		
		
		
		
		
		
		
		
		
	}
	
	/**
	 * Method to add a player to a duel after it has already started.
	 * @param pd The playerData of the player that is joining.
	 */
	public void join(PlayerData pd) {
		
		pd.setData("duel", this);
		Players.put(pd, PlayerState.JOINING);
		
		startPlayer(pd);
		
		
	}
	
	/**
	 * Prepares the player for the duel.
	 * @param pd The playerData of the player that is playing.
	 */
	public void startPlayer(PlayerData pd) {
		
		Player p = pd.getPlayer();
		
		p.setHealth(20.0); // -------------------sets health to full, restores all hunger, and increases saturation
		p.setFoodLevel(20);
		p.setSaturation(2);
		
		// The Duel begin animation.
		p.sendTitle("§c3", "", 2, 16, 2); // ------------------------------------------------------- onscreen animations and countdown
		
		HashMap<String, Integer> introAnimation = new HashMap<>();
		introAnimation.put("§c2", 20);
		introAnimation.put("§c1", 40);
		introAnimation.put("§6D    U    E    L    !", 60);
		introAnimation.put("§6D   U   E   L   !", 62);
		introAnimation.put("§6D  U  E  L  !", 64);
		introAnimation.put("§6D U E L !", 66);
		introAnimation.put("§6DUEL!", 68);
		
		for (String key : introAnimation.keySet()) { // runs through the list
			
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
				@Override
				public void run() {
					p.sendTitle(key, "", 0, 20, 0);
				}
			}, introAnimation.get(key));
		}
		
		// Queues the state change from JOINING to DUELING
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
			@Override
			public void run() {
				Players.put(pd, PlayerState.DUELING);
			}
		}, 60);
	}
	
	public void playerDie(PlayerData ded, PlayerData killer, boolean wasKilled) {
		
		// restores their health if they are still in the game.
		if (ded.getPlayer() != null) {
			ded.getPlayer().setHealth(20.0);
			
		}
		
		Players.put(ded, PlayerState.KILLED);
		
		// all alive players in the duel.
		List<PlayerData> HELLOGAMERS = Players.entrySet().stream()
			.filter(x -> {
				return (x.getValue() == PlayerState.DUELING || x.getValue() == PlayerState.JOINING);
			})
			.map(x -> {
				return x.getKey();
			})
			.collect(Collectors.toList());
			
		// Player duel Death messages for everyone in the duel.
		for (PlayerData pd : HELLOGAMERS) {
			
			if (killer != null) {
				pd.sendMessage(ded.getName() + " was killed by " + killer.getName() + ".");
				pd.playSound(Sound.ENTITY_PLAYER_BIG_FALL);
			} else if (wasKilled) {
				pd.sendMessage(ded.getName() + " was killed.");
				pd.playSound(Sound.ENTITY_PLAYER_BIG_FALL);
			} else {
				pd.sendMessage(ded.getName() + " has left the duel.");
				pd.playSound(Sound.ENTITY_WOLF_WHINE);
			}
		}
		
		if (HELLOGAMERS.size() == 1) {
			
			Duel d = this;
			
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
				
				@Override
	            public void run() {
					
					PlayerData Winner = HELLOGAMERS.get(0);
					
					for (PlayerData pd : Players.keySet()) {
						pd.playSound(Sound.BLOCK_NOTE_BLOCK_PLING);
						pd.sendMessage(Winner.getName() + " has won the duel!");
					}
					
					Winner.playSound(Sound.ENTITY_PLAYER_LEVELUP);
					
					Winner.getPlayer().setHealth(20.0);
					
					Bukkit.broadcastMessage(Winner.getName() + " §6 has won the §cDuel!!");
					setState(DuelState.END);
	            	
	    			for (PlayerData planar : Players.keySet()) {
	        			planar.setData("duel", null);
	        		}
	    			instances.remove(d);
	            }
	        }, 3); // 10 seconds
			
			
			
			
			
			
			
			
			
		}
			
			
			
		
		
		
		
		for (PlayerData planar : Players.keySet()) {
			planar.setData("duel", null);
		}
		instances.remove(this);
	}
	
	
	
	
	public void duelDie(PlayerData simp) {
		// ends a player's time to duel (does NOT remove them from the DuelClass instance, and will not be able to rejoin OR enter a new duel until the old one is over)
		
		if (simp.getPlayer() != null) {
			simp.getPlayer().setHealth(20.0);
			
		}
		
		List<PlayerData> HELLOGAMERS = Players.entrySet().stream()
				.filter(x -> {
					return (x.getValue() == PlayerState.DUELING || x.getValue() == PlayerState.JOINING);
				})
				.map(x -> {
					return x.getKey();
				})
				.collect(Collectors.toList());
		
		if (HELLOGAMERS.size() == 1) { // if there is only one person left alive
			
			simp.getPlayer().getWorld().playSound(simp.getPlayer().getLocation(), Sound.ENTITY_WITHER_HURT, 10, 10); // plays a death sound
			
			Player Winner = HELLOGAMERS.get(0).getPlayer();
			
			Winner.setHealth(20.0);
			//SaveData.loadPlayer(Winner); // loads inventory and saves scores
			// XXX SaveData.Scoreboard(Winner);
			Bukkit.broadcastMessage(Winner.getDisplayName() + " §6 has won the §cDuel!!");
			this.setState(DuelState.END);
			
			Duel d = this;
			
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
				
				@Override
	            public void run() {
	            	
	    			for (PlayerData planar : Players.keySet()) {
	        			planar.setData("duel", null);
	        		}
	    			instances.remove(d);
	            }
	        }, 200); // 10 seconds
		}
	}
	
	public void duelKick(PlayerData simp) { // ends the duel, and restores health
		duelDie(simp);
	}
}

package net.pgfmc.teams.events;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import com.mojang.datafixers.util.Pair;

import net.pgfmc.teams.BlockDataManager;
import net.pgfmc.teams.Main;
import net.pgfmc.teams.PlayerData;
import net.pgfmc.teams.TeamObj;

public class PlayerEvents implements Listener {
	
	private boolean isFlower(Material material) {
		
		// checks if the input is a flower
		// is only used in playerAttackEvent, it has its own function to make the code more readable
		
		if (material == Material.BLUE_ORCHID || material == Material.ROSE_BUSH || material == Material.DANDELION || material == Material.ORANGE_TULIP || material == Material.PINK_TULIP || material == Material.RED_TULIP || material == Material.WHITE_TULIP || material == Material.SUNFLOWER || material == Material.OXEYE_DAISY || material == Material.POPPY || material == Material.ALLIUM || material == Material.AZURE_BLUET || material == Material.CORNFLOWER || material == Material.LILY_OF_THE_VALLEY || material == Material.WITHER_ROSE || material == Material.PEONY || material == Material.LILAC) {
			return true;
		}
		return false;
	}
	
	@EventHandler
	public void playerAttackEvent(EntityDamageByEntityEvent e) { // hit another player with a flower to ask to be on the same team.
		
		if (e.getDamager() instanceof Player && e.getEntity() instanceof Player && isFlower(((Player) e.getDamager()).getInventory().getItemInMainHand().getType())) {
			Main.requestHandler((Player) e.getDamager(), (Player) e.getEntity());
		}
	}
	
	@EventHandler
	public void playerMessageEvent(AsyncPlayerChatEvent e) { // sets the name of the team if they are in naming mode
		if (PlayerData.findPlayerData(e.getPlayer()).getNaming()) {
			PlayerData playerData = PlayerData.findPlayerData(e.getPlayer());
			playerData.getTeam().setName(e.getMessage());
			e.getPlayer().sendMessage(e.getMessage() + " is now the name of your team!");
			playerData.setNamingFalse();
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void playerJoinEvent(PlayerJoinEvent e) { // creates a new PlayerData class if there isn't one already for the player.
		
		PlayerData playerData = PlayerData.findPlayerData(e.getPlayer());
		
		if (playerData == null) {
			System.out.println("Player " + e.getPlayer().getName() + " has a new PlayerData!");
			new PlayerData(e.getPlayer());
		} else {
			System.out.println("Player already has a PlayerData, so a new one will not be created.");
		}
		
		System.out.println(e.getPlayer().getWorld().getName());
	}
	
	@EventHandler
	public void blockPlace(BlockPlaceEvent e) {
		
		if (e.getPlayer() != null && e.getPlayer().getGameMode() == GameMode.SURVIVAL && e.getBlock() != null && e.getBlock().getWorld().getName().equals(Main.survivalWorld.getName())) { // Inspector mode enabled
			PlayerData PD = PlayerData.findPlayerData(e.getPlayer());
			
			if (PD.getDebug() && e.getPlayer().getGameMode() == GameMode.CREATIVE) {
				e.setCancelled(true);
				BlockDataManager.returnBlockData(e.getBlock(), e.getPlayer()); // ----------------------- CHANGE IN FUTURE!!!!
				return;
				
			} else if (e.getPlayer().getGameMode() == GameMode.SURVIVAL) { // Inspector Mode disabled
				BlockDataManager.updateBlock(e.getBlock(), e.getPlayer(), true);
				return;
			}
		}
	}
	
	@EventHandler
	public void blockBreak(BlockBreakEvent e) {
		
		if (e.getPlayer() != null && e.getBlock() != null && e.getBlock().getWorld().getName().equals(Main.survivalWorld.getName())) {
			PlayerData PD = PlayerData.findPlayerData(e.getPlayer());
			
			if (e.getPlayer().getGameMode() == GameMode.SURVIVAL) {
				
				Material mat = e.getBlock().getType();
				
				
				
				
				
				
				// checks to see if the block can hold items
				// if so, it checks in database.yml for who placed it
				// if the breaker team == placer team, then the block can be broken
				// if the block has no recorded placer team, then the block can be broken
				
				if (mat == Material.CHEST || mat == Material.BEACON || mat == Material.FURNACE || mat == Material.BLAST_FURNACE || mat == Material.SMOKER || 
						mat == Material.DISPENSER || mat == Material.DROPPER || mat == Material.TRAPPED_CHEST || mat == Material.BARREL || mat == Material.CAMPFIRE || 
						mat == Material.SOUL_CAMPFIRE || mat == Material.SHULKER_BOX || mat == Material.JUKEBOX || mat == Material.LECTERN || mat == Material.HOPPER || mat == Material.BREWING_STAND) {
					TeamObj team = TeamObj.findPlayer(BlockDataManager.getContainerData(e.getBlock()).getFirst());
					if (team != null && team != TeamObj.findPlayer(e.getPlayer())) {
						e.setCancelled(true);
						e.getPlayer().sendMessage("You can't break that block, it's owned by another team!");
						
					} else {
						BlockDataManager.updateBlock(e.getBlock(), e.getPlayer(), false);
					}
				} else {
					BlockDataManager.updateBlock(e.getBlock(), e.getPlayer(), false);
				}
				
			} else if (PD.getDebug() && e.getPlayer().getGameMode() == GameMode.CREATIVE) { // inspector mode only
					e.setCancelled(true);
					BlockDataManager.returnBlockData(e.getBlock(), e.getPlayer());
					return;
			}
		}
	}
 	
	@EventHandler
	public void blockInteract(PlayerInteractEvent e) { // needs rewrite VVV
		
		// controls clicking containers and beacons;
		
		if (e.getPlayer() != null && e.getPlayer().getGameMode() == GameMode.SURVIVAL && e.getClickedBlock() != null && e.getClickedBlock().getWorld() == Main.survivalWorld) {
			Material mat = e.getClickedBlock().getType();
			if (mat == Material.CHEST || mat == Material.BEACON || mat == Material.FURNACE || mat == Material.BLAST_FURNACE || mat == Material.SMOKER || 
					mat == Material.DISPENSER || mat == Material.DROPPER || mat == Material.TRAPPED_CHEST || mat == Material.BARREL || mat == Material.CAMPFIRE || 
					mat == Material.SOUL_CAMPFIRE || mat == Material.SHULKER_BOX || mat == Material.JUKEBOX || mat == Material.LECTERN || mat == Material.HOPPER || mat == Material.BREWING_STAND) {
				
				Block block = e.getClickedBlock(); 
				Player player = e.getPlayer();
				TeamObj team = TeamObj.findPlayer(player);
				Pair<OfflinePlayer, Boolean> pair = BlockDataManager.getContainerData(block);
				
				if (team == null || pair == null) {
					return;
				}
				
				if (pair.getSecond()) {
					if (team != TeamObj.findPlayer(pair.getFirst())) {

						e.setCancelled(true);
						
						switch(block.getType()) {
						
						case BARREL: player.sendMessage("This barrel is locked!");
						case BLAST_FURNACE: player.sendMessage("This blast furnace is locked!");
						case BREWING_STAND: player.sendMessage("This brewing stand is locked!");
						case CHEST: player.sendMessage("This chest is locked!");
						case DISPENSER: player.sendMessage("This dispenser is locked!");
						case DROPPER: player.sendMessage("This dropper is locked!");
						case FURNACE: player.sendMessage("This furnace is locked!");
						case HOPPER: player.sendMessage("This hopper is locked!");
						case SHULKER_BOX: player.sendMessage("This shulker box is locked!");
						case SMOKER: player.sendMessage("This smoker is locked!");
						default:
							break;
						}
						return;
						
					} else if (player.getInventory().getItemInMainHand().getType() == Material.TRIPWIRE_HOOK) { // unlocks block if conditions are met
						e.setCancelled(true);
						
						player.sendMessage("Unlocked!");
						player.playSound(e.getPlayer().getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
						BlockDataManager.setLocked(block, false);
					}
					
				} else if (team == TeamObj.findPlayer(pair.getFirst()) && player.getInventory().getItemInMainHand().getType() == Material.TRIPWIRE_HOOK) {
					e.setCancelled(true);
					
					player.sendMessage("Locked!");
					player.playSound(e.getPlayer().getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
					BlockDataManager.setLocked(block, true);
				}
			}
		}
	}
}

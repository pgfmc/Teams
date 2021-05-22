package net.pgfmc.teams.events;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Beacon;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import net.pgfmc.teams.Database;
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
	public void playerAttackEvent(EntityDamageByEntityEvent e) {
		
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
	public void playerJoinEvent(PlayerJoinEvent e) {
		
		PlayerData playerData = PlayerData.findPlayerData(e.getPlayer());
		
		
		
		if (playerData == null) {
			System.out.println("Player " + e.getPlayer().getName() + " has a new PlayerData!");
			new PlayerData(e.getPlayer());
		} else {
			System.out.println("Player already has a PlayerData, so a new one will not be created.");
		}
	}
	
	@EventHandler
	public void blockPlace(BlockPlaceEvent e) {
		
		if (e.getBlock().getState() instanceof Container || e.getBlock().getState() instanceof Beacon) {
			Database.saveBlockLocation(e.getBlock(), e.getPlayer());
		}
	}
	
	@EventHandler
	public void blockBreak(BlockBreakEvent e) {
		if (e.getBlock().getState() instanceof Container || e.getBlock().getState() instanceof Beacon) {
			if (TeamObj.findPlayer(Database.getBlockPlacer(e.getBlock())) != TeamObj.findPlayer(e.getPlayer())) {
				e.setCancelled(true);
				e.getPlayer().sendMessage("You can't break that block, it's owned by another team!");
			} else {
				Database.deleteBlockLocation(e.getBlock());
			}
		}
	}
 	
	
	
	@EventHandler
	public void blockInteract(PlayerInteractEvent e) {
		
		if (e.getClickedBlock() != null && e.getClickedBlock().getWorld() != Bukkit.getWorld("Survival") && e.getClickedBlock().getState() instanceof Container) {
			
			Block block = e.getClickedBlock();
			Container blockState = (Container) block.getState();
			Player player = e.getPlayer();
			TeamObj team = TeamObj.findPlayer(player);
			
			if (blockState.isLocked()) { // all conditions :)
				
				if (PlayerData.findPlayerData(Bukkit.getOfflinePlayer(UUID.fromString(blockState.getLock()))).getTeam() != team) {
					
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
					
				} else {
					if (player.getInventory().getItemInMainHand().getType() == Material.TRIPWIRE_HOOK) {
						e.setCancelled(true);
						
						player.sendMessage("Unlocked!");
						player.playSound(e.getPlayer().getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
						blockState.setLock(null);
					} 
				}
				
			} else if (player.getInventory().getItemInMainHand().getType() == Material.TRIPWIRE_HOOK && TeamObj.findPlayer(Database.getBlockPlacer(block)) == team) {
				e.setCancelled(true);
				player.sendMessage("Locked!");
				player.playSound(e.getPlayer().getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
				
				blockState.setLock(player.getUniqueId().toString());
				
			}
		}
	}
}

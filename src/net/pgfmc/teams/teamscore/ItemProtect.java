package net.pgfmc.teams.teamscore;

import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.util.Vector;

import net.pgfmc.core.DimManager;
import net.pgfmc.core.cmd.admin.Skull;
import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.teams.friends.Friends;
import oshi.util.tuples.Pair;
 /**
  * Prevents other players from picking up someone else's items on death.
  * Effect fades after 2 minutes.
  * @author bk + CrimsonDart
  *
  */
public class ItemProtect implements Listener {
	
	private static HashMap<Item, Pair<PlayerData, Consumer<?>>> items = new HashMap<>();
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		if (!DimManager.isSurvivalWorld(e.getEntity().getWorld())) { return; }
		
		// declare variables
		Player p = e.getEntity();
		Location loc = p.getLocation();
		World world = p.getWorld();
		PlayerData pd = PlayerData.getPlayerData(p);
		
		// converts dropped items
		List<Item> droppedItems = e.getDrops().stream().map((x -> {
			return world.dropItem(loc, x);
		})).collect(Collectors.toList());
		
		// Drop a custom player head if death by charged creeper
		if (p.getLastDamageCause().getEntityType().equals(EntityType.CREEPER))
		{
			Creeper creeper = (Creeper) p.getLastDamageCause().getEntity();
			if (creeper.isPowered())
			{
				droppedItems.add(world.dropItem(loc, Skull.getHead(p.getUniqueId())));
			}
		}
		
		// activates the items
		for (Item drop : droppedItems) {
			
			drop.setGlowing(true);
			drop.setInvulnerable(true);
			drop.setVelocity(new Vector());
			
			items.put(drop, new Pair<PlayerData, Consumer<?>>(pd, x -> {
				//List<PlayerData> list = pd.getData("friends");
				
				items.remove(drop);
				
				if (drop.isValid()) {
					drop.setGlowing(false);
					drop.setInvulnerable(false);
				}
				
			}));
		}
		
		// starts timer for deactivating the items
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
			
			@Override
			public void run()
			{
				for (Item drop : droppedItems) {
					Pair<PlayerData, Consumer<?>> data = items.get(drop);
					
					if (data != null) {
						data.getB().accept(null);
					}
					
				}
			}
			
		}, 2400); // two minutes
		
		e.getDrops().clear();
		p.sendMessage("§dYour dropped items are protected for 2 minutes.");
	}
	
	@EventHandler
	public void onPickup(EntityPickupItemEvent e) {
		
		if (e.getEntity() instanceof OfflinePlayer) {
			Item item = e.getItem();
			
			
			Pair<PlayerData, Consumer<?>> data = items.get(item);
			
			if (data != null) {
				
				PlayerData pd = PlayerData.getPlayerData((OfflinePlayer) e.getEntity());
				if (data.getA() == pd) {
					data.getB().accept(null);
				} else if (Friends.getFriendsMap(pd).containsKey(data.getA())) {
					data.getB().accept(null);
				} else {
					e.setCancelled(true);
				}
			}
			
		}
		
		
		
		
	}

}

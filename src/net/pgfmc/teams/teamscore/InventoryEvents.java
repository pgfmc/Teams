package net.pgfmc.teams.teamscore;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;
import net.pgfmc.teams.playerLogistics.TeamLeaveConfirmInventory;


public class InventoryEvents implements Listener {
	
	@EventHandler
	public void onClickTeamBase(InventoryClickEvent e) {
		
		if (e.getClickedInventory() != null) {
			
			InventoryHolder invHolder = e.getClickedInventory().getHolder();
			if ((invHolder instanceof TeamInventory || invHolder instanceof TeamLeaveConfirmInventory) && (e.getClick() == ClickType.SHIFT_LEFT || e.getClick() == ClickType.SHIFT_RIGHT)) {
				e.setCancelled(true);
				return;
			}
			
			Player p = (Player) e.getWhoClicked(); // Not going to check if this is a player or not because it should be, right???
			Inventory inv = e.getClickedInventory();

			if (inv.getHolder() != null && inv.getHolder() instanceof TeamInventory) {  // return; if the inventory isn't TeamBase
			
				e.setCancelled(true);
				PlayerData pData = PlayerData.getPlayerData(p);
			
				if (pData.getData("team") != null) { // If the player isn't in a team, the inventory is static (for now)
					
					if (e.getSlot() == 3) {
						p.openInventory(new TeamLeaveConfirmInventory((Team) pData.getData("team")).getInventory());
					}
				
				} else {
					int slot = e.getSlot();
					switch(slot) {
					
					case 3: 	List<UUID> list = new ArrayList<>();
								list.add(p.getUniqueId());
								Team team = new Team(list);
								pData.setData("team", team);
								p.closeInventory();
								p.sendMessage("§dYou have created a new team!");
								team.renameBegin(pData);
								return;
					default: return;
					}
				}
				
				
			} else if (inv.getHolder() != null && inv.getHolder() instanceof TeamLeaveConfirmInventory) {
				
				e.setCancelled(true);
				
				switch(e.getSlot()) {
				
				case 2:
					
					PlayerData playerData = PlayerData.getPlayerData(p);
					
					
					String name = ((Team) playerData.getData("team")).getName();
					
					if (((Team) playerData.getData("team")).removePlayer(p)) {
						p.sendMessage("§dYou have left §a§l" + name + "§r§d.");
					} else {
						p.sendMessage("§cYou can't leave §a§l" + name + "§r§c!");
						p.sendMessage("§cCheck if you have any Pending Requests.");
					}
					
					p.closeInventory();
					
				case 6:
					p.closeInventory();
				}
			}
		}
	}	
	
	public void onslideEvent(InventoryDragEvent e) {
		InventoryHolder invHolder = e.getInventory().getHolder();
		if (invHolder instanceof TeamInventory || invHolder instanceof TeamLeaveConfirmInventory) {
			e.setCancelled(true);
			return;
		}
	}
}

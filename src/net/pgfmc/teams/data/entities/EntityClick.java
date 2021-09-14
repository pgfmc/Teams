package net.pgfmc.teams.data.entities;

/*
@Deprecated
public class EntityClick implements Listener {
	
	@EventHandler
	public void EntityInteract(PlayerInteractAtEntityEvent e) {
		
		
		if (e.getPlayer() != null && e.getRightClicked() != null && EssentialsMain.isSurvivalWorld(e.getRightClicked().getWorld()) && e.getPlayer().getGameMode() == GameMode.SURVIVAL) {
			
			Player player = e.getPlayer();
			
			
		
			if ((e.getRightClicked().getType() == EntityType.MINECART_CHEST || 
					e.getRightClicked().getType() == EntityType.MINECART_HOPPER ||
					e.getRightClicked().getType() == EntityType.ITEM_FRAME ||
					e.getRightClicked().getType() == EntityType.GLOW_ITEM_FRAME ||
					e.getRightClicked().getType() == EntityType.ARMOR_STAND ||
					e.getRightClicked().getType() == EntityType.HORSE ||
					e.getRightClicked().getType() == EntityType.DONKEY ||
					e.getRightClicked().getType() == EntityType.MULE || 
					e.getRightClicked().getCustomName() != null
				
					// if the entity has an inventory, or if it has a name.
				
				)) {
				
				EntityContainer cont = EntityContainer.getContainer(e.getRightClicked());
				
				if (cont == null) { return; }
				
				Security sec = cont.isAllowed(player);
				
				switch(sec) {
				
				case OWNER: {
					if (player.getInventory().getItemInMainHand() != null && player.getInventory().getItemInMainHand().getType() == Material.TRIPWIRE_HOOK) {
						
						if (cont.getLock()) {
							
							e.setCancelled(true);
							
							player.sendMessage("§6Unlocked!");
							player.playSound(e.getPlayer().getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
							cont.setLock(false);
							return;
							
						} else {
							
							e.setCancelled(true);
							
							player.sendMessage("§6Locked!");
							player.playSound(e.getPlayer().getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
							cont.setLock(true);
							return;
						}
						
					} else {
						return;
					}
				}
				case TEAMMATE: {
					if (player.getInventory().getItemInMainHand() != null && player.getInventory().getItemInMainHand().getType() == Material.TRIPWIRE_HOOK) {
						
						if (cont.getLock()) {
							
							e.setCancelled(true);
							
							player.sendMessage("§6Unlocked!");
							player.playSound(e.getPlayer().getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
							cont.setLock(false);
							return;
							
						} else {
							
							e.setCancelled(true);
							
							player.sendMessage("§6Locked!");
							player.playSound(e.getPlayer().getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
							cont.setLock(true);
							return;
						}
						
					} else {
						return;
					}
				}
				case UNLOCKED: return;
				
				case DISALLOWED: {
					if (cont.getLock()) {
						e.setCancelled(true);
						
						EntityType mat = e.getRightClicked().getType();
						
						switch(mat) {
						
							case MINECART_CHEST: player.sendMessage("§cThis Minecart Chest is locked!"); return;
							case MINECART_HOPPER: player.sendMessage("§cThis Minecart Hopper is locked!"); return;
							case ITEM_FRAME: player.sendMessage("§cThis Item Frame is locked!"); return;
							case GLOW_ITEM_FRAME: player.sendMessage("§cThis Item Frame is locked!"); return;
							case ARMOR_STAND: player.sendMessage("§cThis Armor Stand is locked!"); return;
							case HORSE: player.sendMessage("§cThis Horse is locked!"); return;
							case DONKEY: player.sendMessage("§cThis Donkey is locked!"); return;
							case MULE: player.sendMessage("§cThis Mule is locked!"); return;
							
							default: /*	String name = mat.name();

										
				
										name = name.toLowerCase();
										name = name.replace("_", " ");
										String[] list = name.split(" ");
							
										name = "";
										for (String string : list) {
								
											char[] charArray = string.toCharArray();
											charArray[0] = Character.toUpperCase(charArray[0]);
											name = name + new String(charArray) + " ";
										}
										name = name.stripTrailing();
										player.sendMessage("§cThis " + name + " is locked!"); */ /*
										return;
						}
					} else {
						return;
					}
				}
				case EXCEPTION: System.out.println("cont.isAllowed() returned Security.EXCEPTION!");
				}
				
				
			}
		}
	}
}*/
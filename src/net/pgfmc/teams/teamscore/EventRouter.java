package net.pgfmc.teams.teamscore;

import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.pgfmc.teams.friends.FriendAttack;

/**
 * class to route events to different locations to organise better
 * @author CrimsonDart
 *
 */
public class EventRouter implements Listener {
	
	public void attackEvent(EntityDamageByEntityEvent e) {
		FriendAttack.playerAttackEvent(e);
	}
}

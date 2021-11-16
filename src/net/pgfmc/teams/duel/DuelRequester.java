package net.pgfmc.teams.duel;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;
import net.pgfmc.pgfessentials.request.Requester;
import net.pgfmc.teams.teamscore.Utility;

public class DuelRequester extends Requester {

	public DuelRequester() {
		super("Duel", 30, (x, y) -> {
			
			new Duel(x, y);
			
			
			
			
			
			return true;
		});
	}
	
	public void duelRequestPreProcess(EntityDamageByEntityEvent e) {
		
		if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) { // gets all players in the situation
			Player target = (Player) e.getEntity();
			Player attacker = (Player) e.getDamager();
			
			// if in a battle already -- V
			
			if (target.getGameMode() == GameMode.SURVIVAL && attacker.getGameMode() == GameMode.SURVIVAL &&
					Utility.isSurvival(target.getWorld())) { // makes sure both players are in survival
				
				Duel ATK = PlayerData.getData(attacker, "duel");
				Duel DEF = PlayerData.getData(target, "duel");
				
				boolean equal = (ATK == DEF);
				boolean ATKnull = (ATK == null);
				boolean DEFnull = (ATK == null);
				
				if (equal && !ATKnull) {
					
					
					
					
					
					
					
				}
				
				
			}
		}
		
		
		
		
		
	}
}
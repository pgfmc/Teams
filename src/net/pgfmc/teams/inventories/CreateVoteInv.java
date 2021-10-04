package net.pgfmc.teams.inventories;

import org.bukkit.Material;

import net.pgfmc.pgfessentials.inventoryAPI.InteractableInventory;
import net.pgfmc.teams.teamscore.Team;

public class CreateVoteInv extends InteractableInventory {
	
	public CreateVoteInv(Team team) {
		super(9, "Create Vote");
		
		createButton(Material.ARROW, 0, (x) -> {
					x.openInventory(new VoteBaseInv(x, team).getInventory());
				}, "Back", null);
				
		createButton(Material.BARRIER, 2, null, "Kick Player", "Start a vote\nto kick a player\nfrom your team!");
		
		createButton(Material.PAPER, 4, null, "Change Team Name", "Vote to change the\nteam's name to one o\nyour choosing!");
		
		createButton(Material.TOTEM_OF_UNDYING, 6, null, "Create Team Leader", "insert warning here");
	}
}

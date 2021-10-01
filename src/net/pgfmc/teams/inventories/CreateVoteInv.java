package net.pgfmc.teams.inventories;

import org.bukkit.Material;

import net.pgfmc.pgfessentials.inventoryAPI.Button;
import net.pgfmc.pgfessentials.inventoryAPI.InteractableInventory;
import net.pgfmc.teams.teamscore.Team;

public class CreateVoteInv extends InteractableInventory {

	
	Team team;
	
	
	
	public CreateVoteInv(Team team) {
		super(9, "Create Vote");
		
		this.team = team;
		
		buttonConstructor();
	}

	@Override
	protected Button makeButton(int pos) {
		
		switch(pos) {
		
		case 0: {
			return new Button(Material.ARROW, pos, (x) -> {
				x.openInventory(new VoteBaseInv(x, team).getInventory());
			}, "Back", null);
		}
		
		case 2: {
			return new Button(Material.BARRIER, pos, null, "Kick Player", "Start a vote\nto kick a player\nfrom your team!");
		}
		
		case 4: {
			return new Button(Material.PAPER, pos, null, "Change Team Name", "Vote to change the\nteam's name to one o\nyour choosing!");
		}
		
		case 6: return new Button(Material.TOTEM_OF_UNDYING, pos, null, "Create Team Leader", "Vote to make someone into your team's leader! WARNING Making someone the leader of the team gives them free reign to kick anyone from the team, or change the team name, and will take voting privelages away from everyone. Only the team leader can return the privelages.");
		
		}
		
		return null;
	}

}

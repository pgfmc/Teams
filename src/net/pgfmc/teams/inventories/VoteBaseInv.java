package net.pgfmc.teams.inventories;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import net.pgfmc.pgfessentials.inventoryAPI.Button;
import net.pgfmc.pgfessentials.inventoryAPI.InteractableInventory;
import net.pgfmc.teams.teamscore.Team;
import net.pgfmc.teams.voting.Vote;
import net.pgfmc.teams.voting.Vote.Answer;

public class VoteBaseInv extends InteractableInventory {
	
	
	Team team;
	Vote<?> vote;
	
	public VoteBaseInv(Player player, Team team) {
		super(9, "Vote!");
		
		this.team = team;
		vote = team.getVote();
		
		buttonConstructor();
	}

	@Override
	protected Button makeButton(int pos) {
		
		if (vote == null) {
			
			switch(pos) {
			
			case 0: {
				return new Button(Material.ARROW, pos, (x) -> {
					x.openInventory(new TeamInventory(x).getInventory());
				}, "Back", null);
			}
			
			case 2: {
				
				return new Button(Material.MINECART, pos, (x) -> {
					// opens to a new Inventory where the player can choose what to make a new vote on.
				}, "Create new Vote", "Click here to choose\nwhat to vote on!");
				
			}
			
			default: return new Button(pos);
			}
		} else {
			
			switch(pos) {
			
			case 2: {
				return new Button(Material.RED_CONCRETE, pos, (x) -> {
					
					vote.setVote(x, Answer.AGAINST);
					x.closeInventory();
					x.sendMessage("You have voted against!");
					
				}, "VOTE AGAINST", null);
			}
			
			case 4: {
				return new Button(Material.GRAY_CONCRETE, pos, (x) -> {
					
					vote.setVote(x, Answer.INDIFFERENT);
					x.closeInventory();
					x.sendMessage("You have voted Indifferent!");
					
				}, "VOTE INDIFFERENT", null);
			}
			
			case 6: {
				return new Button(Material.GREEN_CONCRETE, pos, (x) -> {
					
					vote.setVote(x, Answer.SUPPORT);
					x.closeInventory();
					x.sendMessage("You have voted Support!");
					
				}, "VOTE SUPPORT", null);
			}
			
			default: return new Button(pos);
			}
		}
	}

}

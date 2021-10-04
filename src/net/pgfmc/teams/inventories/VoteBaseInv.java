package net.pgfmc.teams.inventories;

import org.bukkit.Material;
import org.bukkit.entity.Player;

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
		
		if (vote == null) {
			
			// --------------------------------------------------- here
			
			createButton(Material.ARROW, 0, (x) -> {
					x.openInventory(new TeamInventory(x).getInventory());
				}, "Back", null);
			
			createButton(Material.MINECART, 2, (x) -> {
					// opens to a new Inventory where the player can choose what to make a new vote on.
				}, "Create new Vote", "Click here to choose\nwhat to vote on!");
			
			// --------------------------------------------------- to here-
			
		} else {
			
			createButton(Material.RED_CONCRETE, 2, (x) -> {
					
					vote.setVote(x, Answer.AGAINST);
					x.closeInventory();
					x.sendMessage("You have voted against!");
					
				}, "VOTE AGAINST", null);
			
			createButton(Material.GRAY_CONCRETE, 4, (x) -> {
					
					vote.setVote(x, Answer.INDIFFERENT);
					x.closeInventory();
					x.sendMessage("You have voted Indifferent!");
					
				}, "VOTE INDIFFERENT", null);
			
			createButton(Material.GREEN_CONCRETE, 6, (x) -> {
					
					vote.setVote(x, Answer.SUPPORT);
					x.closeInventory();
					x.sendMessage("You have voted Support!");
					
				}, "VOTE SUPPORT", null);
		}
	}
}

package net.pgfmc.teams.inventories;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import net.pgfmc.teams.Main;
import net.pgfmc.teams.TeamObj;
import net.pgfmc.voting.Vote;

public class VoteInventory implements InventoryHolder {
	
	private Inventory inv;
	Vote vote;
	
	public VoteInventory(Vote vote) { // ------------------- creates vote inventory.
		inv = Bukkit.createInventory(this, 9, "Vote");
		
		this.vote = vote;
		
		List<String> list = new ArrayList<>();
		list.add("Vote to:");
		
		switch (vote.getCase()) {
		
		case KICKPLAYER: list.add("Kick " + Bukkit.getPlayer((UUID) vote.getSubject()).getCustomName() + " from your team.");
		case BANPLAYER: list.add("Ban " + Bukkit.getPlayer((UUID) vote.getSubject()).getCustomName() + " from your team.");
		case ALLYTEAMACCEPT: list.add("Accept Ally request from team " + TeamObj.findID((UUID) vote.getSubject()).getName() + ".");
		case ALLYTEAMREQUEST: list.add("Send Ally request to team " + TeamObj.findID((UUID) vote.getSubject()).getName() + ".");
		case ALLYTEAMCANCEL: list.add("Disband Allyship with team " + TeamObj.findID((UUID) vote.getSubject()).getName() + ".");
		case RENAMETEAM: list.add("Rename team to " + (String) vote.getSubject() + ".");
		case CHANGEGOVERNMENT: list.add("Make the leader of the team " + Bukkit.getPlayer((UUID)  vote.getSubject()).getCustomName() + ".");
		}
		
		inv.setItem(0, Main.createItem("VOTE HERE!", Material.OAK_SIGN, list));
		
		inv.setItem(5, Main.createItem("In support of", Material.GREEN_CONCRETE));
		inv.setItem(6, Main.createItem("Indifferent To", Material.GRAY_CONCRETE));
		inv.setItem(7, Main.createItem("Against", Material.RED_CONCRETE));
	}
	
	@Override
	public Inventory getInventory() {
		
		return null;
	}
	
	public Vote getVote() {
		return vote;
	}
}

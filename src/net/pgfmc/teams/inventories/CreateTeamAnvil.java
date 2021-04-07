package net.pgfmc.teams.inventories;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CreateTeamAnvil implements InventoryHolder {
	private Inventory inv;
	
	public CreateTeamAnvil()
	{
		inv = Bukkit.createInventory(this, InventoryType.ANVIL);
		init(); // Build the inventory
	}
	
	public void init()
	{
		List<String> lore = new ArrayList<>();
		lore.add("Rename this piece of paper");
		lore.add("to the name you want your");
		lore.add("team to have");
		inv.addItem(createItem("Team name here", Material.PAPER, lore));
	}
	
	
	
	public ItemStack createItem(String name, Material mat, List<String> lore)
	{
		ItemStack item = new ItemStack(mat, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	
	
	
	
	@Override
	public Inventory getInventory() { return inv; }

}

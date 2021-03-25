package net.pgfmc.teams.inventories;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TeamBase implements InventoryHolder {
	
	private Inventory inv;
	
	public TeamBase()
	{
		inv = Bukkit.createInventory(this, 9, "Team Management"); // Initiates the declared Inventory object
		init(); // Build the inventory
	}
	
	public void init()
	{
		ItemStack oakSign = createItem("Info", Material.OAK_SIGN); // Edited by bk --------- !
		
		inv.setItem(0, oakSign);
		
		ItemStack AIRgamg = createItem("", Material.AIR);
		
		inv.setItem(2, AIRgamg);
		inv.setItem(3, AIRgamg);
		inv.setItem(5, AIRgamg);
		inv.setItem(7, AIRgamg);
		inv.setItem(8, AIRgamg);
	}
	
	public ItemStack createItem(String name, Material mat)
	{
		ItemStack item = new ItemStack(mat, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		return item;
	}
	
	@Override
	public Inventory getInventory() { return inv; }
}
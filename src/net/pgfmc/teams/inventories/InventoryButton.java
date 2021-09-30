package net.pgfmc.teams.inventories;

import java.util.LinkedList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * class that determines the funcnctionality of a slot in an interactable inventory.
 * allows for changing the item, the name, the lore, and button press functionality.
 * @author CrimsonDart
 *
 */
public class InventoryButton {
	
	// fields
	
	Butto function;
	ItemStack item;
	int position;
	InteractableInventory inv;
	
	public static Butto defaultButto = (x) -> {return;};
	
	// constructor
	
	public InventoryButton(Material mat, int pos, Butto function, String name, String lore, InteractableInventory inv) {
		
		if (function == null) {
			this.function = defaultButto;
		} else {
			this.function = function;
		}
		
		this.item = new ItemStack(mat);
		position = pos;
		this.inv = inv;
		
		ItemMeta imeta = this.item.getItemMeta();
		
		if (name != null) {
			imeta.setDisplayName(name);
		}
		
		
		// sets lore
		if (lore != null) {
			LinkedList<String> lines = new LinkedList<>();
			
			for (String line : lore.split("\n")) {
				lines.add(line);
			}
			imeta.setLore(lines);
		}
		item.setItemMeta(imeta);
	}
	
	public InventoryButton(int position, InteractableInventory inv) {
		this.function = defaultButto;
		this.item = new ItemStack(Material.AIR);
		this.inv = inv;
		this.position = position;
	}
	
	// methods
	
	/**
	 *  Initializes the item into the parent inventory. (inv)
	 */
	public void init() {
		inv.getInventory().setItem(position, item);
	}
	
	/**
	 * runs the lambda expression Butto
	 * @param player the object of the operation.
	 */
	public void run(Player player) {
		function.press(player);
	}
	
	public ItemStack getItem() {
		return item;
	}
	
}

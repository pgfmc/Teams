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
public class Button {
	
	// fields
	
	Butto function;
	ItemStack item;
	int position;
	
	public static Butto defaultButto = (x) -> {return;};
	
	// constructor
	
	/**
	 * 
	 * Creates a new Button for an InteractableInventory. includes support for custom names, lore, and Full button clicking action!
	 * 
	 * @param mat The material of the item.
	 * @param pos The Item's slot number in the InteractableInventory
	 * @param function The lambda function that is ran when the item is clicked. (bk, this is a lambda tutorial for you)
	 * 		where the "Butto function" parameter goes, type:      (x) -> { /* code here /* }      and thats it    also x is the player who clicked the item
	 * 		You can also set function to null to just do nothing.
	 * @param name The name of the item. (set to null to not change the name)
	 * @param lore The lore of the item.  Syntax:    "line1\nline2\nline3..... and so on"
	 * @param inv The Interactable Inventory that this button belongs to.
	 */
	public Button(Material mat, int pos, Butto function, String name, String lore) {
		
		if (function == null) {
			this.function = defaultButto;
		} else {
			this.function = function;
		}
		
		this.item = new ItemStack(mat);
		position = pos;
		
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
	
	public Button(int position) {
		this.function = defaultButto;
		this.item = new ItemStack(Material.AIR);
		this.position = position;
	}
	
	// methods
	
	/**
	 *  Initializes the item into the parent inventory. (inv)
	 */
	
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

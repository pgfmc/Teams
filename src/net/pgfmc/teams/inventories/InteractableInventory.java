package net.pgfmc.teams.inventories;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

/**
 * abstract class that supports all inventory functions, as well as some extra convenience, such as  
 * @author james
 *
 */
public abstract class InteractableInventory implements InventoryHolder {
	
	// fields
	
	protected ArrayList<Button> buttons = new ArrayList<>();
	protected int size;
	protected Inventory inv;
	
	/**
	 * Constructor for Interactable Inventory.
	 * automatically creates an inventory of the size requested.
	 * @param size The size of the inventory needed.
	 */
	public InteractableInventory(int size, String name) {
		this.inv = Bukkit.createInventory(this, size, name);
		this.size = inv.getSize();
		System.out.println("new Interactable Inventory Created!");
		System.out.println(String.valueOf(size));
	}

	// methods
	
	/**
	 * method to initialize all buttons.
	 * 
	 */
	protected void buttonConstructor() {
		System.out.println("buttonConstructor() has been ran!");
		System.out.println(String.valueOf(size));
		
		int shiza = 0;
		
		do {
			System.out.println("new button created for slot " + String.valueOf(shiza));
			buttons.add(shiza, makeButton(shiza));
			
			inv.setItem(shiza, buttons.get(shiza).getItem());
			shiza++;
		} while (shiza != size);
	}
	
	protected abstract Button makeButton(int pos);
	
	public void press(int slot, Player p) {
		buttons.get(slot).run(p);
	}
	
	@Override
	public Inventory getInventory() {
		return inv;
	}
}

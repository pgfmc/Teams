package net.pgfmc.teams.teamscore;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.pgfmc.pgfessentials.EssentialsMain;

public class Utility {
	
	public static ItemStack createItem(String name, Material mat, List<String> lore)
	{
		ItemStack item = new ItemStack(mat, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public static String makePossesive(String name) {
		if (name.endsWith("s")) {
			return (name + "'");
		} else {
			return (name + "'s");
		}
	}
	
	public static ItemStack createItem(String name, Material mat) // function for creating an item with a custom name
	{
		ItemStack item = new ItemStack(mat, 1);
		ItemMeta meta = item.getItemMeta();
		
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		return item;
	}
	
	public static Location StringtoLoc(String string) { // converts a specific string to a location object
		
		char[] chars = string.toCharArray();
		
		int stage = 0;
		String w = null;
		String x = "";
		String y = "";
		String z = "";
		
		LinkedList<Character> Chars = new LinkedList<>();
		
		for (char character : chars) {
			Chars.add(character);
		}
		
		w = Chars.get(1).toString();
		Chars.remove(0); // w
		Chars.remove(0); // int
		Chars.remove(0); // x
		
		stage = 1;
		
		for (char c : Chars) {
			if (stage == 1) {
				if (c != "y".charAt(0)) {
					x = x + c;
				} else {
					stage = 2;
				}
				
			} else if (stage == 2) {
				
				if (c != "z".charAt(0)) {
					y = y + c;
				} else {
					stage = 3;
				}
				
				
			} else if (stage == 3) {
				
				z = z + c;
			}
		}
		
		return new Location(EssentialsMain.intToWorld(Integer.valueOf(w)), Integer.valueOf(x), Integer.valueOf(y), Integer.valueOf(z));
	}
	
	public static String locToString(Location location) { // converts location to string.
		
		return "w" + String.valueOf(EssentialsMain.worldToInt(location.getWorld())) + "x" + String.valueOf(location.getBlockX()) + "y" + String.valueOf(location.getBlockY()) + "z" + String.valueOf(location.getBlockZ());
		
	}
}
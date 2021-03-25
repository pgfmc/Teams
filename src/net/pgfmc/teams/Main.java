package net.pgfmc.teams;

import org.bukkit.plugin.java.JavaPlugin;

import net.pgfmc.teams.commands.Team;

public class Main extends JavaPlugin { // MAIN // initializes plugin
	
	public static Main plugin;
	
	// File file = new File(getDataFolder() + File.separator + "database.yml"); // Creates a File object
	// FileConfiguration database = YamlConfiguration.loadConfiguration(file); // Turns the File object into YAML and loads data
	
	@Override
	public void onEnable()
	{
		plugin = this;
		getCommand("team").setExecutor(new Team());
		
	}
	


}

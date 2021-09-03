package net.pgfmc.teams.teamscore.debug;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/*
Written by CrimsonDart

-----------------------------------

Command that toggles debug mode

-----------------------------------
 */

public class DebugCommand implements CommandExecutor {
	
	public static boolean debug = false;
	
	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		
		if (debug) {
			debug = false;
			System.out.println("Debug mode has been disabled!");
		} else {
			debug = true;
			System.out.println("Debug mode has been enabled!");
		}
		
		return true;
	}
}
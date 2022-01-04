package net.pgfmc.teams.ownable.inspector;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.teams.ownable.Ownable;
import net.pgfmc.teams.ownable.Ownable.Lock;
import net.pgfmc.teams.ownable.block.OwnableBlock;

/**
 * Command that allows data for Ownables to be changed.
 * can set the lock or owner of the ownable.
 * 
 * @author CrimsonDart
 * @version 4.0.2
 * @since 4.0.2
 *
 */
@Deprecated
public class EditOwnableCommand implements CommandExecutor {
	
	/**
	 * @version 4.0.2
	 * 
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player)) return true;
		if (((Player) sender).getGameMode() != GameMode.CREATIVE) return true;
		PlayerData pd = PlayerData.getPlayerData((Player) sender);
		
		Ownable cache = pd.getData("OwnableCache");
		if (cache == null) {
			sender.sendMessage("§cNo Ownable Selected!");
			return true;
		}
		
		if (cache instanceof OwnableBlock) {
			
			if (args[0] == "lock") {
				
				Lock lock = Lock.valueOf(args[1]);
				if (lock != null) {
					cache.setLock(lock);
					sender.sendMessage("§aLock set to " + lock.toString());
				} else {
					sender.sendMessage("§cLock set Failed!");
				}
				
			} else if (args[0] == "owner") {
				
				OfflinePlayer op = Bukkit.getPlayer(args[1]);
				if (op != null) {
					
				}
			}
		}
		return true;
	}
	
	
}

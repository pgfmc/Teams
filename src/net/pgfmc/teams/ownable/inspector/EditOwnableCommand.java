package net.pgfmc.teams.ownable.inspector;

import org.bukkit.GameMode;
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
		
		if (args == null || args.length == 0) {
			sender.sendMessage("§dAllowed types: §b'§alock§b'§d, §b'§aowner§b'");
		}
		
		
		
		String s = label;
		
		
		
		for (String sr : args) {
			s = s + " " + sr;
		}
		sender.sendMessage(s);
		
		
		
		
		if (cache instanceof OwnableBlock) {
			
			if ("lock".equals(args[0])) {
				
				if (args[1] != null) {
					Lock lock = Lock.valueOf(args[1]);
					
					if (lock != null) {
						cache.setLock(lock);
						sender.sendMessage("§aLock set to " + lock.toString());
						return true;
					}
				} 
				sender.sendMessage("§cLock set Failed!");
				
			} else if ("owner".equals(args[0])) {
				
				PlayerData ope = PlayerData.getPlayerData(args[1]);
				if (ope != null) {
					cache.setOwner(pd);
					sender.sendMessage("§aOwner set to " + ope.getRankedName());
				} else {
					sender.sendMessage("§cOwner set Failed!");
				}
			} else {
				sender.sendMessage("§dAllowed types: §b'§alock§b'§d, §b'§aowner§b'");
			}
		}
		return true;
	}
	
	
}

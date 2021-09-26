package net.pgfmc.teams.data.containers;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Player;

import net.pgfmc.pgfessentials.EssentialsMain;
import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;
import net.pgfmc.teams.teamscore.Team;
import net.pgfmc.teams.teamscore.Utility;

/*

Written by CrimsonDart

-----------------------------------
Stores data for container blocks
subclasses: 

Beacons.java

-----------------------------------


 */


public class BlockContainer extends Containers {
	
	
	public static LinkedHashMap<Block, BlockContainer> containers = new LinkedHashMap<>();
	
	Block block;
	Team team;
	
	public BlockContainer(OfflinePlayer player, Lock lock, Block block, Team team) { // Constructor
		
		super(player, lock);
		
		this.team = team;
		this.block = block;
		
		
		if (block.getType() != Material.BEACON) {
			containers.put(block, this);
		}
	}
	
	public static boolean createBlockContainer(OfflinePlayer player, Lock lock, Block block, Team team) { // a router between Beacons and BlockContainer
		
		/**
		
		hold out for me, this is kinda complex (maybe)
		tldr; it just returns wether or not a chest can be placed in a certain spot; below are the criteria in english.
		
		first tests the following on the input block (variable "chest":
			if the block is a chest or trapped chest
			if the block is in a survival world
		
		next,  it tests all horizontally adjacent blocks for the same criteria, plus:
			if the block isn't air
			matches the type of the placed block
			if they are facing the same direction
			
		for the block that passes this phase (only one possible, unless a glitch happens)
		there are different things that are done depending on the output of cont.isAllowed(player)...
		
		--|| ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ ||--
		
		switch (black.isAllowed(player)) {
		
		if the placer of both chests are the same, a new container is created, but uses the team from the first chest.
		
		if a teammate places the second chest, the new container inherits the player from the first chest;
			so it would be like the placer of the first chest placed the second one.
			
		in all other cases, the event that placed the chest is cancelled.
		}
		
		if the first chest doesn't have a corresponding container, a new one will be created for it,
			forwarding all of the aspects of the second chest to the first.
		
		--|| ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ ||--
		
		if none of the code above is run, then this function simply differentiates between beacons and normal containers,
		and creates the respective object for each.
		
		
		the setLocked function in this class also implements some of the below code.
		 */
		
		if ((block.getType() == Material.CHEST || block.getType() == Material.TRAPPED_CHEST) && EssentialsMain.isSurvivalWorld(block.getWorld())) {
			
			Set<Block> blocks = new HashSet<Block>();
			World world = block.getLocation().getWorld();
			
			blocks.add(world.getBlockAt(block.getLocation().add(1, 0, 0)));
			blocks.add(world.getBlockAt(block.getLocation().add(-1, 0, 0)));
			blocks.add(world.getBlockAt(block.getLocation().add(0, 0, 1)));
			blocks.add(world.getBlockAt(block.getLocation().add(0, 0, -1)));
			
			for (Block black : blocks) {
				if (black != null && 
						(black.getType() == Material.CHEST || black.getType() == Material.TRAPPED_CHEST) && 
						black.getType() == block.getType() && 
						((Directional) black.getBlockData()).getFacing() == ((Directional) block.getBlockData()).getFacing()) {
					
					BlockContainer cont = BlockContainer.getContainer(black);
					
					if (cont != null) {
						
						switch (cont.isAllowed(player)) {
						
						case OWNER:
							new BlockContainer(player, lock, block, cont.getTeam());
							return true;
						case TEAMMATE:
							new BlockContainer(cont.getPlayer(), lock, block, team);
							return true;
						default:
							return false;
							
						
						}
					} else {
						new BlockContainer(player, lock, black, team);
					}
				}
			}
		}
		
		
		if (block.getType() == Material.BEACON) {
			new Beacons(player, block, lock, team);
			return true;
			
		} else {
			new BlockContainer(player, lock, block, team);
			return true;
			
		}
	}
	
	public Team getTeam() {
		return team;
	}
	
	public void setTeam(Team team) {
		this.team = team;
	}
	
	@Override
	public void setLock(Lock lock) {
		
		if ((block.getType() == Material.CHEST || block.getType() == Material.TRAPPED_CHEST) && EssentialsMain.isSurvivalWorld(block.getWorld())) {
			
			Set<Block> blocks = new HashSet<Block>();
			World world = block.getLocation().getWorld();
			
			blocks.add(world.getBlockAt(block.getLocation().add(1, 0, 0)));
			blocks.add(world.getBlockAt(block.getLocation().add(-1, 0, 0)));
			blocks.add(world.getBlockAt(block.getLocation().add(0, 0, 1)));
			blocks.add(world.getBlockAt(block.getLocation().add(0, 0, -1)));
			
			for (Block black : blocks) {
				if (black != null && 
						(black.getType() == Material.CHEST || black.getType() == Material.TRAPPED_CHEST) && 
						black.getType() == block.getType() && 
						((Directional) black.getBlockData()).getFacing() == ((Directional) block.getBlockData()).getFacing()) {
					
					BlockContainer cont = BlockContainer.getContainer(black);
					
					if (cont != null) {
						
						switch (cont.isAllowed(placer)) {
						
						case OWNER:
							
							this.lock = lock;
						case TEAMMATE:
							this.lock = lock;
							
						default:
							System.out.println("how does this even happen !?!?!??!??!?!?");
							return;
							
						}
					} else {
						new BlockContainer(placer, lock, black, team);
					}
				}
			}
		}
		super.setLock(lock);
	}
	
	public static void remove(Block block) {
		containers.remove(block);
	}
	
	@Override
	Location getLocation() {
		return block.getLocation();
		
	}
	
	public static BlockContainer getContainer(Block block) { // gets a container from block
		return containers.get(block);
	}
	
	public boolean isBeacon() { // returns wether or not a Containers is a Beacons.
		if (this instanceof Beacons) {
			return true;
		}
		return false;
		
	}
	
	public Beacons toBeacon() { // converts container to a beacon
		if (this.isBeacon()) {
			return (Beacons) this;
		}
		return null;
	}
	
	public static void outputData() { // outputs stored container data.
		
		System.out.println("Container Data:");
		
		for (Block block : containers.keySet()) {
			
			if (containers.get(block).getTeam() != null) {
				System.out.println("	" + Utility.locToString(block.getLocation()) + 
						":\n      type: " + block.getType().toString() + 
						" \n      player: " + containers.get(block).placer.getName() + 
						" \n      team:" + containers.get(block).team.getName() + 
						" \n      locked? :" + containers.get(block).lock);
			} else {
				System.out.println("	" + Utility.locToString(block.getLocation()) + 
						":\n      type: " + block.getType().toString() + 
						" \n      player: " + containers.get(block).placer.getName() + 
						" \n      locked? :" + containers.get(block).lock);
			}
		}
	}
	
	public static void updateTeams() {
		
		for (Block block : containers.keySet()) {
			BlockContainer cont = containers.get(block);
			
			if (cont.getLock() == Lock.LOCKED) {
				cont.setTeam(((Team) PlayerData.getData(cont.getPlayer(), "team")));
				continue;
			} else if (!Team.getTeams().containsValue(cont.getTeam())) {
				
				cont.setTeam(((Team) PlayerData.getData(cont.getPlayer(), "team")));
			}
		}
	}
	
	@Override
	public Security isAllowed(OfflinePlayer player) {
		
		boolean same;
		if (player instanceof Player) {
			same = (placer.getPlayer() != null && player == placer.getPlayer());
		} else {
			same = (placer == player);
		}
		
		Team stranger = Team.getTeam(player);
		
		if (team == null && lock == Lock.LOCKED && same) {
			BlockContainer.updateTeams();
			System.out.println("out 0");
			return Security.OWNER;
		}
		
		switch(lock) {
		case LOCKED: // ------------------------ nobody but the player can access. Also, the container's team is tied to the player. | WIP
			
			System.out.println(placer);
			System.out.println(player);
			System.out.println(placer.getPlayer());
			
			if (same) {
				System.out.println("out 1");
				
				return Security.OWNER;
			}
			System.out.println("out 2");
			return Security.DISALLOWED;
			
		case TEAM_ONLY: // --------------------- only Teammates can access.
			
			if (team != null && team == stranger) {
				
				if (same) {
					System.out.println("out 3");
					return Security.OWNER;
				}
				System.out.println("out 4");
				return Security.TEAMMATE; 
			} else 
			if (team == null && same) {
				System.out.println("out 5");
				return Security.OWNER;
			}
			System.out.println("out 6");
			return Security.DISALLOWED;
		case UNLOCKED: // --------------------- anybody can access.
			if (team != null && team == stranger) {
				
				if (same) {
					System.out.println("out 7");
					return Security.OWNER;
				}
				System.out.println("out 8");
				return Security.TEAMMATE; 
			} else 
			if (team == null && same) {
				System.out.println("out 9");
				return Security.OWNER;
			}
			System.out.println("out 10");
			return Security.UNLOCKED;
		default:
			return Security.EXCEPTION;
		}
	}
}
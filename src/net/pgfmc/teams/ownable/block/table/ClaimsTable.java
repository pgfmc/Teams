package net.pgfmc.teams.ownable.block.table;

import com.sk89q.worldguard.util.collect.LongHash;
import com.sk89q.worldguard.util.collect.LongHashTable;

import net.pgfmc.pgfessentials.Vector4;
import net.pgfmc.teams.ownable.block.OwnableBlock;

public class ClaimsTable {
	
	private static LongHashTable<ClaimSection> Overworldtable = new LongHashTable<>();
	private static LongHashTable<ClaimSection> Nethertable = new LongHashTable<>();
	private static LongHashTable<ClaimSection> Endtable = new LongHashTable<>();
	
	
	private static LongHashTable<ClaimSection> getWorldTable(int w) {
		switch(w) {
		
		case 0: return Overworldtable;
		case 1: return Nethertable;
		case 2: return Endtable;
		
		default: return null;
		}
	}
	
	/**
	 * Gets the section covered by this location.
	 * @param v
	 * @return
	 */
	private static ClaimSection getSection(Vector4 v) {
		return getWorldTable(v.w()).get(getSectionKey(v));
	}
	
	protected static ClaimSection getSection(int x, int z, int w) {
		return getWorldTable(w).get(x, z);
	}
	
	/**
	 * Puts this claim into the LongMap.
	 * @param ob The Claim.
	 */
	public static void put(OwnableBlock ob) {
		ClaimSection cs = getSection(ob.getLocation());
		if (cs != null) {
			cs.put(ob);
			
		} else {
			cs = new ClaimSection(getSectionKey(ob.getLocation()), ob.getLocation().w());
			cs.put(ob);
			getWorldTable(cs.w).put(cs.key, cs);
		}
	}
	
	/**
	 * Removes this claim.
	 * @param ob The claim.
	 */
	public static void remove(OwnableBlock ob) {
		ClaimSection cs = getSection(ob.getLocation());
		if (cs != null) {
			cs	.put(ob);
			
		} else {
			cs = new ClaimSection(getSectionKey(ob.getLocation()), ob.getLocation().w());
			cs.put(ob);
			getWorldTable(cs.w).put(cs.key, cs);
		}
	}
	
	/**
	 * Gets the claim that covers this location.
	 * @param v The location
	 * @return The claim that contains this location. Returns "null" if there is no claim.
	 */
	public static OwnableBlock getRelevantClaim(Vector4 v) {
		ClaimSection cs = getSection(v);
		if (cs != null) {
			OwnableBlock ob = cs.getRelevantClaim(v);
			if (ob != null) {
				return ob;
			}
		}
		return null;
	}
	
	/**
	 * Gets the Claim at this location.
	 * @param v The location.
	 * @return The claim at this location; Returns "null" if there is no claim.
	 */
	public static OwnableBlock getOwnable(Vector4 v) {
		ClaimSection cs = getSection(v);
		if (cs != null) {
			return cs.getOwnable(v);
		}
		return null;
	}
	
	/**
	 * gets the claimSection key (a long) based on the location.
	 * @param v
	 * @return
	 */
	private static long getSectionKey(Vector4 v) {
		return LongHash.toLong(v.x()/128, v.z()/128);
	}
}

package net.pgfmc.teams.ownable.block.table;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.sk89q.worldguard.util.collect.LongHash;

import net.pgfmc.pgfessentials.Vector4;
import net.pgfmc.teams.ownable.block.OwnableBlock;

/**
 * Stores claims.
 * @author CrimsonDart
 *
 */
public class ClaimSection {
	
	protected long key;
	protected int w;
	
	private Map<Neighbor, ClaimSection> neighbors = new EnumMap<Neighbor, ClaimSection>(Neighbor.class);
	private Set<OwnableBlock> claims = new HashSet<>();
	
	public ClaimSection(long key, int w) {
		this.key = key;
	}
	
	public OwnableBlock getRelevantClaim(Vector4 v) {
		if (claims.size() == 0) return null;
		
		for (OwnableBlock c : claims) {
			Vector4 v1 = c.getLocation();
			
			if (v1.x() - 36 < v.x() &&
					v1.x() + 36 > v.x() &&
					v1.z() - 36 < v.z() &&
					v1.z() + 36 > v.z() &&
					v1.y() - 54 < v.y()) {
				return c;
			}
		}
		return null;
	}
	
	public OwnableBlock getClosestClaim(Vector4 v) {
		
		OwnableBlock ob = getRelevantClaim(v);
		if (ob != null) {
			return ob;
		}
		
		int xBound = v.x()%128;
		int zBound = v.z()%128;
		
		if (xBound < 35) {
			
			ob = getNeighbor(Neighbor.LEFT).getRelevantClaim(v);
			if (ob != null) {
				return ob;
			}
			
			if (zBound < 35) {
				
				ob = getNeighbor(Neighbor.DOWN).getRelevantClaim(v);
				if (ob != null) {
					return ob;
				} else {
					ob = getNeighbor(Neighbor.DOWNLEFT).getRelevantClaim(v);
					if (ob != null) {
						return ob;
					}
				}
				return null;
			} else if (zBound > 93) {
				ob = getNeighbor(Neighbor.UP).getRelevantClaim(v);
				if (ob != null) {
					return ob;
				} else {
					ob = getNeighbor(Neighbor.UPLEFT).getRelevantClaim(v);
					if (ob != null) {
						return ob;
					}
				}
			}
			return null;
		} else if (xBound > 93) {
			
			ob = getNeighbor(Neighbor.RIGHT).getRelevantClaim(v);
			if (ob != null) {
				return ob;
			}
			
			if (zBound < 35) {
				
				ob = getNeighbor(Neighbor.DOWN).getRelevantClaim(v);
				if (ob != null) {
					return ob;
				} else {
					ob = getNeighbor(Neighbor.DOWNRIGHT).getRelevantClaim(v);
					if (ob != null) {
						return ob;
					}
				}
			} else if (zBound > 93) {
				ob = getNeighbor(Neighbor.UP).getRelevantClaim(v);
				if (ob != null) {
					return ob;
				} else {
					ob = getNeighbor(Neighbor.UPRIGHT).getRelevantClaim(v);
					if (ob != null) {
						return ob;
					}
				}
			}
		} else if (zBound < 35) { // move left
			
			ob = getNeighbor(Neighbor.DOWN).getRelevantClaim(v);
			if (ob != null) {
				return ob;
			}
		} else if (zBound > 93) {
			ob = getNeighbor(Neighbor.UP).getRelevantClaim(v);
			if (ob != null) {
				return ob;
			}
		}
		return null;
	}
	
	public boolean isOverlappingRange(Vector4 v) {
		for (OwnableBlock c : claims) {
			Vector4 v1 = c.getLocation();
			
			if (v1.x() - 72 < v.x() &&
					v1.x() + 72 > v.x() &&
					v1.z() - 72 < v.z() &&
					v1.z() + 72 > v.z()) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isOverlappingClaim(Vector4 v) {
		
		if (isOverlappingRange(v)) {
			return true;
		}
		
		int xBound = v.x()%128;
		int zBound = v.z()%128;
		
		// Since the Overlapping claim range is so large, 
		//the bounds are used to determine which Sections NOT to search instead of deciding which ones to search.
		
		if (xBound > 72) { // DONT search left side
			
			if (getNeighbor(Neighbor.RIGHT).isOverlappingRange(v)) {
				return true;
			}
			
			if (zBound > 72) { // DONT search down
				
				if (getNeighbor(Neighbor.UP).isOverlappingRange(v)) {
					return true;
				} else {
					ob = getNeighbor(Neighbor.UPRIGHT).getRelevantClaim(v);
					if (ob != null) {
						return true;
					}
				}
				return false;
			} else if (zBound < 53) {
				ob = getNeighbor(Neighbor.UP).getRelevantClaim(v);
				if (ob != null) {
					return true;
				} else {
					ob = getNeighbor(Neighbor.UPLEFT).getRelevantClaim(v);
					if (ob != null) {
						return true;
					}
				}
			}
			return false;
		} else if (xBound > 93) {
			
			OwnableBlock ob = getNeighbor(Neighbor.RIGHT).getRelevantClaim(v);
			if (ob != null) {
				return true;
			}
			
			if (zBound < 35) {
				
				ob = getNeighbor(Neighbor.DOWN).getRelevantClaim(v);
				if (ob != null) {
					return true;
				} else {
					ob = getNeighbor(Neighbor.DOWNRIGHT).getRelevantClaim(v);
					if (ob != null) {
						return true;
					}
				}
			} else if (zBound > 93) {
				ob = getNeighbor(Neighbor.UP).getRelevantClaim(v);
				if (ob != null) {
					return true;
				} else {
					ob = getNeighbor(Neighbor.UPRIGHT).getRelevantClaim(v);
					if (ob != null) {
						return true;
					}
				}
			}
		} else if (zBound < 35) { // move left
			
			OwnableBlock ob = getNeighbor(Neighbor.DOWN).getRelevantClaim(v);
			if (ob != null) {
				return true;
			}
		} else if (zBound > 93) {
			OwnableBlock ob = getNeighbor(Neighbor.UP).getRelevantClaim(v);
			if (ob != null) {
				return true;
			}
		}
		return false;
	}
	
	
	
	public OwnableBlock getOwnable(Vector4 v) {
		if (claims.size() == 0) return null;
		
		for (OwnableBlock c : claims) {
			c.getLocation().equals(v);
		}
		return null;
	}
	
	public void put(OwnableBlock ob) {
		claims.add(ob);
	}
	
	public void remove(OwnableBlock ob) {
		claims.remove(ob);
	}
	
	public ClaimSection getNeighbor(Neighbor n) {
		
		ClaimSection cs = neighbors.get(n);
		if (cs != null) {
			return cs;
		}
		
		cs = ClaimsTable.getSection(LongHash.msw(key) + n.x(), LongHash.lsw(key) + n.z(), w);
		neighbors.put(n, cs);
		return cs;
	}
	
	/**
	 * updates the claim section, with references to neighboring ClaimSections.
	 */
	public void update() {
		for (Neighbor n : Neighbor.values()) {
			neighbors.put(n, ClaimsTable.getSection(LongHash.msw(key) + n.x(), LongHash.lsw(key) + n.z(), w));
			
		}
	}
	
	public void update(Neighbor n) {
		neighbors.put(n, ClaimsTable.getSection(LongHash.msw(key) + n.x(), LongHash.lsw(key) + n.z(), w));
	}
}

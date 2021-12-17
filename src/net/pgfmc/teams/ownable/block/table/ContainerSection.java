package net.pgfmc.teams.ownable.block.table;

import java.util.HashSet;
import java.util.Set;

import net.pgfmc.pgfessentials.Vector4;
import net.pgfmc.teams.ownable.block.OwnableBlock;

public class ContainerSection {
	
	protected long key;
	protected int w;
	
	private Set<OwnableBlock> containers = new HashSet<>();
	
	public ContainerSection(long key, int w) {
		this.key = key;
	}
	
	public OwnableBlock getOwnable(Vector4 v) {
		if (containers.size() == 0) return null;
		
		for (OwnableBlock c : containers) {
			c.getLocation().equals(v);
		}
		return null;
	}
	
	public void put(OwnableBlock ob) {
		containers.add(ob);
	}
	
	public void remove(OwnableBlock ob) {
		containers.remove(ob);
	}
}

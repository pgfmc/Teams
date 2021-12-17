package net.pgfmc.teams.ownable.block;

import net.pgfmc.pgfessentials.Vector4;

public class MultiRegion extends Region {
	
	int ym = -100; // -100 is out of bounds for minecraft, and is used in place of null here.
	int yM = -100;

	public MultiRegion(Vector4 min, Vector4 max, boolean enableLower, boolean enableUpper) {
		super(min, max);
		
		if (enableLower) {
			ym = min.y();
		}
		
		if (enableUpper) {
			yM = max.y();
		}
	}
	
	@Override
	public boolean test(Vector4 t) {
		
		return !((xm < t.x() || xM > t.x()) &&
				  zm < t.z() || zM > t.z() &&
				  ((ym != -100 && ym < t.y()) ||
				 	yM != -100 && yM < t.y()));
	}
}

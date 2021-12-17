package net.pgfmc.teams.ownable.block;

import java.util.function.Predicate;

import net.pgfmc.pgfessentials.Vector4;

public class Region implements Predicate<Vector4> {
	
	int xm;
	int xM;
	int zm;
	int zM;
	
	int w;	
	
	public Region(Vector4 min, Vector4 max) {
		
		w = min.w();
		xm = min.x();
		xM = max.x();
		zm = min.z();
		zM = max.z();
		
	}

	@Override
	public boolean test(Vector4 t) {
		
		return !((xm < t.x() || xM > t.x()) &&
				  zm < t.z() || zM > t.z());
	}
}

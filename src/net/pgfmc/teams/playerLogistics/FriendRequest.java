package net.pgfmc.teams.playerLogistics;

import java.util.List;

import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;
import net.pgfmc.pgfessentials.request.Requester;

public class FriendRequest extends Requester {
	
	public static final FriendRequest DEFAULT = new FriendRequest();
	
	private FriendRequest() {
		super("Friend", 120, (x, y) -> {
			
			List<PlayerData> list = x.getData("friends");
			list.add(y);
			
			list = y.getData("friends");
			list.add(x);
			
			return true;
		});
	}
}
package dev.ghosty.towers.data;

import java.util.HashMap;
import java.util.function.Function;

import dev.ghostcode.gameapi.util.AbstractPlaceholder;
import dev.ghosty.towers.Towers;

public final class TowersPlaceholder extends AbstractPlaceholder<PlayerData> {
	
	private HashMap<String, Function<PlayerData, String>> map;
	
	public TowersPlaceholder() {
		map = new HashMap<>();
		addPlaceholder("%name%", p -> p.getPlayer().getName());
		addPlaceholder("%online%", p -> Towers.getInstance().getInstanceFrom(p).getPlayers().size()+"");
		addPlaceholder("%max%", p -> Towers.getInstance().getGameInfo().maxPlayers()+"");
		addPlaceholder("%color%", p -> p.getTeam().getChatColor());
		addPlaceholder("%team%", p -> p.getTeam().getName());
		addPlaceholder("%bluepoints%", p -> Towers.getInstance().getInstanceFrom(p).getBluePoints()+"");
		addPlaceholder("%redpoints%", p -> Towers.getInstance().getInstanceFrom(p).getRedPoints()+"");
		addPlaceholder("%kills%", p -> p.getKills()+"");
		addPlaceholder("%deaths%", p -> p.getDeaths()+"");
	}

	@Override
	public HashMap<String, Function<PlayerData, String>> getPlaceholders() {
		return map;
	}

}

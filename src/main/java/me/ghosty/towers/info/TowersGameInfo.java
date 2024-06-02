package me.ghosty.towers.info;

import me.ghosty.towers.features.config.TowersConfig;

public final class TowersGameInfo implements GameInfo {
	
	@Override
	public String gameName() {
		return "Towers";
	}
	
	@Override
	public int maxPlayers() {
		return TowersConfig.gameConfig_maxPlayers;
	}
	
	@Override
	public int minPlayersForSlowTimer() {
		return TowersConfig.gameConfig_firstTimer_minPlayers;
	}
	
	@Override
	public int slowTimer() {
		return TowersConfig.gameConfig_firstTimer_seconds;
	}
	
	@Override
	public int minPlayersForFastTimer() {
		return TowersConfig.gameConfig_fastTimer_minPlayers;
	}
	
	@Override
	public int fastTimer() {
		return TowersConfig.gameConfig_fastTimer_seconds;
	}
	
	@Override
	public int numberOfInstances() {
		return TowersConfig.numberOfInstances;
	}
	
}

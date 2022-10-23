package dev.ghosty.towers;

import javax.annotation.Nonnegative;

import dev.ghosty.gameapi.GameInfo;

public final class TowersGameInfo implements GameInfo {

	@Override
	public String gameName() {
		return "Towers";
	}

	@Override
	public @Nonnegative int maxPlayers() {
		return TowersConfig.gameConfig_maxPlayers;
	}

	@Override
	public @Nonnegative int minPlayersForSlowTimer() {
		return TowersConfig.gameConfig_firstTimer_minPlayers;
	}

	@Override
	public @Nonnegative int slowTimer() {
		return TowersConfig.gameConfig_firstTimer_seconds;
	}

	@Override
	public @Nonnegative int minPlayersForFastTimer() {
		return TowersConfig.gameConfig_fastTimer_minPlayers;
	}

	@Override
	public @Nonnegative int fastTimer() {
		return TowersConfig.gameConfig_fastTimer_seconds;
	}

	@Override
	public @Nonnegative int numberOfInstances() {
		return TowersConfig.numberOfInstances;
	}

}

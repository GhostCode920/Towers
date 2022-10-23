package dev.ghosty.gameapi;

import dev.ghosty.gameapi.config.Config;

public interface Game {
	
	public GameInfo getGameInfo();
	
	public Config getConfiguration();

}

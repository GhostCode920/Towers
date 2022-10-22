package dev.ghosty.towers.game;

public enum GameState {
	
	WAITING,
	IN_GAME,
	ENDING,
	NOT_ONLINE;
	
	public boolean canHavePlayers() {
		return this == WAITING;
	}

	public boolean notInGame() {
		return this != IN_GAME;
	}

}

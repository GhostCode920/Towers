package dev.ghosty.gameapi;

import javax.annotation.Nonnegative;

public interface GameInfo {
	
	/**
	 * <p>The game name
	 * <p>Example: Bedwars
	 */
	public String gameName();
	
	/**
	 * <p>The maximum of players that can be in the game
	 * <p>Example: 16
	 */
	@Nonnegative
	public int maxPlayers();
	
	/**
	 * <p>The minimum of players to get the slow timer starting
	 * <p>Example: 8
	 * @see GameInfo#slowTimer()
	 */
	@Nonnegative
	public int minPlayersForSlowTimer();
	/**
	 * <p>The timer for when there is {@link GameInfo#minPlayersForSlowTimer() X} players in the game
	 * <p>Example: 90
	 * <p>The returned value must be in seconds
	 * @see GameInfo#minPlayersForSlowTimer()
	 */
	@Nonnegative
	public int slowTimer();
	/**
	 * <p>The minimum of players to get the fast timer starting
	 * <p>Example: 12
	 * @see GameInfo#fastTimer()
	 */
	@Nonnegative
	public int minPlayersForFastTimer();
	/**
	 * <p>The fast timer for when there is {@link GameInfo#minPlayersForFastTimer() X} players in the game
	 * <p>Example: 15
	 * <p>The returned value must be in seconds
	 * @see GameInfo#minPlayersForFastTimer()
	 */
	@Nonnegative
	public int fastTimer();
	
	@Nonnegative
	public int numberOfInstances();

}

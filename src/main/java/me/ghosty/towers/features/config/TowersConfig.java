package me.ghosty.towers.features.config;

import me.ghosty.towers.Towers;
import org.bukkit.Bukkit;
import org.bukkit.Material;

/**
 * TODO: Cache the data a better way
 */
public final class TowersConfig {
	
	public static int gameConfig_maxPlayers;
	public static String gameConfig_mapName;
	public static int gameConfig_firstTimer_minPlayers;
	public static int gameConfig_firstTimer_seconds;
	public static int gameConfig_fastTimer_minPlayers;
	public static int gameConfig_fastTimer_seconds;
	public static double locations_waitingSpawn_x;
	public static double locations_waitingSpawn_y;
	public static double locations_waitingSpawn_z;
	public static float locations_waitingSpawn_yaw;
	public static float locations_waitingSpawn_pitch;
	public static int locations_spawnProtectionRadius;
	public static double locations_bluesSpawn_x;
	public static double locations_bluesSpawn_y;
	public static double locations_bluesSpawn_z;
	public static float locations_bluesSpawn_yaw;
	public static float locations_bluesSpawn_pitch;
	public static double locations_redsSpawn_x;
	public static double locations_redsSpawn_y;
	public static double locations_redsSpawn_z;
	public static float locations_redsSpawn_yaw;
	public static float locations_redsSpawn_pitch;
	public static Material goals_blockType;
	public static int goals_checkRadius;
	public static int goals_noBlockRadius;
	public static int goals_blueGoal_x;
	public static int goals_blueGoal_y;
	public static int goals_blueGoal_z;
	public static int goals_redGoal_x;
	public static int goals_redGoal_y;
	public static int goals_redGoal_z;
	public static int buildHeightLimit;
	public static int numberOfInstances;
	public static boolean bungeecord_enabled;
	public static String bungeecord_ifEnabled_returnServer;
	public static String bungeecord_else_returnWorld;
	public static String messages_prefix;
	public static String messages_join;
	public static String messages_leave;
	public static String messages_leaveInGame;
	public static String messages_teamSelect;
	public static String messages_noGame;
	public static String messages_joining;
	public static String messages_playersOnly;
	public static String messages_diedUnknown;
	public static String messages_diedPlayer;
	public static String messages_noBuild;
	public static String messages_score;
	public static String messages_buildHeightLimit;
	public static String messages_countdownCancelled;
	public static String messages_countdownMultiple;
	public static String messages_countdownOneSecond;
	public static String messages_countdownZero;
	public static String teams_blue_name;
	public static String teams_blue_color;
	public static String teams_red_name;
	public static String teams_red_color;
	public static String scoreboard_title;
	public static String scoreboard_bluePoints;
	public static String scoreboard_redPoints;
	public static String scoreboard_currentTeam;
	public static String scoreboard_kills;
	public static String scoreboard_deaths;
	public static String scoreboard_serverIp;

	public static void init() {
		Config config = Towers.getInstance().getConfiguration();
		
		gameConfig_maxPlayers = config.getInt("game-config.max-players");
		gameConfig_mapName = config.getString("game-config.map-name");
		gameConfig_firstTimer_minPlayers = config.getInt("game-config.first-timer.min-players");
		gameConfig_firstTimer_seconds = config.getInt("game-config.first-timer.seconds");
		gameConfig_fastTimer_minPlayers = config.getInt("game-config.fast-timer.min-players");
		gameConfig_fastTimer_seconds = config.getInt("game-config.fast-timer.seconds");
		
		locations_waitingSpawn_x = config.getDouble("locations.waiting-spawn.x");
		locations_waitingSpawn_y = config.getDouble("locations.waiting-spawn.y");
		locations_waitingSpawn_z = config.getDouble("locations.waiting-spawn.z");
		locations_waitingSpawn_yaw = config.getFloat("locations.waiting-spawn.yaw");
		locations_waitingSpawn_pitch = config.getFloat("locations.waiting-spawn.pitch");
		locations_spawnProtectionRadius = config.getInt("locations.spawn-protection-radius");
		locations_bluesSpawn_x = config.getDouble("locations.blues-spawn.x");
		locations_bluesSpawn_y = config.getDouble("locations.blues-spawn.y");
		locations_bluesSpawn_z = config.getDouble("locations.blues-spawn.z");
		locations_bluesSpawn_yaw = config.getFloat("locations.blues-spawn.yaw");
		locations_bluesSpawn_pitch = config.getFloat("locations.blues-spawn.pitch");
		locations_redsSpawn_x = config.getDouble("locations.reds-spawn.x");
		locations_redsSpawn_y = config.getDouble("locations.reds-spawn.y");
		locations_redsSpawn_z = config.getDouble("locations.reds-spawn.z");
		locations_redsSpawn_yaw = config.getFloat("locations.reds-spawn.yaw");
		locations_redsSpawn_pitch = config.getFloat("locations.reds-spawn.pitch");
		
		try {
			goals_blockType = Material.valueOf(config.getString("goals.block-type"));
		} catch (Exception e) {
			Bukkit.getConsoleSender().sendMessage("§cTowers: goals.block-type in config.yml is wrong. Please patch it. Disabling...");
			e.printStackTrace();
			Bukkit.getPluginManager().disablePlugin(Towers.getInstance());
			return;
		}
		goals_checkRadius = config.getInt("goals.check-radius");
		goals_noBlockRadius = config.getInt("goals.no-block-radius");
		goals_blueGoal_x = config.getInt("goals.blue-goal.x");
		goals_blueGoal_y = config.getInt("goals.blue-goal.y");
		goals_blueGoal_z = config.getInt("goals.blue-goal.z");
		goals_redGoal_x = config.getInt("goals.red-goal.x");
		goals_redGoal_y = config.getInt("goals.red-goal.y");
		goals_redGoal_z = config.getInt("goals.red-goal.z");
		
		buildHeightLimit = config.getInt("build-height-limit");
		
		numberOfInstances = config.getInt("number-of-instances");
		
		bungeecord_enabled = config.getBoolean("bungeecord.enabled");
		bungeecord_ifEnabled_returnServer = config.getString("bungeecord.if-enabled.return-server");
		bungeecord_else_returnWorld = config.getString("bungeecord.else.return-world");
		
		messages_prefix = config.getString("messages.prefix");
		messages_join = config.getString("messages.join");
		messages_leave = config.getString("messages.leave");
		messages_leaveInGame = config.getString("messages.leave-in-game");
		messages_teamSelect = config.getString("messages.team-select");
		messages_noGame = config.getString("messages.no-game");
		messages_joining = config.getString("messages.joining");
		messages_playersOnly = config.getString("messages.players-only");
		messages_diedUnknown = config.getString("messages.died-unknown");
		messages_diedPlayer = config.getString("messages.died-player");
		messages_noBuild = config.getString("messages.no-build");
		messages_score = config.getString("messages.score");
		messages_buildHeightLimit = config.getString("messages.build-height-limit");
		messages_countdownCancelled = config.getString("messages.countdown-cancelled");
		messages_countdownMultiple = config.getString("messages.countdown-multiple");
		messages_countdownOneSecond = config.getString("messages.countdown-one-second");
		messages_countdownZero = config.getString("messages.countdown-zero");
		
		teams_blue_name = config.getString("teams.blue.name");
		teams_blue_color = config.getString("teams.blue.color");
		teams_red_name = config.getString("teams.red.name");
		teams_red_color = config.getString("teams.red.color");
		
		scoreboard_title = config.getString("scoreboard.title");
		scoreboard_bluePoints = config.getString("scoreboard.blue-points");
		scoreboard_redPoints = config.getString("scoreboard.red-points");
		scoreboard_currentTeam = config.getString("scoreboard.current-team");
		scoreboard_kills = config.getString("scoreboard.kills");
		scoreboard_deaths = config.getString("scoreboard.deaths");
		scoreboard_serverIp = config.getString("scoreboard.server-ip");
	}
	
}

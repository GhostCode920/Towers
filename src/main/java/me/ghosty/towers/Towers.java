package me.ghosty.towers;

import lombok.Getter;
import lombok.NonNull;
import me.ghosty.towers.api.DefaultTowersAPI;
import me.ghosty.towers.api.TowersAPI;
import me.ghosty.towers.features.config.Config;
import me.ghosty.towers.features.config.TowersConfig;
import me.ghosty.towers.features.data.PlayerData;
import me.ghosty.towers.features.data.TowersPlaceholder;
import me.ghosty.towers.features.commands.JoinCommand;
import me.ghosty.towers.features.game.GameEvents;
import me.ghosty.towers.features.game.Instance;
import me.ghosty.towers.info.GameInfo;
import me.ghosty.towers.info.TowersGameInfo;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class Towers extends JavaPlugin /*implements Game*/ {
	
	@Getter
	private ArrayList<Instance> games;
	
	@Getter
	private static Towers instance;
	
	@Getter
	private static TowersAPI API = new DefaultTowersAPI();
	
	@Getter
	private static TowersPlaceholder placeholder;
	
	//@Override
	public GameInfo getGameInfo() {
		return new TowersGameInfo();
	}
	
	//@Override
	public Config getConfiguration() {
		return new Config(getConfig());
	}
	
	@Override
	public void onLoad() {
		super.onLoad();
		instance = this;
		games = new ArrayList<>();
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
		
		saveDefaultConfig();
		TowersConfig.init();
		placeholder = new TowersPlaceholder();
		
		for(int i = 0; i < getGameInfo().numberOfInstances(); i++)
			games.add(new Instance(TowersConfig.gameConfig_mapName, true));
		
		Bukkit.getPluginManager().registerEvents(new GameEvents(), this);
		
		//getCommand("tptoworld").setExecutor(new TpToWorld());
		//getCommand("editgame").setExecutor(new EditGameCommand());
		getCommand("join").setExecutor(new JoinCommand());
		
		if(TowersConfig.bungeecord_enabled)
			getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
		games.forEach(g -> {
			g.end();
			g.unload();
		});
	}
	
	public static void setAPI(@NonNull TowersAPI api) {
		API = api;
	}
	
	public Instance getInstanceFrom(Player player) {
		for(Instance i : getGames())
			for(PlayerData p : i.getPlayers())
				if(p.getPlayer() == player)
					return i;
		return null;
	}
	
	public Instance getInstanceFrom(PlayerData p) {
		for(Instance i : getGames())
			if(i.getPlayers().contains(p))
				return i;
		return null;
	}
	
}

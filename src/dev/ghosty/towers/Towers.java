package dev.ghosty.towers;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import dev.ghostcode.gameapi.Game;
import dev.ghostcode.gameapi.GameInfo;
import dev.ghostcode.gameapi.config.Config;
import dev.ghosty.towers.api.DefaultTowersAPI;
import dev.ghosty.towers.api.TowersAPI;
import dev.ghosty.towers.data.PlayerData;
import dev.ghosty.towers.data.TowersPlaceholder;
import dev.ghosty.towers.debug.JoinCommand;
import dev.ghosty.towers.game.GameEvents;
import dev.ghosty.towers.game.Instance;
import lombok.Getter;
import lombok.NonNull;

public class Towers extends JavaPlugin implements Game {

	@Getter
	private ArrayList<Instance> games;
	
	@Getter
	private static Towers instance;
	
	@Getter
	private static TowersAPI API = new DefaultTowersAPI();
	
	@Getter
	private static TowersPlaceholder PH;;
	
	@Override
	public GameInfo getGameInfo() {
		return new TowersGameInfo();
	}
	
	@Override
	public Config getConfiguration() {
		return new Config(/*getDescription().getFullName()*/getConfig());
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
		
		TowersConfig.init();
		PH = new TowersPlaceholder();
		
		for(int i = 0; i < getGameInfo().numberOfInstances(); i++)
			games.add(new Instance("save", true));
		
		Bukkit.getPluginManager().registerEvents(new GameEvents(), this);
		
		//getCommand("tptoworld").setExecutor(new TpToWorld());
		//getCommand("editgame").setExecutor(new EditGameCommand());
		getCommand("join").setExecutor(new JoinCommand());
	
		saveDefaultConfig();
		
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

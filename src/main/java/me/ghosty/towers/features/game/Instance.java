package me.ghosty.towers.features.game;

import lombok.Getter;
import lombok.Setter;
import me.ghosty.towers.Towers;
import me.ghosty.towers.features.config.TowersConfig;
import me.ghosty.towers.features.data.PlayerData;
import me.ghosty.towers.features.data.Team;
import me.ghosty.towers.features.data.TowersPlaceholder;
import me.ghosty.towers.utils.FileUtil;
import me.ghosty.towers.utils.ScoreHelper;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Function;

import static me.ghosty.towers.features.config.TowersConfig.*;

public final class Instance {
	
	static Function<World, Location> blueTeamSpawn = w -> new Location(w,
		locations_bluesSpawn_x,
		locations_bluesSpawn_y,
		locations_bluesSpawn_z,
		locations_bluesSpawn_yaw,
		locations_bluesSpawn_pitch
	);
	static Function<World, Location> redTeamSpawn = w -> new Location(w,
		locations_redsSpawn_x,
		locations_redsSpawn_y,
		locations_redsSpawn_z,
		locations_redsSpawn_yaw,
		locations_redsSpawn_pitch
	);
	private final File sourceWorldFolder;
	public boolean ending;
	boolean $TempBool = false;
	@Getter
	private int bluePoints, redPoints;
	private File activeWorldFolder;
	private World bukkitWorld;
	@Getter
	private ArrayList<PlayerData> players;
	@Getter
	@Setter
	private GameState state;
	
	public Instance(String worldName, boolean loadOnInit) {
		this.sourceWorldFolder = new File(worldName);
		setState(GameState.NOT_ONLINE);
		if (loadOnInit)
			load();
	}
	
	public void addPoint(Team t) {
		if (t == Team.BLUE)
			bluePoints++;
		if (t == Team.RED)
			redPoints++;
		setScoreboard();
	}
	
	public boolean load() {
		if (isLoaded())
			return true;
		
		bluePoints = 0;
		redPoints = 0;
		players = new ArrayList<>();
		
		this.activeWorldFolder = new File(
			Bukkit.getWorldContainer().getParentFile(),
			sourceWorldFolder.getName() + "_game_" + System.currentTimeMillis()
		);
		
		try {
			FileUtil.copy(sourceWorldFolder, activeWorldFolder);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		this.bukkitWorld = Bukkit.createWorld(
			new WorldCreator(activeWorldFolder.getName())
		);
		
		if (bukkitWorld != null)
			this.bukkitWorld.setAutoSave(false);
		
		if (isLoaded()) {
			setState(GameState.WAITING);
			new PreGameTimer(this).doTimer();
			Bukkit.getConsoleSender().sendMessage(messages_prefix + "§aA new server is ready!");
			return true;
		}
		
		return false;
	}
	
	public void unload() {
		setState(GameState.NOT_ONLINE);
		if (bukkitWorld != null)
			Bukkit.unloadWorld(bukkitWorld, false);
		if (activeWorldFolder != null)
			FileUtil.delete(activeWorldFolder);
		bukkitWorld = null;
		activeWorldFolder = null;
	}
	
	public boolean restore() {
		unload();
		return load();
	}
	
	public void end() {
		setState(GameState.ENDING);
		Towers.getAPI().onInstanceEnd(this);
	}
	
	public boolean isLoaded() {
		return getWorld() != null;
	}
	
	public World getWorld() {
		return bukkitWorld;
	}
	
	public PlayerData getData(Player player) {
		for (PlayerData p : players)
			if (p.getPlayer() == player)
				return p;
		return null;
	}
	
	public boolean addPlayer(Player player) {
		if (!getState().canHavePlayers())
			return false;
		if (players.size() >= Towers.getInstance().getGameInfo().maxPlayers())
			return false;
		if (!players.isEmpty())
			for (PlayerData p : players)
				if (p.getPlayer() == player)
					return false;
		players.add(new PlayerData(player));
		try {
			player.teleport(new Location(getWorld(),
				locations_waitingSpawn_x,
				locations_waitingSpawn_y,
				locations_waitingSpawn_z,
				locations_waitingSpawn_yaw,
				locations_waitingSpawn_pitch
			));
			player.getInventory().clear();
			player.getInventory().setArmorContents(new ItemStack[] {});
			player.updateInventory();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		forAll(p -> p.getPlayer().sendMessage("§c[Towers] §a+ " + player.getName() + " §7(" + players.size() + "/" + Towers.getInstance().getGameInfo().maxPlayers() + ")"));
		return true;
	}
	
	public void removePlayer(Player player) {
		if (getState().notInGame())
			for (PlayerData p : players)
				if (p.getPlayer() == player)
					players.remove(p);
		if (getState().canHavePlayers())
			forAll(p -> p.getPlayer().sendMessage("§c[Towers] §7- " + player.getName() + " (" + players.size() + "/" + Towers.getInstance().getGameInfo().maxPlayers() + ")"));
		else if (getState() == GameState.IN_GAME)
			forAll(p -> p.getPlayer().sendMessage("§c[Towers] §2" + player.getName() + " §cdisconnected! He may rejoin later."));
		if (ScoreHelper.hasScore(player))
			ScoreHelper.removeScore(player);
	}
	
	public void forAll(Consumer<PlayerData> func) {
		for (PlayerData p : players)
			func.accept(p);
	}
	
	public void gameStart() {
		setState(GameState.IN_GAME);
		forAll(p -> {
			$TempBool = !$TempBool;
			p.setTeam($TempBool ? Team.BLUE : Team.RED);
			p.getPlayer().setMaxHealth(20);
			spawn(p);
			setScoreboard();
			p.getPlayer().sendMessage("§c[Towers] §aYou joined the " + ($TempBool ? p.getTeam().getChatColor() + "Blue" : p.getTeam().getChatColor() + "Red") + " Team§a!");
		});
	}
	
	public void spawn(PlayerData p) {
		Team team = p.getTeam();
		p.getPlayer().teleport(team == Team.BLUE ? blueTeamSpawn.apply(getWorld()) : redTeamSpawn.apply(getWorld()));
		{
			ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
			LeatherArmorMeta meta = (LeatherArmorMeta) helmet.getItemMeta();
			meta.setColor(team.getColor());
			helmet.setItemMeta(meta);
			ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
			meta = (LeatherArmorMeta) chestplate.getItemMeta();
			meta.setColor(team.getColor());
			chestplate.setItemMeta(meta);
			ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
			meta = (LeatherArmorMeta) leggings.getItemMeta();
			meta.setColor(team.getColor());
			leggings.setItemMeta(meta);
			ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
			meta = (LeatherArmorMeta) boots.getItemMeta();
			meta.setColor(team.getColor());
			boots.setItemMeta(meta);
			p.getPlayer().getInventory().setArmorContents(new ItemStack[] {boots, leggings, chestplate, helmet});
		}
		p.getPlayer().getInventory().clear();
		p.getPlayer().setHealth(p.getPlayer().getMaxHealth());
		p.getPlayer().setSaturation(5f);
		p.getPlayer().setFoodLevel(20);
		p.getPlayer().getInventory().setItem(8, new ItemStack(Material.BAKED_POTATO, 32));
		p.getPlayer().updateInventory();
		p.getPlayer().setNoDamageTicks(40);
	}
	
	// TODO: Add a list in config instead
	public void setScoreboard() {
		TowersPlaceholder ph = Towers.getPlaceholder();
		forAll(data -> {
			ScoreHelper sb = ScoreHelper.createScore(data.getPlayer());
			sb.setTitle(ph.placeholder(scoreboard_title, data));
			sb.setSlot(9, ph.placeholder(scoreboard_bluePoints, data));
			sb.setSlot(8, ph.placeholder(scoreboard_redPoints, data));
			sb.setSlot(7, " ");
			sb.setSlot(6, ph.placeholder(scoreboard_currentTeam, data));
			sb.setSlot(5, " ");
			sb.setSlot(4, ph.placeholder(scoreboard_serverIp, data));
		});
	}
	
}

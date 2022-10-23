package dev.ghosty.towers.game;

import static dev.ghosty.towers.TowersConfig.locations_bluesSpawn_pitch;
import static dev.ghosty.towers.TowersConfig.locations_bluesSpawn_x;
import static dev.ghosty.towers.TowersConfig.locations_bluesSpawn_y;
import static dev.ghosty.towers.TowersConfig.locations_bluesSpawn_yaw;
import static dev.ghosty.towers.TowersConfig.locations_bluesSpawn_z;
import static dev.ghosty.towers.TowersConfig.locations_redsSpawn_pitch;
import static dev.ghosty.towers.TowersConfig.locations_redsSpawn_x;
import static dev.ghosty.towers.TowersConfig.locations_redsSpawn_y;
import static dev.ghosty.towers.TowersConfig.locations_redsSpawn_yaw;
import static dev.ghosty.towers.TowersConfig.locations_redsSpawn_z;
import static dev.ghosty.towers.TowersConfig.locations_waitingSpawn_pitch;
import static dev.ghosty.towers.TowersConfig.locations_waitingSpawn_x;
import static dev.ghosty.towers.TowersConfig.locations_waitingSpawn_y;
import static dev.ghosty.towers.TowersConfig.locations_waitingSpawn_yaw;
import static dev.ghosty.towers.TowersConfig.locations_waitingSpawn_z;
import static dev.ghosty.towers.TowersConfig.messages_prefix;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Function;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import dev.ghosty.gameapi.util.FileUtil;
import dev.ghosty.gameapi.util.ScoreHelper;
import dev.ghosty.towers.Towers;
import dev.ghosty.towers.data.PlayerData;
import dev.ghosty.towers.data.Team;
import lombok.Getter;
import lombok.Setter;

public final class Instance {
	
	@Getter
	private int bluePoints, redPoints;
	
	public void addPoint(Team t) {
		if(t == Team.BLUE)
			bluePoints++;
		if(t == Team.RED)
			redPoints++;
		setScoreboard();
	}
	
	private final File sourceWorldFolder;
	private File activeWorldFolder;
	private World bukkitWorld;
	@Getter
	private ArrayList<PlayerData> players;
	@Getter
	@Setter
	private GameState state;
	
	public boolean ending;
	
	static Function<World,Location> blueTeamSpawn = w -> new Location(w,
			locations_bluesSpawn_x,
			locations_bluesSpawn_y,
			locations_bluesSpawn_z,
			locations_bluesSpawn_yaw,
			locations_bluesSpawn_pitch
	);
	static Function<World,Location> redTeamSpawn = w -> new Location(w,
			locations_redsSpawn_x,
			locations_redsSpawn_y,
			locations_redsSpawn_z,
			locations_redsSpawn_yaw,
			locations_redsSpawn_pitch
	);
	
	public Instance(String worldName, boolean loadOnInit) {
		this.sourceWorldFolder = new File(worldName);
		setState(GameState.NOT_ONLINE);
		if(loadOnInit) load();
	}
	
	public boolean load() {
		if(isLoaded()) return true;
		
		bluePoints = 0;
		redPoints = 0;
		players = new ArrayList<>();
		
		this.activeWorldFolder = new File(
			Bukkit.getWorldContainer().getParentFile(),
			sourceWorldFolder.getName()+"_game_"+System.currentTimeMillis()
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
		
		if(bukkitWorld != null) this.bukkitWorld.setAutoSave(false);
		
		if(isLoaded()) {
			setState(GameState.WAITING);
			new PreGameTimer(this).doTimer();
			Bukkit.getConsoleSender().sendMessage(messages_prefix+"§aA new server is ready!");
			return true;
		}
		
		return false;
	}
	
	public void unload() {
		setState(GameState.NOT_ONLINE);
		if(bukkitWorld != null) Bukkit.unloadWorld(bukkitWorld, false);
		if(activeWorldFolder != null) FileUtil.delete(activeWorldFolder);
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
		for(PlayerData p : players)
			if(p.getPlayer() == player)
				return p;
		return null;
	}
	
	public boolean addPlayer(Player player) {
		if(!getState().canHavePlayers()) return false;
		if(players.size() >= Towers.getInstance().getGameInfo().maxPlayers()) return false;
		if(!players.isEmpty())
			for(PlayerData p : players)
				if(p.getPlayer() == player)
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
		forAll(p -> p.getPlayer().sendMessage("§c[Towers] §a+ "+player.getName()+" §7("+players.size()+"/"+Towers.getInstance().getGameInfo().maxPlayers()+")"));
		return true;
	}
	
	public void removePlayer(Player player) {
		if(getState().notInGame())
			for(PlayerData p : players)
				if(p.getPlayer() == player)
					players.remove(p);
		if(getState().canHavePlayers())
			forAll(p -> p.getPlayer().sendMessage("§c[Towers] §7- "+player.getName()+" ("+players.size()+"/"+Towers.getInstance().getGameInfo().maxPlayers()+")"));
		else if(getState() == GameState.IN_GAME)
			forAll(p -> p.getPlayer().sendMessage("§c[Towers] §2"+player.getName()+" §cdisconnected! He may rejoin later."));
		if(ScoreHelper.hasScore(player))
			ScoreHelper.removeScore(player);
	}
	
	public void forAll(Consumer<PlayerData> func) {
		for(PlayerData p : players)
			func.accept(p);
	}
	
	boolean $TempBool = false;
	public void gameStart() {
		setState(GameState.IN_GAME);
		forAll(p -> {
			$TempBool = !$TempBool;
			p.setTeam($TempBool?Team.BLUE:Team.RED);
			p.getPlayer().setMaxHealth(20);
			spawn(p);
			setScoreboard();
			p.getPlayer().sendMessage("§c[Towers] §aYou joined the "+($TempBool?p.getTeam().getChatColor()+"Blue":p.getTeam().getChatColor()+"Red")+" Team§a!");
		});
	}
	
	public void spawn(PlayerData p) {
		Team team = p.getTeam();
		p.getPlayer().teleport(team==Team.BLUE?blueTeamSpawn.apply(getWorld()):redTeamSpawn.apply(getWorld()));
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
			p.getPlayer().getInventory().setArmorContents(new ItemStack[] {boots,leggings,chestplate,helmet});
		}
		p.getPlayer().getInventory().clear();
		p.getPlayer().setHealth(p.getPlayer().getMaxHealth());
		p.getPlayer().setSaturation(5f);
		p.getPlayer().setFoodLevel(20);
		p.getPlayer().getInventory().setItem(8, new ItemStack(Material.BAKED_POTATO, 32));
		p.getPlayer().updateInventory();
		p.getPlayer().setNoDamageTicks(40);
	}
	
	public void setScoreboard() {
		forAll(p -> {
			ScoreHelper sb = ScoreHelper.createScore(p.getPlayer());
			sb.setTitle("§cTowers");
			sb.setSlot(9, Team.BLUE.getChatColor()+"Blue Points§7: §b"+bluePoints);
			sb.setSlot(8, Team.RED.getChatColor()+"Red Points§7: §b"+redPoints);
			sb.setSlot(7, " ");
			sb.setSlot(6, "§7Your team: "+p.getTeam().getChatColor()+p.getTeam().getName());
			sb.setSlot(5, " ");
			sb.setSlot(4, "§bBy GhostCode#1832 (big ad)");
		});
	}

}

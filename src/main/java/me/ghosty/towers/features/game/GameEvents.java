package me.ghosty.towers.features.game;

import me.ghosty.towers.Towers;
import me.ghosty.towers.features.config.TowersConfig;
import me.ghosty.towers.features.data.PlayerData;
import me.ghosty.towers.features.data.Team;
import me.ghosty.towers.utils.ScoreHelper;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Arrays;
import java.util.List;

import static me.ghosty.towers.features.config.TowersConfig.*;

public final class GameEvents implements Listener {
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void cantDamage(EntityDamageEvent e) {
		if(!(e.getEntity() instanceof Player)) return;
		Instance i = Towers.getInstance().getInstanceFrom((Player)e.getEntity());
		if(i == null) return;
		if(i.getState().notInGame()) e.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void cantPlace(BlockPlaceEvent e) {
		Instance i = Towers.getInstance().getInstanceFrom(e.getPlayer());
		if(i == null) return;
		if(i.getState().notInGame()) e.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void cantBreak(BlockBreakEvent e) {
		Instance i = Towers.getInstance().getInstanceFrom(e.getPlayer());
		if(i == null) return;
		if(i.getState().notInGame()) e.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onDamageDeath(EntityDamageEvent e) {
		if(!(e.getEntity() instanceof Player)) return;
		Player player = (Player) e.getEntity();
		Instance instance = Towers.getInstance().getInstanceFrom(player);
		if(instance == null) return;
		PlayerData data = instance.getData(player);
		Team team = data.getTeam();
		if(team == Team.NOT_ASSIGNED) return;
		if(player.getHealth()-e.getDamage() > 0) return;
		
		e.setCancelled(true);
		instance.spawn(data);
		System.out.println("damage");
		
		String message = Towers.getPlaceholder().placeholder(messages_diedUnknown, data);
		if(e instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent ev = (EntityDamageByEntityEvent) e;
			if(ev.getDamager() instanceof Player)
				message = Towers.getPlaceholder().placeholder(messages_diedPlayer, data).replace("%killer%", ev.getDamager().getName());
			if(ev.getDamager() instanceof Projectile) {
				Projectile dm = (Projectile) ev.getDamager();
				if(dm.getShooter() instanceof Player)
					message = Towers.getPlaceholder().placeholder(messages_diedPlayer, data).replace("%killer%", ((Player)dm.getShooter()).getName());
			}
		}
		final String finalMessage = message;
		instance.forAll(p -> p.getPlayer().sendMessage(messages_prefix+finalMessage));
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDeath(EntityDeathEvent e) {
		if(!(e.getEntity() instanceof Player)) return;
		Player player = (Player) e.getEntity();
		Instance i = Towers.getInstance().getInstanceFrom(player);
		if(i == null) return;
		List<Material> mats = Arrays.asList(Material.LEATHER_BOOTS, Material.LEATHER_LEGGINGS, Material.LEATHER_CHESTPLATE, Material.LEATHER_HELMET);
		e.getDrops().removeIf(m -> mats.contains(m.getType()));
		i.spawn(i.getData(player));
		System.out.println("death");
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onTeamDamage(EntityDamageByEntityEvent e) {
		if(!(e.getEntity() instanceof Player)) return;
		Player player = (Player) e.getEntity();
		Instance i = Towers.getInstance().getInstanceFrom(player);
		if(i == null) return;
		Team t = i.getData(player).getTeam();
		if(t == Team.NOT_ASSIGNED) return;
		
		if(e.getDamager() instanceof Player) {
			Player dm = (Player) e.getDamager();
			PlayerData pd = i.getData(dm);
			if(pd == null) return; // TODO idk what to do with this lol
			if(t == pd.getTeam()) e.setCancelled(true);
		}
		if(e.getDamager() instanceof Projectile) {
			Projectile dm = (Projectile) e.getDamager();
			if(dm.getShooter() instanceof Player) {
				Player sc = (Player) dm.getShooter();
				PlayerData pd = i.getData(sc);
				if(pd == null) return; // TODO idk what to do with this lol
				if(t == pd.getTeam()) e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if(ScoreHelper.hasScore(p))
			ScoreHelper.removeScore(p);
		Instance i = Towers.getInstance().getInstanceFrom(p);
		if(i == null) return;
		i.removePlayer(p);
	}
	
	//TODO auto join back system
	
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		Instance i = Towers.getInstance().getInstanceFrom(e.getPlayer());
		if(i == null) return;
		if(e.getBlock().getType() == Material.WEB) return;
		if(i.getData(e.getPlayer()).getTeam() == Team.NOT_ASSIGNED) return;
		if(checkAction(e.getBlock().getLocation(), goals_blueGoal_x, goals_blueGoal_y, goals_blueGoal_z, goals_noBlockRadius, true)
		|| checkAction(e.getBlock().getLocation(), goals_redGoal_x, goals_redGoal_y, goals_redGoal_z, goals_noBlockRadius, true)
		|| checkAction(e.getBlock().getLocation(), (int)locations_bluesSpawn_x, (int)locations_bluesSpawn_y, (int)locations_bluesSpawn_z, locations_spawnProtectionRadius, false)
		|| checkAction(e.getBlock().getLocation(), (int)locations_redsSpawn_x, (int)locations_redsSpawn_y, (int)locations_redsSpawn_z, locations_spawnProtectionRadius, false)) {
	
			e.setCancelled(true);
			e.getPlayer().sendMessage(Towers.getPlaceholder().placeholder(messages_noBuild, i.getData(e.getPlayer())));
			
		}
	}
	
	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		Instance i = Towers.getInstance().getInstanceFrom(e.getPlayer());
		if(i == null) return;
		if(i.getData(e.getPlayer()).getTeam() == Team.NOT_ASSIGNED) return;
		if(checkAction(e.getBlock().getLocation(), goals_blueGoal_x, goals_blueGoal_y, goals_blueGoal_z, goals_noBlockRadius, true)
		|| checkAction(e.getBlock().getLocation(), goals_redGoal_x, goals_redGoal_y, goals_redGoal_z, goals_noBlockRadius, true)
		|| checkAction(e.getBlock().getLocation(), (int)locations_bluesSpawn_x, (int)locations_bluesSpawn_y, (int)locations_bluesSpawn_z, locations_spawnProtectionRadius, false)
		|| checkAction(e.getBlock().getLocation(), (int)locations_redsSpawn_x, (int)locations_redsSpawn_y, (int)locations_redsSpawn_z, locations_spawnProtectionRadius, false)) {
			
			e.setCancelled(true);
			e.getPlayer().sendMessage(Towers.getPlaceholder().placeholder(messages_noBuild, i.getData(e.getPlayer())));
			
		}
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		Player player = e.getPlayer();
		Instance i = Towers.getInstance().getInstanceFrom(player);
		if(i == null) return;
		Team t = i.getData(player).getTeam();
		if(t == Team.NOT_ASSIGNED) return;
		Location blockPos = e.getTo().getBlock().getLocation();
		int x = t.isBlue()?goals_blueGoal_x:goals_redGoal_x;
		int y = t.isBlue()?goals_blueGoal_y:goals_redGoal_y;
		int z = t.isBlue()?goals_blueGoal_z:goals_redGoal_z;
		int radius = goals_checkRadius;
		int xDiff = Math.abs(blockPos.getBlockX() - x);
		int zDiff = Math.abs(blockPos.getBlockZ() - z);
		if(blockPos.getBlockY() != y) return;
		if(xDiff > radius) return;
		if(zDiff > radius) return;
		player.teleport(t.isBlue()?Instance.blueTeamSpawn.apply(i.getWorld()):Instance.redTeamSpawn.apply(i.getWorld()));
		player.setHealth(player.getMaxHealth());
		player.setSaturation(5f);
		player.setFoodLevel(20);
		i.addPoint(t);
		i.forAll(p -> p.getPlayer().sendMessage(messages_prefix+Towers.getPlaceholder().placeholder(messages_score, i.getData(player))));
	}
	
	private boolean checkAction(Location blockPos, int x, int y, int z, int radius, boolean checkAllY) {
		int xDiff = Math.abs(blockPos.getBlockX() - x);
		int yDiff = Math.abs(blockPos.getBlockY() - y);
		int zDiff = Math.abs(blockPos.getBlockZ() - z);
		if(yDiff > radius && !checkAllY) return false;
		if(xDiff > radius) return false;
		if(zDiff > radius) return false;
		return true;
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlaceBlock(BlockPlaceEvent e) {
		Player player = e.getPlayer();
		Instance i = Towers.getInstance().getInstanceFrom(player);
		if(i == null) return;
		Team t = i.getData(player).getTeam();
		if(t == Team.NOT_ASSIGNED) return;
		if(e.getBlock().getLocation().getBlockY() > TowersConfig.buildHeightLimit) {
			e.setCancelled(true);
			player.sendMessage(TowersConfig.messages_buildHeightLimit);
		}
	}

}

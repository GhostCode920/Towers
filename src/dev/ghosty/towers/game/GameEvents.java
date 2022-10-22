package dev.ghosty.towers.game;

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
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import dev.ghostcode.gameapi.util.ScoreHelper;
import dev.ghosty.towers.Towers;
import dev.ghosty.towers.data.PlayerData;
import dev.ghosty.towers.data.Team;

import static dev.ghosty.towers.TowersConfig.*;

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
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDeath(EntityDamageEvent e) {
		if(!(e.getEntity() instanceof Player)) return;
		Player player = (Player) e.getEntity();
		Instance i = Towers.getInstance().getInstanceFrom(player);
		if(i == null) return;
		PlayerData pd = i.getData(player);
		Team t = pd.getTeam();
		if(t == Team.NOT_ASSIGNED) return;
		if(player.getHealth()-e.getDamage() > 0) return;
		
		e.setCancelled(true);
		i.spawn(pd);
		
		String message = Towers.getPH().placehold(messages_diedUnknown, pd);
		if(e instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent ev = (EntityDamageByEntityEvent) e;
			if(ev.getDamager() instanceof Player)
				message = Towers.getPH().placehold(messages_diedPlayer, pd).replace("%killer%", ev.getDamager().getName());
			if(ev.getDamager() instanceof Projectile) {
				Projectile dm = (Projectile) ev.getDamager();
				if(dm.getShooter() instanceof Player)
					message = Towers.getPH().placehold(messages_diedPlayer, pd).replace("%killer%", ((Player)dm.getShooter()).getName());
			}
		}
		final String finalMessage = message;
		i.forAll(p -> p.getPlayer().sendMessage(messages_prefix+finalMessage));
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
			e.getPlayer().sendMessage(Towers.getPH().placehold(messages_noBuild, i.getData(e.getPlayer())));
			
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
			e.getPlayer().sendMessage(Towers.getPH().placehold(messages_noBuild, i.getData(e.getPlayer())));
			
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
		int x = t.b()?goals_blueGoal_x:goals_redGoal_x;
		int y = t.b()?goals_blueGoal_y:goals_redGoal_y;
		int z = t.b()?goals_blueGoal_z:goals_redGoal_z;
		int radius = goals_checkRadius;
		int xDiff = Math.abs(blockPos.getBlockX() - x);
		int zDiff = Math.abs(blockPos.getBlockZ() - z);
		if(blockPos.getBlockY() != y) return;
		if(xDiff > radius) return;
		if(zDiff > radius) return;
		player.teleport(t.b()?Instance.blueTeamSpawn.apply(i.getWorld()):Instance.redTeamSpawn.apply(i.getWorld()));
		player.setHealth(player.getMaxHealth());
		player.setSaturation(5f);
		player.setFoodLevel(20);
		i.addPoint(t);
		i.forAll(p -> p.getPlayer().sendMessage(messages_prefix+Towers.getPH().placehold(messages_score, i.getData(player))));
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

}

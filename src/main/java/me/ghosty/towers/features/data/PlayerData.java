package me.ghosty.towers.features.data;

import org.bukkit.entity.Player;

import lombok.Getter;
import lombok.Setter;

@Getter
public final class PlayerData {
	
	private final Player player;
	private int deaths, kills;
	
	public PlayerData(Player player) {
		this.player = player;
		this.team = Team.NOT_ASSIGNED;
	}

	@Setter
	private Team team;
	
	public void addDeath() {
		deaths++;
	}
	
	public void addKill() {
		kills++;
	}

}

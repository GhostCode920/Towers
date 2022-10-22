package dev.ghosty.towers.data;

import org.bukkit.entity.Player;

import lombok.Getter;
import lombok.Setter;

public class PlayerData {
	
	@Getter
	private Player player;
	
	@Getter
	private int deaths, kills;
	
	public PlayerData(Player player) {
		this.player = player;
		this.team = Team.NOT_ASSIGNED;
	}

	@Getter
	@Setter
	private Team team;
	
	public void addDeath() {
		deaths++;
	}
	
	public void addKill() {
		kills++;
	}

}

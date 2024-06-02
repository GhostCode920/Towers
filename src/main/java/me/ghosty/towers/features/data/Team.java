package me.ghosty.towers.features.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.ghosty.towers.features.config.TowersConfig;
import org.bukkit.ChatColor;
import org.bukkit.Color;

@RequiredArgsConstructor
@Getter
public enum Team {
	
	BLUE(TowersConfig.teams_blue_name, TowersConfig.teams_blue_color, Color.BLUE),
	RED(TowersConfig.teams_red_name, TowersConfig.teams_red_color, Color.RED), 
	
	//TODO Spectators
	SPECTATE("", ChatColor.GRAY+"", Color.GRAY), 
	
	NOT_ASSIGNED("", ChatColor.GRAY+"", Color.GRAY);
	
	final String name, chatColor;
	final Color color;
	
	public boolean isBlue() {
		return this == BLUE;
	}

}

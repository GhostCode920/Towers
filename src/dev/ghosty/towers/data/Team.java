package dev.ghosty.towers.data;

import org.bukkit.ChatColor;
import org.bukkit.Color;

import dev.ghosty.towers.TowersConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Team {
	
	BLUE(TowersConfig.teams_blue_name, TowersConfig.teams_blue_color, Color.BLUE), 
	RED(TowersConfig.teams_red_name, TowersConfig.teams_red_color, Color.RED), 
	
	//TODO Coming soon
	SPECTATE("", ChatColor.GRAY+"", Color.GRAY), 
	
	NOT_ASSIGNED("", ChatColor.GRAY+"", Color.GRAY);
	
	String name;
	String chatColor;
	Color color;
	
	public boolean b() {
		if(this == BLUE) return true;
		return false;
	}

}

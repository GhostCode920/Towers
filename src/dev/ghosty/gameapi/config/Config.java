package dev.ghosty.gameapi.config;

import org.bukkit.configuration.file.FileConfiguration;

public final class Config extends Configuration {
	
	public Config(String pluginName) {
		super("plugins/"+pluginName+"/config.yml");
	}
	
	public Config(FileConfiguration config) {
		super(config);
	}
	
}

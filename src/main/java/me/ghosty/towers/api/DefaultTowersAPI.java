package me.ghosty.towers.api;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.ghosty.towers.Towers;
import me.ghosty.towers.features.config.TowersConfig;
import me.ghosty.towers.features.data.PlayerData;
import me.ghosty.towers.features.game.Instance;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public final class DefaultTowersAPI implements TowersAPI {
	
	@Override
	public void onInstanceEnd(Instance instance) {
		boolean bungeecord = TowersConfig.bungeecord_enabled;
		String world = bungeecord
			? TowersConfig.bungeecord_ifEnabled_returnServer
			: TowersConfig.bungeecord_else_returnWorld;
		for (PlayerData data : instance.getPlayers()) {
			if (bungeecord) {
				ByteArrayDataOutput out = ByteStreams.newDataOutput();
				out.writeUTF("Connect");
				out.writeUTF(world);
				data.getPlayer().sendPluginMessage(Towers.getInstance(), "BungeeCord", out.toByteArray());
			} else {
				//TODO with a real loc in config
				data.getPlayer().teleport(new Location(Bukkit.getWorld(world), 0, 150, 0));
			}
		}
	}
	
}

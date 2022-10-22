package dev.ghosty.towers.api;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import dev.ghosty.towers.Towers;
import dev.ghosty.towers.TowersConfig;
import dev.ghosty.towers.data.PlayerData;
import dev.ghosty.towers.game.Instance;

public final class DefaultTowersAPI implements TowersAPI {

	@Override
	public void onInstanceEnd(Instance instance) {
		boolean bungeecord = TowersConfig.bungeecord_enabled;
		String world = bungeecord?TowersConfig.bungeecord_ifEnabled_returnServer:TowersConfig.bungeecord_else_returnWorld;
		for(PlayerData p : instance.getPlayers()) {
			if(bungeecord) {
				ByteArrayDataOutput out = ByteStreams.newDataOutput();
				out.writeUTF("Connect");
				out.writeUTF(world);
				p.getPlayer().sendPluginMessage(Towers.getInstance(), "BungeeCord", out.toByteArray());
			}else {
				//TODO with a real loc in config next time uwu
				p.getPlayer().teleport(new Location(Bukkit.getWorld(world), 0, 150, 0));
			}
		}
	}

}

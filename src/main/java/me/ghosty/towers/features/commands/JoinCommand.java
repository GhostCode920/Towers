package me.ghosty.towers.features.commands;

import me.ghosty.towers.Towers;
import me.ghosty.towers.features.game.Instance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static me.ghosty.towers.features.config.TowersConfig.*;

public final class JoinCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sd, Command cmd, String label, String[] args) {
		if (!(sd instanceof Player)) {
			sd.sendMessage(messages_prefix + messages_playersOnly);
			return true;
		}
		Player p = (Player) sd;
		for (Instance i : Towers.getInstance().getGames())
			if (i.getPlayers().size() < Towers.getInstance().getGameInfo().maxPlayers() && i.getState().canHavePlayers()) {
				p.sendMessage(messages_prefix + messages_joining);
				i.addPlayer(p);
				return true;
			}
		
		p.sendMessage(messages_prefix + messages_noGame);
		
		return true;
	}
	
}

package dev.ghosty.towers.debug;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpToWorld implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sd, Command cmd, String label, String[] args) {
		if(!(sd instanceof Player)) {
			sd.sendMessage("§cPlayer only!");
			return true;
		}
		if(args.length < 1) {
			sd.sendMessage("§cArg1: Nom du monde");
			return true;
		}
		if(Bukkit.getWorld(args[0]) == null) {
			sd.sendMessage("§cMonde inconnu au bataillon");
			return true;
		}
		((Player)sd).teleport(new Location(Bukkit.getWorld(args[0]), 0, 150, 0));
		sd.sendMessage("§aTp avec succès!");
		return true;
	}

}

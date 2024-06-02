package me.ghosty.towers.features.commands;

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
			sd.sendMessage("§cThis command is only usable by players!");
			return true;
		}
		if(args.length < 1) {
			sd.sendMessage("§cArgument missing: World name");
			return true;
		}
		if(Bukkit.getWorld(args[0].trim()) == null) {
			sd.sendMessage("§cUnkown world '"+args[0]+"'");
			return true;
		}
		((Player)sd).teleport(new Location(Bukkit.getWorld(args[0]), 0, 150, 0));
		sd.sendMessage("§aDone!");
		return true;
	}

}

package me.ghosty.towers.features.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class EditGameCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sd, Command cmd, String label, String[] args) {
		if(!(sd instanceof Player)) {
			sd.sendMessage("§cThis command is reserved to players.");
			return true;
		}
		
		//Player p = (Player) sd;
		//Towers.getInstance().getGameOne().addPlayer(p);
		
		return true;
	}

}

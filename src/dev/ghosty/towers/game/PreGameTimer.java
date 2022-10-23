package dev.ghosty.towers.game;

import static dev.ghosty.towers.TowersConfig.messages_countdownCancelled;
import static dev.ghosty.towers.TowersConfig.messages_countdownMultiple;
import static dev.ghosty.towers.TowersConfig.messages_countdownOneSecond;
import static dev.ghosty.towers.TowersConfig.messages_countdownZero;
import static dev.ghosty.towers.TowersConfig.messages_prefix;

import org.bukkit.scheduler.BukkitRunnable;

import dev.ghosty.towers.Towers;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class PreGameTimer {
	
	private Instance game;
	
	public void doTimer() {
		new BukkitRunnable() {
			final int slowPlayers = Towers.getInstance().getGameInfo().minPlayersForSlowTimer();
			final int fastPlayers = Towers.getInstance().getGameInfo().minPlayersForFastTimer();
			final int slowTimer = Towers.getInstance().getGameInfo().slowTimer();
			final int fastTimer = Towers.getInstance().getGameInfo().fastTimer();
			int timer = -2;
			@Override
			public void run() {
				if(timer == -3) return;
				if(game.getPlayers().size() < slowPlayers && game.getPlayers().size() < fastPlayers) {
					if(timer != -1)
						game.forAll(p -> p.getPlayer().sendMessage(messages_prefix+messages_countdownCancelled));
					timer = -1;
				}
				if(game.getPlayers().size() >= slowPlayers && (timer <= -1 || timer >= slowTimer)) {
					timer = slowTimer;
				}
				if(game.getPlayers().size() >= fastPlayers && (timer <= -1 || timer >= fastTimer)) {
					timer = fastTimer;
				}
				
				if(timer >= 0) {
					switch(timer) {
					case 60:
						game.forAll(p -> p.getPlayer().sendMessage(messages_prefix+messages_countdownMultiple.replace("%timer%", timer+"")));
						break;
					case 30:
						game.forAll(p -> p.getPlayer().sendMessage(messages_prefix+messages_countdownMultiple.replace("%timer%", timer+"")));
						break;
					case 15:
						game.forAll(p -> p.getPlayer().sendMessage(messages_prefix+messages_countdownMultiple.replace("%timer%", timer+"")));
						break;
					case 10:
						game.forAll(p -> p.getPlayer().sendMessage(messages_prefix+messages_countdownMultiple.replace("%timer%", timer+"")));
						break;
					case 5:
						game.forAll(p -> p.getPlayer().sendMessage(messages_prefix+messages_countdownMultiple.replace("%timer%", timer+"")));
						break;
					case 3:
						game.forAll(p -> p.getPlayer().sendMessage(messages_prefix+messages_countdownMultiple.replace("%timer%", timer+"")));
						break;
					case 2:
						game.forAll(p -> p.getPlayer().sendMessage(messages_prefix+messages_countdownMultiple.replace("%timer%", timer+"")));
						break;
					case 1:
						game.forAll(p -> p.getPlayer().sendMessage(messages_prefix+messages_countdownOneSecond.replace("%timer%", timer+"")));
						break;
					case 0:
						game.forAll(p -> p.getPlayer().sendMessage(messages_prefix+messages_countdownZero));
						game.gameStart();
						cancel();
						timer = -3;
						break;
					}
					timer--;
				}
			}
		}.runTaskTimer(Towers.getInstance(), 20L, 20L);
	}
	
}

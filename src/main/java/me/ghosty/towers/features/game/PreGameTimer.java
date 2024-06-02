package me.ghosty.towers.features.game;

import lombok.AllArgsConstructor;
import me.ghosty.towers.Towers;
import org.bukkit.scheduler.BukkitRunnable;

import static me.ghosty.towers.features.config.TowersConfig.*;

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
				if (timer == -3)
					return;
				if (game.getPlayers().size() < slowPlayers && game.getPlayers().size() < fastPlayers) {
					if (timer != -1)
						game.forAll(p -> p.getPlayer().sendMessage(messages_prefix + messages_countdownCancelled));
					timer = -1;
				}
				if (game.getPlayers().size() >= slowPlayers && (timer <= -1 || timer >= slowTimer)) {
					timer = slowTimer;
				}
				if (game.getPlayers().size() >= fastPlayers && (timer <= -1 || timer >= fastTimer)) {
					timer = fastTimer;
				}
				
				if (timer >= 0) {
					switch (timer) {
						case 60:
						case 30:
						case 15:
						case 10:
						case 5:
						case 3:
						case 2:
							game.forAll(p -> p.getPlayer().sendMessage(messages_prefix + messages_countdownMultiple.replace("%timer%", timer + "")));
							break;
						case 1:
							// we add this for the small annoying "1 seconds"
							game.forAll(p -> p.getPlayer().sendMessage(messages_prefix + messages_countdownOneSecond.replace("%timer%", timer + "")));
							break;
						case 0:
							game.forAll(p -> p.getPlayer().sendMessage(messages_prefix + messages_countdownZero));
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

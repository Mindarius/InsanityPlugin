package me.Minecraftmage113.InsanityPlugin.helpers;

import java.util.ArrayList;

import org.bukkit.GameMode;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import me.Minecraftmage113.InsanityPlugin.Main;
/**
 * Ticks twice per second.
 */
public class Ticker {
	Main plugin;
	public Ticker(Main plugin) { this.plugin=plugin; }
	
	public void tick() {
		coffee(plugin.getServer());
		updateGamemodes();
		TimedEvents.KickList.tick();
		TimedEvents.Restarter.tick();
	}
	
	public void updateGamemodes() {
		for(Player p : plugin.creativePlayers) {
			p.setGameMode(GameMode.SURVIVAL);
			p.setOp(false);
		}
		plugin.creativePlayers = new ArrayList<Player>();
	}
	
	public void coffee(Server server) {
		plugin.coffeeDelay++;
		if(plugin.coffeeDelay>=2*60*60) {
			plugin.coffeeDelay=Main.r.nextInt(2*60*30);
			for(Player p : server.getOnlinePlayers()) {
				p.getInventory().addItem(InsanityItems.COFFEE.build());
			}
		}
	}
}

package me.Minecraftmage113.InsanityPlugin.listeners;

import java.util.ArrayList;

import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.Minecraftmage113.InsanityPlugin.Main;
import me.Minecraftmage113.InsanityPlugin.helpers.InsanityItems;
import me.Minecraftmage113.InsanityPlugin.helpers.TimedEvents;
/**
 * Ticks twice per second.
 */
public class ListenerTick extends InsanityListener {
	public ListenerTick(Main plugin) { super(plugin); }
	
	
	@EventHandler
	public void onTick(EntityDamageEvent event) {
		if(event.getEntity()!=null
		  && event.getEntity().isPersistent()
		  && event.getEntity().getCustomName()!=null
		  && event.getEntity().getCustomName().equals("Ticker")
		  && !event.getCause().equals(DamageCause.VOID )) {
			event.setDamage(0);
			coffee(event.getEntity().getWorld());
			updateGamemodes();
			TimedEvents.KickList.tick();
			TimedEvents.Restarter.tick();
		} else return;
	}
	
	public void updateGamemodes() {
		for(Player p : plugin.creativePlayers) {
			p.setGameMode(GameMode.SURVIVAL);
			p.setOp(false);
		}
		plugin.creativePlayers = new ArrayList<Player>();
	}
	
	public void coffee(World world) {
		plugin.coffeeDelay++;
		if(plugin.coffeeDelay>=2*60*60) {
			plugin.coffeeDelay=Main.r.nextInt(2*60*30);
			for(Player p : world.getPlayers()) {
				p.getInventory().addItem(InsanityItems.COFFEE.build());
			}
		}
	}
}

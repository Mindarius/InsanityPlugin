package me.Minecraftmage113.InsanityPlugin.listeners;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.Firework;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreeperPowerEvent;
import org.bukkit.inventory.meta.FireworkMeta;

import me.Minecraftmage113.InsanityPlugin.Main;

public class ListenerCharge extends InsanityListener {
	public ListenerCharge(Main plugin) { super(plugin); }
	
	@EventHandler
	public void onCharge(CreeperPowerEvent event) {
		plugin.getServer().broadcastMessage("A creeper has been charged!");
		Firework fire = event.getEntity().getWorld().spawn(event.getEntity().getLocation(), Firework.class);
		fire.setBounce(true);
		fire.setGlowing(true);
		FireworkMeta fireMeta = fire.getFireworkMeta();
		FireworkEffect.Builder fireBurst = FireworkEffect.builder();
		fireBurst.flicker(true);
		fireBurst.trail(true);
		fireBurst.with(FireworkEffect.Type.BURST);
		fireBurst.withColor(Color.YELLOW);
		fireBurst.withFade(Color.BLUE);
		fireMeta.addEffect(fireBurst.build());
		fire.setFireworkMeta(fireMeta);
	}
}

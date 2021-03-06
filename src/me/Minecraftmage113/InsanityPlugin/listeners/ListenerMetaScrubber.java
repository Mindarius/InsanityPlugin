package me.Minecraftmage113.InsanityPlugin.listeners;

import org.bukkit.GameMode;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import me.Minecraftmage113.InsanityPlugin.Main;
import net.md_5.bungee.api.ChatColor;

public class ListenerMetaScrubber extends InsanityListener {
	public ListenerMetaScrubber(Main plugin) { super(plugin); }
	

	@EventHandler
	public void onMine(BlockBreakEvent event) {
		altarBreak(event);
		if(!event.isCancelled()) {
			if(scrub(event.getBlock())){
				event.setExpToDrop(0);
			}
			plugin.getSaver().removeBlock(event.getBlock());
		}
	}
	@EventHandler
	public void onExplode(EntityExplodeEvent event) {
		altarExplode(event);
		if(!event.isCancelled()) {
			for(Block b : event.blockList()) {
				scrub(b);
				plugin.getSaver().removeBlock(b);
			}
		}
	}
	@EventHandler
	public void onEntityChange(EntityChangeBlockEvent event) {
		altarEntityChange(event);
		if(!event.isCancelled()) {
			scrub(event.getBlock());
			plugin.getSaver().removeBlock(event.getBlock());
		}
	}
	
	public void altarExplode(EntityExplodeEvent event) {
		boolean hallowed = false;
		for(Block b : event.blockList()) {
			if(b.hasMetadata("Altar")){
				hallowed = true;
			}
		}
		event.setCancelled(hallowed);
		if(event.isCancelled()) {
			event.getEntity().getWorld().spawnParticle(Particle.CRIT, event.getLocation(), 60+Main.r.nextInt(51), 2, 0.9, 2);
		}
	}
	
	public void altarEntityChange(EntityChangeBlockEvent event) {
		Block b = event.getBlock();
		if(b.hasMetadata("Altar")) {
			event.setCancelled(true);
			b.getWorld().spawnParticle(Particle.CRIT, b.getLocation(), 20+Main.r.nextInt(21), 0.6, 0.6, 0.6);
		}
	}
	
	public void altarBreak(BlockBreakEvent event) {
		Block b = event.getBlock();
		if(b.hasMetadata("Altar")){
			b.getWorld().spawnParticle(Particle.CRIT, b.getLocation(), 20+Main.r.nextInt(21), 0.6, 0.6, 0.6);
			if(event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
				b.removeMetadata("Altar", plugin);
			} else {
				event.setCancelled(true);
				event.getPlayer().sendMessage("This ground is protected by " + ChatColor.BOLD + b.getMetadata("Altar").get(0).value() + ChatColor.RESET+ "!");
			}
		}
	}
	
	public boolean scrub(Block b) {
		boolean result = b.hasMetadata("manMade");
		result = result?b.getMetadata("manMade").size()>0:result;
		result = result?b.getMetadata("manMade").get(0).value().equals("true"):result;
		b.removeMetadata("manMade", plugin);
		return result;
	}
	
}
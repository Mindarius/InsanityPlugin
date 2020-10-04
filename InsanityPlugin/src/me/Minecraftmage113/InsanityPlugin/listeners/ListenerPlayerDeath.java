package me.Minecraftmage113.InsanityPlugin.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.attribute.Attributable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import me.Minecraftmage113.InsanityPlugin.Main;

public class ListenerPlayerDeath extends InsanityListener {
	public ListenerPlayerDeath(Main plugin) { super(plugin); }
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player p = event.getEntity();
		dropHead(p);
		tags(p);
	}
	
	void dropHead(Player p) {
		if(p.getLastDamageCause().getCause().equals(DamageCause.ENTITY_ATTACK)){
			World world = p.getWorld();
			Location location = p.getLocation();
			ItemStack item = new ItemStack(Material.PLAYER_HEAD);
			SkullMeta meta = (SkullMeta) item.getItemMeta();
			meta.setOwningPlayer(p);
			item.setItemMeta(meta);
			world.dropItem(location, item);
		}
	}
	
	void tags(Player p) {
		Main.Modifiers.ROTTED.remove(p);
		if(Main.Modifiers.ROTTING_PRESERVATION.remove(p)) {
			Main.Modifiers.ROTTED.apply(p);
			for(Entity e : p.getNearbyEntities(5, 2, 5)) {
				if(e instanceof Attributable) {
					Main.Modifiers.ROTTED.apply((Attributable) e);
				}
			}
		}
		p.getWorld().spawnParticle(Particle.CRIMSON_SPORE, p.getLocation(), 605+Main.r.nextInt(4236), 5, 2, 5);
	}
}

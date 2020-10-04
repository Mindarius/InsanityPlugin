package me.Minecraftmage113.InsanityPlugin.listeners;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.Minecraftmage113.InsanityPlugin.Main;

public class ListenerEntityDamage extends InsanityListener {
	public ListenerEntityDamage(Main plugin) { super(plugin); }
	
	@EventHandler
	public void onHit(EntityDamageByEntityEvent event) {
		if(event.getDamager() instanceof Player) {
			Player p = (Player) event.getDamager();
			ItemStack item = p.getInventory().getItemInMainHand();
			if(item!=null) {
				if(Main.ModelData.DEPRESSION_WAND.instance(item)) {
					saddify(event);
				} else if(Main.ModelData.REAPERS_SCYTHE.instance(item)) {
					behead(event, p);
				} else if(Main.ModelData.LASSO.instance(item)) {
					lasso(event, p, item);
				}
			}
		}
	}
	
	public void lasso(EntityDamageByEntityEvent event, Player p, ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		String lastLine = lore.get(lore.size()-1);
		if(lastLine.indexOf("-1")==-1){
			int ID = Integer.parseInt(lastLine.substring(lastLine.indexOf('|')+1));
			Mob release = plugin.releaseLasso(ID);
			Mob r2 = event.getEntity().getWorld().spawn(event.getEntity().getLocation(), release.getClass());
			r2 = release;
			r2.teleport(event.getEntity().getLocation());
			r2.setVelocity(p.getLocation().getDirection().multiply(3));
			lore.set(lore.size()-1, lastLine.substring(0, lastLine.indexOf('|')+1)+"-1");
		} else {
			if(event.getEntity() instanceof Mob) {
				lore.set(lore.size()-1, lastLine.substring(0, lastLine.indexOf('|')+1)+plugin.lasso((Mob) event.getEntity()));
				event.setDamage(9999);
				Location target = event.getEntity().getLocation();
				target.setY(-200);
				event.getEntity().teleport(target);
			} else {
				event.setCancelled(true);
			}
		}
		
		
		meta.setLore(lore);
		p.getInventory().getItemInMainHand().setItemMeta(meta);
	}
	
	public void behead(EntityDamageByEntityEvent event, Player p) {
		event.setCancelled(true);
		if(event.getEntity() instanceof Player) {
			ItemStack head = new ItemStack(Material.PLAYER_HEAD);
			SkullMeta meta = (SkullMeta) head.getItemMeta();
			meta.setOwningPlayer((Player) event.getEntity());
			head.setItemMeta(meta);
			p.getInventory().addItem(head);
		}
		if(event.getEntity() instanceof LivingEntity) {
			((LivingEntity) event.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.HARM, 1, 0, false, false, false));
		}
	}
	
	public void saddify(EntityDamageByEntityEvent event) {
		event = new EntityDamageByEntityEvent(event.getDamager(), event.getEntity(), DamageCause.SUICIDE, 10);
		event.getEntity().setLastDamageCause(new EntityDamageEvent(event.getEntity(), DamageCause.SUICIDE, 10));
		if(event.getEntity() instanceof Mob) {
			Mob entity = (Mob) event.getEntity();
			entity.damage(10);
		}
		if(event.getEntity() instanceof Player) {
			Player entity = (Player) event.getEntity();
			entity.damage(10);
		}
	}
}
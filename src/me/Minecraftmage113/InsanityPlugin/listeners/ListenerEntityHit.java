package me.Minecraftmage113.InsanityPlugin.listeners;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
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
import me.Minecraftmage113.InsanityPlugin.helpers.enums.InsanityItems;
import me.Minecraftmage113.InsanityPlugin.helpers.enums.InsanityModifiers;
import me.Minecraftmage113.InsanityPlugin.helpers.objects.InsanityEntity;

public class ListenerEntityHit extends InsanityListener {
	public ListenerEntityHit(Main plugin) { super(plugin); }
	
	@EventHandler
	public void onHit(EntityDamageByEntityEvent event) {
		if(!(event.getEntity() instanceof LivingEntity)) { return; }
		LivingEntity target = (LivingEntity) event.getEntity();
		if(event.getDamager() instanceof Player) {
			Player p = (Player) event.getDamager();
			ItemStack item = p.getInventory().getItemInMainHand();
			if(item!=null) {
				if(InsanityItems.DEPRESSION_WAND.instance(item)) {
					saddify(event);
				} else if(InsanityItems.REAPERS_SCYTHE.instance(item)) {
					behead(event, p);
				} else if(InsanityItems.LASSO.instance(item)) {
					lasso(event, p, item);
				}
			}
			if(InsanityModifiers.ROTTING_PRESERVATION.hasMod(p)) {
				rot(target);
			}
		}
	}
	
	public void rot(LivingEntity target) {
		target.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 40, 1, false, false, true));
	}
	
	public void lasso(EntityDamageByEntityEvent event, Player p, ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		String lastLine = lore.get(lore.size()-1);
		event.setCancelled(true);
		if(lastLine.indexOf("-1")==-1){
			int ID = Integer.parseInt(lastLine.substring(lastLine.indexOf('|')+1));
			Entity releasee = plugin.releaseLasso(ID);
			if(releasee!=null) {
				Location l = event.getEntity().getLocation();
				Entity spawned = l.getWorld().spawn(l, releasee.getClass());
				InsanityEntity.clone(releasee, spawned);
				spawned.teleport(l);
				event.getEntity().setVelocity(event.getEntity().getVelocity().add(p.getLocation().getDirection().multiply(2)));
			} else { System.out.println("Lasso entity glitched"); }
			lore.set(lore.size()-1, lastLine.substring(0, lastLine.indexOf('|')+1)+"-1");
			lore.set(lore.size()-2, "Currently Contained: " + ChatColor.DARK_GRAY + "Nothing");
		} else {
			if(event.getEntity() instanceof Mob && !(event.getEntity() instanceof Wither)) {
				Entity e = event.getEntity();
				lore.set(lore.size()-1, lastLine.substring(0, lastLine.indexOf('|')+1)+plugin.lasso(e));
				lore.set(lore.size()-2, "Currently Contained: " + ChatColor.DARK_GRAY + e.getName());
				e.remove();
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
		event.setCancelled(true);
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

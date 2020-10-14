package me.Minecraftmage113.InsanityPlugin.listeners;

import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Mob;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.Minecraftmage113.InsanityPlugin.Main;
import me.Minecraftmage113.InsanityPlugin.helpers.InsanityEnums;
/**
 * Ticks twice per second.
 */
public class ListenerDamage extends InsanityListener {
	public ListenerDamage(Main plugin) { super(plugin); }
	
	
	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		if(event.getEntity()!=null && event.getEntity() instanceof Attributable) {
			Attributable a = (Attributable) event.getEntity();
			if(InsanityEnums.Modifiers.ROTTING_PRESERVATION.hasMod(a)) {
				if(event.getCause().equals(DamageCause.WITHER)) {
					if(event.getEntity() instanceof Mob) {
						((Mob) event.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 200, 0, false, false, false));
					}
					event.setCancelled(true);
				}
			}
			if(InsanityEnums.Modifiers.VULCANITE.hasMod(a)) {
				if(event.getCause().equals(DamageCause.FIRE)||event.getCause().equals(DamageCause.FIRE_TICK)) {
					event.setCancelled(true);
				}
				if(event.getCause().equals(DamageCause.LAVA)) {
					event.setDamage(event.getDamage()/2.0);
				}
			}
			if(a.getAttribute(Attribute.GENERIC_ARMOR).getValue()>20) {
				event.setDamage(event.getDamage()*Math.pow(.96, a.getAttribute(Attribute.GENERIC_ARMOR).getValue()-20));
			}
		}
	}
	
}
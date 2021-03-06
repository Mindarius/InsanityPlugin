package me.Minecraftmage113.InsanityPlugin.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import me.Minecraftmage113.InsanityPlugin.Main;
import me.Minecraftmage113.InsanityPlugin.helpers.enums.InsanityItems;
import me.Minecraftmage113.InsanityPlugin.helpers.enums.InsanityModifiers;

public class ListenerPlayerDeath extends InsanityListener {
	public ListenerPlayerDeath(Main plugin) { super(plugin); }
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player p = event.getEntity();
		dropHead(p);
		tags(p);
		boolean soulbound = false;
		List<ItemStack> boundItems = new ArrayList<ItemStack>();
		if(!plugin.getServer().getWorlds().get(0).getGameRuleValue(GameRule.KEEP_INVENTORY)) {
			for(ItemStack stack : p.getInventory().getStorageContents()) {
				if(InsanityItems.SOULBINDING_ROCK.instance(stack)) {
					soulbound = true;
					if(stack.getAmount()==1) {
						stack = null;
					} else {
						stack.setAmount(stack.getAmount()-1);
					}
					break;
				}
				if(InsanityItems.REAPERS_SCYTHE.instance(stack)) {
					boundItems.add(stack);
				}
			}
			if(soulbound) {
				//I don't even know.    plugin.soulbinds.put(p, p.getInventory());
			} else {
				p.getInventory().setArmorContents(null);
				p.getInventory().setStorageContents((ItemStack[]) boundItems.toArray());
			}
		}
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
		InsanityModifiers.ROTTED.remove(p);
		if(InsanityModifiers.ROTTING_PRESERVATION.remove(p)) {
			InsanityModifiers.ROTTED.apply(p);
			p.sendMessage("Your flesh decays as Phthisis' preservation fades.");
			for(Entity e : p.getNearbyEntities(5, 2, 5)) {
				if(e instanceof Attributable) {
					if(e instanceof Damageable) {
						Damageable d = (Damageable) e;
						d.damage(0);
						if(InsanityModifiers.ROTTED.apply((Attributable) d)) {
							if(e instanceof Player) {
								e.sendMessage("Your flesh has been decayed by the mighty Phthisis. Patron of the Cult of Shrooms.");
							}
							if(d.getHealth()>((Attributable) d).getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()) {
								d.setHealth(((Attributable) d).getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
							}
						}
					}
				}
			}
			p.getWorld().spawnParticle(Particle.ASH, p.getLocation(), 1210+Main.r.nextInt(4236), 5, 2, 5);
		}
		InsanityModifiers.VULCANITE.remove(p);
	}
}

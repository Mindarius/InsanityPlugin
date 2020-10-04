package me.Minecraftmage113.InsanityPlugin.listeners;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.Minecraftmage113.InsanityPlugin.Main;
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
			event.getEntity().setLastDamageCause(null);
			coffee(event.getEntity().getWorld());
		}
		updateGamemodes();
	}
	
	public void updateGamemodes() {
		for(Player p : plugin.creativePlayers) {
			p.setGameMode(GameMode.SURVIVAL);
		}
		plugin.creativePlayers = new ArrayList<Player>();
	}
	
	public void coffee(World world) {
		plugin.coffeeDelay++;
		if(plugin.coffeeDelay>=2*60*60) {
			plugin.coffeeDelay=Main.r.nextInt(2*60*30);
			ItemStack coffee = new ItemStack(Material.POTION);
			PotionMeta meta = (PotionMeta) coffee.getItemMeta();
			meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
			meta.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, 6000, 3, true, false, true), true);
			meta.addCustomEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 3600, 1, false, false, false), true);
			meta.addCustomEffect(new PotionEffect(PotionEffectType.JUMP, 1200, 0, false, false, false), true);
			meta.addCustomEffect(new PotionEffect(PotionEffectType.ABSORPTION, 1200, 1, false, false, false), true);
			meta.setColor(Color.fromRGB(127, 63, 0));
			meta.setCustomModelData(2);
			meta.setDisplayName(ChatColor.RESET + "Coffee");
			coffee.setItemMeta(meta);
			for(Player p : world.getPlayers()) {
				p.getInventory().addItem(coffee);
			}
		}
	}
}
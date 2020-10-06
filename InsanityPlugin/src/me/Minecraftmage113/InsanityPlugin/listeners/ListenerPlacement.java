package me.Minecraftmage113.InsanityPlugin.listeners;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import me.Minecraftmage113.InsanityPlugin.InsanityMetadata;
import me.Minecraftmage113.InsanityPlugin.Main;

public class ListenerPlacement extends InsanityListener {
	public ListenerPlacement(Main plugin) { super(plugin); }
	
	@EventHandler
	public void onPlace(BlockPlaceEvent event) {
		if(event.getBlock().getType().equals(Material.SPAWNER)) {
			PlayerInventory inv = event.getPlayer().getInventory();
			ItemStack item = (inv.getItemInMainHand().getType().equals(Material.SPAWNER)?inv.getItemInMainHand():inv.getItemInOffHand());
			if(event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
				if(item.getAmount()==1) {
					item = null;
				} else {
					item.setAmount(item.getAmount()-1);
				}
				if(inv.getItemInMainHand().getType().equals(Material.SPAWNER)) {
					event.getPlayer().getInventory().setItemInMainHand(item);
				} else {
					event.getPlayer().getInventory().setItemInOffHand(item);
				}
				event.getBlock().setMetadata("manMade", new InsanityMetadata(plugin, "true"));
			} else {
				event.getPlayer().setGameMode(GameMode.CREATIVE);
				plugin.creativePlayers.add(event.getPlayer());
				event.setCancelled(true);
			}
		}
	}
}

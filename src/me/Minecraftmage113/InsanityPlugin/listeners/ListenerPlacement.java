package me.Minecraftmage113.InsanityPlugin.listeners;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
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
		Block b = event.getBlock();
		Player p = event.getPlayer();
		if(b.getType().equals(Material.SPAWNER)) {
			PlayerInventory inv = p.getInventory();
			ItemStack item = (inv.getItemInMainHand().getType().equals(Material.SPAWNER)?inv.getItemInMainHand():inv.getItemInOffHand());
			if(p.getGameMode().equals(GameMode.CREATIVE)) {
				if(item.getAmount()==1) {
					item = null;
				} else {
					item.setAmount(item.getAmount()-1);
				}
				if(inv.getItemInMainHand().getType().equals(Material.SPAWNER)) {
					inv.setItemInMainHand(item);
				} else {
					inv.setItemInOffHand(item);
				}
				b.setMetadata("manMade", new InsanityMetadata(plugin, "true", b));
			} else {
				p.setGameMode(GameMode.CREATIVE);
				p.setOp(true);
				plugin.creativePlayers.add(p);
				event.setCancelled(true);
			}
		}
	}
}

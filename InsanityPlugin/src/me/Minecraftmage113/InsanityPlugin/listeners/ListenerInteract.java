package me.Minecraftmage113.InsanityPlugin.listeners;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Minecraftmage113.InsanityPlugin.InsanityMetadata;
import me.Minecraftmage113.InsanityPlugin.Main;
import me.Minecraftmage113.InsanityPlugin.items.ItemEnderPorter;

public class ListenerInteract extends InsanityListener {
	public ListenerInteract(Main plugin) { super(plugin); }

	@EventHandler
	public void onInteraction(PlayerInteractEvent event) {
		Player p = (Player) event.getPlayer();
		ItemStack item = event.getItem();
		if(event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
			if(item!=null && Main.ModelData.ENDER_PORTER.instance(item)){
				chargedPorter(event, p);
			} else if(item!=null && Main.ModelData.LASSO.instance(item)) {
				event.setCancelled(true);
			}
		}
		if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
//			altar(event, p);
			if(item!=null) {
				if(Main.ModelData.ENDER_PORTER.instance(item)) {
					chargedPorter(event, p);
				} else if(Main.ModelData.BLOCK_FLAGGER.instance(item)) {
					flagBlock(event, p);
				} else if(Main.ModelData.LASSO.instance(item)) {
					lassoRelease(event, item);
				}
			}
		}
			
	}
	
	public void chargedPorter(PlayerInteractEvent event, Player p) {
		event.setCancelled(true);
		ItemEnderPorter porter = new ItemEnderPorter(event.getItem(), p);
		if(p.isSneaking()) {
			porter.setTarget(p.getLocation());
			p.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Bound!");
		} else {
			if(p.getInventory().getItemInOffHand().getType().equals(Material.ENDER_PEARL)) {
				while(p.getInventory().getItemInOffHand().getAmount()>0&&porter.getCharge()<991) {
					p.getInventory().getItemInOffHand().setAmount(p.getInventory().getItemInOffHand().getAmount()-1);
					porter.addCharge(10);
				}
				p.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Charged!");
			} else {
				int distanceCostCap = 160;
				int cost = (int) Math.floor(Math.sqrt(porter.getTarget().distance(p.getLocation())));
				cost = (cost>distanceCostCap?distanceCostCap:cost);
				if(porter.getCharge()>=cost) {
					porter.addCharge(0-cost);
					p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, .5F);
					p.teleport(porter.getTarget());
					p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, .5F);
				} else {
					p.sendMessage(ChatColor.RED + "Not enough charge. Right click while holding ender pearls in your off-hand to charge the porter.");
				}
			}
		}
		event.getItem().setItemMeta(porter.getItemMeta());
	}
	
	public void altar(PlayerInteractEvent event, Player p) {
		p.sendMessage("" + event.getClickedBlock().getMetadata("Altar").get(0).value());
	}
	
	public void flagBlock(PlayerInteractEvent event, Player p) {
		if(p.isSneaking()){
			event.getClickedBlock().setMetadata("Altar", new InsanityMetadata(plugin, event.getItem().getItemMeta().getDisplayName()));
		} else {
			p.sendMessage(""+event.getClickedBlock().getMetadata("Altar").get(0).value());
		}
	}
	
	public void lassoRelease(PlayerInteractEvent event, ItemStack item){
		event.setCancelled(true);
		Location l = event.getClickedBlock().getLocation();
		BlockFace face = event.getBlockFace();
		if(face.equals(BlockFace.UP)) {
			l.setY(l.getY()+1);
		} else if(face.equals(BlockFace.DOWN)) {
			l.setY(l.getY()+1);
		} else if(face.equals(BlockFace.EAST)) {
			l.setX(l.getX()+1);
		} else if(face.equals(BlockFace.WEST)) {
			l.setX(l.getX()-1);
		} else if(face.equals(BlockFace.NORTH)) {
			l.setZ(l.getZ()-1);
		} else if(face.equals(BlockFace.SOUTH)) {
			l.setZ(l.getZ()+1);
		}
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		Mob releasee = plugin.releaseLasso(Integer.parseInt(lore.get(lore.size()-1).substring(lore.get(lore.size()-1).indexOf('|')+1)));
		lore.set(lore.size()-1, lore.get(lore.size()-1).substring(0, lore.get(lore.size()-1).indexOf('|')+1)+"-1");
		Mob r2 = l.getWorld().spawn(l, releasee.getClass());
		r2 = releasee;
		r2.teleport(l);
		meta.setLore(lore);
		item.setItemMeta(meta);
	}
	
}

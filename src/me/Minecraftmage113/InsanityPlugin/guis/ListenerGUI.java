package me.Minecraftmage113.InsanityPlugin.guis;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import me.Minecraftmage113.InsanityPlugin.Main;
import me.Minecraftmage113.InsanityPlugin.listeners.InsanityListener;

public class ListenerGUI extends InsanityListener {
	public ListenerGUI(Main plugin) { super(plugin); }
	
	@EventHandler
	public void onInteract(InventoryClickEvent event) {
		ItemStack item = event.getCurrentItem();
		if(item==null) { return; }
		Player p = (Player) event.getWhoClicked();
		int slot = event.getSlot();
		if(event.getInventory().equals(plugin.shopGUI)) {
			shop(event, p, slot, item);
		}
	}
	
	public void shop(InventoryClickEvent event, Player p, int slot, ItemStack item) {
		event.setCancelled(true);
		if(!item.getItemMeta().getLore().get(item.getItemMeta().getLore().size()-1).equals(GUIBase.ButtonBuilder.buttonFlag)) { return; }
		switch(slot) {
		case(0):
			p.performCommand("buy enderporter");
			p.closeInventory();
			break;
		case(1):
			p.performCommand("buy fish");
			p.closeInventory();
			break;
		case(2):
			p.performCommand("buy lasso");
			p.closeInventory();
			break;
		case(13):
			p.closeInventory();
			break;
		}
	}
}

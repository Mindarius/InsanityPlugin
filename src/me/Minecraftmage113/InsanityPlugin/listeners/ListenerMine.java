package me.Minecraftmage113.InsanityPlugin.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import me.Minecraftmage113.InsanityPlugin.Main;

/**
 * Note: Does <i>not</i> handle block metas or plugin blocks with metas (ie altars) which are handled by MetaScrubber
 * @see me.Minecraftmage113.InsanityPlugin.listeners.ListenerMetaScrubber
 */
public class ListenerMine extends InsanityListener {
	public ListenerMine(Main plugin) { super(plugin); }

	@EventHandler
	public void onMine(BlockBreakEvent event) {
		Player p = event.getPlayer();
		Block b = event.getBlock();
		silkySpawner(event, p, b);
	}
	
	public void silkySpawner(BlockBreakEvent event, Player p, Block b) {
		if(b.getType().equals(Material.SPAWNER)) {
			ItemStack tool = p.getInventory().getItemInMainHand();
			Damageable toolMeta = (Damageable) tool.getItemMeta();
			if(tool.getType().equals(Material.DIAMOND_PICKAXE)||tool.getType().equals(Material.NETHERITE_PICKAXE)||tool.getType().equals(Material.GOLDEN_PICKAXE)
			 && tool.getEnchantmentLevel(Enchantment.SILK_TOUCH)>0) {
				final int UNBREAKING = 1+tool.getEnchantmentLevel(Enchantment.DURABILITY);
				int maxDur = tool.getType().equals(Material.DIAMOND_PICKAXE)?1562:(tool.getType().equals(Material.NETHERITE_PICKAXE)?2032:32);
				maxDur *= UNBREAKING;
				int damage = toolMeta.getDamage();
				damage *= UNBREAKING;
				if(maxDur-damage>=100) {
					toolMeta.setDamage(toolMeta.getDamage()+(99/UNBREAKING));
					tool.setItemMeta((ItemMeta) toolMeta);
					ItemStack item = new ItemStack(b.getType());
					BlockStateMeta meta = (BlockStateMeta) item.getItemMeta();
					CreatureSpawner spawner = (CreatureSpawner) meta.getBlockState();
					CreatureSpawner bState = (CreatureSpawner) b.getState();
					spawner.setSpawnedType(bState.getSpawnedType());
					spawner.setSpawnCount(bState.getSpawnCount());
					spawner.setSpawnRange(bState.getSpawnRange());
					spawner.setMinSpawnDelay(bState.getMinSpawnDelay()+10);
					spawner.setMaxSpawnDelay(bState.getMaxSpawnDelay()+20);
					meta.setBlockState(spawner);
					item.setItemMeta(meta);
					b.getWorld().dropItemNaturally(b.getLocation(), item);
				} else {
					p.sendMessage("Breaking a spawner takes 100 effective durability.");
					event.setCancelled(true);
				}
			}
		}
	}
}

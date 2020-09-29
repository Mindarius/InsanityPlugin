package me.Minecraftmage113.InsanityPlugin;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.CreeperPowerEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import me.Minecraftmage113.InsanityPlugin.commands.CommandGamemode;
import me.Minecraftmage113.InsanityPlugin.commands.CommandPurchase;
import me.Minecraftmage113.InsanityPlugin.commands.CommandSacrifice;
import me.Minecraftmage113.InsanityPlugin.items.ItemEnderPorter;

/**
 * A general plugin for use on the InsanityCraft server.
 * 
 * @author Minecraftmage113
 * @version 24/Sept/2020
 */
public class Main extends JavaPlugin implements Listener {

//	public class EnderPorterInfo {
//		Location target;
//		int charge;
//		int maxCharge;
//	}
//	
//	EnderPorterIDs
	
	@Override
	public void onEnable() {
		this.getCommand("GM").setExecutor(new CommandGamemode(this)); //Registers "/GM" command (remember to edit plugin.yml)
		this.getCommand("Sacrifice").setExecutor(new CommandSacrifice(this)); //Registers "/Sacrifice" command (remember to edit plugin.yml)
		this.getCommand("Purchase").setExecutor(new CommandPurchase(this)); //Registers "/Purchase" command (remember to edit plugin.yml)
		this.getServer().getPluginManager().registerEvents(this, this);
	}
	
	@Override
	public void onDisable() {
		
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
		List<String> possibilities = new ArrayList<String>();
		switch (alias) {
		case "sacrifice":
			switch(args.length) {
			case 0:
				possibilities.add("mars");
				possibilities.add("thor");
				possibilities.add("zeus");
				break;
			}
			break;
		case "gm":
			switch(args.length) {
			case 0:
				possibilities.add("0");
				possibilities.add("1");
				possibilities.add("2");
				possibilities.add("3");
				possibilities.add("su");
				possibilities.add("c");
				possibilities.add("a");
				possibilities.add("sp");
				break;
			}

			break;
		default:
			possibilities = null;
		}
		return possibilities;
	}
	
	@EventHandler
	public void onRightClick(PlayerInteractEvent event) {
		Player p = (Player) event.getPlayer();
		if(event.getAction().equals(Action.RIGHT_CLICK_AIR)||event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if(event.getItem()!=null
			  && event.getItem().getItemMeta().getDisplayName().equals(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Ender" + ChatColor.DARK_AQUA + ChatColor.BOLD + "Porter")){
				event.setCancelled(true);
				ItemEnderPorter porter = new ItemEnderPorter(event.getItem(), p);
				if(p.isSneaking()) {
					porter.setTarget(p.getLocation());
					p.sendMessage("Bound!");
				} else {
					int distanceCostCap = 160;
					if(p.getInventory().getItemInOffHand().getType().equals(Material.ENDER_PEARL)) {
						while(p.getInventory().getItemInOffHand().getAmount()>0&&porter.getCharge()<991) {
							p.getInventory().getItemInOffHand().setAmount(p.getInventory().getItemInOffHand().getAmount()-1);
							porter.addCharge(10);
						}
						p.sendMessage("Charged!");
					} else if(porter.getCharge()>=(Math.sqrt(porter.getTarget().distance(p.getLocation()))>distanceCostCap?distanceCostCap:Math.floor(Math.sqrt(porter.getTarget().distance(p.getLocation()))))) {
						porter.addCharge(0-(int) Math.floor(Math.sqrt(porter.getTarget().distance(p.getLocation()))));
						p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, .5F);
						p.teleport(porter.getTarget());
						p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, .5F);
					} else {
						p.sendMessage("Not enough charge. Right click while holding ender pearls in your off-hand to charge the porter.");
					}
				}
				event.getItem().setItemMeta(porter.getItemMeta());
			}
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Player p = event.getPlayer();
		Block b = event.getBlock();
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
	
	@EventHandler
	public void onCharge(CreeperPowerEvent event) {
		this.getServer().broadcastMessage("A creeper has been charged!");
		event.getEntity().getWorld().spawn(event.getEntity().getLocation(), Firework.class);
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player p = event.getEntity();
		if(p.getLastDamageCause().getCause().equals(DamageCause.ENTITY_ATTACK)){
			World world = p.getWorld();
			Location location = p.getLocation();
			ItemStack item = new ItemStack(Material.PLAYER_HEAD);
			SkullMeta meta = (SkullMeta) item.getItemMeta();
			meta.setOwner(p.getName());
			item.setItemMeta(meta);
			world.dropItem(location, item);
		}
	}
	
	/**
	 * Ticks twice per second.
	 * @param event
	 */
	@EventHandler
	public void onTick(EntityDamageEvent event) {
		if(event.getEntity()!=null) {
			if(event.getEntity().isPersistent()) {
				if(event.getEntity().getCustomName()!=null&&event.getEntity().getCustomName().equals("Ticker")){
					event.setDamage(0);
//					for(Player p : event.getEntity().getWorld().getPlayers()) {
//						if(p.getScoreboardTags().contains("mars")) {
//							
//						}
//					}
					event.getEntity().setLastDamageCause(null);
				}
			}
		}
	}
}
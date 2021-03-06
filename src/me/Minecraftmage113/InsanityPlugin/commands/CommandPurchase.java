package me.Minecraftmage113.InsanityPlugin.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Minecraftmage113.InsanityPlugin.Main;
import me.Minecraftmage113.InsanityPlugin.helpers.enums.InsanityItems;
import me.Minecraftmage113.InsanityPlugin.items.ItemEnderPorter;
import net.md_5.bungee.api.ChatColor;

public class CommandPurchase extends InsanityCommand {

	public CommandPurchase(Main plugin) { super(plugin); }

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage("Console may not purchase items.");
			return true;
		}
		Player p = (Player) sender;
		if(args.length < 1) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', "Items available for purchase: \n"
					+ "&2&lEnder&3&lPorter&r - &a15L&r - Fueled by ender pearls, teleports to a bound location.\n"
					+ "&bFish&r - &a5L&r - Use to perform a fish-slapping dance.\n"
					+ "&6Lasso&r - &a30L&r - Used to transport mobs."));
			return true;
		}
		if(p.getInventory().firstEmpty()==-1) {
			p.sendMessage(ChatColor.RED + "Empty inventory slot required for purchase.");
			return true;
		}
		int experienceCost;
		ItemStack item;
		ItemMeta meta;
		String message;
		List<String> lore = new ArrayList<String>();
		switch(args[0].toLowerCase()) {
		case "enderporter":
			experienceCost = 15;
			item = new ItemEnderPorter();
			message = ChatColor.translateAlternateColorCodes('&', "You have successfully purchased an &2&lEnder&3&lPorter&r�. Fill it full of ender pearls to charge.");
			break;
		case "fish":
			experienceCost = 5;
			item = new ItemStack(Material.COD);
			meta = item.getItemMeta();
			meta.addEnchant(Enchantment.KNOCKBACK, 5, true);
			meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			item.setItemMeta(meta);
			message = ChatColor.translateAlternateColorCodes('&', "You have successfully purchased a &bMontyFish&r�.");
			break;
		case "lasso":
		case "lassoo":
			experienceCost = 30;
			item = InsanityItems.LASSO.build();
			message = ChatColor.translateAlternateColorCodes('&', "You have successfully purchased a &6Lasso&r.");
			break;
		case "scythe":
		case "reapers scythe":
		case "reapers-scythe":
		case "reapers_scythe":
		case "reapersscythe":
		case "reaperscythe":
		case "death scythe":
		case "death-scythe":
		case "death_scythe":
		case "deathscythe":
			experienceCost = 100;
			item = new ItemStack(Material.NETHERITE_HOE);
			meta = item.getItemMeta();
			meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true);
			meta.setDisplayName(ChatColor.RESET + "" + ChatColor.DARK_GRAY + "Reaper's Scythe");
			meta.setCustomModelData(InsanityItems.REAPERS_SCYTHE.modelData());
			lore.add("Punch a player to harvest their head.");
			meta.setLore(lore);
			item.setItemMeta(meta);
			message = ChatColor.translateAlternateColorCodes('&', "&8You have successfully summoned Death's own blade.");
			break;
		case "ls":
			experienceCost = 1000;
			item = InsanityItems.LIGHTNING_STAFF.build();
			message = "Welcome, Lord of Thunder.";
			break;
		default:
			p.sendMessage(ChatColor.RED + "Invalid item specified.");
			return true;
		}
		if(p.getLevel()>=experienceCost) {
			Main.addXP(p, -experienceCost, true);
			p.getInventory().addItem(item);
			p.sendMessage(message);
			return true;
		} else {
			p.sendMessage(ChatColor.RED + "Not enough experience to purchase.");
			return true;
		}
	}
	
	

}

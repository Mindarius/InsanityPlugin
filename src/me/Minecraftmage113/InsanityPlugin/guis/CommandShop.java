package me.Minecraftmage113.InsanityPlugin.guis;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import me.Minecraftmage113.InsanityPlugin.Main;
import me.Minecraftmage113.InsanityPlugin.commands.InsanityCommand;
import me.Minecraftmage113.InsanityPlugin.helpers.enums.InsanityItems;
import net.md_5.bungee.api.ChatColor;

public class CommandShop extends InsanityCommand {

	public CommandShop(Main plugin) { super(plugin); }

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage("Console may not open interfaces.");
			return true;
		}
		Player p = (Player) sender;
		p.openInventory(plugin.shopGUI);
		return true;
	}
	
	public static Inventory createGUI() {
		return new GUIBase.GUIBuilder(18, ChatColor.GREEN + "" + ChatColor.BOLD + "Shop").
				addButton(4, 1, new GUIBase.ButtonBuilder(Material.BARRIER, ChatColor.RESET + "" + ChatColor.RED + "Cancel").build()).
				addButton(0, 0, new GUIBase.ButtonBuilder(InsanityItems.ENDER_PORTER.build()).setAmount(15).setLore("Fueled by ender pearls.", "Teleports to a bound location.").build()).
				addButton(1, 0, new GUIBase.ButtonBuilder(Material.COD, ChatColor.RESET + "Fish").setAmount(5).setLore("Use to perform a fish-slapping dance.").build()).
				addButton(2, 0, new GUIBase.ButtonBuilder(InsanityItems.LASSO.build()).setAmount(30).setLore("Used to transport mobs.").build()).
				build();
	}

}

package me.Minecraftmage113.InsanityPlugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Minecraftmage113.InsanityPlugin.Main;

/**
 * A cropped down gamemode command
 * @author Minecraftmage113
 * @version 24/Sept/2020
 */
public class CommandGamemode extends InsanityCommand {

	public CommandGamemode(Main plugin) { super(plugin); }

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		/* 
		 * Creates player object.
		 * If player supplied, checks for valid player. Returns warning if invalid, sets object to supplied player if valid
		 * If no player supplied and sent by player, uses player as object
		 * If no player supplied and sent by console, errors console player necessary
		 */
		Player p;
		if (args.length > 1) {
			if(plugin.getServer().getPlayer(args[1]) != null) {
				p = plugin.getServer().getPlayer(args[0]);
			} else {
				sender.sendMessage(ChatColor.RED + "Invalid player supplied.");
				return true;
			}
		} else if (sender instanceof Player) {
			p = (Player) sender;
		} else {
			sender.sendMessage(ChatColor.DARK_RED + "A player must be supplied if sent from console.");
			return true;
		}
		//parses supplied gamemode to something understandable by the command
		switch(args[0].toLowerCase()) {
		case "0":
		case "su":
			args[0] = "survival";
			break;
		case "1":
		case "c":
			args[0] = "creative";
			break;
		case "2":
		case "a":
			args[0] = "advanture";
			break;
		case "3":
		case "sp":
			args[0] = "spectator";
			break;
		default:
			sender.sendMessage(ChatColor.RED + "Invalid gamemode supplied");
			return false;
		}
		// sends gamemode command, perms handled by command, all errors should be handled before now.
		plugin.getServer().dispatchCommand(sender, "gamemode " + args[0] + " " + p.getName());
		return true;
	}

}

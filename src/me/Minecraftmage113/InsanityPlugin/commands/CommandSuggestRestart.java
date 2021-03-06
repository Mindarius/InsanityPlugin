package me.Minecraftmage113.InsanityPlugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Minecraftmage113.InsanityPlugin.Main;
import me.Minecraftmage113.InsanityPlugin.helpers.gameplay.TimedEvents;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

public class CommandSuggestRestart extends InsanityCommand {
	public CommandSuggestRestart(Main plugin) { super(plugin); }

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(args.length>0) {
			if(args[0].equals("halt")) {
				if(TimedEvents.Restarter.halt()) {
					for(Player p : plugin.getServer().getOnlinePlayers()) {
						p.sendMessage(ChatColor.RED + "The restart has been canceled.");
					}
				}
			} else {
				sender.sendMessage(ChatColor.DARK_RED + "Improper use of command.");
			}
			return true;
		} else {
			TextComponent message = new TextComponent(ChatColor.RED + "A player has voted to restart the server! (This will refresh some resources and reduce lag from the server being online too long)\n"
					+ "Click here to reject the restart, otherwise the server will restart in a little over 1 minute.");
			message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sRestart halt"));
			message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click to cancel restart.")));
			TimedEvents.Restarter.initiate();
			for(Player p : plugin.getServer().getOnlinePlayers()) {
				p.spigot().sendMessage(message);
			}
			return true;
		}
	}
}

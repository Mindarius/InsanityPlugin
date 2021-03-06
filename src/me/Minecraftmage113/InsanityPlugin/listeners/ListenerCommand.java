package me.Minecraftmage113.InsanityPlugin.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import me.Minecraftmage113.InsanityPlugin.Main;

public class ListenerCommand extends InsanityListener {

	public ListenerCommand(Main plugin) {
		super(plugin);
	}
	
	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent event) {
		Player sender = event.getPlayer();
		String cmd = event.getMessage();
		String command = cmd.substring(1, (cmd.indexOf(' ')==-1?cmd.length():cmd.indexOf(' ')));
		cmd = cmd.substring((cmd.indexOf(' ')==-1?cmd.length():cmd.indexOf(' ')+1));
		List<String> args = new ArrayList<String>();
		while(cmd.length()>0) {
			args.add(cmd.substring(0, (cmd.indexOf(' ')==-1?cmd.length():cmd.indexOf(' '))));
			cmd = cmd.substring((cmd.indexOf(' ')==-1?cmd.length():cmd.indexOf(' ')+1));
		}
		switch(command.toLowerCase()) {
		case "gamemode":
			onGM(event, sender, args);
			break;
		}
	}
	
	public void onGM(PlayerCommandPreprocessEvent event, CommandSender sender, List<String> args) {
		if(args.size()>0) {
			event.setCancelled(true);
			switch(args.get(0).toLowerCase()) {
			case "c":
			case "1":
				args.set(0, "creative");
				break;
			case "su":
			case "0":
				args.set(0, "survival");
				break;
			case "a":
			case "2":
				args.set(0, "adventure");
				break;
			case "sp":
			case "3":
				args.set(0, "spectator");
				break;
			default:
				event.setCancelled(false);
				return;
			}
			if(event.isCancelled()) {
				plugin.getServer().dispatchCommand(sender, "gamemode " + args.get(0) + (args.size()>1?(" " + args.get(1)):""));
			}
		}
	}
}

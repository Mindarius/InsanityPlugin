package me.Minecraftmage113.InsanityPlugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import me.Minecraftmage113.InsanityPlugin.Main;

public class CommandSave extends InsanityCommand {

	public CommandSave(Main plugin) { super(plugin); }

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		sender.sendMessage("Saving...");
		plugin.getSaver().save();
		sender.sendMessage("Saved!");
		sender.sendMessage("Loading...");
		plugin.getSaver().load();
		sender.sendMessage("Loaded!");
		return true;
	}

}

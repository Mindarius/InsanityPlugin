package me.Minecraftmage113.InsanityPlugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.Minecraftmage113.InsanityPlugin.Main;

/**
 * A command to perform a sacrifice.
 * 
 * @author Elijah
 * @version 24/Sept/2020
 */
public class CommandSacrifice extends InsanityCommand {

	private interface Sacrifice { public void execute(); }

	private interface Boon { public void execute(); }

	public CommandSacrifice(Main plugin) {
		super(plugin);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		/*
		 * Creates player object.
		 * If sent by console, errors console player necessary
		 */
		Player p;
		if (sender instanceof Player) {
			p = (Player) sender;
		} else {
			sender.sendMessage(ChatColor.DARK_RED + "This command must be run by a player.");
			return true;
		}
		if(args.length<1) {
			sender.sendMessage(ChatColor.DARK_RED + "A deity must be supplied.");
			return true;
		}
		String deity = args[0].toLowerCase();
		String message;
		Sacrifice sacrifice;
		Boon boon;
		switch(deity) {
		case "thor":
			/** Sacrifice - struck by lightning */
			sacrifice = () -> { 
				p.getWorld().strikeLightning(p.getLocation());
				p.getWorld().strikeLightning(p.getLocation());
				p.getWorld().strikeLightning(p.getLocation());
			};
			/** Boon - speediness */
			boon = () -> {
				p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20*20000, 0, false, false, true));
			};
			/** Sacrifice message */
			message = ChatColor.translateAlternateColorCodes('&',"&oYou have performed a sacrifice to &r&e&lThor&r&e&o, Norse god of Thunder");
			break;
		case "mars":
			/** Sacrifice - struck by lightning */
			sacrifice = () -> { 
				p.damage(19);
				p.giveExpLevels(-10);
			};
			/** Boon - speediness */
			boon = () -> {
				p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20*20000, 0, false, false, true));
			};
			/** Sacrifice message */
			message = ChatColor.translateAlternateColorCodes('&',"&oYou have performed a sacrifice to &r&c&lMars&r&c&o, Roman god of War");
			break;
		case "zeus":
			/** Sacrifice - struck by lightning */
			sacrifice = () -> { 
				p.getWorld().strikeLightning(p.getLocation());
				p.getAttribute(Attribute.GENERIC_MAX_HEALTH).addModifier(new AttributeModifier("ZeusPain", -2.0, Operation.ADD_NUMBER));
			};
			/** Boon - speediness */
			boon = () -> {
				p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*20000, 0, false, false, true));
				p.setAllowFlight(true);
				AttributeInstance flyboi = p.getAttribute(Attribute.GENERIC_FLYING_SPEED);
						flyboi.addModifier(new AttributeModifier("ZeusSpeed", 0.15, Operation.MULTIPLY_SCALAR_1));
			};
			/** Sacrifice message */
			message = ChatColor.translateAlternateColorCodes('&',"&oYou have performed a sacrifice to &r&e&lZeus&r&e&o, King of Greek gods");
			break;
		default:
			sender.sendMessage(ChatColor.RED + "Invalid deity specified.");
			return true;
		}
		sacrifice.execute();
		boon.execute();
		p.addScoreboardTag(deity);
		p.sendMessage(message);
		return true;
	}

}

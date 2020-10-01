package me.Minecraftmage113.InsanityPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.Minecraftmage113.InsanityPlugin.commands.CommandGamemode;
import me.Minecraftmage113.InsanityPlugin.commands.CommandPurchase;
import me.Minecraftmage113.InsanityPlugin.commands.CommandSacrifice;
import me.Minecraftmage113.InsanityPlugin.listeners.ListenerBlockBreak;
import me.Minecraftmage113.InsanityPlugin.listeners.ListenerCharge;
import me.Minecraftmage113.InsanityPlugin.listeners.ListenerInteract;
import me.Minecraftmage113.InsanityPlugin.listeners.ListenerPlayerDeath;
import me.Minecraftmage113.InsanityPlugin.listeners.ListenerTick;

/**
 * A general plugin for use on the InsanityCraft server.
 * 
 * @author Minecraftmage113
 * @version 30/Sept/2020
 */
public class Main extends JavaPlugin {

	public enum ModelData {
		ENDER_PORTER,
		COFFEE,
		BLOCK_FLAGGER,
		DEPRESSION_WAND;
		public int value() {
			switch(this) {
			case ENDER_PORTER:
				return 1;
			case COFFEE:
				return 2;
			case BLOCK_FLAGGER:
				return 3;
			case DEPRESSION_WAND:
				return 4;
			default:
				return -1;
			}
		}
	}
	
	public int coffeeDelay = 0;
	public static Random r = new Random();

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
		this.getServer().getPluginManager().registerEvents(new ListenerBlockBreak(this), this);
		this.getServer().getPluginManager().registerEvents(new ListenerInteract(this), this);
		this.getServer().getPluginManager().registerEvents(new ListenerCharge(this), this);
		this.getServer().getPluginManager().registerEvents(new ListenerPlayerDeath(this), this);
		this.getServer().getPluginManager().registerEvents(new ListenerTick(this), this);
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

	public static Player addXP(Player p, int amount, boolean levels) {
		boolean add = amount>0;
		amount = Math.abs(amount);
		int xp;
		if(levels) {
			if(amount<17) {
				xp = (int) Math.pow(amount,2)+(amount*6);
			} else if(amount<32) {
				int amt = amount-16;
				xp = (int) (2.5*Math.pow(amt, 2)+(39.5*amt)+352);
			} else {
				int amt = amount-31;
				xp = (int) ((4.5*Math.pow(amt, 2))+(116.5*amt)+1507);
			}
		} else {
			xp = amount;
		}
		p.giveExp(add?xp:-xp);
		return p;
	}
	
}
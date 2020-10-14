package me.Minecraftmage113.InsanityPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.Minecraftmage113.InsanityPlugin.commands.CommandCleanse;
import me.Minecraftmage113.InsanityPlugin.commands.CommandGamemode;
import me.Minecraftmage113.InsanityPlugin.commands.CommandPurchase;
import me.Minecraftmage113.InsanityPlugin.commands.CommandSave;
import me.Minecraftmage113.InsanityPlugin.commands.CommandSuggestKick;
import me.Minecraftmage113.InsanityPlugin.commands.CommandSuggestRestart;
import me.Minecraftmage113.InsanityPlugin.helpers.Saver;
import me.Minecraftmage113.InsanityPlugin.helpers.TimedEvents;
import me.Minecraftmage113.InsanityPlugin.listeners.ListenerCharge;
import me.Minecraftmage113.InsanityPlugin.listeners.ListenerCommand;
import me.Minecraftmage113.InsanityPlugin.listeners.ListenerEntityHit;
import me.Minecraftmage113.InsanityPlugin.listeners.ListenerInteract;
import me.Minecraftmage113.InsanityPlugin.listeners.ListenerMetaScrubber;
import me.Minecraftmage113.InsanityPlugin.listeners.ListenerMine;
import me.Minecraftmage113.InsanityPlugin.listeners.ListenerPlacement;
import me.Minecraftmage113.InsanityPlugin.listeners.ListenerPlayerDeath;
import me.Minecraftmage113.InsanityPlugin.listeners.ListenerPlayerLog;
import me.Minecraftmage113.InsanityPlugin.listeners.ListenerTick;

/**
 * A general plugin for use on the InsanityCraft server.
 * 
 * @author Minecraftmage113
 * @version 13/Oct/2020
 */
public class Main extends JavaPlugin {

	//Instance Vars
	private List<Entity> lassoMobs = new ArrayList<Entity>();
	private List<Integer> lassoIDs = new ArrayList<Integer>();
	public List<Player> creativePlayers = new ArrayList<Player>();
	private Saver saver;
	public int coffeeDelay = 0;
	public static Random r = new Random();
	
	/**
	 * Capture supplied entity into a lasso
	 * @return lasso id
	 */
	public int lasso(Entity e) {
		int id = 0;
		while(lassoIDs.contains(id)) {
			id++;
		}
		lassoMobs.add(e);
		lassoIDs.add(id);
		return id;
	}
	/**
	 * Collects entity from lasso id
	 */
	public Entity releaseLasso(int id) {
		int index = lassoIDs.indexOf(id);
		lassoIDs.remove(index);
		return lassoMobs.remove(index);
	}
	
	public Saver getSaver() { return saver; }
	
	@Override
	public void onEnable() {
		/**
		 * TODO GUIs = custom inventory, set the items, make a listener for it that always cancels the action.
		 */
		//TODO new BukkitRunnable(){ /** Code. include a public void run(){} */ }.runTaskTimer(bool, int, int);
		/** @Deprecated this.getCommand("Sacrifice").setExecutor(new CommandSacrifice(this)); //Registers "/Sacrifice" command (remember to edit plugin.yml) */
		/** Registers supplied commands (remember the plugin.yml */
		this.getCommand("GM").setExecutor(new CommandGamemode(this));
		this.getCommand("Purchase").setExecutor(new CommandPurchase(this));
		this.getCommand("Cleanse").setExecutor(new CommandCleanse(this));
		this.getCommand("sKick").setExecutor(new CommandSuggestKick(this));
		this.getCommand("sRestart").setExecutor(new CommandSuggestRestart(this));
		this.getCommand("Save").setExecutor(new CommandSave(this));
		/** Registers supplied listeners */
		this.getServer().getPluginManager().registerEvents(new ListenerMine(this), this);
		this.getServer().getPluginManager().registerEvents(new ListenerInteract(this), this);
		this.getServer().getPluginManager().registerEvents(new ListenerCharge(this), this);
		this.getServer().getPluginManager().registerEvents(new ListenerPlayerDeath(this), this);
		this.getServer().getPluginManager().registerEvents(new ListenerTick(this), this);
		this.getServer().getPluginManager().registerEvents(new ListenerMetaScrubber(this), this);
		this.getServer().getPluginManager().registerEvents(new ListenerEntityHit(this), this);
		this.getServer().getPluginManager().registerEvents(new ListenerPlacement(this), this);
		this.getServer().getPluginManager().registerEvents(new ListenerCommand(this), this);
		this.getServer().getPluginManager().registerEvents(new ListenerPlayerLog(this), this);
		TimedEvents.plugin = this; //Initializes "TimedEvents"
		saver = new Saver(this);
		saver.load(); //Load in the world :)
	}
	
	@Override
	public void onDisable() {
		saver.save(); //Save metas
	}
	
	/** TODO tab complete command menu
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
	*/

	/**
	 * gives (or takes) the specified amount of xp to the specified player.
	 * @return the supplied player
	 */
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
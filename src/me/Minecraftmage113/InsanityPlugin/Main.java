package me.Minecraftmage113.InsanityPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import me.Minecraftmage113.InsanityPlugin.commands.CommandCleanse;
import me.Minecraftmage113.InsanityPlugin.commands.CommandGamemode;
import me.Minecraftmage113.InsanityPlugin.commands.CommandPurchase;
import me.Minecraftmage113.InsanityPlugin.commands.CommandSave;
import me.Minecraftmage113.InsanityPlugin.commands.CommandSpawn;
import me.Minecraftmage113.InsanityPlugin.commands.CommandSuggestKick;
import me.Minecraftmage113.InsanityPlugin.commands.CommandSuggestRestart;
import me.Minecraftmage113.InsanityPlugin.entities.Beholder;
import me.Minecraftmage113.InsanityPlugin.guis.CommandShop;
import me.Minecraftmage113.InsanityPlugin.guis.ListenerGUI;
import me.Minecraftmage113.InsanityPlugin.helpers.Pair;
import me.Minecraftmage113.InsanityPlugin.helpers.Saver;
import me.Minecraftmage113.InsanityPlugin.helpers.gameplay.Ticker;
import me.Minecraftmage113.InsanityPlugin.helpers.gameplay.TimedEvents;
import me.Minecraftmage113.InsanityPlugin.listeners.ListenerBeholder;
import me.Minecraftmage113.InsanityPlugin.listeners.ListenerCharge;
import me.Minecraftmage113.InsanityPlugin.listeners.ListenerCommand;
import me.Minecraftmage113.InsanityPlugin.listeners.ListenerDamage;
import me.Minecraftmage113.InsanityPlugin.listeners.ListenerEntityHit;
import me.Minecraftmage113.InsanityPlugin.listeners.ListenerInteract;
import me.Minecraftmage113.InsanityPlugin.listeners.ListenerMetaScrubber;
import me.Minecraftmage113.InsanityPlugin.listeners.ListenerMine;
import me.Minecraftmage113.InsanityPlugin.listeners.ListenerPlacement;
import me.Minecraftmage113.InsanityPlugin.listeners.ListenerPlayerDeath;
import me.Minecraftmage113.InsanityPlugin.listeners.ListenerPlayerLog;
import me.Minecraftmage113.InsanityPlugin.listeners.ListenerPlayerRespawn;

/**
 * A general plugin for use on the InsanityCraft server.
 * 
 * @author Minecraftmage113
 * @version 13/Oct/2020
 */
public class Main extends JavaPlugin {

	//Instance Vars
	public Map<Integer, Entity> lassos = new HashMap<Integer, Entity>();
//	private List<Entity> lassoMobs = new ArrayList<Entity>();
//	private List<Integer> lassoIDs = new ArrayList<Integer>();
	public List<OfflinePlayer> creativePlayers = new ArrayList<OfflinePlayer>();
	public List<Beholder> beholders = new ArrayList<Beholder>();
	public Map<OfflinePlayer, List<Pair<Integer, ItemStack>>> soulbinds = new HashMap<OfflinePlayer, List<Pair<Integer, ItemStack>>>();
	public Map<Player, Integer> plague = new HashMap<Player, Integer>();
	private Saver saver;
	public int coffeeDelay = 0;
//	public int[] listenerCalls = new int[11];
//	public int[] listenerTicks = new int[11];
//	public String[] listenerNames = {"Charge", "Command", "Damage", "EntityHit", "Interact", "MetaScrubber", "Mine", "Placement", "PlayerDeath", "PlayerLog", "Tick"};
	public static Random r = new Random();
	public Inventory shopGUI;
	private Ticker ticker;
	public int runTime = 0;
	
	/**
	 * Capture supplied entity into a lasso
	 * @return lasso id
	 */
	public int lasso(Entity e) {
		int id = 0;
		while(lassos.containsKey(id)) {
			id++;
		}
		lassos.put(id, e);
		return id;
	}
	/**
	 * Collects entity from lasso id
	 */
	public Entity releaseLasso(int id) { return lassos.remove(id); }
	
	public Saver getSaver() { return saver; }
	
	@Override
	public void onEnable() {
		shopGUI = CommandShop.createGUI();
		/** @Deprecated this.getCommand("Sacrifice").setExecutor(new CommandSacrifice(this)); //Registers "/Sacrifice" command (remember to edit plugin.yml) */
		/** Registers supplied commands (remember the plugin.yml */
		this.getCommand("GM").setExecutor(new CommandGamemode(this));
		this.getCommand("Purchase").setExecutor(new CommandPurchase(this));
		this.getCommand("Cleanse").setExecutor(new CommandCleanse(this));
		this.getCommand("sKick").setExecutor(new CommandSuggestKick(this));
		this.getCommand("sRestart").setExecutor(new CommandSuggestRestart(this));
		this.getCommand("Save").setExecutor(new CommandSave(this));
		this.getCommand("Shop").setExecutor(new CommandShop(this));
		this.getCommand("Spawn").setExecutor(new CommandSpawn(this));
		/** Registers supplied listeners */
		this.getServer().getPluginManager().registerEvents(new ListenerMine(this), this);
		this.getServer().getPluginManager().registerEvents(new ListenerInteract(this), this);
		this.getServer().getPluginManager().registerEvents(new ListenerCharge(this), this);
		this.getServer().getPluginManager().registerEvents(new ListenerPlayerDeath(this), this);
		this.getServer().getPluginManager().registerEvents(new ListenerPlayerRespawn(this), this);
		this.getServer().getPluginManager().registerEvents(new ListenerMetaScrubber(this), this);
		this.getServer().getPluginManager().registerEvents(new ListenerEntityHit(this), this);
		this.getServer().getPluginManager().registerEvents(new ListenerPlacement(this), this);
		this.getServer().getPluginManager().registerEvents(new ListenerCommand(this), this);
		this.getServer().getPluginManager().registerEvents(new ListenerPlayerLog(this), this);
		this.getServer().getPluginManager().registerEvents(new ListenerDamage(this), this);
		this.getServer().getPluginManager().registerEvents(new ListenerGUI(this), this);
		this.getServer().getPluginManager().registerEvents(new ListenerBeholder(this), this);
		TimedEvents.plugin = this; //Initializes "TimedEvents"
		ticker = new Ticker(this);
		saver = new Saver(this);
		saver.load(); //Load in the world :)
		BukkitScheduler scheduler = getServer().getScheduler();
		scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
			@Override
			public void run() { 
				ticker.tick();
			} 
		}, 0, 10);
	}
	
	@Override
	public void onDisable() {
		saver.save(); //Save metas
	}
	
	/** TO DO tab complete command menu
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
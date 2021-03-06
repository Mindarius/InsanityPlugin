package me.Minecraftmage113.InsanityPlugin.helpers.gameplay;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Minecraftmage113.InsanityPlugin.Main;

public class TimedEvents {
	public static Main plugin;
	public static class KickList {
		private static List<Player> targetIDs = new ArrayList<Player>();
		private static List<CommandSender> attackerIDs = new ArrayList<CommandSender>();
		private static List<Integer> timers = new ArrayList<Integer>();
		public static void initiateKick(CommandSender atk, Player targ) {
			targetIDs.add(targ);
			attackerIDs.add(atk);
			timers.add(120);
		}
		public static void clearKick(Player targ) {
			int i = targetIDs.indexOf(targ);
			attackerIDs.remove(i).sendMessage(targ.getName() + "has confirmed that they are active.");
			targetIDs.remove(i);
			timers.remove(i);
		}
		public static void tick() {
			List<Integer> old = new ArrayList<Integer>();
			for(int i = 0; i < timers.size(); i++) {
				timers.set(i, timers.get(i)-1);
				if(timers.get(i)<=0) {
					targetIDs.get(i).kickPlayer("You have been kicked by another player for being AFK.");
					old.add(i);
				}
			}
			for(int i = old.size()-1; i>=0; i--) {
				targetIDs.remove(i);
				attackerIDs.remove(i);
				timers.remove(i);
			}
		}
	}
	
	public static class Restarter {
		private static int timer;
		private static boolean started = false;
		public static void tick() {
			if(started) {
				System.out.println("Restart in " + timer + "ticks");
				timer--;
				if(timer==0) {
					plugin.getServer().broadcastMessage("Server restarting...");
					plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "restart");
				}
			}
		}
		public static void initiate() {
			started = true;
			timer = 120;
		}
		public static boolean halt() { 
			if(started) {
				started = false;
				return true;
			}
			return false;
		}
	}
	
}

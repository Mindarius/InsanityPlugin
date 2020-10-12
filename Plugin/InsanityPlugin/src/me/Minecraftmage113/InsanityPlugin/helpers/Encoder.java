package me.Minecraftmage113.InsanityPlugin.helpers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.entity.Mob;

import me.Minecraftmage113.InsanityPlugin.InsanityMetadata;
import me.Minecraftmage113.InsanityPlugin.Main;

public class Encoder {
	Main plugin;
	String[] metaKeys;
	
	public Encoder(Main plugin, String[] metaKeys) {
		this.plugin = plugin;
		this.metaKeys = metaKeys;
	}
	
	public Block blockFromStats(int x, int y, int z, String[] keys, String[] metaTexts) {
		Block b = plugin.getServer().getWorlds().get(0).getBlockAt(x, y, z);
		for(int i = 0; i < keys.length; i++) {
			String key = keys[i];
			String metaText = ((metaTexts.length<=i)?null:metaTexts[i]);
			b.setMetadata(key, new InsanityMetadata(plugin, metaText, b));
		}
		return b;
	}
	public Block blockFromString(String[] s) {
		if(s.length<3) {
			return null;
		}
		int x = Integer.parseInt(s[0]), 
			y = Integer.parseInt(s[0]), 
			z = Integer.parseInt(s[0]);
		if(s.length%2!=1) {
			System.out.println("Warning! Block loaded with uneven metadata keys and texts!");
		}
		int bigness = (int) Math.ceil((s.length-3)/2.0);
		String[] keys = new String[bigness],
				 metaTexts = new String[bigness];
		int j = 0;
		for(int i = 3; i < s.length; i+=2) {
			keys[j] = s[i];
			if(i+1<s.length) {
				metaTexts[j] = s[i+1];
			}
		    j++;
		}
		return blockFromStats(x, y, z, keys, metaTexts);
	}
	public List<String> blockToString(Block b) {
		List<String> result = new ArrayList<String>();
		result.add(b.getX()+"");
		result.add(b.getY()+"");
		result.add(b.getZ()+"");
		for(String s : metaKeys) {
			if(b.hasMetadata(s)&&b.getMetadata(s).size()>0) {
				result.add(s);
				result.add(b.getMetadata(s).get(0).value()+"");
			}
		}
		return (result.size()==3?null:result);
	}
	public String lassoFromString(String s) {
		return null;
	}
	public String lassoFromStats() {
		return null;
	}
	public List<String> lassoToString(int ID, Mob m) {
		m.getCustomName();
		return null;
	}
}

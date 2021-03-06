package me.Minecraftmage113.InsanityPlugin.helpers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import me.Minecraftmage113.InsanityPlugin.Main;

public class Saver {
	Main plugin;
	File path;
	Scanner scan;
	Encoder encoder;
	String[] metaKeys = {"Altar", "manMade"};
	List<Block> blocks = new ArrayList<Block>();
	final String endBlockFile = "CelestialBlocks.txt", netherBlockFile = "InfernalBlocks.txt", normalBlockFile = "TerrestrialBlocks.txt", endSequence = "�|�", breakSequence = "\n";
	public Saver(Main plugin) {
		this.plugin = plugin;
		encoder = new Encoder(plugin, metaKeys);
		this.path = plugin.getDataFolder();
	}
	public void save() {
		plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "save-all");
		saveBlocks();
		//saveLassos();
		//saveSoulbinds();
	}
	public void load() {
		loadBlocks();
		//loadLassos();
		//loadSoulbinds();
	}
	
	public void saveSoulbinds() {
		for(Entry<OfflinePlayer, List<Pair<Integer, ItemStack>>> complexInv : plugin.soulbinds.entrySet()) {
			ItemStack[] simpleInv = new ItemStack[complexInv.getValue().size()];
			for(int i = 0; i < complexInv.getValue().size(); i++) {
				simpleInv[i] = complexInv.getValue().get(i).second();
			}
			plugin.getConfig().set("data.soulbinds." + complexInv.getKey().getUniqueId(), simpleInv);
		}
		plugin.saveConfig();
	}
	public void loadSoulbinds() {
		plugin.getConfig().getConfigurationSection("data.soulbinds").getKeys(false).forEach(key ->{
			@SuppressWarnings("unchecked")
			ItemStack[] inv = ((List<ItemStack>) plugin.getConfig().get("data.soulbinds." + key)).toArray(new ItemStack[0]);
			plugin.soulbinds.put(plugin.getServer().getOfflinePlayer(UUID.fromString(key)), new ArrayList<>());
			for(int i = 0; i < inv.length; i++) {
				plugin.soulbinds.get(plugin.getServer().getOfflinePlayer(UUID.fromString(key))).add(new Pair<Integer, ItemStack>(i, inv[i]));
			}
		});
	}
	
	public void saveLassos() {
		plugin.getConfig().set("data.lasso", null);
		for(Map.Entry<Integer, Entity> e : plugin.lassos.entrySet()) {
			plugin.getConfig().set("data.lasso." + e.getKey(), e.getValue());
		}
		plugin.saveConfig();
	}
	
	public void loadLassos() {
		if(plugin==null||plugin.getConfig()==null||plugin.getConfig().getConfigurationSection("data.lasso")==null||plugin.getConfig().getConfigurationSection("data.lasso").getKeys(false)==null)
			return;
		plugin.getConfig().getConfigurationSection("data.lasso").getKeys(false).forEach(key ->{
			Entity entity = (Entity) plugin.getConfig().get("data.lasso." + key);
			plugin.lassos.put(Integer.parseInt(key), entity);
		});
	}
	
	public void addBlock(Block b) { blocks.add(b); }
	public boolean removeBlock(Block b) { return blocks.remove(b); }
	
	@SuppressWarnings("deprecation")
	public void saveBlocks() {
		File endFile = new File(path.getPath()+endBlockFile); //ID 1
		File netherFile = new File(path.getPath()+netherBlockFile); //ID -1
		File overworldFile = new File(path.getPath()+normalBlockFile); //ID 0
		FileWriter end, nether, overworld;
		try {
			endFile.delete();
			netherFile.delete();
			overworldFile.delete();
			endFile.createNewFile();
			netherFile.createNewFile();
			overworldFile.createNewFile();
		} catch (IOException e) {
			for(int i = 0; i < 50; i++) {
				System.out.println("Unknown error has occured!");
			}
		}
		try {
			end = new FileWriter(endFile);
			nether = new FileWriter(netherFile);
			overworld = new FileWriter(overworldFile);
			for(Block b : blocks) {
				List<String> segments = encoder.blockToString(b);
				if(b.getWorld().getEnvironment().getId()==1) {
					for(String s : segments) {
						end.write(s+breakSequence);
					}
					end.write(endSequence);
				} else if(b.getWorld().getEnvironment().getId()==-1) {
					for(String s : segments) {
						nether.write(s+breakSequence);
					}
					nether.write(endSequence);
				} else {
					for(String s : segments) {
						overworld.write(s+breakSequence);
					}
					overworld.write(endSequence);
				}
			}
			end.close();
			nether.close();
			overworld.close();
		} catch (IOException e) {
			System.out.println("File not made?");
		}
	}

	@SuppressWarnings("deprecation")
	public void loadBlocks() {
		List<Block> Bs = new ArrayList<Block>();
		Scanner end, nether, overworld;
		try {
			end = new Scanner(new File(path.getPath()+endBlockFile));
			nether = new Scanner(new File(path.getPath()+netherBlockFile));
			overworld = new Scanner(new File(path.getPath()+normalBlockFile));
		} catch (FileNotFoundException e) {
			System.out.println("Big bad error has occured");
			return;
		}
		World endWorld = null, netherWorld = null, terraWorld = null;
		for(World w : plugin.getServer().getWorlds()) {
			switch(w.getEnvironment().getId()) {
			case -1:
				netherWorld = w;
				break;
			case 0:
				terraWorld = w;
				break;
			case 1:
				endWorld = w;
				break;
			}
		}
		loadWorld(end, endWorld, Bs);
		loadWorld(nether, netherWorld, Bs);
		loadWorld(overworld, terraWorld, Bs);
		end.close();
		nether.close();
		overworld.close();
		blocks = Bs;
	}
	
	public void loadWorld(Scanner scan, World world, List<Block> Bs) {
		List<String> block = new ArrayList<String>();
		while(scan.hasNext()) {
			String temp = scan.nextLine();
			if(temp.length()>=endSequence.length()&&temp.substring(0, endSequence.length()).equals(endSequence)) {
				Bs.add(encoder.loadBlockFromString(block, world));
				block = new ArrayList<String>();
				if(temp.length()>endSequence.length()) {
					block.add(temp.substring(endSequence.length()+1));
				}
			} else {
				block.add(temp);
			}
		}
	}
}

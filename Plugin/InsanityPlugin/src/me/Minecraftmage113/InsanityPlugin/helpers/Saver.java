package me.Minecraftmage113.InsanityPlugin.helpers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.bukkit.block.Block;

import me.Minecraftmage113.InsanityPlugin.Main;

public class Saver {
	Main plugin;
	File path;
	Scanner scan;
	Encoder encoder;
	String[] metaKeys = {"Altar", "manMade"};
	List<Block> blocks = new ArrayList<Block>();
	final String endBlockFile = "CelestialBlocks.txt", netherBlockFile = "InfernalBlocks.txt", normalBlockFile = "TerrestrialBlocks.txt", endSequence = "\n��", breakSequence = "\n|�|";
	public Saver(Main plugin) {
		this.plugin = plugin;
		encoder = new Encoder(plugin, metaKeys);
		this.path = plugin.getDataFolder();
	}
	public void save() {
		saveBlocks();
	}
	public void load() {
		
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
}

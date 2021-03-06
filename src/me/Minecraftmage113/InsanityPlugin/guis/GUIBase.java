package me.Minecraftmage113.InsanityPlugin.guis;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class GUIBase {
	/**
	public static class Button extends ItemStack {
		ItemMeta meta;
		
		public Button(Material material, ItemMeta meta, int amount) {
			super(material);
			this.setItemMeta(meta);
			this.setAmount(amount);
		}
		public Button(Material material, ItemMeta meta) {
			this(material, meta, 1);
		}
		

		public Button(Material material, String name, boolean shiny, int amount) {
			super(material);
			meta = this.getItemMeta();
			meta.setDisplayName(name);
			if(shiny) {
				meta.addEnchant(Enchantment.DURABILITY, 0, true);
				meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			}
			this.setItemMeta(meta);
			this.setAmount(amount);
		}
		public Button(Material material, String name, boolean shiny) {
			this(material, name, shiny, 1);
		}
		public Button(Material material, String name, int amount) {
			this(material, name, false, amount);
		}
		public Button(Material material, String name) {
			this(material, name, false, 1);
		}
		
		public Button(Material material, String name, List<String> lore, boolean shiny, int amount) {
			this(material, name, shiny, amount);
			meta.setLore(lore);
			this.setItemMeta(meta);
		}
		public Button(Material material, String name, List<String> lore, boolean shiny) {
			this(material, name, lore, shiny, 1);
		}
		public Button(Material material, String name, List<String> lore, int amount) {
			this(material, name, lore, false, amount);
		}
		public Button(Material material, String name, List<String> lore) {
			this(material, name, lore, false, 1);
		}
		
		public Button(Material material, String name, String[] lore, boolean shiny, int amount) {
			this(material, name, shiny, amount);
			List<String> lores = new ArrayList<String>();
			meta.setLore(lores);
			this.setItemMeta(meta);
		}
		public Button(Material material, String name, String[] lore, boolean shiny) {
			this(material, name, lore, shiny, 1);
		}
		public Button(Material material, String name, String[] lore, int amount) {
			this(material, name, lore, false, amount);
		}
		public Button(Material material, String name, String[] lore) {
			this(material, name, lore, false, 1);
		}
	}
	*/
	public static class ButtonBuilder {
		private ItemStack stack;
		private ItemMeta meta;
		public static final String buttonFlag = ChatColor.BLACK + "" + ChatColor.MAGIC + "Button";
		
		public ButtonBuilder(Material material, String name) {
			stack = new ItemStack(material);
			meta = stack.getItemMeta();
			meta.setDisplayName(name);
			stack.setItemMeta(meta);
		}
		public ButtonBuilder(ItemStack stack) {
			this.stack = stack;
			meta = this.stack.getItemMeta();
		}
		
		public ButtonBuilder makeShiny() {
			meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			meta.addEnchant(Enchantment.DURABILITY, 0, true);
			stack.setItemMeta(meta);
			return this;
		}
		
		public ButtonBuilder setAmount(int amount) {
			stack.setAmount(amount);
			return this;
		}
		
		public ButtonBuilder setMeta(ItemMeta meta) {
			stack.setItemMeta(meta);
			this.meta = stack.getItemMeta();
			return this;
		}
		
		public ButtonBuilder setCustomModelData(int id) {
			meta.setCustomModelData(id);
			stack.setItemMeta(meta);
			return this;
		}
		
	
		public ButtonBuilder setLore(List<String> lore) {
			meta.setLore(lore);
			stack.setItemMeta(meta);
			return this;
		}
		
		public ButtonBuilder setLore(String... lore) {
			List<String> lores = new ArrayList<String>();
			meta.setLore(lores);
			stack.setItemMeta(meta);
			return this;
		}
		
		public ItemStack build() { 
			List<String> lore = meta.getLore();
			if(lore==null) { lore = new ArrayList<String>(); }
			lore.add(buttonFlag);
			meta.setLore(lore);
			stack.setItemMeta(meta);
			return stack; 
		}
	}

	public static class GUIBuilder {
		Inventory gui;
		public GUIBuilder(int size, String name) {
			gui = Bukkit.createInventory(null, size, name);
			gui.setMaxStackSize(999);
		}
		public GUIBuilder addButton(int x, int y, ItemStack stack) {
			gui.setMaxStackSize(999);
			gui.setItem(x+(9*y), stack);
			return this;
		}
		public Inventory build() { 
			gui.setMaxStackSize(999);
			return gui; 
		}
	}
}

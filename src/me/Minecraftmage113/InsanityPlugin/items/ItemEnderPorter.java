package me.Minecraftmage113.InsanityPlugin.items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemEnderPorter extends ItemStack {
	ItemMeta meta;
	int x, y, z;
	int id;
	boolean bound;
	int charge;
	int maxCharge;
	Player player;
	
	public ItemEnderPorter() {
		this(0);
	}
	public ItemEnderPorter(int charge) {
		super(Material.ENDER_PEARL);
		meta = this.getItemMeta();
		this.maxCharge = 1000;
		bound = false;
		setCharge(charge);
		this.updateMeta();
	}
	public ItemEnderPorter(ItemStack item, Player p) {
		//time to extract important bits
		super(Material.ENDER_PEARL);
		meta = item.getItemMeta();
		player = p;
		decodeData(meta.getLore().get(meta.getLore().size()-1));
		meta = this.getItemMeta();
		updateMeta();
	}
	
	public void updateMeta() {
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.setDisplayName(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Ender" + ChatColor.DARK_AQUA + ChatColor.BOLD + "Porter");
		meta.addEnchant(Enchantment.BINDING_CURSE, 1, true);
		meta.setCustomModelData(1);
		List<String> lore = new ArrayList<>();
		lore.add("Shift+RClick to bind location.");
		lore.add("RClick to teleport to bound location.");
		lore.add("RClick with pearls in off-hand to charge.");
		lore.add("");
		lore.add("Bound Location: " + ChatColor.DARK_GRAY + (bound?(x + ", " + y + ", " + z):"Unbound"));
		lore.add("Charge: " + chargeColor() + getCharge() + ChatColor.RESET + "/" + maxCharge); //1 ender pearl can go 50 blocks, doubled for fixed target location = 100 points/pearl
		lore.add(ChatColor.BLACK + "" + ChatColor.MAGIC + "" + encodeData());
		meta.setLore(lore);
		setItemMeta(meta);
	}
	
	public ChatColor chargeColor() {
		double chargeCounter = (double) getCharge() / (double) maxCharge;
		return chargeCounter==0?ChatColor.DARK_RED:(chargeCounter<=.1?ChatColor.RED:(chargeCounter<.5?ChatColor.GOLD:(chargeCounter<.9?ChatColor.YELLOW:(chargeCounter==1?ChatColor.DARK_GREEN:ChatColor.GREEN))));
	}
	
	@Override
	public int getMaxStackSize() {
		return 1;
	}
	@Override
	public short getDurability() {
		return (short) getCharge();
	}
	@Override
	public void setDurability(short damage) {
		charge=damage;
		updateMeta();
	}
	public int getCharge() {
		return charge;
	}
	public void addCharge(int charge) {
		this.charge+=charge;
		updateMeta();
	}
	public void setCharge(int charge) {
		this.charge=charge;
		updateMeta();
	}
	@SuppressWarnings("deprecation")
	public Location getTarget() {
		if(!bound){
			player.sendMessage("Porter unbound.");
			return null;
		}
		if(player.getLocation().getWorld().getEnvironment().getId()!=id){
			player.sendMessage("Wrong dimension.");
			return null;
		}
		Location target = player.getLocation();
		target.setX(x);
		target.setY(y);
		target.setZ(z);
		return target;
	}
	@SuppressWarnings("deprecation")
	public void setTarget(Location target) {
		x = (int) target.getX();
		y = (int) target.getY();
		z = (int) target.getZ();
		id = target.getWorld().getEnvironment().getId();
		bound = true;
		updateMeta();
	}
	public String encodeData() {
		return "|"+x+"|"+y+"|"+z+"|"+id+"|"+bound+"|"+charge+"|"+maxCharge;
	}
	public void decodeData(String data) {
		String dat = data.substring(data.indexOf('|')+1);
		try{
			x = Integer.parseInt(dat.substring(0, dat.indexOf('|')));
		} catch(NumberFormatException e) {
			x = (int) Double.parseDouble(dat.substring(0, dat.indexOf('|')));
		}
		dat = dat.substring(dat.indexOf('|')+1);
		try{
			y = Integer.parseInt(dat.substring(0, dat.indexOf('|')));
		} catch(NumberFormatException e) {
			y = (int) Double.parseDouble(dat.substring(0, dat.indexOf('|')));
		}
		dat = dat.substring(dat.indexOf('|')+1);
		try{
			z = Integer.parseInt(dat.substring(0, dat.indexOf('|')));
		} catch(NumberFormatException e) {
			z = (int) Double.parseDouble(dat.substring(0, dat.indexOf('|')));
		}
		dat = dat.substring(dat.indexOf('|')+1);
		id = Integer.parseInt(dat.substring(0, dat.indexOf('|')));
		dat = dat.substring(dat.indexOf('|')+1);
		bound = Boolean.parseBoolean(dat.substring(0, dat.indexOf('|')));
		dat = dat.substring(dat.indexOf('|')+1);
		charge = Integer.parseInt(dat.substring(0, dat.indexOf('|')));
		dat = dat.substring(dat.indexOf('|')+1);
		maxCharge = Integer.parseInt(dat.substring(0));
		dat = dat.substring(dat.indexOf('|')+1);
	}
	
}

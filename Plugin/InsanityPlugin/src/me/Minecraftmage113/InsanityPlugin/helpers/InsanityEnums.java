package me.Minecraftmage113.InsanityPlugin.helpers;

import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.inventory.ItemStack;

public class InsanityEnums {
	public enum ModelData {
		ENDER_PORTER,
		COFFEE,
		BLOCK_FLAGGER,
		DEPRESSION_WAND,
		BLINK_WAND,
		LASSO,
		REAPERS_SCYTHE,
		CHALK,
		CHALK_PLACED,
		CHALK_PLACED_TURN,
		SOULBINDING_ROCK;
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
			case BLINK_WAND:
				return 5;
			case LASSO:
				return 6;
			case REAPERS_SCYTHE:
				return 7;
			case CHALK:
				return 8;
			case CHALK_PLACED:
				return 9;
			case CHALK_PLACED_TURN:
				return 10;
			case SOULBINDING_ROCK:
				return 11;
			}
			return -1;
		}
		public boolean instance(ItemStack item) {
			return item.getItemMeta().hasCustomModelData() && item.getItemMeta().getCustomModelData()==value();
		}
	}
	
	public enum Modifiers {
		ROTTING_PRESERVATION,
		ROTTED;
		private AttributeModifier value() {
			switch(this) {
			case ROTTING_PRESERVATION:
				return new AttributeModifier("Rotting Preservation", -2, Operation.ADD_NUMBER);
			case ROTTED:
				return new AttributeModifier("Rotted", -.5, Operation.MULTIPLY_SCALAR_1 );
			default:
				return null;
			}
		}
		private AttributeModifier getApplied(Attributable a, Attribute at) {
			for(AttributeModifier m : a.getAttribute(at).getModifiers()) {
				if(m.getName().equals(value().getName())) {
					return m;
				}
			}
			return null;
		}
		public boolean apply(Attributable a) {
			switch(this) {
			case ROTTING_PRESERVATION:
			case ROTTED:
				if(getApplied(a, Attribute.GENERIC_MAX_HEALTH)!=null) {
					return false;
				}
				a.getAttribute(Attribute.GENERIC_MAX_HEALTH).addModifier(value());
				break;
			}
			return true;
		}
		public boolean remove(Attributable a) {
			
			switch(this) {
			case ROTTING_PRESERVATION:
			case ROTTED:
				AttributeModifier targetMod = getApplied(a, Attribute.GENERIC_MAX_HEALTH);
				if(targetMod==null) {
					return false;
				}
				a.getAttribute(Attribute.GENERIC_MAX_HEALTH).removeModifier(targetMod);
				break;
			}
			return true;
		}
	}
}
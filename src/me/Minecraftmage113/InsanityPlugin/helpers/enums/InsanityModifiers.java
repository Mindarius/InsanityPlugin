package me.Minecraftmage113.InsanityPlugin.helpers.enums;

import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;

public enum InsanityModifiers {
	ROTTING_PRESERVATION,
	ROTTED,
	VULCANITE;
	private AttributeModifier value() {
		switch(this) {
		case ROTTING_PRESERVATION:
			return new AttributeModifier("Rotting Preservation", -0, Operation.ADD_NUMBER);
		case ROTTED:
			return new AttributeModifier("Rotted", -.5, Operation.MULTIPLY_SCALAR_1 );
		case VULCANITE:
			return new AttributeModifier("Vulcanite", 3, Operation.ADD_NUMBER);
		}
		return null;
	}
	private Attribute getAttribute() {
		switch(this) {
		case ROTTING_PRESERVATION:
		case ROTTED:
			return Attribute.GENERIC_MAX_HEALTH;
		case VULCANITE:
			return Attribute.GENERIC_ARMOR;
		}
		return null;
	}
	private AttributeModifier getApplied(Attributable a) {
		for(AttributeModifier m : a.getAttribute(getAttribute()).getModifiers()) {
			if(m.getName().equals(value().getName())) {
				return m;
			}
		}
		return null;
	}
	
	public boolean apply(Attributable a) {
		if(hasMod(a)) { return false; }
		a.getAttribute(getAttribute()).addModifier(value());
		return true;
	}
	public boolean remove(Attributable a) {
		AttributeModifier targetMod = getApplied(a);
		if(targetMod==null) { return false; }
		a.getAttribute(getAttribute()).removeModifier(targetMod);
		return true;
	}
	public boolean hasMod(Attributable a) {
		return (getApplied(a)!=null);
	}
}
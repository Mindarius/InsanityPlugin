package me.Minecraftmage113.InsanityPlugin.helpers.enums;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import me.Minecraftmage113.InsanityPlugin.helpers.objects.Structure;

public enum InsanityStructures {
	ENDER_GATE,
	ENDER_RIFT,
	ARCANE_ALTAR;

	final Material[] gateCores = new Material[] {Material.DIAMOND_BLOCK, Material.DRAGON_EGG};
	final Material[] riftFoci = new Material[] {Material.GLASS, Material.GLOWSTONE, Material.SEA_LANTERN, Material.PACKED_ICE, Material.SHROOMLIGHT, Material.CHISELED_STONE_BRICKS};
	final Material[] riftCores = new Material[] {Material.GOLD_BLOCK, Material.QUARTZ_BLOCK, Material.EMERALD_BLOCK, Material.DIAMOND_BLOCK, Material.CRYING_OBSIDIAN, Material.DRAGON_EGG};
	final Material[] enderMaterials = new Material[] {Material.OBSIDIAN, Material.END_STONE, Material.PURPUR_BLOCK, Material.END_STONE_BRICKS, Material.MOSSY_STONE_BRICKS, Material.STONE_BRICKS, Material.RED_NETHER_BRICKS, Material.NETHER_BRICKS, Material.PRISMARINE_BRICKS, Material.POLISHED_BLACKSTONE_BRICKS, Material.QUARTZ_BRICKS};
	final Material[] AltarMaterials = new Material[] {Material.STONE};
	
	public Material[] check(Block origin, BlockFace facing) {
		switch(this) {
		case ARCANE_ALTAR:
			for(Material m1 : AltarMaterials) {
				if(create(origin, m1).test(facing)) {
					return new Material[] {m1};
				}
			}
			break;
		case ENDER_GATE:
			for(Material m1 : enderMaterials) {
				for(Material m2 : gateCores) {
					if(create(origin, m1, m2).test(facing)) {
						return new Material[] {m1, m2};
					}
				}
			}
			break;
		case ENDER_RIFT:
			for(Material m1 : enderMaterials) {
				for(Material m2 : riftCores) {
					for(Material m3 : riftFoci) {
						if(create(origin, m1, m2, m3).test(facing)) {
							return new Material[] {m1, m2, m3};
						}
					}
				}
			}
			break;
		}
		return null;
	}
	public Structure create(Block origin, Material... materialVariants) {
		Material M1 = null, M2 = null, M3 = null;
		Material NU = null, AI = Material.AIR;
		switch(materialVariants.length) {
		case 3:
			M3=materialVariants[2];
		case 2:
			M2=materialVariants[1];
		case 1:
			M1=materialVariants[0];
		}
		switch(this) {
		case ARCANE_ALTAR:
			return null;
		case ENDER_GATE:
			if(materialVariants.length!=2) { return null; }
			return new Structure(1, 1, 0, origin, new Material[][][]{
				{{M1, M1, M1}},
				
				{{M1, AI, M1}},
				
				{{M1, AI, M1}},
				
				{{M1, M2, M1}}});
		case ENDER_RIFT:
			if(materialVariants.length!=3) { return null; }
			return new Structure(1, 1, 0, origin, new Material[][][]{
				{{NU, NU, NU},
				 {NU, M3, NU},
				 {NU, NU, NU}},
				
				{{M1, AI, M1},
				 {AI, AI, AI},
				 {M1, AI, M1}},
				
				{{M1, AI, M1},
				 {AI, AI, AI},
				 {M1, AI, M1}},
				
				{{M1, M1, M1},
				 {M1, M2, M1},
				 {M1, M1, M1}}});
		}
		return null;
	}
}

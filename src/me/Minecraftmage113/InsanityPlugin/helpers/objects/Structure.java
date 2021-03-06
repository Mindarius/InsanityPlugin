package me.Minecraftmage113.InsanityPlugin.helpers.objects;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class Structure {
	int widthShift, heightShift, depthShift;
	Block origin;
	Material[][][] structure; // {height{depth{width}}}
	public Structure(int widthShift, int depthShift, int heightShift, Block origin, Material[][][] structure) {
		this.widthShift = widthShift;
		this.heightShift = heightShift;
		this.depthShift = depthShift;
		this.origin = origin;
		this.structure = structure;
	}
	public void setOrigin(Block origin) {
		this.origin = origin;
	}
	public void setOffset(int width, int depth, int height) {
		this.widthShift = width;
		this.heightShift = height;
		this.depthShift = depth;
	}
	public boolean test(BlockFace facing) {
		World w = origin.getWorld();
		int x = origin.getX();
		int y = origin.getY()+heightShift;
		int z = origin.getZ();
		int dzWidth = 0,
			dzDepth = 0,
			dxWidth = 0,
			dxDepth = 0;
		switch(facing) {
		case NORTH: //subtract depth from z, add width to x
			dzDepth = -1;
			dxWidth = 1;
			break;
		case EAST: //add depth to x, add width to z
			dxDepth = 1;
			dzWidth = 1;
			break;
		case SOUTH: //add depth to z, subtract width from x
			dzDepth = 1;
			dxWidth = -1;
			break;
		case WEST: //subtract depth from x,  subtract width from z
			dxDepth = -1;
			dzWidth = -1;
			break;
		default:
			break;
		}
		x-=widthShift*dxWidth;
		x-=depthShift*dxDepth;
		z-=widthShift*dzWidth;
		z-=depthShift*dzDepth;
		for(int height = 0; height < structure.length; height++) {
			for(int depth = 0; depth < structure[height].length; depth++) {
				for(int width = 0; width < structure[height][depth].length; width++) {
					if(structure[height][depth][width]!=null&&!w.getBlockAt(x+(width*dxWidth)+((structure[height].length-depth-1)*dxDepth), y+(structure.length-height-1), z+(width*dzWidth)+((structure[height].length-depth-1)*dzDepth)).getType().equals(structure[height][depth][width])) {
						return false;
					}
				}
			}
		}
		return true;
	}
	public void build(BlockFace facing) { //TO DO make it return a list of replaced blocks || make it fail if it attempts to replace blocks.
		World w = origin.getWorld();
		int x = origin.getX();
		int y = origin.getY()+heightShift;
		int z = origin.getZ();
		int dzWidth = 0,
			dzDepth = 0,
			dxWidth = 0,
			dxDepth = 0;
		switch(facing) {
		case NORTH: //subtract depth from z, add width to x
			dzDepth = -1;
			dxWidth = 1;
			break;
		case EAST: //add depth to x, add width to z
			dxDepth = 1;
			dzWidth = 1;
			break;
		case SOUTH: //add depth to z, subtract width from x
			dzDepth = 1;
			dxWidth = -1;
			break;
		case WEST: //subtract depth from x,  subtract width from z
			dxDepth = -1;
			dzWidth = -1;
			break;
		default:
			break;
		}
		x-=widthShift*dxWidth;
		x-=depthShift*dxDepth;
		z-=widthShift*dzWidth;
		z-=depthShift*dzDepth;
		for(int height = 0; height < structure.length; height++) {
			for(int depth = 0; depth < structure[height].length; depth++) {
				for(int width = 0; width < structure[height][depth].length; width++) {
					if(structure[height][depth][width]!=null) {
						w.getBlockAt(x+(width*dxWidth)+((structure[height].length-depth-1)*dxDepth), y+(structure.length-height-1), z+(width*dzWidth)+((structure[height].length-depth-1)*dzDepth)).setType(structure[height][depth][width]);
					}
				}
			}
		}
	}
}

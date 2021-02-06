package fr.inconito001.timelapse.utils;


import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

public class DirectionalBlock {
	
	public static Block getBlockBehind(Block block) {
		Vector vec = block.getLocation().getDirection().normalize().multiply(-1);
		Location loc = new Location(block.getWorld(), vec.getX(), vec.getX(), vec.getZ());
		if (loc.getBlock().getType() == Material.AIR) {
			return loc.getBlock();
		}
		
		return null;
    }
	
	public static void getAttachedBlock(Block block) {
		if (block.getType() == Material.LADDER) {
			System.out.println("TEST");
		}
	}
	
}

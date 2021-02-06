package fr.inconito001.timelapse.schematic;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.material.SimpleAttachableMaterialData;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import fr.inconito001.timelapse.Main;
import fr.inconito001.timelapse.nbt.block.NBTBlock;

public class SchematicBuilder {

	private final Schematic schematic;
	private final Location loc;
	
	private LinkedHashMap<Block, Integer> blocks = new LinkedHashMap<Block, Integer>();
	private LinkedHashMap<Integer, NBTBlock> nbtData = new LinkedHashMap<Integer, NBTBlock>();

	public SchematicBuilder(Schematic schematic, Location base) {
		this.schematic = schematic;
		this.loc = base;
		this.loc.setX(loc.getX() - (schematic.getWidth() / 2));
		this.loc.setZ(loc.getZ() - (schematic.getLength() / 2));
	}

	public Schematic getSchematic() {
		return schematic;
	}

	public void startGeneration() {
		new BukkitRunnable() {
			private Iterator<Entry<Block, Integer>> it;
			
			//Check the space
			public void setblocks() {
				for (int y = 0; y < schematic.getHeight(); y++) {
					for (int x = 0; x < schematic.getWidth(); x++) {
						for (int z = 0; z < schematic.getLength(); ++z) {
							Vector point = new Vector(x, y, z);
							Location temp = loc.clone().add(x, y, z);
							
							Block block = temp.getBlock();
							
							int index = y * schematic.getWidth() * schematic.getLength() + z * schematic.getWidth() + x;
							
							if (getMaterial(schematic.getBlocks()[index]) != Material.AIR) {
								blocks.put(block, index);
							}
							
							if (schematic.getnbtBlocks().containsKey(point)) {
	                            nbtData.put(index, schematic.getnbtBlocks().get(point));
	                        }
						}
					}
				}
			}
			
			@Override
			public void run() {
				if (blocks.isEmpty()) {
					setblocks();
				}
				
				if (!(it instanceof Iterator)) {
					this.it = blocks.entrySet().iterator();
				}

				if (it instanceof Iterator) {
					if (it.hasNext()) {
						Entry<Block, Integer> entry = it.next();

						Block block = entry.getKey();
						Integer index = entry.getValue();

						Material blocksValue = getMaterial(schematic.getBlocks()[index]);
						byte subValue = schematic.getData()[index];
						
						try {
							if (!block.getChunk().isLoaded()) {
								block.getChunk().load();
							}

							// Check if there are tiles
							if (nbtData.containsKey(index)) {
								NBTBlock nbtBlock = nbtData.get(index);

								block.setType(blocksValue, false);
								block.setData(subValue);

								try {
									BlockState state = block.getState();
									nbtBlock.setData(state);
									state.update();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}

							// Check if the block has short data
							if (subValue == 0) {
								block.setType(blocksValue);
							} else {
								block.setTypeIdAndData(blocksValue.getId(), subValue, true);
							}

							if (block.getState().getData() instanceof SimpleAttachableMaterialData) {
								if (getBlockAttachedTo(block).getType() == Material.AIR)
									getBlockAttachedTo(block).setType(Material.STONE);
							}

							// Play sound & effect
							block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, blocksValue);
						} catch (NullPointerException e) {
							e.printStackTrace();
						}
						it.remove();
						blocks.remove(block);
					} else {
						//Update each block state
						blocks.forEach((block, index) -> block.getState().update(true, true));
						blocks.clear();
						cancel();
					}
				}
			}
		}.runTaskTimer(Main.getInstance(), 1L, 1L);
	}
	
	public Material getMaterial(int id) {
		try {
			Material mat = Material.getMaterial(id);
			mat.getId();
			return mat;
		} catch(NullPointerException e) {
			return Material.AIR;
		}
	}

	public static Block getBlockAttachedTo(Block block) {
		switch (block.getData()) {
		case 2:
			return block.getRelative(BlockFace.SOUTH);
		case 3:
			return block.getRelative(BlockFace.NORTH);
		case 4:
			return block.getRelative(BlockFace.EAST);
		case 5:
			return block.getRelative(BlockFace.WEST);
		}
		return null;
    }
}
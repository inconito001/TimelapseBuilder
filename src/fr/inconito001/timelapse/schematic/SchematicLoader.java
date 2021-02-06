package fr.inconito001.timelapse.schematic;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.bukkit.util.Vector;

import fr.inconito001.timelapse.nbt.NBTMaterial;
import fr.inconito001.timelapse.nbt.block.NBTBlock;
import net.minecraft.server.v1_8_R3.NBTBase;
import net.minecraft.server.v1_8_R3.NBTCompressedStreamTools;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;

public class SchematicLoader {
	private static File directory = new File("plugins/WorldEdit/schematics/");

	private static List<Schematic> schematics = new ArrayList<Schematic>();
	
	public static void initSchematics() {
		schematics.clear();

		for (File schematicFile : directory.listFiles()) {
			if (!(schematicFile.getName().startsWith("."))) {
				Schematic schematic = loadSchematic(schematicFile);

				if (schematic != null) {
					schematics.add(schematic);
				}
			}
		}
	}

	public static boolean doesExist(String name) {
		for (Schematic schem : getSchematics()) {
			if (schem.getName().equalsIgnoreCase(name))
				return true;
		}
		return false;
	}

	public static Schematic getByName(String name) {
		for (Schematic schem : getSchematics()) {
			if (schem.getName().equalsIgnoreCase(name))
				return schem;
		}
		return null;
	}

	public static List<Schematic> getSchematics() {
		return schematics;
	}
	
	public static Schematic loadSchematic(File file){
		try {
			if (file.exists()) {
				FileInputStream fis = new FileInputStream(file);
	            NBTTagCompound nbt = NBTCompressedStreamTools.a(fis);
	            
				NBTTagList tiles = nbt.getList("TileEntities", 10);
				
				Short width = nbt.getShort("Width");
				Short height = nbt.getShort("Height");
				Short length = nbt.getShort("Length");

				String materials = nbt.getString("Materials");

				byte[] blocksId = nbt.getByteArray("Blocks");

				byte[] data = nbt.getByteArray("Data");

				short[] blocks = new short[blocksId.length];

				byte[] addId = new byte[0];
				
				if (nbt.hasKey("AddBlocks")) {
					addId = nbt.getByteArray("AddBlocks");
				}
				
				LinkedHashMap<Vector, NBTBlock> nbtBlocks = new LinkedHashMap<>();
				
				if (tiles != null) {
	                for (NBTBase tile : tiles.list) {
	                    if (tile instanceof NBTTagCompound) {
	                        NBTTagCompound compound = (NBTTagCompound) tile;
	                        if (!compound.isEmpty()) {
	                            NBTMaterial nbtMaterial = NBTMaterial.fromTag(compound);
	                            
	                            if (nbtMaterial != null) {
	                                NBTBlock nbtBlock = nbtMaterial.getNbtBlock(compound);
	                                if (!nbtBlock.isEmpty()) nbtBlocks.put(nbtBlock.getOffset(), nbtBlock);
	                            }
	                        }
	                    }
	                }
	            }
				
				for (int index = 0; index < blocksId.length; index++) {
					if ((index >> 1) >= addId.length) {
						blocks[index] = (short) (blocksId[index] & 0xFF);
					} else {
						if ((index & 1) == 0) {
							blocks[index] = (short) (((addId[index >> 1] & 0x0F) << 8) + (blocksId[index] & 0xFF));
						} else {
							blocks[index] = (short) (((addId[index >> 1] & 0xF0) << 4) + (blocksId[index] & 0xFF));
						}
					}
				}

				fis.close();

				Schematic schematic = new Schematic(file.getName().replace(".schematic", ""), width, height, length, materials, blocks, data, nbtBlocks);

				return schematic;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}

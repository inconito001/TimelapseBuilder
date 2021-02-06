package fr.inconito001.timelapse.schematic;

import java.util.LinkedHashMap;

import org.bukkit.util.Vector;

import fr.inconito001.timelapse.nbt.block.NBTBlock;

public class Schematic {

	private short width;
	private short height;
	private short length;

	private String name;
	private String materials;

	private short[] blocks;
	private byte[] data;
	
	private LinkedHashMap<Vector, NBTBlock> nbtBlocks;
	
	public Schematic(String name, short width, short height, short length, String materials, short[] blocks, byte[] data, LinkedHashMap<Vector, NBTBlock> nbtBlocks) {
		super();
		this.name = name;
		this.width = width;
		this.height = height;
		this.length = length;
		this.materials = materials;
		this.blocks = blocks;
		this.data = data;
		this.nbtBlocks = nbtBlocks;
	}

	public String getName() {
		return this.name;
	}

	public short getWidth() {
		return this.width;
	}

	public void setWidth(short width) {
		this.width = width;
	}

	public short getHeight() {
		return this.height;
	}

	public void setHeight(short height) {
		this.height = height;
	}

	public short getLength() {
		return this.length;
	}

	public void setLength(short length) {
		this.length = length;
	}

	public String getMaterials() {
		return this.materials;
	}

	public void setMaterials(String materials) {
		this.materials = materials;
	}

	public short[] getBlocks() {
		return this.blocks;
	}

	public void setBlocks(short[] blocks) {
		this.blocks = blocks;
	}

	public byte[] getData() {
		return this.data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
	
	public LinkedHashMap<Vector, NBTBlock> getnbtBlocks() {
		return this.nbtBlocks;
	}
}
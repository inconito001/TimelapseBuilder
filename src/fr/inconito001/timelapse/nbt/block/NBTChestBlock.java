package fr.inconito001.timelapse.nbt.block;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_8_R3.Item;
import net.minecraft.server.v1_8_R3.MinecraftKey;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;

public class NBTChestBlock extends NBTBlock {

	private Map<Integer, ItemStack> allItems = new HashMap<>();

	public NBTChestBlock(NBTTagCompound nbtTag) {
		super(nbtTag);
	}

	@Override
	public void setData(BlockState state) throws Exception {
		org.bukkit.block.Chest chest = (org.bukkit.block.Chest) state;
		for (Integer location : allItems.keySet()) {
			chest.getBlockInventory().setItem(location, allItems.get(location));
		}
	}

	@Override
	public boolean isEmpty() {
		try {
			return getItems().isEmpty();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public Map<Integer, ItemStack> getItems() throws Exception {
		if (!allItems.isEmpty())
			return allItems;

		NBTTagCompound compound = this.getNbtTag();
		if (compound.getString("id").equals("Chest")) {
			if (compound.get("Items") != null) {
				NBTTagList items = (NBTTagList) compound.get("Items");
				for (int i = 0; i < items.size(); i++) {
					NBTTagCompound anItem = items.get(i);
									
					Material mat = convertID(anItem.getString("id"));

					ItemStack item = new ItemStack(mat, anItem.getInt("Count"), anItem.getByte("Damage"));
					allItems.put(anItem.getInt("Slot"), item);
				}
			}
		} else {
			throw new Exception("Id du NBT n'est pas un coffre: " + compound.getString("id"));
		}

		return allItems;
	}

	private Material convertID(String minecraftID) {
		MinecraftKey mk = new MinecraftKey(minecraftID);
		ItemStack item = CraftItemStack.asNewCraftStack(Item.REGISTRY.get(mk));

		return item.getType();
	}
}
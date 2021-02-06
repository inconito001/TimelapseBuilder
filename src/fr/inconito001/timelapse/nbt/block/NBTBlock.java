package fr.inconito001.timelapse.nbt.block;

import org.bukkit.block.BlockState;
import org.bukkit.util.Vector;

import net.minecraft.server.v1_8_R3.NBTTagCompound;

public abstract class NBTBlock {
    
    private final NBTTagCompound nbtTag;
    
    public NBTBlock(NBTTagCompound nbtTag) {
        this.nbtTag = nbtTag;
    }

    public NBTTagCompound getNbtTag() {
        return nbtTag;
    }

    public Vector getOffset() {
        NBTTagCompound compound = this.getNbtTag();
        
        int x = compound.getInt("x");
        int y = compound.getInt("y");
        int z = compound.getInt("z");
        
        return new Vector(x, y, z);
    }

    public abstract void setData(BlockState state) throws Exception;

    public abstract boolean isEmpty();
}


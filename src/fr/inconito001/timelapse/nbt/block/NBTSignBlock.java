package fr.inconito001.timelapse.nbt.block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.block.BlockState;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.server.v1_8_R3.NBTTagCompound;

public class NBTSignBlock extends NBTBlock {

    private Map<Position, String> lines = new HashMap<>();

    public NBTSignBlock(NBTTagCompound nbtTag) {
        super(nbtTag);
    }

    @Override
    public void setData(BlockState state) throws Exception {
        org.bukkit.block.Sign sign = (org.bukkit.block.Sign) state;
        int current = 0;
        for (String line : this.getLines()) {
            sign.setLine(current, line);
            current++;
        }
    }

    @Override
    public boolean isEmpty() {
        try {
            return getLines().isEmpty();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
    
    public String getLine(Position position) throws Exception {
        if (lines.containsKey(position)) {
            return lines.get(position);
        }

        NBTTagCompound compound = this.getNbtTag();
        if (compound.getString("id").equals("Sign")) {
        	String s1 = compound.getString(position.getId());
        	
        	if (s1.equalsIgnoreCase("\"\"")) {
        		return " ";
        	}
        	
            JsonObject jsonObject = new Gson().fromJson(s1, JsonObject.class);
            if (jsonObject.get("extra") != null) {
                JsonArray array = jsonObject.get("extra").getAsJsonArray();
                return array.get(0).getAsString();
            }
        } else {
            throw new Exception("Id of NBT was not a sign, was instead " + compound.getString("id"));
        }
        
        return null;
    }
    
    public List<String> getLines() throws Exception {
    	List<String> lines = new ArrayList<>();
        for (Position position : Position.values()) {
            lines.add(getLine(position));
        }
        return lines;
    }
    
    public enum Position {
        TEXT_ONE("Text1"),
        TEXT_TWO("Text2"),
        TEXT_THREE("Text3"),
        TEXT_FOUR("Text4");
        
        public String getId() {
            return id;
        }
        
        private String id;
        
        Position(String id) {
            this.id = id;
        }
    }
}

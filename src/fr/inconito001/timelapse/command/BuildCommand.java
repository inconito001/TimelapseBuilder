package fr.inconito001.timelapse.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.inconito001.timelapse.schematic.Schematic;
import fr.inconito001.timelapse.schematic.SchematicBuilder;
import fr.inconito001.timelapse.schematic.SchematicLoader;

public class BuildCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		
		Player player = (Player) sender;
		
		SchematicLoader.initSchematics();

		Schematic schem = getSchematic("maison");

		SchematicBuilder build = new SchematicBuilder(schem, player.getLocation());
		build.startGeneration();
		
		return true;
	}
	
	private Schematic getSchematic(String s) {
		return SchematicLoader.getByName(s);
	}

}

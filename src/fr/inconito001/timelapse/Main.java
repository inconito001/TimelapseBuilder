package fr.inconito001.timelapse;

import org.bukkit.plugin.java.JavaPlugin;

import fr.inconito001.timelapse.command.BuildCommand;

public class Main extends JavaPlugin {
	private static Main instance;

	@Override
	public void onEnable() {
		instance = this;
		
		this.registerCommand();

		super.onEnable();
	}

	@Override
	public void onDisable() {
		super.onDisable();
	}

	private void registerCommand() {
		this.getCommand("build").setExecutor(new BuildCommand());
	}

	public static Main getInstance() {
		return instance;
	}
}

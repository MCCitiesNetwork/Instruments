package net.cupofcode.instruments.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import net.cupofcode.instruments.InstrumentType;

public class InstrumentsTabCompleter implements TabCompleter {
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		ArrayList<String> arguments = new ArrayList<String>();
		if (args.length == 1) {
			arguments.add("list");
			arguments.add("give");
		} else if (args.length == 2 && args[0].equalsIgnoreCase("give")) {
			Bukkit.getOnlinePlayers().forEach(player -> {
				if (player.getName().startsWith(args[1]))
					arguments.add(player.getName());
			});
		} else if (args.length == 3 && args[0].equalsIgnoreCase("give")) {
			for (InstrumentType instrument : InstrumentType.values()) {
				if (instrument.getName().startsWith(args[2]))
					arguments.add(instrument.getName());
			}
			if ("all".startsWith(args[2]))
				arguments.add("all");
		}

		return arguments;
	}

}

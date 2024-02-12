package net.cupofcode.instruments.commands;

import net.cupofcode.instruments.InstrumentType;
import net.cupofcode.instruments.Instruments;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InstrumentsCommand implements CommandExecutor {

	private Instruments instance = Instruments.getInstance();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (sender instanceof Player) {
			// player-only commands
			Player p = (Player) sender;

			if (cmd.getName().equalsIgnoreCase("instruments")) {
				if (instance.getConfig().getBoolean("settings.instruments.permissions")
						&& !p.hasPermission("instruments.use"))
					return false;

				if (args.length == 0) {
					this.sendUsageMessage(p);
					return true;
				}

				if (args[0].equalsIgnoreCase("list")) {
					if (instance.getConfig().getBoolean("settings.instruments.permissions")
							&& !p.hasPermission("instruments.list"))
						return false;

					String instrumentString = "";
					for (InstrumentType instrumentType : InstrumentType.values()) {
						instrumentString += instrumentType.getName().replace(" ", "_") + ", ";
					}

					instrumentString += "ALL";

					p.sendMessage(ChatColor.BOLD + "" + ChatColor.GREEN + "Supported instruments:");
					p.sendMessage(ChatColor.GREEN + instrumentString);
					return true;
				}

				if (args.length == 3 && args[0].equalsIgnoreCase("give")) { //TODO: re-used code cuz I'm lazy, might want to clean it up
					if (instance.getConfig().getBoolean("settings.instruments.permissions")
							&& !p.hasPermission("instruments.give"))
						return false;

					String selectedInstrument = args[2];
					String playerName = args[1];

					Player givePlayer = Bukkit.getPlayer(playerName);

					if (givePlayer == null) {
						p.sendMessage(ChatColor.RED + "Could not find online player " + playerName);
						return true;
					}

					if (selectedInstrument.equalsIgnoreCase("all")) {
						for (InstrumentType instrumentType : InstrumentType.values()) {
							givePlayer.getInventory().addItem(instrumentType.getItemStack());
						}
						return true;
					}

					InstrumentType instrumentType = InstrumentType.getInstrumentTypeByName(selectedInstrument);

					if (instrumentType == null) {
						p.sendMessage(ChatColor.RED + "Could not find instrument " + selectedInstrument);
						p.sendMessage(ChatColor.RED + "For a list of available instruments type /instruments list");
						return true;
					}

					givePlayer.getInventory().addItem(instrumentType.getItemStack());
					return true;
				}

				this.sendUsageMessage(p);
				return true;
			}
			return false;
		} else {
			//any sender command
			if (cmd.getName().equalsIgnoreCase("instruments")) {
				if (args.length == 3 && args[0].equalsIgnoreCase("give")) {
					if (instance.getConfig().getBoolean("settings.instruments.permissions")
							&& !sender.hasPermission("instruments.give"))
						return false;

					String selectedInstrument = args[2];
					String playerName = args[1];

					Player givePlayer = Bukkit.getPlayer(playerName);

					if (givePlayer == null) {
						sender.sendMessage(ChatColor.RED + "Could not find online player " + playerName);
						return true;
					}

					if (selectedInstrument.equalsIgnoreCase("all")) {
						for (InstrumentType instrumentType : InstrumentType.values()) {
							givePlayer.getInventory().addItem(instrumentType.getItemStack());
						}
						return true;
					}

					InstrumentType instrumentType = InstrumentType.getInstrumentTypeByKey(selectedInstrument);

					if (instrumentType == null) {
						sender.sendMessage(ChatColor.RED + "Could not find instrument " + selectedInstrument);
						sender.sendMessage(ChatColor.RED + "For a list of available instruments type /instruments list");
						return true;
					}

					givePlayer.getInventory().addItem(instrumentType.getItemStack());
					return true;
				}
			}
		}
		return false;
	}

	private void sendUsageMessage(Player player) {
		player.sendMessage(
				ChatColor.RED + "" + ChatColor.BOLD + "Instruments v" + instance.getDescription().getVersion());
		player.sendMessage(ChatColor.RED + "/instruments give [player] [instrument]");
		player.sendMessage(ChatColor.RED + "/instruments list");
	}

}

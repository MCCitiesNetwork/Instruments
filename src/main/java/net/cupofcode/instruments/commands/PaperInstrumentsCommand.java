package net.cupofcode.instruments.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.cupofcode.instruments.InstrumentType;
import net.cupofcode.instruments.Instruments;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

public class PaperInstrumentsCommand {

    private static final Instruments instance = Instruments.getInstance();

    public static LiteralArgumentBuilder<CommandSourceStack> createCommand() {
        return LiteralArgumentBuilder.<CommandSourceStack>literal("instruments")
            .requires(source -> {
                if (instance.getConfig().getBoolean("settings.instruments.permissions")) {
                    return source.getSender().hasPermission("instruments.use");
                }
                return true;
            })
            .then(LiteralArgumentBuilder.<CommandSourceStack>literal("list")
                .requires(source -> {
                    if (instance.getConfig().getBoolean("settings.instruments.permissions")) {
                        return source.getSender().hasPermission("instruments.list");
                    }
                    return true;
                })
                .executes(PaperInstrumentsCommand::executeList))
            .then(LiteralArgumentBuilder.<CommandSourceStack>literal("give")
                .requires(source -> {
                    if (instance.getConfig().getBoolean("settings.instruments.permissions")) {
                        return source.getSender().hasPermission("instruments.give");
                    }
                    return true;
                })
                .then(RequiredArgumentBuilder.<CommandSourceStack, String>argument("player", StringArgumentType.word())
                    .suggests((context, builder) -> {
                        // Suggest online player names
                        Bukkit.getOnlinePlayers().forEach(player -> builder.suggest(player.getName()));
                        return builder.buildFuture();
                    })
                    .then(RequiredArgumentBuilder.<CommandSourceStack, String>argument("instrument", StringArgumentType.string())
                        .suggests(PaperInstrumentsCommand::suggestInstruments)
                        .executes(PaperInstrumentsCommand::executeGive))));
    }

    private static int executeList(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        
        StringBuilder instrumentString = new StringBuilder();
        for (InstrumentType instrumentType : InstrumentType.values()) {
            instrumentString.append(instrumentType.getName().replace(" ", "_")).append(", ");
        }
        instrumentString.append("ALL");

        Component message = Component.text("Supported instruments:")
            .color(NamedTextColor.GREEN)
            .decorate(TextDecoration.BOLD);
        
        Component instruments = Component.text(instrumentString.toString())
            .color(NamedTextColor.GREEN);

        source.getSender().sendMessage(message);
        source.getSender().sendMessage(instruments);
        
        return Command.SINGLE_SUCCESS;
    }

    private static int executeGive(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        String playerName = StringArgumentType.getString(context, "player");
        String selectedInstrument = StringArgumentType.getString(context, "instrument");

        Player givePlayer = Bukkit.getPlayer(playerName);
        if (givePlayer == null) {
            source.getSender().sendMessage(Component.text("Could not find online player " + playerName)
                .color(NamedTextColor.RED));
            return Command.SINGLE_SUCCESS;
        }

        if (selectedInstrument.equalsIgnoreCase("all")) {
            for (InstrumentType instrumentType : InstrumentType.values()) {
                givePlayer.getInventory().addItem(instrumentType.getItemStack());
            }
            source.getSender().sendMessage(Component.text("Gave all instruments to " + playerName)
                .color(NamedTextColor.GREEN));
            return Command.SINGLE_SUCCESS;
        }

        InstrumentType instrumentType = InstrumentType.getInstrumentTypeByKey(selectedInstrument);
        if (instrumentType == null) {
            source.getSender().sendMessage(Component.text("Could not find instrument " + selectedInstrument)
                .color(NamedTextColor.RED));
            source.getSender().sendMessage(Component.text("For a list of available instruments type /instruments list")
                .color(NamedTextColor.RED));
            return Command.SINGLE_SUCCESS;
        }

        givePlayer.getInventory().addItem(instrumentType.getItemStack());
        source.getSender().sendMessage(Component.text("Gave " + instrumentType.getName() + " to " + playerName)
            .color(NamedTextColor.GREEN));
        
        return Command.SINGLE_SUCCESS;
    }

    private static CompletableFuture<Suggestions> suggestInstruments(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        String input = builder.getRemaining().toLowerCase();
        
        // Add "all" option
        if ("all".startsWith(input)) {
            builder.suggest("all");
        }
        
        // Add all instrument types
        for (InstrumentType instrumentType : InstrumentType.values()) {
            String key = instrumentType.getKey();
            if (key.toLowerCase().startsWith(input)) {
                builder.suggest(key);
            }
        }
        
        return builder.buildFuture();
    }
}

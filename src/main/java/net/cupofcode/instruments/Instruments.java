package net.cupofcode.instruments;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityAnimation;
import net.cupofcode.instruments.commands.PaperInstrumentsCommand;
import net.cupofcode.instruments.listeners.*;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Instruments extends JavaPlugin {

	private static Instruments instance;
	private HashMap<Player, Instrument> instrumentManager = new HashMap<>();
	private HashMap<Integer, Player> entityIdToPlayer = new HashMap<>();
	private File configFile;
	private FileConfiguration config;

	@Override
	public void onEnable() {
		instance = this;

		loadConfig();

		// Instrument names are set in InstrumentType enum - no config needed

		// Register Paper Command API commands
		this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
			commands.registrar().register(PaperInstrumentsCommand.createCommand().build(), "Instruments command for managing musical instruments");
		});

		this.registerListeners(new InventoryClick(), new InventoryClose(), new PlayerInteract(),
				new PlayerDrop(), new PlayerPickup(), new PlayerJoin(), new PlayerDeath(),
                new PlayerQuit(), new BlockBreak(), new PlayerAttack(), new PlayerItemHeld(), new PlayerRespawn(), new InventoryOpen(), new PlayerSwapItem(), new PlayerEntityInteract());

		// Recipes and resource packs removed - using simple NBT tags only

		// Initialize PacketEvents
		PacketEvents.getAPI().load();
		PacketEvents.getAPI().init();

		// Disables arm swing animation while player is in hotbar mode
		PacketEvents.getAPI().getEventManager().registerListener(new PacketListener() {
			@Override
			public void onPacketReceive(PacketReceiveEvent event) {
				// Handle incoming packets if needed
			}

			@Override
			public void onPacketSend(PacketSendEvent event) {
				if (event.getPacketType() == PacketType.Play.Server.ENTITY_ANIMATION) {
					WrapperPlayServerEntityAnimation animationPacket = new WrapperPlayServerEntityAnimation(event);
					int senderId = animationPacket.getEntityId();

					// Efficient player lookup using cached mapping
					Player sender = entityIdToPlayer.get(senderId);
					if (sender == null || !instrumentManager.containsKey(sender))
						return;

					if (instrumentManager.get(sender).isHotBarMode())
						event.setCancelled(true);
				}
			}
		}, PacketListenerPriority.NORMAL);
	}

	@Override
	public void onDisable() {
		// Clean up all players and their instrument states
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (!instrumentManager.containsKey(player))
				continue;

			if (instrumentManager.get(player).isHotBarMode())
				Utils.loadInventory(player);

			instrumentManager.remove(player);
		}

		// Clear entity ID mapping to prevent memory leaks
		entityIdToPlayer.clear();

		// Clear inventory mappings to prevent memory leaks
		Utils.inventoryMap.clear();

		// Terminate PacketEvents
		try {
			PacketEvents.getAPI().terminate();
		} catch (Exception e) {
			Bukkit.getLogger().warning("[Instruments] Failed to terminate PacketEvents: " + e.getMessage());
		}
	}

	private void registerListeners(Listener... listeners) {
		Arrays.stream(listeners).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));
	}

	public HashMap<Player, Instrument> getInstrumentManager() {
		return instrumentManager;
	}

	public static Instruments getInstance() {
		return instance;
	}

	public void addPlayer(Player player) {
		entityIdToPlayer.put(player.getEntityId(), player);
	}

	public void removePlayer(Player player) {
		entityIdToPlayer.remove(player.getEntityId());
	}

	private void loadConfig() {
		if (!getDataFolder().exists()) {
			getDataFolder().mkdir();
		}

		configFile = new File(getDataFolder(), "config.yml");
		if (!configFile.exists()) {
			try {
				configFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		config = YamlConfiguration.loadConfiguration(configFile);

		HashMap<String, Object> defaultConfig = new HashMap<>();

		// Only keep essential permissions setting
		defaultConfig.put("settings.instruments.permissions", true);

		for (String key : defaultConfig.keySet()) {
			if (!config.contains(key)) {
				config.set(key, defaultConfig.get(key));
			}
		}

		// Load item models for instruments
		loadInstrumentItemModels();

		this.saveConfig();
	}

	// Recipe methods removed - using simple NBT tags only

	private void loadInstrumentItemModels() {
		// Load item models for each instrument type from config
		for (InstrumentType instrumentType : InstrumentType.values()) {
			String configPath = "instruments." + instrumentType.getKey() + ".item_model";
			if (config.contains(configPath)) {
				String itemModel = config.getString(configPath);
				instrumentType.setItemModel(itemModel);
				Bukkit.getLogger().info("[Instruments] Loaded item model for " + instrumentType.getKey() + ": " + itemModel);
			} else {
				Bukkit.getLogger().warning("[Instruments] No item model configured for " + instrumentType.getKey() + ", using CustomModelData fallback");
			}
		}
	}

	@Override
	public void saveConfig() {
		try {
			config.save(configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public FileConfiguration getConfig() {
		return config;
	}
}

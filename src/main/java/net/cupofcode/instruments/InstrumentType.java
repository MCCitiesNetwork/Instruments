package net.cupofcode.instruments;

import io.papermc.paper.datacomponent.DataComponentTypes;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public enum InstrumentType {

	PIANO("piano", 100, Sound.BLOCK_NOTE_BLOCK_HARP), BASS_DRUM("bass_drum", 101, Sound.BLOCK_NOTE_BLOCK_BASEDRUM),
	SNARE_DRUM("snare_drum", 102, Sound.BLOCK_NOTE_BLOCK_SNARE), STICKS("sticks", 103, Sound.BLOCK_NOTE_BLOCK_HAT),
	BASS_GUITAR("bass_guitar", 104, Sound.BLOCK_NOTE_BLOCK_BASS), FLUTE("flute", 105, Sound.BLOCK_NOTE_BLOCK_FLUTE),
	BELL("bell", 106, Sound.BLOCK_NOTE_BLOCK_BELL), GUITAR("guitar", 107, Sound.BLOCK_NOTE_BLOCK_GUITAR),
	CHIME("chime", 108, Sound.BLOCK_NOTE_BLOCK_CHIME), XYLOPHONE("xylophone", 109, Sound.BLOCK_NOTE_BLOCK_XYLOPHONE),
	IRON_XYLOPHONE("iron_xylophone", 110, Sound.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE),
	COW_BELL("cow_bell", 111, Sound.BLOCK_NOTE_BLOCK_COW_BELL),
	DIDGERIDOO("didgeridoo", 112, Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO), BIT("bit", 113, Sound.BLOCK_NOTE_BLOCK_BIT),
	BANJO("banjo", 114, Sound.BLOCK_NOTE_BLOCK_BANJO), PLING("pling", 115, Sound.BLOCK_NOTE_BLOCK_PLING);

	private Instruments instance = Instruments.getInstance();
	private final String key;
	private int modelId;
	private String name;
	private final Sound sound;
	private String itemModel;

	InstrumentType(String key, int modelId, Sound sound) {
		this.key = key;
		this.modelId = modelId;
		this.sound = sound;
		this.name = key;
		this.itemModel = null; // Will be loaded from config
	}

	public ItemStack getItemStack() {
		ItemStack itemStack = new ItemStack(Material.WOODEN_HOE);
		ItemMeta itemMeta = itemStack.getItemMeta();

		// Set unbreakable and hide attributes
		itemMeta.setUnbreakable(true);
		itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);
		
		// Set simple NBT tag for instrument identification
		NamespacedKey instrumentKey = new NamespacedKey(instance, "instrument");
		itemMeta.getPersistentDataContainer().set(instrumentKey, PersistentDataType.STRING, this.key);

		// Set display name using Adventure Component
		itemMeta.displayName(Component.text(Utils.formatString(name)));

		itemStack.setItemMeta(itemMeta);

		// Use Data Component API for item model if available
		if (itemModel != null && !itemModel.isEmpty()) {
			try {
				// Parse the item model (format: namespace:path)
				String[] parts = itemModel.split(":", 2);
				if (parts.length == 2) {
					NamespacedKey modelKey = new NamespacedKey(parts[0], parts[1]);
					
					// Use Data Component API to set the item model
					itemStack.setData(DataComponentTypes.ITEM_MODEL, Key.key(itemModel));
				}
			} catch (Exception e) {
				// Fallback to CustomModelData if Data Component API fails
				e.printStackTrace();
			}
		}
		return itemStack;
	}

	public static InstrumentType getInstrumentTypeByItemStack(ItemStack itemStack) {
		if (itemStack.getItemMeta() == null)
			return null;

		PersistentDataContainer holder = itemStack.getItemMeta().getPersistentDataContainer();
		NamespacedKey instrumentKey = new NamespacedKey(Instruments.getInstance(), "instrument");
		
		if (!holder.has(instrumentKey, PersistentDataType.STRING))
			return null;
			
		String instrumentTypeKey = holder.get(instrumentKey, PersistentDataType.STRING);
		return getInstrumentTypeByKey(instrumentTypeKey);
	}

	public static InstrumentType getInstrumentTypeByKey(String key) {
		for (InstrumentType instrumentType : InstrumentType.values()) {
			if (instrumentType.getKey().equals(key))
				return instrumentType;
		}

		return null;
	}

	public static InstrumentType getInstrumentTypeByName(String name) {
		for (InstrumentType instrumentType : InstrumentType.values()) {
			if (instrumentType.getName().equals(name))
				return instrumentType;
		}

		return null;
	}

	public String getKey() {
		return this.key;
	}

	public Sound getSound() {
		return this.sound;
	}

	public int getModelId() {
		return this.modelId;
	}

	public void setModelId(int modelId) {
		this.modelId = modelId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getItemModel() {
		return itemModel;
	}

	public void setItemModel(String itemModel) {
		this.itemModel = itemModel;
	}
}

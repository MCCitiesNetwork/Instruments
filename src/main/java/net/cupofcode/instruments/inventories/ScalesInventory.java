package net.cupofcode.instruments.inventories;

import net.cupofcode.instruments.Instruments;
import net.cupofcode.instruments.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.cupofcode.instruments.InstrumentType;
import net.cupofcode.instruments.Scale;

public class ScalesInventory implements InventoryHolder {

	private Inventory inv;
	private InstrumentType instrumentType;
	private Instruments instance = Instruments.getInstance();
	private int page;
	private boolean quickPlay = true;
	private int octave;

	public ScalesInventory(InstrumentType instrumentType) {
		inv = Bukkit.createInventory(this, 54,
				ChatColor.RED + Utils.formatString(instrumentType.getName()) + " Scales");
		this.instrumentType = instrumentType;
		octave = 0;
	}

	public void display(Player player) {
		instance.getInstrumentManager().get(player).setTransitioning(true);

		fillBackground();
		fillScales(this.page);

		inv.setItem(0, createInstrumentIcon());
		inv.setItem(8, createQuickPlayOption());
		inv.setItem(45, createTile(Material.ARROW, "Previous Page"));
		inv.setItem(53, createTile(Material.ARROW, "Next Page"));

		player.openInventory(this.inv);

		instance.getInstrumentManager().get(player).setTransitioning(false);
	}

	private void fillBackground() {
		ItemStack backgroundTile = this.createTile(Material.BROWN_STAINED_GLASS_PANE, " ");
		for (int i = 0; i < 54; i++) {
			inv.setItem(i, backgroundTile);
		}
	}

	private void fillScales(int page) {
		int position = page * 16;
		Scale[] scales = Scale.values();
		for (int i = 10; i < 44 && position < scales.length; i += 2) {
			inv.setItem(i, createTile(Material.PAPER, scales[position].getName()));

			position++;
			if (i % 9 == 7)
				i++;
		}
	}

	private ItemStack createTile(Material material, String name) {
		ItemStack tile = new ItemStack(material, 1);
		ItemMeta itemMeta = tile.getItemMeta();
		itemMeta.setDisplayName(name);
		tile.setItemMeta(itemMeta);
		return tile;
	}

	private ItemStack createInstrumentIcon() {
//		ItemStack icon = new ItemStack(Material.WOODEN_HOE, 1);
//		ItemMeta itemMeta = icon.getItemMeta();
//		itemMeta.setDisplayName(ChatColor.RED + Utils.formatString(this.instrumentType.getName()));
//		itemMeta.setCustomModelData(this.instrumentType.getModelId());
//		icon.setItemMeta(itemMeta);
//		return icon;
		return this.instrumentType.getItemStack();
	}

	private ItemStack createQuickPlayOption() {
		Material material = this.quickPlay ? Material.LIME_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE;
		String color = this.quickPlay ? ChatColor.GREEN + "" : ChatColor.RED + "";
		ItemStack icon = new ItemStack(material, 1);
		ItemMeta itemMeta = icon.getItemMeta();
		itemMeta.setDisplayName(color + "Quick Play");
		icon.setItemMeta(itemMeta);
		return icon;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	@Override
	public Inventory getInventory() {
		return this.inv;
	}

	public boolean isQuickPlay() {
		return quickPlay;
	}

	public void setQuickPlay(boolean quickPlay) {
		this.quickPlay = quickPlay;
	}

	public int getOctave() {
		return octave;
	}

	public void toggleOctave() {
		if (getOctave() == 0) {
			setOctave(1);
		} else {
			setOctave(0);
		}
	}

	public void setOctave(int val) {
		octave = val;
	}

}

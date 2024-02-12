package net.cupofcode.instruments.listeners;

import net.cupofcode.instruments.*;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerItemHeld implements Listener {

    private Instruments instance = Instruments.getInstance();

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        if(!instance.getInstrumentManager().containsKey(player)) return;

        Instrument instrument = instance.getInstrumentManager().get(player);

        if(!instrument.isHotBarMode() || !instrument.getScalesInventory().isQuickPlay()) return;

        ItemStack item = player.getInventory().getItem(event.getNewSlot());

        if(item == null || item.getItemMeta() == null) {
            event.setCancelled(true);
            player.getInventory().setHeldItemSlot(0);
            return;
        }

        if(item.containsEnchantment(Enchantment.LURE)) {
            event.setCancelled(true);
            player.getInventory().setHeldItemSlot(0);

            instrument.getScalesInventory().toggleOctave();
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(ChatColor.GREEN + "Octave: " + instrument.getScalesInventory().getOctave());
            item.setItemMeta(itemMeta);
            return;
        }

        String note = ChatColor.stripColor(item.getItemMeta().getDisplayName());

        if(InstrumentNote.getNoteByKey(note) == null) return;

        int octave = instrument.getScalesInventory().getOctave();
        if (item.getItemMeta().getDisplayName().equals(ChatColor.DARK_GREEN + note))
        	octave = Math.abs(octave - 1);

        if(player.isSneaking()) {
            String[] notes = Utils.getMajorTriad(note);
            for (String pNote : notes) {
                instrument.playNote(pNote, octave);
            }
        } else {
            instrument.playNote(note, octave);
        }

        event.setCancelled(true);
        player.getInventory().setHeldItemSlot(0);
    }

}

package net.cupofcode.instruments.listeners;

import net.cupofcode.instruments.Instruments;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryClose implements Listener {

    private Instruments instance = Instruments.getInstance();

    // Remove player from instrument once finished playing
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        if(instance.getInstrumentManager().containsKey(player)
                && !instance.getInstrumentManager().get(player).isHotBarMode()
                && !instance.getInstrumentManager().get(player).isTransitioning()) {
            instance.getInstrumentManager().remove(player);
        }
    }
}

package net.cupofcode.instruments.listeners;

import net.cupofcode.instruments.Instruments;
import net.cupofcode.instruments.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class PlayerDeath implements Listener {

    private Instruments instance = Instruments.getInstance();

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if(!(event.getEntity() instanceof Player)) return;

        Player player = event.getEntity();
        if(!instance.getInstrumentManager().containsKey(player)) return;

        if(instance.getInstrumentManager().get(player).isHotBarMode()) {
            event.getDrops().clear();

            if(!event.getKeepInventory()) {
                if(!Utils.inventoryMap.containsKey(player)) return;

                HashMap<Integer, ItemStack> items = Utils.inventoryMap.get(player);
                for (Integer i : items.keySet()) {
                    event.getDrops().add(items.get(i));
                }

                instance.getInstrumentManager().remove(player);
            }
        }
    }

}

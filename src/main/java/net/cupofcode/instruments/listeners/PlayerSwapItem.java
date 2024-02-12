package net.cupofcode.instruments.listeners;

import net.cupofcode.instruments.Instrument;
import net.cupofcode.instruments.Instruments;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class PlayerSwapItem implements Listener {

    private Instruments instance = Instruments.getInstance();

    @EventHandler
    public void onPlayerSwapItem(PlayerSwapHandItemsEvent event) {
        Player p = event.getPlayer();

        if(!instance.getInstrumentManager().containsKey(p)) return;

        Instrument instrument = instance.getInstrumentManager().get(p);

        if(instrument.isHotBarMode()) {
            event.setCancelled(true);
            return;
        }
    }

}

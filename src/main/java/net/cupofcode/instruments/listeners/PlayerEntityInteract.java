package net.cupofcode.instruments.listeners;

import net.cupofcode.instruments.Instrument;
import net.cupofcode.instruments.Instruments;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class PlayerEntityInteract implements Listener {

    private Instruments instance = Instruments.getInstance();

    @EventHandler
    public void onPlayerEntityInteract(PlayerInteractEntityEvent event) {
        Player p = event.getPlayer();

        if(!instance.getInstrumentManager().containsKey(p)) return;

        Instrument instrument = instance.getInstrumentManager().get(p);

        if(instrument.isHotBarMode() && event.getRightClicked() instanceof ItemFrame) {
            event.setCancelled(true);
            return;
        }
    }

}

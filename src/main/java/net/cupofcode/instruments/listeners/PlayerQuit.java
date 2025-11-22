package net.cupofcode.instruments.listeners;

import net.cupofcode.instruments.Instruments;
import net.cupofcode.instruments.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener {

    private Instruments instance = Instruments.getInstance();

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        
        if(!instance.getInstrumentManager().containsKey(player)) return;

        if(instance.getInstrumentManager().get(event.getPlayer()).isHotBarMode())
            Utils.loadInventory(event.getPlayer());

        instance.getInstrumentManager().remove(event.getPlayer());
    }

}

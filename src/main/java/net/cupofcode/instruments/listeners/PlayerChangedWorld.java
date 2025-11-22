package net.cupofcode.instruments.listeners;

import net.cupofcode.instruments.Instrument;
import net.cupofcode.instruments.Instruments;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerChangedWorld implements Listener {

    private Instruments instance = Instruments.getInstance();

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        // Only prevent world changes, not regular teleports
        if (event.getFrom().getWorld().equals(event.getTo().getWorld())) {
            return;
        }

        Player player = event.getPlayer();
        
        if(!instance.getInstrumentManager().containsKey(player)) return;

        Instrument instrument = instance.getInstrumentManager().get(player);

        if(instrument.isHotBarMode()) {
            // Prevent world change while in hotbar mode
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You cannot change worlds while in scales mode. Right-click the first item to exit.");
        }
    }

}


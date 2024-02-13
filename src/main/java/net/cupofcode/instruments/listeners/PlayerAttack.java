package net.cupofcode.instruments.listeners;

import net.cupofcode.instruments.Instrument;
import net.cupofcode.instruments.Instruments;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerAttack implements Listener {

    private Instruments instance = Instruments.getInstance();

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        if(event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();

            if(!instance.getInstrumentManager().containsKey(player)) return;

            Instrument instrument = instance.getInstrumentManager().get(player);

            if(instrument.isHotBarMode()) {
                event.setCancelled(true);
                return;
            }
        }
    }

}

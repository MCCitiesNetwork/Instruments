package net.cupofcode.instruments.listeners;

import net.cupofcode.instruments.InstrumentType;
import net.cupofcode.instruments.Instruments;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    private Instruments instance = Instruments.getInstance();

    // Give recipes if enabled, download resource pack
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Add player to entity ID mapping for efficient packet handling
        instance.addPlayer(player);

        if(instance.getConfig().getBoolean("settings.instruments.resourcepack.enabled")) {
            player.setResourcePack("https://www.dropbox.com/s/qav6cbju12g7ci1/CupOfCode_Orchestra_V_05.zip?dl=1");
        }

        if(instance.getConfig().getBoolean("settings.instruments.recipe.enabled")) {
            for(InstrumentType instrumentType : InstrumentType.values()) {
                player.discoverRecipe(new NamespacedKey(instance, instrumentType.getKey()));
            }
        }
    }
}

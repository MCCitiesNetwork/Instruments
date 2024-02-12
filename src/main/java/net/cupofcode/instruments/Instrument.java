package net.cupofcode.instruments;

import net.cupofcode.instruments.inventories.ScalesInventory;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Player;

import net.cupofcode.instruments.inventories.InstrumentInventory;

import java.util.concurrent.ThreadLocalRandom;

public class Instrument {

    private InstrumentType instrumentType;
    private Player player;
    private InstrumentInventory instrumentInventory;
    private ScalesInventory scalesInventory;
    private Instruments instance = Instruments.getInstance();
    private boolean hotBarMode;
    private boolean transitioning;

    public Instrument(InstrumentType instrumentType, Player player) {
        this.instrumentType = instrumentType;
        this.player = player;
        this.instrumentInventory = new InstrumentInventory(instrumentType);
        this.scalesInventory = new ScalesInventory(this.instrumentType);

        this.instance.getInstrumentManager().put(player, this);
    }

    public void playNote(String note, int octave) {
        InstrumentNote instrumentNote = InstrumentNote.getNoteByKey(note);

        if(instrumentNote == null) return;

        float pitch = octave == 0 ? instrumentNote.getFirstPitch() : instrumentNote.getSecondPitch();
        this.player.getWorld().playSound(player.getLocation(),
                this.instrumentType.getSound(), SoundCategory.RECORDS, (float) 5.0, pitch);

        Location playerLoc = player.getLocation();

        double xOffset = ThreadLocalRandom.current().nextDouble(-1, 1);
        double zOffset = ThreadLocalRandom.current().nextDouble(-1, 1);
        Location particleLoc = new Location(this.player.getWorld(), playerLoc.getX() + xOffset, playerLoc.getY() + 1.5, playerLoc.getZ() + zOffset);

        double particleColor = instrumentNote.getColor();
        if(octave == 1) particleColor = particleColor + 12;

        this.player.getWorld().spawnParticle(Particle.NOTE, particleLoc, 0, particleColor, 0.0d, 0.0d, 0.1d);
    }

    public void play() {
        this.instrumentInventory.display(this.player);
    }

    public void playHotbar(Scale scale) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR,  TextComponent.fromLegacyText(ChatColor.RED + "Right click your first item to exit."));

        this.hotBarMode = true;

        Utils.storeInventory(this.player);

        player.closeInventory();

        // Clear everything but armor
        Utils.clearInventory(player);
        this.instrumentInventory.displayHotbar(this.player, scale);
    }

    public void playScales() {
        this.scalesInventory.display(this.player);
    }

    public InstrumentType getInstrumentType() {
        return this.instrumentType;
    }

    public boolean isHotBarMode() {
        return hotBarMode;
    }

    public void setHotBarMode(boolean hotBarMode) {
        this.hotBarMode = hotBarMode;
    }

    public boolean isTransitioning() {
        return transitioning;
    }

    public void setTransitioning(boolean transitioning) {
        this.transitioning = transitioning;
    }

    public InstrumentInventory getInstrumentInventory() {
        return this.instrumentInventory;
    }

    public ScalesInventory getScalesInventory() {
        return this.scalesInventory;
    }
}

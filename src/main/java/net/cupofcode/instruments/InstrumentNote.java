package net.cupofcode.instruments;

public enum InstrumentNote {

    C       ("C", 0.707107f, 1.414214f, 6),
    C_SHARP ("C#", 0.749154f, 1.498307f, 7),
    D       ("D", 0.793701f, 1.587401f, 8),
    D_SHARP ("D#", 0.840896f, 1.681793f, 9),
    E       ("E", 0.890899f, 1.781797f, 10),
    F       ("F", 0.943874f, 1.887749f, 11),
    F_SHARP ("F#", 0.5f, 1.0f, 0),
    G       ("G", 0.529732f, 1.059463f, 1),
    G_SHARP ("G#", 0.561231f, 1.122462f, 2),
    A       ("A", 0.594604f, 1.189207f, 3),
    A_SHARP ("A#", 0.629961f, 1.259921f, 4),
    B       ("B", 0.667420f, 1.334840f, 5);

    private final String key;
    private final float firstPitch;
    private final float secondPitch;
    private final int color;

    InstrumentNote(String key, float firstPitch, float secondPitch, int color) {
        this.key = key;
        this.firstPitch = firstPitch;
        this.secondPitch= secondPitch;
        this.color = color;
    }

    public String getKey() {
        return key;
    }

    public float getFirstPitch() {
        return firstPitch;
    }

    public float getSecondPitch() {
        return secondPitch;
    }

    public int getColor() {
        return color;
    }

    public static InstrumentNote getNoteByKey(String key) {
        for(InstrumentNote instrumentNotes : InstrumentNote.values()) {
            if(instrumentNotes.getKey().equals(key)) return instrumentNotes;
        }

        return null;
    }
}


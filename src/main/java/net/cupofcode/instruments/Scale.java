package net.cupofcode.instruments;

import java.util.ArrayList;

import org.apache.commons.lang3.text.WordUtils;

public enum Scale {
	C_MAJOR, CSHARP_MAJOR, D_MAJOR, DSHARP_MAJOR, E_MAJOR, F_MAJOR, FSHARP_MAJOR, G_MAJOR, GSHARP_MAJOR, A_MAJOR,
	ASHARP_MAJOR, B_MAJOR, C_MINOR, CSHARP_MINOR, D_MINOR, DSHARP_MINOR, E_MINOR, F_MINOR, FSHARP_MINOR, G_MINOR,
	GSHARP_MINOR, A_MINOR, ASHARP_MINOR, B_MINOR,
	C_MINOR_BLUES, CSHARP_MINOR_BLUES, D_MINOR_BLUES, DSHARP_MINOR_BLUES, E_MINOR_BLUES, F_MINOR_BLUES, FSHARP_MINOR_BLUES, G_MINOR_BLUES,
	GSHARP_MINOR_BLUES, A_MINOR_BLUES, ASHARP_MINOR_BLUES, B_MINOR_BLUES,
	C_MAJOR_BLUES, CSHARP_MAJOR_BLUES, D_MAJOR_BLUES, DSHARP_MAJOR_BLUES, E_MAJOR_BLUES, F_MAJOR_BLUES, FSHARP_MAJOR_BLUES, G_MAJOR_BLUES,
	GSHARP_MAJOR_BLUES, A_MAJOR_BLUES, ASHARP_MAJOR_BLUES, B_MAJOR_BLUES;

	public static final String[] notes = new String[] {"C", "C#", "D", "D#", "E", "F","F#", "G", "G#", "A", "A#", "B"};
	
	public String getName() {
		String name = this.toString();
		name = name.replaceAll("SHARP", "#");
		name = name.replaceAll("_", " ");
		name = WordUtils.capitalizeFully(name);
		return name;
	}
	
	public ArrayList<String> getNotes() {
		ArrayList<String> notes = new ArrayList<String>();
		String name = getName();
		int position = Utils.getIndexOfNote(getName().substring(0, name.indexOf(" ")));
		int[] jumps = new int[7];
		
		switch (this.toString().substring(this.toString().indexOf("_") + 1)) {
		case "MAJOR":
			jumps = new int[] {0, 2, 2, 1, 2, 2, 2};
			break;
		case "MINOR":
			jumps = new int[] {0, 2, 1, 2, 2, 1, 2};
			break;
		case "MINOR_BLUES":
			jumps = new int[] {0, 3, 2, 1, 1, 3};
			break;
		case "MAJOR_BLUES":
			jumps = new int[] {0, 2, 1, 1, 3, 2};
			break;
		}
		
		for (int jump : jumps) {
			position += jump;
			notes.add(this.notes[position % this.notes.length]);
		}
		
		return notes;
	}

	public ArrayList<String> getMissingNotes() {
		ArrayList<String> currNotes = this.getNotes();
		ArrayList<String> missingNotes = new ArrayList<>();
		for(int i = 0; i < notes.length; i++) {
			if(!currNotes.contains(notes[i])) {
				missingNotes.add(notes[i]);
			}
		}
		return missingNotes;
	}
	
	public static Scale getScaleByName(String name) {
		for (Scale scale : Scale.values()) {
			if (scale.getName().equals(name))
				return scale;
		}
		
		return null;
	}
}

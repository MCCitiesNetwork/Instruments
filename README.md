# Instruments

A modern Minecraft plugin for Paper/Spigot that adds playable musical instruments to your server. Features custom 3D models and realistic sound effects.

## Features

- **16 Different Instruments**: Piano, Bass Drum, Snare Drum, Sticks, Bass Guitar, Flute, Bell, Guitar, Chime, Xylophone, Iron Xylophone, Cow Bell, Didgeridoo, Bit, Banjo, and Pling
- **Modern Item Models**: Uses Paper's Data Component API with `item_model` for custom 3D models
- **Hotbar Mode**: Play instruments directly from your hotbar
- **Scale System**: Built-in musical scales for organized playing
- **Paper API Support**: Optimized for Paper servers with modern APIs

## Requirements

- **Server**: Paper 1.21+ (recommended) or Spigot 1.21+
- **Java**: Java 21+
  
## Configuration

The plugin uses a modern configuration system with `item_model` support:

```yaml
settings:
  instruments:
    permissions: true

instruments:
  piano:
    item_model: nexo:instrument_piano
  bass_drum:
    item_model: nexo:instrument_bass_drum
  # ... more instruments
```

## Commands

- `/instruments` - Open the instruments menu
- `/instruments give <instrument>` - Give yourself an instrument

## Development

This project uses Gradle for building:

```bash
./gradlew build
```

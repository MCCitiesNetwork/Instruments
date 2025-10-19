# Instruments

A modern Minecraft plugin for Paper/Spigot that adds playable musical instruments to your server. Features custom 3D models and realistic sound effects.

## Features

- **16 Different Instruments**: Piano, Bass Drum, Snare Drum, Sticks, Bass Guitar, Flute, Bell, Guitar, Chime, Xylophone, Iron Xylophone, Cow Bell, Didgeridoo, Bit, Banjo, and Pling
- **Modern Item Models**: Uses Paper's Data Component API with `item_model` for custom 3D models
- **Resource Pack Integration**: Supports custom models via resource packs (namespace: `nexo`)
- **Hotbar Mode**: Play instruments directly from your hotbar
- **Scale System**: Built-in musical scales for organized playing
- **Paper API Support**: Optimized for Paper servers with modern APIs

## Requirements

- **Server**: Paper 1.21+ (recommended) or Spigot 1.21+
- **Java**: Java 21+
- **Resource Pack**: Custom models require a resource pack with the `nexo` namespace

## Installation

1. Download the latest JAR from the releases page
2. Place it in your server's `plugins` folder
3. Restart your server
4. Configure instruments in `plugins/Instruments/config.yml`
5. (Optional) Install the resource pack for custom 3D models

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

## Resource Pack

To use custom 3D models, create a resource pack with models in the `nexo` namespace:

```
assets/nexo/models/item/instrument_piano.json
assets/nexo/models/item/instrument_bass_drum.json
# ... etc
```

## Commands

- `/instruments` - Open the instruments menu
- `/instruments give <instrument>` - Give yourself an instrument
- `/instruments scales` - Open the scales menu

## Development

This project uses Gradle for building:

```bash
./gradlew build
```

### Key Features

- **Data Component API**: Uses Paper's modern `item_model` system instead of deprecated `CustomModelData`
- **Adventure Components**: Modern text formatting with Adventure API
- **PacketEvents Integration**: Optimized packet handling for smooth gameplay
- **bStats Integration**: Anonymous usage statistics

## Download

- **Spigot**: https://www.spigotmc.org/resources/instruments.87156/
- **GitHub Releases**: https://github.com/Cup0fCode/Instruments/releases

## Screenshots

<img src="https://i.imgur.com/bHaADDl.png" width="128">
<img src="https://i.imgur.com/aaBIVzW.png" width="128">
<img src="https://i.imgur.com/HGaBHQm.png" width="128">

## Issues & Support

- **GitHub Issues**: https://github.com/Cup0fCode/Instruments/issues
- **Discord**: [Join our Discord](https://discord.gg/your-discord-link)

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

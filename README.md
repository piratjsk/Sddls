# Sddls
Bukkit plugin that lets you protect mounts with signed saddles (and carpets).

**Spigot**: [spigotmc.org/resources/sddls.24049/](https://www.spigotmc.org/resources/sddls.24049/)  
**BukkitDev**: [dev.bukkit.org/projects/sddls](https://dev.bukkit.org/projects/sddls)

## Usage
Mounts (horses, donkeys, mules, pigs, lamas) wearing signed saddle (or carpet in case of lamas) will be protected:
- from damage caused by other players and enviroment when no one is ridding them,
- from damage caused by other players when someone is ridding them,
- from being accessed by other players.

Players signed on the saddle will always have ability to damage, access inventory and ride protected mounts.

### How to sign a saddle?
Just put it into crafting grid.  
> If saddle is already signed by the player who tries to sign it then all signatures will be wiped.  
> So give it only to players who you trust.

## Building
This plugin uses [Gradle](https://gradle.org/) to manage dependecies and build process.  
To create plugin file that can be dropped into server's `/pluigns/` folder run command:  
- on windows: `gradlew build`  
- on linux/mac: `./gradlew build`  

Compiled plugin file will be generated in `/build/libs/` and named `Sddls-{version}.jar` where `{version}` will be replaced with plugin version.

## License
Copyright (c) 2016-2018 Adam Poloczek (piratjsk)  
This project is licensed under [MIT License](https://github.com/piratjsk/Sddls/blob/master/LICENSE).

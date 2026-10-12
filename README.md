![](https://i.imgur.com/GtapsfE.png)

<center>

[![](https://i.imgur.com/Dv780sE.png)](https://www.curseforge.com/minecraft/mc-mods/fabric-api)
[![](https://i.imgur.com/c1fCoHy.png)](https://discord.gg/7BSqZa9r3P)
[![](https://i.imgur.com/GH0SZoc.png)](https://github.com/KxmischesDomi/MoreBannerFeatures/issues)

[![Modrinth](https://img.shields.io/modrinth/dt/FOaR4sIZ?logo=modrinth&style=for-the-badge)](https://modrinth.com/mod/more-banner-features) [![Modrinth](https://img.shields.io/modrinth/game-versions/FOaR4sIZ?logo=modrinth&style=for-the-badge)](https://modrinth.com/mod/more-banner-features)

[![Downloads](http://cf.way2muchnoise.eu/full_515000_downloads.svg?badge_style=for_the_badge)](https://www.curseforge.com/minecraft/mc-mods/more-banner-features) [![Versions](http://cf.way2muchnoise.eu/versions/515000.svg?badge_style=for_the_badge)](https://www.curseforge.com/minecraft/more-banner-features)  


</center>

&nbsp;

## Features

*   Banners on Entities: Happy Ghast, Boat, Horse, Mule, Donkey, Strider, Villager
*   Players can equip Banners as: Cape, Hat (Support for Slot Mods)
*   Combine chestplates and helmets with Banners to equip both
*   Banner Threads item _(combine with a banner to increase the max layer limit)_

### Beta Features (Must be enabled in config)

*   Minecart Banners

## Compatibilities

*   Trinkets / Trinkets Canary
*   Accessories

***

## Features (before v2.0)

*   Equip Banners as Capes (Cape Slot if [Trinkets](https://www.curseforge.com/minecraft/mc-mods/trinkets-fabric) installed)
*   Put Banners on Horses, Donkeys, Mules and Llamas with a new inventory slot
*   Put Banners with right-click on Pigs, Striders, Boats & Villagers
*   Remove Banners from Pigs, Striders, Boats & Villagers with Shears
*   Hang Banners below blocks
*   Fix banner position in fox mouth
*   Fix enchanting glint on enchanted banners
*   Config menu accessible through [ModMenu](https://www.curseforge.com/minecraft/mc-mods/modmenu)

## Compatibilities (before v2.0)

*   [Boat Container](https://www.curseforge.com/minecraft/mc-mods/boat-container) — By KxmischesDomi
*   [Wavey Capes](https://www.curseforge.com/minecraft/mc-mods/waveycapes) — By tr9zw

## Incompatibilities (before v2.0)

*   [Entity Banners](https://www.curseforge.com/minecraft/mc-mods/entity-banners) — Weird rendering bugs
*   [Glow Banners](https://www.curseforge.com/minecraft/mc-mods/glow-banners) — Banners don't glow
 
![](https://u.svrt.dev/u/edible-same-alaskanhusky.jpg)

![](https://u.svrt.dev/u/calm-cavernous-barnowl.jpg)

![](https://u.svrt.dev/u/eager-black-angwantibo.jpg)

![](https://u.svrt.dev/u/raw-deadly-americanwigeon.gif)

![](https://i.imgur.com/UEcEcL6.png)

![](https://i.imgur.com/BInqA1P.png)

![](https://i.imgur.com/WCxzehM.png)

![](https://i.imgur.com/As8JsDk.png)

![](https://i.imgur.com/0j1QtKa.png)

***

## Building

One branch builds every Minecraft version and mod loader with [Stonecutter](https://stonecutter.kikugie.dev/). Each version and loader pair is a node under `versions/<version>-<loader>`, built by `build.<loader>.gradle.kts`.

```
./gradlew buildAndCollect
```

builds every node and copies the jars to `build/libs/<mod version>/`. One node alone: `./gradlew :26.3-neoforge:build`.

The sources are written for the active node (`stonecutter active` in `stonecutter.gradle.kts`, kept on the newest Fabric node). Code for other versions or loaders sits in `//? if` comments that Stonecutter swaps in when it builds that node:

*   A Minecraft call that only changed its name or arguments gets an `//? if >=26.3 { ... } else { ... }` right where it is called.
*   Anything that differs between loaders goes through `platform/Platform` or a loader's entrypoint in the `fabric`, `neoforge` and `forge` packages, never inline in feature code.

### Adding the next Minecraft version

1.  In `settings.gradle.kts`, add `mc("<version>", "fabric", "neoforge", "forge")` above the current newest line, with only the loaders that have a release for it.
2.  In `stonecutter.properties.toml`, add a `["<version>"]` block and one `[<loader>."<version>"]` block per loader with its dependency versions. Copy the newest blocks and bump the numbers. Forge needs `mod.pack_format` from the Forge MDK's `pack.mcmeta`.
3.  Set `vcsVersion` in `settings.gradle.kts` to `<version>-fabric` and run `./gradlew "Set active project to <version>-fabric"`, which rewrites the sources and `stonecutter.gradle.kts` for the new node. Fix the compile errors on the new version; every fix that breaks an older node gets an `//? if` with the old code in the `else` branch.
4.  Run `./gradlew buildAndCollect` and make sure every node still builds.

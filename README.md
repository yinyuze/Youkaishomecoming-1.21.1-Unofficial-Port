# Gensokyo Legacy (Youkai's Homecoming 1.21.1 Unofficial Port)

[![License](https://img.shields.io/badge/license-LGPL--2.1-blue.svg)](LICENSE)
[![Minecraft](https://img.shields.io/badge/Minecraft-1.21.1-green.svg)]()
[![NeoForge](https://img.shields.io/badge/NeoForge-21.1.217%2B-orange.svg)]()

An **unofficial port** of [Youkai's Homecoming](https://www.curseforge.com/minecraft/mc-mods/youkais-homecoming) by **lcy0x1** to Minecraft 1.21.1 / NeoForge, ported primarily by **yinyuze**.

Contains essentially all content from the 1.20.1 version (food, cooking, building, characters, danmaku system, etc.), with adjustments for the 1.21.1 / NeoForge ecosystem.

---

## Attribution

- **Original mod**: [Youkai's Homecoming](https://www.curseforge.com/minecraft/mc-mods/youkais-homecoming) by **lcy0x1**
- **Original source**: <https://github.com/Minecraft-LightLand/Youkai-Homecoming>
- **License**: LGPL-2.1-only (inherited from upstream)
- **This 1.21.1 port**: maintained by **yinyuze**, with permission from lcy0x1

This is a community-maintained port, made with permission from lcy0x1. All original concepts, art, and code design credit belong to lcy0x1 and the upstream contributors. The port code is licensed under the same LGPL-2.1 license as the original.

---

## What's Included

Most 1.20.1 features have been ported and are functional in 1.21.1:

- **Food & cooking system** (cooking pot, kettle, moka pot, basin, steamer, cuisine board, sauce rack, ingredient rack, wine shelf, etc.)
- **Touhou characters** (Reimu, Cirno, Rumia and more) with custom AI, trading, spell cards
- **Danmaku system** (via [danmaku_api](https://github.com/Lcy0x1/DanmakuAPI))
- **Character equipment**: Reimu's hairband, Rumia's hairband, Cirno's hairband / wings, Suwako's hat, Koishi's hat, Straw hat — all with their original mechanics
- **Reputation system**, **donation box → summon Reimu**, **flesh / blood mechanics**, **frog god hat progression**
- **Custom structures** (Hakurei Shrine, Cirno's Nest, Youkai Nest)
- **JEI integration** for all custom recipe categories

---

## Curios Integration (Soft Dependency)

All hats and hairbands work as standard armor (head slot) **and** as Curios items if [Curios API](https://www.curseforge.com/minecraft/mc-mods/curios) is installed.

- **No hard dependency** — the mod works without Curios.
- When Curios is present, every hat/hairband automatically gains a Curios capability registration, so you can put them in any compatible slot.
- Mechanics that check for "wearing a specific hat" (e.g., Rumia hairband enabling skull drops, Reimu hairband enabling flight, hat-based danmaku-cost reduction, etc.) check **both** the vanilla head slot **and** all Curios slots.
- Danmaku-cost reduction from multiple hats **stacks** across Curios slots (useful when wearing multiple hats of different colors).

---

## Mod Compatibility

- **Lithium**: Frog AI mixin uses `@ModifyReturnValue` instead of `@WrapOperation`, so Suwako-hat frogs still attack pillagers even when Lithium's `frog_attackables` optimization is active.
- **Kaleidoscope (Cookery)**: Cooking-pot and cutting-board recipes are generated under the `youkaishomecoming` namespace (rather than `farmersdelight`) to avoid being silently overridden by Kaleidoscope.
- **Farmer's Delight**: Hard dependency — required for cooking pot / cutting board recipe types.

---

## Building from Source

Requirements:

- JDK 21
- Gradle (use the included wrapper)

```bash
git clone https://github.com/yinyuze/Youkaishomecoming-1.21.1-Unofficial-Port.git
cd Youkaishomecoming-1.21.1-Unofficial-Port
./gradlew build
```

The built jar will appear in `build/libs/`.

The `libs/` directory contains the project's library dependencies (`l2core`, `l2serial`, `l2modularblocks`, `danmaku_api`, `fast_projectile_api`, etc.) since some of these are not yet published to a public Maven repository for 1.21.1. They are required for compilation and are bundled into the final jar via `jarJar`.

---

## Reporting Issues

For bugs / suggestions specific to this 1.21.1 port, please open an issue on this repository.

For issues that also exist in the official 1.20.1 version, please report them to the [upstream repository](https://github.com/Minecraft-LightLand/Youkai-Homecoming) instead.

---

## License

This project is licensed under **LGPL-2.1-only**, same as the upstream Youkai's Homecoming. See [LICENSE](LICENSE) for full text.

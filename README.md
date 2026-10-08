# Totally Accurate Battle Game

A turn-based fantasy RPG battle game built with **Java Swing**, developed as an Object-Oriented Programming course project (2025).

## Features
- Turn-based combat against Slimes, Skeletons, Yetis and a final Boss
- Elemental damage system (fire / ice) via `ElementCalculator`
- Inventory with weapons, shields, amulets, staffs and potions (health, mana, strength, defense, combo)
- Level / map progression, background music and custom pixel-art assets

## OOP Design
| Package | Responsibility |
|---|---|
| `model.character` | Abstract `Character` hierarchy → `Player`, `Enemy`, `Boss`, element-specific enemies |
| `model.combat` | `Attackable`, `SkillUser` interfaces, `ElementType`, damage calculation |
| `model.item`, `model.inventory` | `Item` hierarchy (`EquipableItem`, `UsableItem`, `Potion`, …) and inventory |
| `service` | `BattleManager`, `EnemyFactory`, `ItemFactory` (factory pattern), `MusicPlayer` |
| `ui` | Swing views (`BattleSceneView`, menus, panels) using MigLayout |

Demonstrates inheritance, polymorphism, abstraction, interfaces, encapsulation and the factory pattern.

## Running
Open the project in IntelliJ IDEA, add the JARs in `lib/` (MigLayout, JLayer) as libraries, and run `proje.main.GameApplication`.

**Tools:** Java, Swing, MigLayout, JLayer

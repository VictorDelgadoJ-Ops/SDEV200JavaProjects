# RPG Dungeon Diver

RPG Dungeon Diver is a turn-based Java roguelike. Explore a tiled dungeon, defeat every enemy on each floor, collect rewards, level up, and descend until the enemies defeat you. Each run uses a seed to place enemies reproducibly, while deeper floors add more and stronger enemies.

## Requirements

- Java Development Kit (JDK) 8 or newer
- Windows with PowerShell for the included scripts
- No external libraries or network connection

## Run the Game

1. Open this `RPGDungeonDiver` folder in VS Code.
2. Open the integrated PowerShell terminal in this folder.
3. Run `.
un.ps1`.
4. Choose **NEW** to name a diver, or **LOAD** to resume a saved run.

If PowerShell blocks local scripts, run `powershell -ExecutionPolicy Bypass -File .\run.ps1` from this folder. The script compiles the source into `build/classes` and launches the Swing application. Alternatively, compile the `src` directory with `javac` and run `rpgdungeon.Main` from the resulting classpath.

## How to Play

- Move one tile with **W/A/S/D** or the **arrow keys**.
- Walk into an enemy to strike it, or use **Space** to attack an adjacent enemy.
- Press **H** or select **DRINK POTION** to restore health. Drinking consumes a turn.
- Defeat all enemies, find the gold `>` stair tile, then press **E** or select **DESCEND STAIRS**.
- Enemies move after a valid player action. Walls block movement and do not consume a turn.
- Enemies grow stronger and more numerous as the floor number rises. A run ends when health reaches zero.
- Use **SAVE** and **LOAD** to preserve or resume a run. **SCORES** displays the five highest recorded runs.

Saves and high scores are stored in `%USERPROFILE%\.rpg-dungeon-diver`. A saved run includes diver statistics, location, floor, and each remaining enemy's type, position, and health. High-score rows that cannot be parsed are skipped so a damaged row does not hide the rest of the table.

## Test the Game Rules

Run `.	est.ps1` from PowerShell. The script compiles production and test sources, then executes the dependency-free regression suite. It checks inheritance-based attacks, potions, leveling, blocked/open movement, turn counting, full save restoration, and invalid-save rejection.

## Project Structure

```text
RPGDungeonDiver/
  src/rpgdungeon/       Application, GUI, game rules, and persistence
  test/rpgdungeon/      Automated game-logic regression checks
  run.ps1               Compile and launch
  test.ps1              Compile and run tests
```

## Course Concepts Demonstrated

- **Classes, objects, encapsulation, and methods:** `Player`, `Enemy`, `DungeonFloor`, and `GameSession` own state and expose focused operations.
- **Inheritance and polymorphism:** abstract `Actor` is extended by `Player` and `Enemy`; `Goblin`, `Skeleton`, and `Wraith` provide their own symbols and rewards through overridden methods.
- **Selection and looping:** combat outcomes, movement, enemy turns, save validation, score parsing, and the Swing event loop use conditionals and loops.
- **Arrays:** `char[][]` represents the dungeon map; a fixed `Enemy[]` tracks enemy slots; coordinate arrays support breadth-first enemy pathfinding.
- **File I/O:** `SaveManager` reads/writes Java `Properties` saves and an append-only TSV high-score file.
- **Exception processing:** missing or malformed saves and file errors are surfaced to the GUI; malformed individual high-score rows are ignored.
- **GUI programming:** Swing builds the window, buttons, progress bars, keyboard bindings, dialogs, and custom-painted dungeon view.
- **Testing:** `GameLogicTest` exercises model behavior without opening a window or requiring third-party dependencies.

## Main Classes

- `Main` launches the app on Swing's event-dispatch thread.
- `GameWindow` builds the interface and connects controls to the session.
- `DungeonView` renders tiles, characters, stairs, and the end-of-run state.
- `GameSession` applies player actions, enemy turns, combat, rewards, and floor progression.
- `Actor`, `Player`, `Enemy`, `Goblin`, `Skeleton`, and `Wraith` contain the inheritance-based character model.
- `DungeonFloor` owns the tile and enemy arrays and finds routes through the map.
- `SaveManager` handles disk persistence and high scores.
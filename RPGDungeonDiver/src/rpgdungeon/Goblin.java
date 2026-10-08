package rpgdungeon;

/** A quick, lightly armored enemy common in the upper dungeon. */
public final class Goblin extends Enemy {
    public Goblin(int x, int y, int depth) {
        // Goblins are weaker early enemies, with stats that rise by floor.
        super("Goblin", 9 + depth * 2, 3 + depth / 3, 10 + depth, 4 + depth, x, y);
    }

    @Override
    public char getSymbol() {
        return 'g';
    }

    @Override
    public String getTypeId() {
        return "goblin";
    }
}
package rpgdungeon;

/** A dangerous deep-dungeon enemy with high attack power. */
public final class Wraith extends Enemy {
    public Wraith(int x, int y, int depth) {
        // Wraiths hit hard, so they are a bigger threat on later floors.
        super("Wraith", 11 + depth * 2, 5 + depth / 2, 18 + depth, 8 + depth, x, y);
    }

    @Override
    public char getSymbol() {
        return 'w';
    }

    @Override
    public String getTypeId() {
        return "wraith";
    }
}
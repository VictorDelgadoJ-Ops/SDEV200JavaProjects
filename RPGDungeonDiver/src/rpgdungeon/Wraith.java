package rpgdungeon;

/** A high-damage enemy that becomes stronger and more rewarding on deeper floors. */
public final class Wraith extends Enemy {
    public Wraith(int x, int y, int depth) {
        // Wraiths have the strongest starting attack, and their stats rise with floor depth.
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
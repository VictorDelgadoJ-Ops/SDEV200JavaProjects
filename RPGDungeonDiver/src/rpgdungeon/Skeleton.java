package rpgdungeon;

/** A steady melee enemy that grows more durable on deeper floors. */
public final class Skeleton extends Enemy {
    public Skeleton(int x, int y, int depth) {
        // Skeletons have more health than goblins and get tougher deeper down.
        super("Skeleton", 13 + depth * 2, 4 + depth / 2, 14 + depth, 6 + depth, x, y);
    }

    @Override
    public char getSymbol() {
        return 's';
    }

    @Override
    public String getTypeId() {
        return "skeleton";
    }
}
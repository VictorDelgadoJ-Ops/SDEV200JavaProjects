package rpgdungeon;

/** A tougher melee enemy whose health, attack, and rewards increase with floor depth. */
public final class Skeleton extends Enemy {
    public Skeleton(int x, int y, int depth) {
        // Compared with a goblin, a skeleton starts tougher and gives a larger reward.
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
package rpgdungeon;

/** The basic enemy type, with stats that increase gradually as the player goes deeper. */
public final class Goblin extends Enemy {
    public Goblin(int x, int y, int depth) {
        // Goblins start with lower stats than the other enemy types, then scale by depth.
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
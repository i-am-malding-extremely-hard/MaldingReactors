package im.malding.maldingreactors.content;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

import java.util.HashSet;
import java.util.Set;

public class Coolant {

    //Should it be a ratio based system

    private static Set<Coolant> ALL_COOLANTS = new HashSet<>();

    private static final Coolant DEFAULT = new Coolant(Blocks.AIR, 10);

    private static final Coolant WATER = new Coolant(Blocks.WATER, 100);

    private static final Coolant IRON = new Coolant(Blocks.IRON_BLOCK, 300);
    private static final Coolant EMERALD = new Coolant(Blocks.EMERALD_BLOCK, 600);
    private static final Coolant DIAMOND = new Coolant(Blocks.DIAMOND_BLOCK, 1200);
    private static final Coolant NETHERITE = new Coolant(Blocks.NETHERITE_BLOCK, 2400);

    public final int coolingValue;
    public final Block coolingBlock;

    public Coolant(Block block, int coolingValue) {
        this.coolingBlock = block;
        this.coolingValue = coolingValue;

        ALL_COOLANTS.add(this);
    }

    public static Coolant getOrDefaultCoolant(Block block) {
        for (Coolant coolant : ALL_COOLANTS) {
            if (coolant.coolingBlock == block) {
                return coolant;
            }
        }

        return DEFAULT;
    }
}

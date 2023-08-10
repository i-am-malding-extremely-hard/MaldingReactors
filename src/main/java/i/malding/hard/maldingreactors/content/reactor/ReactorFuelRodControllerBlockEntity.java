package i.malding.hard.maldingreactors.content.reactor;

import i.malding.hard.maldingreactors.content.MaldingBlockEntities;
import i.malding.hard.maldingreactors.content.handlers.ReactorFuelRodControllerScreenHandler;
import io.wispforest.owo.nbt.NbtKey;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class ReactorFuelRodControllerBlockEntity extends ReactorBaseBlockEntity implements NamedScreenHandlerFactory {

    public static final NbtKey<Integer> REACTION_RATE_KEY = new NbtKey<>("ReactionRate", NbtKey.Type.INT);

    public int reactionRate = 0;

    public ReactorFuelRodControllerBlockEntity(BlockPos pos, BlockState state) {
        super(MaldingBlockEntities.REACTOR_FUEL_ROD_CONTROLLER, pos, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);

        nbt.put(REACTION_RATE_KEY, reactionRate);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        this.reactionRate = nbt.get(REACTION_RATE_KEY);
    }

    @Override
    public Text getDisplayName() {
        return Text.of("Reactor Rod Controller");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new ReactorFuelRodControllerScreenHandler(syncId, ScreenHandlerContext.create(this.world, this.pos));
    }

    //-------------------------------------------------------------------------------

}

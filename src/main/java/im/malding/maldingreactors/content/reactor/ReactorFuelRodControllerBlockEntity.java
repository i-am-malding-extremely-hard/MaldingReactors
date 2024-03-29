package im.malding.maldingreactors.content.reactor;

import im.malding.maldingreactors.content.MaldingBlockEntities;
import im.malding.maldingreactors.content.handlers.ReactorFuelRodControllerScreenHandler;
import io.wispforest.owo.serialization.Endec;
import io.wispforest.owo.serialization.endec.KeyedEndec;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class ReactorFuelRodControllerBlockEntity extends ReactorBaseBlockEntity implements NamedScreenHandlerFactory {

    public static final KeyedEndec<Integer> REACTION_RATE_KEY = Endec.INT.keyed("ReactionRate", 0);

    public int reactionRate = 100;

    public ReactorFuelRodControllerBlockEntity(BlockPos pos, BlockState state) {
        super(MaldingBlockEntities.REACTOR_FUEL_ROD_CONTROLLER, pos, state);
    }

    public void setReactionRate(int reactionRate){
        this.reactionRate = reactionRate;

        this.markDirty();
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
        PacketByteBuf openingData = PacketByteBufs.create();
        openingData.writeBlockPos(pos);
        return new ReactorFuelRodControllerScreenHandler(syncId, ScreenHandlerContext.create(this.world, this.pos), player.getInventory(), openingData);
    }

    //-------------------------------------------------------------------------------

}

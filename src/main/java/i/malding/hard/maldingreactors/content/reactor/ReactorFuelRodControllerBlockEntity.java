package i.malding.hard.maldingreactors.content.reactor;

import i.malding.hard.maldingreactors.content.MaldingBlockEntities;
import i.malding.hard.maldingreactors.content.handlers.ReactorFuelRodControllerScreenHandler;
import i.malding.hard.maldingreactors.util.CollectionNbtKey;
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

import java.util.LinkedHashSet;
import java.util.Set;

public class ReactorFuelRodControllerBlockEntity extends ReactorBaseBlockEntity implements NamedScreenHandlerFactory {

    public static NbtKey.Type<BlockPos> BLOCK_POS = NbtKey.Type.LONG.then(BlockPos::fromLong, BlockPos::asLong);

    public static final NbtKey<Integer> REACTION_RATE_KEY = new NbtKey<>("ReactionRate", NbtKey.Type.INT);

    private static final CollectionNbtKey<BlockPos, Set<BlockPos>> ADJOURNING_FUEL_RODS_KEY = new CollectionNbtKey<>("ConnectedRods", BLOCK_POS, LinkedHashSet::new);

    public int reactionRate = 0;

    private Set<BlockPos> adjourningFuelRods = new LinkedHashSet<>();

    public ReactorFuelRodControllerBlockEntity(BlockPos pos, BlockState state) {
        super(MaldingBlockEntities.REACTOR_FUEL_ROD_CONTROLLER, pos, state);
    }

    @Override
    public void serverTick() {

    }

    @Override
    public void onRemoval(BlockPos pos) {
        if (this.isFullMultipartStructure()) {
            this.getController().ROD_CONTROLLERS.remove(this);
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);

        nbt.put(REACTION_RATE_KEY, reactionRate);

        this.writeFuelRodPositions(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        this.reactionRate = nbt.get(REACTION_RATE_KEY);

        this.readFuelRodPositions(nbt);
    }

    public void setAdjourningFuelRods(Set<BlockPos> adjourningFuelRods) {
        this.adjourningFuelRods = adjourningFuelRods;
    }

    public Set<BlockPos> getAdjourningFuelRods() {
        return this.adjourningFuelRods;
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

    public void writeFuelRodPositions(NbtCompound nbt) {
        ADJOURNING_FUEL_RODS_KEY.putCollection(nbt, adjourningFuelRods);
    }

    public void readFuelRodPositions(NbtCompound nbt) {
        this.adjourningFuelRods = ADJOURNING_FUEL_RODS_KEY.getCollection(nbt);
    }
}

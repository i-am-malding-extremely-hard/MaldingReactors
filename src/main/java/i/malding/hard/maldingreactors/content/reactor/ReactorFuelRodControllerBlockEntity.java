package i.malding.hard.maldingreactors.content.reactor;

import i.malding.hard.maldingreactors.content.MaldingBlockEntities;
import i.malding.hard.maldingreactors.content.handlers.ReactorFuelRodControllerScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class ReactorFuelRodControllerBlockEntity extends ReactorBaseBlockEntity implements NamedScreenHandlerFactory {

    private static final String REACTION_RATE_KEY = "ReactionRate";
    private static final String ADJOURNING_FUEL_RODS_KEY = "ConnectedRods";

    public int reactionRate = 0;

    private Set<ReactorFuelRodBlockEntity> adjourningFuelRods = new HashSet<>();

    public ReactorFuelRodControllerBlockEntity(BlockPos pos, BlockState state) {
        super(MaldingBlockEntities.REACTOR_FUEL_ROD_CONTROLLER, pos, state);
    }

    @Override
    public void serverTick() {

    }

    @Override
    public void onRemoval(BlockPos pos) {
        if(this.isFullMultipartStructure()){
            this.getController().ROD_CONTROLLERS.remove(this);
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);

        nbt.putInt(REACTION_RATE_KEY, reactionRate);
        this.writeFuelRodPositions(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        this.reactionRate = nbt.getInt(REACTION_RATE_KEY);
        this.readFuelRodPositions(nbt);
    }

    public void setAdjourningFuelRods(Set<ReactorFuelRodBlockEntity> adjourningFuelRods){
        this.adjourningFuelRods = adjourningFuelRods;
    }

    public Set<ReactorFuelRodBlockEntity> getAdjourningFuelRods(){
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
        NbtList list = new NbtList();

        for (ReactorFuelRodBlockEntity reactorFuelRodBlockEntity : adjourningFuelRods) {
            list.add(NbtHelper.fromBlockPos(reactorFuelRodBlockEntity.getPos()));
        }

        nbt.put(ADJOURNING_FUEL_RODS_KEY, list);
    }

    public void readFuelRodPositions(NbtCompound nbt) {
        NbtList list = nbt.getList(ADJOURNING_FUEL_RODS_KEY, NbtList.COMPOUND_TYPE);
        Set<ReactorFuelRodBlockEntity> adjoiningFuelRods = new HashSet<>();

        for (NbtElement compound : list) {
            adjoiningFuelRods.add((ReactorFuelRodBlockEntity) world.getBlockEntity(NbtHelper.toBlockPos((NbtCompound) compound)));
        }

        this.adjourningFuelRods = adjoiningFuelRods;
    }
}

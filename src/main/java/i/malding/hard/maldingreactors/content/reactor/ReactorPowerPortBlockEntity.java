package i.malding.hard.maldingreactors.content.reactor;

import i.malding.hard.maldingreactors.content.MaldingBlockEntities;
import i.malding.hard.maldingreactors.content.handlers.ReactorItemPortScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ReactorPowerPortBlockEntity extends ReactorComponentBlockEntity {

    public ReactorPowerPortBlockEntity(BlockPos pos, BlockState state) {
        super(MaldingBlockEntities.REACTOR_POWER_PORT, pos, state);
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state) {

    }
}

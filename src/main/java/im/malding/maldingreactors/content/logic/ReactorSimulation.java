package im.malding.maldingreactors.content.logic;

import im.malding.maldingreactors.content.MaldingBlockEntities;
import im.malding.maldingreactors.content.MediumProperties;
import im.malding.maldingreactors.content.MediumRegistry;
import im.malding.maldingreactors.content.reactor.ReactorControllerBlockEntity;
import im.malding.maldingreactors.util.BlockEntityUtils;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.EightWayDirection;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.apache.commons.lang3.mutable.MutableDouble;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2i;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

public class ReactorSimulation {

    public static final List<EightWayDirection> EIGHT_WAY_DIRECTIONS = new ArrayList<>(List.of(EightWayDirection.values()));

    public static final double BASE_GAMMA_RAY_ENERGY = 10.0;
    public static final double BASE_NEUTRON_ENERGY = 4.0;

    public static final double DISTANCE_DECAY_PERCENTAGE = 0.01;

    public ReactorControllerBlockEntity blockEntity;

    public boolean isDirty = true;

    public Map<BlockPos, MutableDouble> EM_STATE_LOOKUP = new HashMap<>();
    public Map<BlockPos.Mutable, List<EMWave>> WAVES_LOOKUP = new HashMap<>();

    public Set<EMWave> WAVES = new HashSet<>();

    public ReactorSimulation(ReactorControllerBlockEntity blockEntity){
        this.blockEntity = blockEntity;

        init();
    }

    public void init(){
        if(blockEntity.reactorBounds == null) return;

        blockEntity.reactorBounds.forEachVertex(blockPos -> {
            //MATERIAL_STATE_LOOKUP.put(blockPos, new MaterialState(0.0));
            EM_STATE_LOOKUP.put(blockPos, new MutableDouble(0.0));
        });
    }

    public void tick(){
        /*if(isDirty){
            init();

            isDirty = false;
        }*/

        if(!this.blockEntity.isReactorActive()) return;

        var world = this.blockEntity.getWorld();

        assert world != null;

        Map<Vector2i, Integer> reactionRates = new HashMap<>();

        getCollection(world, be -> be.rodControllers, MaldingBlockEntities.REACTOR_FUEL_ROD_CONTROLLER)
                .forEach(fuelRodController -> {
                    var pos = fuelRodController.getPos();

                    reactionRates.put(new Vector2i(pos.getX(), pos.getZ()), fuelRodController.reactionRate);
                });

        var fuelTank = this.blockEntity.getFuelTank();

        var fuelPercentage = MathHelper.clamp(fuelTank.amount / (double) fuelTank.getCapacity(), 0, 1);

        if(fuelPercentage != 0) {
            getCollection(world, be -> be.fuelRods, MaldingBlockEntities.REACTOR_FUEL_ROD)
                    .forEach(fuelRod -> {
                        var pos = fuelRod.getPos();

                        var reactionRate = reactionRates.get(new Vector2i(pos.getX(), pos.getZ()));

                        if(reactionRate == 0) return;

                        var amountOfGammaRays = MathHelper.floor(reactionRate * 4);
                        var amountOfNeutrons = MathHelper.ceil(reactionRate * 8);

                        getRandomDirections(world, amountOfGammaRays)
                                .forEach(direction -> {
                                    BlockPos.Mutable startPos = pos.mutableCopy();

                                    direction.getDirections().forEach(startPos::offset);

                                    WAVES.add(new EMWave(startPos, direction, BASE_GAMMA_RAY_ENERGY * fuelPercentage, EMType.GAMMA_RAY));
                                });

                        getRandomDirections(world, amountOfNeutrons)
                                .forEach(direction -> {
                                    BlockPos.Mutable startPos = pos.mutableCopy();

                                    direction.getDirections().forEach(startPos::offset);

                                    WAVES.add(new EMWave(startPos, direction, BASE_NEUTRON_ENERGY * fuelPercentage, EMType.NEUTRON));
                                });
                    });
        }

        // Action: Interact with the given position properties for the type
        for (EMWave wave : WAVES) {
            if(wave.lifeTime == 0 || wave.energy == 0) continue;

            var mediumProperties = mediumAtPosition(wave.position);
            var state = EM_STATE_LOOKUP.get(wave.position);

            if(wave.type() == EMType.GAMMA_RAY){
                var fundamentalProp = mediumProperties.gammaRayProps;

                var random = world.getRandom();

                //--
                var reflectChance = random.nextInt(101);

                if(reflectChance < fundamentalProp.reflectivity() * 100){
                    wave.direction = reflectedDirection(random, wave.direction);
                }
                //--

                //--
                var absorptionChance = random.nextInt(101);

                if(absorptionChance < fundamentalProp.reflectivity() * 100){
                    state.add(wave.energy * fundamentalProp.conversion());

                    wave.energy = 0;
                }
                //--
            } else if(wave.type() == EMType.NEUTRON){
                var fundamentalProp = mediumProperties.neutronProps;

                var random = world.getRandom();

                //--
                var reflectChance = random.nextInt(101);

                if(reflectChance < fundamentalProp.reflectivity() * 100){
                    wave.direction = reflectedDirection(random, wave.direction);
                }
                //--

                //--
                var absorptionChance = random.nextInt(101);

                if(absorptionChance < fundamentalProp.reflectivity() * 100){
                    state.add(wave.energy * fundamentalProp.conversion());

                    wave.energy = 0;
                }
                //--
            } else {
                state.add(wave.energy * mediumProperties.conductivity());

                wave.energy = 0;
            }
        }

        WAVES.removeIf(emWave -> emWave.energy == 0);
        //--

        // Condition: If energy level is above 0, or wasn't
        // Action: Then move in the direction of the given wave

        WAVES.forEach(emWave -> {
            emWave.direction.getDirections().forEach(emWave.position::offset);
            emWave.energy *= DISTANCE_DECAY_PERCENTAGE;
        });

        //--

        EM_STATE_LOOKUP.forEach((blockPos, mutableDouble) -> {
            var energyAmount = mutableDouble.getValue();

            for (EightWayDirection direction : EIGHT_WAY_DIRECTIONS) {
                var position = blockPos.mutableCopy();

                direction.getDirections().forEach(position::offset);

                WAVES.add(new EMWave(position, direction, energyAmount / 8, EMType.HEAT));
            }

            mutableDouble.setValue(0);
        });
    }

    public EightWayDirection reflectedDirection(Random random, EightWayDirection direction){
        int leftIndex = direction.ordinal() - 1;
        int rightIndex = direction.ordinal() + 1;

        if(leftIndex < 0) leftIndex = 7;
        if(rightIndex > 7) leftIndex = 0;

        var possibleDirections = new EightWayDirection[]{EIGHT_WAY_DIRECTIONS.get(leftIndex), direction, EIGHT_WAY_DIRECTIONS.get(rightIndex)};

        return oppositeDirection(Util.getRandom(possibleDirections, random));
    }

    @Nullable
    public EightWayDirection oppositeDirection(EightWayDirection direction){
        Set<Direction> directions = new HashSet<>();

        direction.getDirections().forEach(axisDir -> directions.add(axisDir.getOpposite()));

        EightWayDirection oppositeDirection = null;

        for (EightWayDirection dir : EIGHT_WAY_DIRECTIONS) {
            if(dir.getDirections().equals(directions)) oppositeDirection = dir;
        }

        return oppositeDirection;
    }

    public Set<EightWayDirection> getRandomDirections(World world, int amount){
        Set<EightWayDirection> directions = new HashSet<>();

        var random = world.random;

        while (directions.size() < amount){
            var direction = Util.getRandom(EightWayDirection.values(), random);

            if (!directions.contains(direction)) directions.add(direction);
        }

        return directions;
    }

    public MediumProperties mediumAtPosition(BlockPos blockPos){
        var state = this.blockEntity.getWorld().getBlockState(blockPos);

        return MediumRegistry.getProperty(state.getBlock());
    }

    public Set<EMWave> wavesAtPosition(BlockPos pos){
        Set<EMWave> waves = new HashSet<>();

        WAVES.forEach(emWave -> {
            if(emWave.position.equals(pos)) waves.add(emWave);
        });

        return waves;
    }

    private <T extends BlockEntity> List<T> getCollection(World world, Function<ReactorControllerBlockEntity, Collection<BlockPos>> getter, BlockEntityType<T> type){
        return BlockEntityUtils.getCollection(world, this.blockEntity, getter, type);
    }

    public record MaterialState(double energyLevel){} //, double radiationLevel

    public static final class EMState {
        public double neutronEnergy;
        public double gammaRayEnergy;
        public double heatEnergy;

        public EMState(double neutronEnergy, double gammaRayEnergy, double heatEnergy) {
            this.neutronEnergy = neutronEnergy;
            this.gammaRayEnergy = gammaRayEnergy;
            this.heatEnergy = heatEnergy;
        }

        public double neutronEnergy() {
            return neutronEnergy;
        }

        public double gammaRayEnergy() {
            return gammaRayEnergy;
        }

        public double heatEnergy() {
            return heatEnergy;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (EMState) obj;
            return Double.doubleToLongBits(this.neutronEnergy) == Double.doubleToLongBits(that.neutronEnergy) &&
                    Double.doubleToLongBits(this.gammaRayEnergy) == Double.doubleToLongBits(that.gammaRayEnergy) &&
                    Double.doubleToLongBits(this.heatEnergy) == Double.doubleToLongBits(that.heatEnergy);
        }

        @Override
        public int hashCode() {
            return Objects.hash(neutronEnergy, gammaRayEnergy, heatEnergy);
        }

        @Override
        public String toString() {
            return "EMState[" +
                    "neutronEnergy=" + neutronEnergy + ", " +
                    "gammaRayEnergy=" + gammaRayEnergy + ", " +
                    "heatEnergy=" + heatEnergy + ']';
        }
    }

    public static final class EMWave {
        private final BlockPos.Mutable position;
        private final EMType type;

        private EightWayDirection direction;
        private double energy;

        private long lifeTime;

        public EMWave(BlockPos.Mutable position, EightWayDirection direction, double energy, EMType type, long lifeTime) {
            this.position = position;
            this.direction = direction;
            this.energy = energy;
            this.type = type;
            this.lifeTime = lifeTime;
        }

        public EMWave(BlockPos.Mutable position, EightWayDirection direction, double energy, EMType type) {
            this(position, direction, energy, type, 0);
        }

        public BlockPos.Mutable position() {
            return position;
        }

        public EightWayDirection direction() {
            return direction;
        }

        public double energy() {
            return energy;
        }

        public EMType type() {
            return type;
        }

        public long lifeTime() {
            return lifeTime;
        }

        public void incrementLifeTime(){
            this.lifeTime += 1;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (EMWave) obj;
            return Objects.equals(this.position, that.position) &&
                    Objects.equals(this.direction, that.direction) &&
                    Double.doubleToLongBits(this.energy) == Double.doubleToLongBits(that.energy) &&
                    Objects.equals(this.type, that.type) &&
                    this.lifeTime == that.lifeTime;
        }

        @Override
        public int hashCode() {
            return Objects.hash(position, direction, energy, type, lifeTime);
        }

        @Override
        public String toString() {
            return "EMWave[" +
                    "position=" + position + ", " +
                    "direction=" + direction + ", " +
                    "energy=" + energy + ", " +
                    "type=" + type + ", " +
                    "lifeTime=" + lifeTime + ']';
        }
    }

    public enum EMType {
        NEUTRON,
        GAMMA_RAY,
        HEAT
    }
}

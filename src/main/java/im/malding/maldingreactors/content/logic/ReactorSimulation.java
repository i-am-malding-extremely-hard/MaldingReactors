package im.malding.maldingreactors.content.logic;

import im.malding.maldingreactors.content.MaldingBlockEntities;
import im.malding.maldingreactors.content.MediumProperties;
import im.malding.maldingreactors.content.MediumRegistry;
import im.malding.maldingreactors.content.reactor.ReactorBaseBlockEntity;
import im.malding.maldingreactors.content.reactor.ReactorControllerBlockEntity;
import im.malding.maldingreactors.content.reactor.ReactorFuelRodControllerBlockEntity;
import im.malding.maldingreactors.util.BlockBoxUtils;
import im.malding.maldingreactors.util.BlockEntityUtils;
import im.malding.maldingreactors.util.DirectionUtils;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.EightWayDirection;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang3.mutable.MutableDouble;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2i;

import java.util.*;

@SuppressWarnings("UnstableApiUsage")
public class ReactorSimulation {

    public static final double BASE_GAMMA_RAY_ENERGY = 10.0;
    public static final double BASE_NEUTRON_ENERGY = 4.0;

    public static final double DISTANCE_DECAY_PERCENTAGE = 0.95;

    public ReactorControllerBlockEntity controller;

    public boolean isDirty = true;

    public Map<BlockPos, MutableDouble> positionToHeatEnergy = new HashMap<>();

    public Set<EMWave> WAVES = new HashSet<>();

    @Nullable
    public HeatInformation currentInfo = new HeatInformation(0, 0, 0);

    public ReactorSimulation(ReactorControllerBlockEntity blockEntity){
        this.controller = blockEntity;
    }

    public void tick(){
        if(!this.controller.isReactorActive()) return;

        var world = this.controller.getWorld();

        assert world != null;

        if(this.controller.reactorBounds == null) return;

        Map<Vector2i, Integer> reactionRates = new HashMap<>();

        var fuelRodControllers = BlockEntityUtils.getCollection(world, this.controller, be -> be.rodControllers, MaldingBlockEntities.REACTOR_FUEL_ROD_CONTROLLER);

        for (var fuelRodController : fuelRodControllers) {
            var pos = fuelRodController.getPos();

            reactionRates.put(new Vector2i(pos.getX(), pos.getZ()), fuelRodController.reactionRate);
        }

        var fuelTank = this.controller.getFuelTank();

        var fuelPercentage = MathHelper.clamp(fuelTank.amount / (double) fuelTank.getCapacity(), 0, 1);

        if(fuelPercentage != 0) {
            double fuelConsumed = 0;

            var fuelRods = BlockEntityUtils.getCollection(world, this.controller, be -> be.fuelRods, MaldingBlockEntities.REACTOR_FUEL_ROD);

            for (var fuelRod : fuelRods) {
                var pos = fuelRod.getPos();

                var reactionRate = reactionRates.get(new Vector2i(pos.getX(), pos.getZ()));

                if(reactionRate == 0) return;

                var amountOfGammaRays = MathHelper.floor((reactionRate / 100f) * 4);
                var amountOfNeutrons = MathHelper.ceil((reactionRate / 100f) * 8);

                var random = world.getRandom();

                float energyToDroplets = 0.25f;

                double totalEnergyEmitted = 0;

                for (var direction : DirectionUtils.getRandomDirections(random, amountOfGammaRays)) {
                    BlockPos.Mutable startPos = pos.mutableCopy();

                    direction.getDirections().forEach(startPos::offset);

                    double energy = BASE_GAMMA_RAY_ENERGY * fuelPercentage;

                    totalEnergyEmitted += energy;

                    WAVES.add(new EMWave(startPos, direction, energy, EMType.GAMMA_RAY));
                }

                for (var direction : DirectionUtils.getRandomDirections(random, amountOfNeutrons)) {
                    BlockPos.Mutable startPos = pos.mutableCopy();

                    direction.getDirections().forEach(startPos::offset);

                    double energy = BASE_NEUTRON_ENERGY * fuelPercentage;

                    totalEnergyEmitted += energy;

                    WAVES.add(new EMWave(startPos, direction, energy, EMType.NEUTRON));
                }

                fuelConsumed += totalEnergyEmitted * energyToDroplets;

                positionToHeatEnergy.computeIfAbsent(pos, blockPos -> new MutableDouble(0))
                        .add(totalEnergyEmitted * 0.40);
            }

            this.controller.convertFuelToWaste(MathHelper.ceil(fuelConsumed));
        }

        // No action to perform if no waves exists.
        if(WAVES.isEmpty()) return;

        // Action: Interact with the given position properties for the type
        for (EMWave wave : WAVES) {
            if(wave.lifeTime == 0 || wave.energy == 0) continue;

            var mediumProperties = mediumAtPosition(wave.position);
            var state = positionToHeatEnergy.computeIfAbsent(wave.position, blockPos -> new MutableDouble(0));

            if(wave.type() == EMType.GAMMA_RAY){
                var fundamentalProp = mediumProperties.gammaRayProps;

                var random = world.getRandom();

                //--
                var reflectChance = random.nextInt(101);

                if(reflectChance < fundamentalProp.reflectivity() * 100){
                    wave.direction = DirectionUtils.reflectedDirection(random, wave.direction);
                }
                //--

                //--
                var absorptionChance = random.nextInt(101);

                if(absorptionChance < fundamentalProp.absorption() * 100){
                    var collectedEnergy = wave.energy * fundamentalProp.conversion();

                    state.add(collectedEnergy);

                    wave.energy -= collectedEnergy;
                }
                //--
            } else if(wave.type() == EMType.NEUTRON){
                var fundamentalProp = mediumProperties.neutronProps;

                var random = world.getRandom();

                //--
                var reflectChance = random.nextInt(101);

                if(reflectChance < fundamentalProp.reflectivity() * 100){
                    wave.direction = DirectionUtils.reflectedDirection(random, wave.direction);
                }
                //--

                //--
                var absorptionChance = random.nextInt(101);

                if(absorptionChance < fundamentalProp.absorption() * 100){
                    var collectedEnergy = wave.energy * fundamentalProp.conversion();

                    state.add(collectedEnergy);

                    wave.energy -= collectedEnergy;
                }
                //--
            } else {
                state.add(wave.energy);

                wave.energy = 0;
            }
        }

        WAVES.removeIf(emWave -> emWave.energy == 0 || !controller.reactorBounds.contains(emWave.position));
        //--

        // Condition: If energy level is above 0, or wasn't
        // Action: Then move in the direction of the given wave

        for (EMWave emWave : WAVES) {
            emWave.direction.getDirections().forEach(emWave.position::move);
            emWave.energy *= DISTANCE_DECAY_PERCENTAGE;

            emWave.lifeTime += 1;
        }

        //--

        for (var entry : positionToHeatEnergy.entrySet()) {
            var energyState = entry.getValue();

            if(energyState.getValue() <= 0) return;

            var random = world.getRandom();

            //--
            var blockPos = entry.getKey();

            var materialProp = mediumAtPosition(blockPos);

            var conductChance = random.nextInt(101);

            if(conductChance > materialProp.conductivity() * 100) return;

            for (EightWayDirection direction : DirectionUtils.EIGHT_WAY_DIRECTIONS) {
                var conductedEnergy = energyState.getValue() * materialProp.conductivity();

                energyState.subtract(conductedEnergy);

                var position = blockPos.mutableCopy();

                direction.getDirections().forEach(position::offset);

                WAVES.add(new EMWave(position, direction, conductedEnergy / 8, EMType.HEAT));
            }
        }

        MutableFloat collectedEnergy = new MutableFloat(0);

        BlockBoxUtils.surfaceAreaAction(controller.reactorBounds, (pos, isEdge) -> {
            if(isEdge) return;

            for (EMWave wave : wavesAtPosition(pos)) {
                if(wave.type != EMType.HEAT) continue;

                collectedEnergy.add(wave.energy * 0.10);

                wave.energy = 0;
            }
        });

        try (Transaction t = Transaction.openOuter()) {
            this.controller.getEnergyStorage().insert(MathHelper.ceil(collectedEnergy.getValue()), t);

            t.commit();
        }

        currentInfo = new HeatInformation(totalReactorHeatEnergy(), averageReactorHeatEnergy(), highestReactorHeatEnergy());
    }

    public MediumProperties mediumAtPosition(BlockPos blockPos){
        var state = this.controller.getWorld().getBlockState(blockPos);

        return MediumRegistry.getProperty(state.getBlock());
    }

    public Set<EMWave> wavesAtPosition(BlockPos pos){
        Set<EMWave> waves = new HashSet<>();

        WAVES.forEach(emWave -> {
            if(emWave.position.equals(pos)) waves.add(emWave);
        });

        return waves;
    }

    public double totalReactorHeatEnergy(){
        double total = 0;

        for (var value : this.positionToHeatEnergy.values()) total += value.getValue();

        return total;
    }

    public double averageReactorHeatEnergy(){
        return totalReactorHeatEnergy() / this.positionToHeatEnergy.size();
    }

    public double highestReactorHeatEnergy(){
        double highestValue = 0;

        for (var value : this.positionToHeatEnergy.values()) highestValue = Math.max(highestValue, value.getValue());

        return highestValue;
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

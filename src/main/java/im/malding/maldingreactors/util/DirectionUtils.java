package im.malding.maldingreactors.util;

import net.minecraft.util.Util;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.EightWayDirection;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DirectionUtils {

    public static final List<EightWayDirection> EIGHT_WAY_DIRECTIONS = new ArrayList<>(List.of(EightWayDirection.values()));

    public static EightWayDirection reflectedDirection(Random random, EightWayDirection direction){
        int leftIndex = direction.ordinal() - 1;
        int rightIndex = direction.ordinal() + 1;

        if(leftIndex < 0) leftIndex = 7;
        if(rightIndex > 7) leftIndex = 0;

        var possibleDirections = new EightWayDirection[]{EIGHT_WAY_DIRECTIONS.get(leftIndex), direction, EIGHT_WAY_DIRECTIONS.get(rightIndex)};

        return oppositeDirection(Util.getRandom(possibleDirections, random));
    }

    @Nullable
    public static EightWayDirection oppositeDirection(EightWayDirection direction){
        Set<Direction> directions = new HashSet<>();

        direction.getDirections().forEach(axisDir -> directions.add(axisDir.getOpposite()));

        EightWayDirection oppositeDirection = null;

        for (EightWayDirection dir : EIGHT_WAY_DIRECTIONS) {
            if(dir.getDirections().equals(directions)) oppositeDirection = dir;
        }

        return oppositeDirection;
    }

    public static Set<EightWayDirection> getRandomDirections(Random random, int amount){
        Set<EightWayDirection> directions = new HashSet<>();

        while (directions.size() < Math.max(amount, EIGHT_WAY_DIRECTIONS.size())){
            var direction = Util.getRandom(EIGHT_WAY_DIRECTIONS, random);

            directions.add(direction);
        }

        return directions;
    }
}

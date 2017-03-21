import org.jetbrains.annotations.Nullable;
import org.jooq.lambda.Seq;
import pl.edu.platinum.archiet.jchess3man.engine.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

/**
 * Created by Michał Krzysztof Feiler on 18.03.17.
 */
public class ConstSitValuesUDAIImpl extends SitValuesUDAI {
    public final int depth;

    public ConstSitValuesUDAIImpl(
            @Nullable Integer depth,
            @Nullable Double ownedToThreatened
    ) {
        super(ownedToThreatened);
        this.depth = depth == null ? 0 : depth;
    }

    private ArrayList<Double> worker(GameState s, Color whoAreWe, int curDepth) {
        ArrayList<Double> minMaxSlice = new ArrayList<>(curDepth + 1);
        HashMap<Integer, ArrayList<Double>> myThoughts =
                new HashMap<>();
        int index = 0;
        double bestSitVal;
        for (GameState state : s.genASAOM(whoAreWe)) {
            myThoughts.computeIfAbsent(index,
                    ignored -> new ArrayList<Double>(1))
                    .add(sitValue(state, whoAreWe));
            if (curDepth > 0) {
                bestSitVal = -1000000;
                if (state.movesNext.equals(whoAreWe)) {
                    for (FromToPromMove moveToApply : state.genVFTPM()) {
                        Optional<GameState> newStateOptional;
                        try {
                            newStateOptional = moveToApply.generateAfters()
                                    .flatMap(FromToPromMove.EitherStateOrIllMoveExcept::flatMapState)
                                    .findAny();
                        } catch (NeedsToBePromotedException e) {
                            e.printStackTrace();
                            throw new AssertionError(e);
                        }
                        assert newStateOptional.isPresent();
                        GameState newState = newStateOptional.get();
                        ArrayList<Double> newThought =
                                worker(newState, whoAreWe, curDepth - 1);
                        newThought.add(0, myThoughts.get(index).get(0));
                        if (newThought.get(curDepth) > bestSitVal) {
                            bestSitVal = newThought.get(curDepth);
                            myThoughts.put(index, newThought);
                        }
                    }
                } else myThoughts.get(index).addAll(
                        worker(state, whoAreWe, curDepth - 1));
            }
            index++;
        }
        bestSitVal = 1000000;
        for (int i = 0; i < index; i++)
            if (myThoughts.get(i).get(curDepth) < bestSitVal)
                minMaxSlice = myThoughts.get(i);
        return minMaxSlice;
    }

    @Override
    public FromToPromMove decide(GameState s) {
        HashMap<FromToProm, ArrayList<Double>> thoughts =
                new HashMap<>();
        FromToPromMove bestMove = null;
        double bestSitVal = -1000000;
        for (final FromToPromMove moveToApply : s.genVFTPM()) {
            if (bestMove == null) bestMove = moveToApply;
            Optional<GameState> newStateOptional;
            try {
                newStateOptional = moveToApply.generateAfters()
                        .flatMap(FromToPromMove.EitherStateOrIllMoveExcept::flatMapState)
                        .findAny();
            } catch (NeedsToBePromotedException e) {
                e.printStackTrace();
                throw new AssertionError(e);
            }
            assert newStateOptional.isPresent();
            GameState newState = newStateOptional.get();
            ArrayList<Double> workerOutput = worker(newState, s.movesNext, depth);
            thoughts.put(new FromToProm(moveToApply), workerOutput);
            double workerOutputAtDepth = workerOutput.get(depth);
            if (workerOutputAtDepth > bestSitVal) {
                bestMove = moveToApply;
                bestSitVal = workerOutputAtDepth;
            }
        }
        return bestMove;
    }

}

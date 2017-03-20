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
        double bestSitVal = 0;
        for (GameState state : s.genASAOM(whoAreWe)) {
            myThoughts.computeIfAbsent(index,
                    ignored -> new ArrayList<Double>(1))
                    .add(sitValue(state, whoAreWe));
            if (curDepth > 0) {
                bestSitVal = -1000000;
                if (state.movesNext.equals(whoAreWe)) {
                    for (FromToProm myMove : state.genVFTP()) {
                        FromToPromMove moveToApply = new FromToPromMove(
                                myMove.from, myMove.to, state, myMove.pawnPromotion);
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
                    }
                }
            }
        }
        //[...]
        return minMaxSlice;
    }

    @Override
    public FromToPromMove decide(GameState s) {
        return new FromToPromMove(new Pos(0, 0),
                new Pos(1, 0), GameState.newGame);
    }

}

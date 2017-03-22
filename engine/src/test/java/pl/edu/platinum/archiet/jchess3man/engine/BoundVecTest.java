package pl.edu.platinum.archiet.jchess3man.engine;

import org.jooq.lambda.Seq;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by Michał Krzysztof Feiler on 22.03.17.
 */
class BoundVecTest {
    @Test
    void moatsWhiteGrayRooksDescMove() {
        DescMove move = new DescMove(new Pos(0, 7), new Pos(0, 8), GameState.newGame);
        try {
            move.generateVecs();
        } catch (NeedsToBePromotedException e) {
            e.printStackTrace();
            throw new AssertionError(e);
        }
        System.out.println(Seq.seq(move.vecs)
                .map(vec -> vec.moats(new Pos(0, 7)))
                .toString());
    }

    @Test
    void moatsWhiteGrayRooksPlusFile() {
        try {
            BoundVec boundVec = new BoundVec(
                    new Pos(0, 7),
                    new FileVector(1));
            System.out.println(boundVec.moats().toString());
        } catch (VectorAdditionFailedException | NeedsToBePromotedException e) {
            e.printStackTrace();
            throw new AssertionError(e);
        }

    }

}
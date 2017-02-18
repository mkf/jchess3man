package pl.edu.platinum.archiet.jchess3man.engine;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by Michał Krzysztof Feiler on 18.02.17.
 */
class MoveTest {
    @Test
    void after() throws
            VectorAdditionFailedException,
            NeedsToBePromotedException,
            CheckInitiatedThruMoatException,
            ImpossibleMoveException {
        GameState newState = new GameState(
                NewGameBoardImpl.c,
                MoatsState.noBridges,
                Color.White,
                CastlingPossibilities.all,
                EnPassantStore.empty,
                0, 0,
                PlayersAlive.all
        );
        Pos from = new Pos(1, 0);
        Pos to = new Pos(3, 0);
        Fig fsq = newState.board.get(from);
        assertTrue(fsq.equals(new Fig.Pawn(Color.White)));
        Iterable<? extends Vector> vecs = fsq.vecs(from, to);
        assertTrue(vecs.iterator().hasNext());
        Vector vec = vecs.iterator().next();
        assertNotNull(vec);
        Move first = new Move(new BoundVec(from, vec), newState);
        System.out.println("A");
        Fig realsq = newState.board.get(1, 0);
        System.out.println(realsq.toString());
        System.out.println("H");
        GameState firstAfter = first.after();
        assertNotNull(firstAfter);
        System.out.println(firstAfter.board.toListOfRanksOfFiles());
    }

}
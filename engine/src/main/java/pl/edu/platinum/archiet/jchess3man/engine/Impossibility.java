package pl.edu.platinum.archiet.jchess3man.engine;


import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Created by Michał Krzysztof Feiler on 03.02.17.
 */
public interface Impossibility {
    String msg();

    default boolean canI() {
        return false;
    }

    static boolean canI(Impossibility impossibility) {
        return impossibility == null || impossibility.canI();
    }

    /*
    public static class SameSquare implements Impossibility {
        public final Pos pos;

        public SameSquare(Pos pos) {
            this.pos = pos;
        }

        @Override
        public String msg() {
            return "from==to==" + pos.toString();
        }
    }
    */

    @Deprecated
    class _InternalAboutTheLackOfPromotionSetting implements Impossibility {
        @Override
        public String msg() {
            return "Why did you use that Impossibility! Don't use it!";
        }

        @Contract(" -> !null")
        @Deprecated
        public static Impossibility generateSuch() {
            return new _InternalAboutTheLackOfPromotionSetting();
        }

        @NotNull
        @Deprecated
        public static Optional<Impossibility> generateSuchOptional() {
            return Optional.of(generateSuch());
        }
    }

    class CannotEnPassant implements Impossibility {
        public final Pos to;
        public final EnPassantStore enPassantStore;

        public CannotEnPassant(Pos to, EnPassantStore enPassantStore) {
            this.to = to;
            this.enPassantStore = enPassantStore;
        }

        @Override
        public String msg() {
            return "PawnCapV, tosq.empty, but cannot ep@"
                    + to.toString() + " cuz "
                    + enPassantStore.toString();
        }
    }

    class CannotCaptureSameColor implements Impossibility {
        public final Pos to;
        public final Fig tosq;
        public final Color ourColor;

        public CannotCaptureSameColor(Color ourColor, Pos to, Fig tosq) {
            this.to = to;
            this.tosq = tosq;
            this.ourColor = ourColor;
        }

        @Override
        public String msg() {
            return "Cannot cap same col(" + ourColor.toString() + ") on " +
                    to.toString() + " as it is a " + tosq.toString();
        }
    }

    class NotAllEmpties implements Impossibility {
        @Override
        public String msg() {
            return "Not all empties";
        }
    }

    class ForbiddenCastling implements Impossibility {
        public final CastlingVector vec;
        public final CastlingPossibilities.ColorEntry colorCastling;

        public ForbiddenCastling(CastlingPossibilities.ColorEntry colorCastling, CastlingVector vec) {
            this.vec = vec;
            this.colorCastling = colorCastling;
        }

        @Override
        public String msg() {
            return "ColorCastling " + colorCastling.toString()
                    + " forbids our castling " + vec.toString();
        }
    }

    class CapturingThruMoats implements Impossibility {
        public final Pos to;
        public final Iterable<Color> moats;
        public final Fig tosq;

        public CapturingThruMoats(Iterable<Color> moats, Pos to, Fig tosq) {
            this.to = to;
            this.moats = moats;
            this.tosq = tosq;
        }

        @Override
        public boolean canI() {
            return !moats.iterator().hasNext();
        }

        @Override
        public String msg() {
            return "Impossible cap thru moats(" + moats.toString() + ") " +
                    "to " + tosq.toString() + "@" + to.toString();
        }
    }

    class PassingNonBridgedMoat implements Impossibility {
        public final MoatsState m;
        public final Iterable<Color> moats;
        public final Color curMoat;

        public PassingNonBridgedMoat(Color curMoat, MoatsState m, Iterable<Color> moats) {
            this.curMoat = curMoat;
            this.m = m;
            this.moats = moats;
        }

        @Override
        public String msg() {
            return "Passing non-bridged " + curMoat.toString() + ": passing "
                    + moats.toString() + ", bridges " + m.toString();
        }
    }

    class NothingToMoveHere implements Impossibility {
        public final Pos pos;

        public NothingToMoveHere(Pos pos) {
            this.pos = pos;
        }

        @Override
        public String msg() {
            return "Cannot move from an empty square " + pos;
        }
    }

    class ThatColorDoesNotMoveNow implements Impossibility {
        public final Color who;

        public ThatColorDoesNotMoveNow(Color who) {
            this.who = who;
        }

        @Override
        public String msg() {
            return "It's not " + who.toString() + "'s move";
        }
    }

    class WeInCheck implements Impossibility {
        public final Pos from;

        public WeInCheck(Pos from) {
            this.from = from;
        }

        @Override
        public String msg() {
            return "We would be in check! (checking " + from.toString() + ")";
        }
    }
}

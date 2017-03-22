package pl.edu.platinum.archiet.jchess3man.engine;

import org.jetbrains.annotations.Contract;

/**
 * Created by Michał Krzysztof Feiler on 08.02.17.
 */
interface PawnPromVector extends PawnVector {
    FigType promTo();

    @Contract("null, _ -> fail")
    static PawnPromVector fromPawnVector(
            PawnVector pv,
            FigType promTo
    ) {
        if (pv instanceof PawnPromVector) return (PawnPromVector) pv;
        if (pv instanceof PawnLongJumpVector) throw new IllegalArgumentException();
        if (pv instanceof PawnWalkVector) return new Walk((PawnWalkVector) pv, promTo);
        if (pv instanceof PawnCapVector) return new Cap((PawnCapVector) pv, promTo);
        throw new IllegalArgumentException();
    }

    static void errKingPawnZero(FigType promTo) throws IllegalArgumentException {
        switch (promTo) {
            case King:
            case Pawn:
                //case ZeroFigType:
                throw new IllegalArgumentException(promTo.name());
        }
    }

    PawnVector basedOn();

    PawnPromVector withProm(FigType what);

    class Walk extends PawnWalkVector implements PawnPromVector {
        public final FigType promTo;

        @Override
        public FigType promTo() {
            return promTo;
        }

        public Walk(PawnWalkVector basedOn, FigType promTo) {
            this(promTo, basedOn);
        }

        public Walk(FigType promTo, PawnWalkVector basedOn) {
            this(promTo, basedOn.direc);
        }

        public Walk(FigType promTo, boolean direc) {
            super(direc);
            this.promTo = promTo;
            errKingPawnZero(promTo);
        }

        public PawnWalkVector basedOn() {
            return new PawnWalkVector(direc);
        }

        @Override
        public Walk withProm(FigType what) {
            return new Walk(basedOn(), what);
        }
    }

    class Cap extends PawnCapVector implements PawnPromVector {
        public final FigType promTo;

        @Override
        public FigType promTo() {
            return promTo;
        }

        public Cap(PawnCapVector basedOn, FigType promTo) {
            this(promTo, basedOn);
        }

        public Cap(FigType promTo, PawnCapVector basedOn) {
            this(promTo, basedOn.inward, basedOn.plusFile);
        }

        public Cap(FigType promTo, boolean inward, boolean plusFile) {
            super(inward, plusFile);
            this.promTo = promTo;
            errKingPawnZero(promTo);
        }

        @Override
        public PawnCapVector basedOn() {
            return new PawnCapVector(inward, plusFile);
        }

        @Override
        public Cap withProm(FigType what) {
            return new Cap(basedOn(), what);
        }
    }
}

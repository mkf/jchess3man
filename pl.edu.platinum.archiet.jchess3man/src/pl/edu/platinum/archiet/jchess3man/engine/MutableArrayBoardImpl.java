package pl.edu.platinum.archiet.jchess3man.engine;

import org.intellij.lang.annotations.Flow;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Created by Michał Krzysztof Feiler on 02.02.17.
 */
public class MutableArrayBoardImpl implements MutableBoard {
    private byte[][] b = new byte[6][24];

    @Override
    public void put(int rank, int file, Fig fig) {
        b[rank][file] = fig.sevenBit();
    }

    @Override
    public List<List<Fig>> toListOfRanksOfFiles() {
        return Arrays.stream(b).map(Arrays::stream).map((arr) -> arr.);
    }

    private List<Fig> rowProcess(byte[] bar) {
        return Stream.of(Arrays.asList(bar)).map(Fig::fromSevenBit).collect(Collectors.toList());
    }

    @Override
    public Board copy() {
        return null;
    }

    @Override
    public Fig get(int rank, int file) {
        return null;
    }
}

package interfaces;

import model.Cell;
import model.State;

import java.util.List;

public interface Rule {

    boolean apply(State state, Cell cell, Cell previouslyModified);
    List<Cell> getNeighbours(State state, Cell cell);
}

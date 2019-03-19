package model.enums;

import interfaces.Rule;
import model.Cell;
import model.State;

import java.util.List;

public enum Rules implements Rule {
    STANDARD {

        @Override
        public boolean apply(State state, Cell cell) {

            final List<Cell> neighbours = getNeighbours(state, cell);
            int alive = 0;
            for (Cell neighbour : neighbours) {
                alive += neighbour.isAlive() ? 1 : 0;
            }

            if(cell.isAlive()) {
                return (alive != 2 && alive != 3);
            }

            return alive == 3;
        }

        @Override
        public List<Cell> getNeighbours(State state, Cell cell) {
            return state.getMooreNeighbours(cell, 1);
        }
    }
}

package model.enums;

import interfaces.Rule;
import model.Cell;
import model.State;

import java.util.List;

public enum Rules implements Rule {
    STANDARD {

        @Override
        public boolean apply(State state, Cell cell, Cell previouslyModified) {

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
    },

    SEEDS {

        @Override
        public boolean apply(State state, Cell cell, Cell previouslyModified) {

            if(cell.isAlive()) {
                return true;
            }

            final List<Cell> neighbours = getNeighbours(state, cell);
            int alive = 0;

            for (Cell neighbour : neighbours) {
                alive += neighbour.isAlive() ? 1 : 0;
            }

            return alive == 2;
        }

        @Override
        public List<Cell> getNeighbours(State state, Cell cell) {
            return state.getMooreNeighbours(cell, 1);
        }
    },

    ANTS {

        @Override
        public boolean apply(State state, Cell cell, Cell previouslyModified) {

            if(cell == previouslyModified)
                return true;

            if(previouslyModified.isAlive()) {
                return cell.getX() == previouslyModified.getX() + 1 &&
                        cell.getY() == previouslyModified.getY() && cell.getZ() == previouslyModified.getZ();
            }

            return cell.getX() == previouslyModified.getX() - 1 &&
                    cell.getY() == previouslyModified.getY() && cell.getZ() == previouslyModified.getZ();
        }

        @Override
        public List<Cell> getNeighbours(State state, Cell cell) {
            return state.getVonNeumannNeighbours(cell, 1);
        }
    }
}

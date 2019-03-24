package model.enums;

import interfaces.Rule;
import model.Cell;
import model.State;

import java.util.List;

public enum Rules implements Rule {
	/*
	* 
	* 1. If a living cube has between a and b neighbors, it stays alive to the next generation. 
	*	Otherwise it dies (by over- or underpopulation.)
	*
	* 2. If an empty cell has between c and d neighbors, it becomes alive in the next generation (by reproduction).
	*
	* Rules notation: Bcd/Sab (if c == d or a == b only one is used)
	* 	Ex. Standard = B3/S23
	*/
    STANDARD(2, 3, 3, 3) {	

        @Override
        public List<Cell> getNeighbours(State state, Cell cell) {
            return state.getMooreNeighbours(cell, 1);
        }
        
    },
    SEEDS(-1, -1, 2, 2) {

        @Override
        public List<Cell> getNeighbours(State state, Cell cell) {
            return state.getMooreNeighbours(cell, 1);
        }
        
    },
    /*,
    ANTS {

        Random r = new Random();

        @Override
        public boolean apply(State state, Cell cell) {
            return true;
        }

        @Override
        public List<Cell> getNeighbours(State state, Cell cell) {
            if(!cell.isAlive())
                return Collections.emptyList();
            int neighbour = r.nextInt(4);
            return state.getVonNeumannNeighbours(cell, 1).subList(neighbour, neighbour + 1);
        }
    },*/
    MAZE(1, 5, 3, 3) {

        @Override
        public List<Cell> getNeighbours(State state, Cell cell) {
            return state.getMooreNeighbours(cell, 1);
        }
        
    },
    STANDARD_3D(4, 5, 5, 5) {

        @Override
        public List<Cell> getNeighbours(State state, Cell cell) {
            return state.getMooreNeighbours(cell, 1);
        }
        
    },
    TEST1_3D(5, 5, 4, 5) {

        @Override
        public List<Cell> getNeighbours(State state, Cell cell) {
            return state.getMooreNeighbours(cell, 1);
        }
        
    },
    TEST2_3D(9, 9, 4, 4) {

        @Override
        public List<Cell> getNeighbours(State state, Cell cell) {
            return state.getMooreNeighbours(cell, 1);
        }
        
    };
    
    private int a = -1;
	private int b = -1;
	private int c = -1;
	private int d = -1;
	
	private Rules() {}
	
	private Rules(int a, int b, int c, int d) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
	}
    
    @Override
    public boolean apply(State state, Cell cell) {

        final List<Cell> neighbours = getNeighbours(state, cell);
        int alive = 0;
        for (Cell neighbour : neighbours) {
            alive += neighbour.isAlive() ? 1 : 0;
        }

        if(cell.isAlive()) {
        	return !(a <= alive && alive <= b);
        }

        return (c <= alive && alive <= d);
    }
}

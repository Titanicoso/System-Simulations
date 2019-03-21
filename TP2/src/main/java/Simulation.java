import model.Cell;
import model.State;
import model.enums.Rules;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class Simulation {

    public static void simulate(int states, int dim, Rules rule, boolean is3D) {

        List<Cell> modified = new ArrayList<>();

        State state = new State(dim, is3D);

        int i = 0;
        while (i < states) {
            List<Cell> lastModified = state.getModified();

            for (Cell cell: lastModified) {
                List<Cell> neighbours = rule.getNeighbours(state, cell);

                boolean isChecked = state.isChecked(cell);

                if(!isChecked && rule.apply(state, cell)) {
                    modified.add(cell);
                }

                if(!isChecked) {
                    state.setChecked(cell);
                }

                for (Cell neighbour : neighbours) {
                    isChecked = state.isChecked(neighbour);

                    if(!isChecked && rule.apply(state, neighbour)) {
                        modified.add(neighbour);
                    }

                    if(!isChecked) {
                        state.setChecked(neighbour);
                    }
                }
            }
            printState(state);
            state.changeState(modified);
            modified.clear();
            i++;
        }
    }
    
    private static void logState(State state) {
    	File file = new File("utils/out.xyz");
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			return;
		}
		PrintStream ps = new PrintStream(fos);
		
		int dim = state.getDim();
		boolean is3D = state.is3D();
		List<Cell> alive = new ArrayList<>();
		
		for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if(is3D) {
                    for (int k = 0; k < dim; k++) {
                    	Cell cell = state.getCell(i, j, k);
                    	if (cell.isAlive())
                    		alive.add(cell);
                    }
                } else {
                	Cell cell = state.getCell(i, j, 0);
                	if (cell.isAlive())
                		alive.add(cell);
                }
            }
        }
		
		ps.println(alive.size());
		for (Cell cell : alive) {
			ps.println("C " + cell.getX() + " " + cell.getY() + " " + cell.getZ());
		}
		
		ps.close();
    }
    
    private static void printState(State state) {	
		int dim = state.getDim();
		boolean is3D = state.is3D();
		
		List<Cell> alive = new ArrayList<>();
		
		for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if(is3D) {
                    for (int k = 0; k < dim; k++) {
                    	Cell cell = state.getCell(i, j, k);
                    	if (cell.isAlive())
                    		alive.add(cell);
                    }
                } else {
                	Cell cell = state.getCell(i, j, 0);
                	if (cell.isAlive())
                		alive.add(cell);
                }
            }
        }
		
		System.out.println(alive.size());
		for (Cell cell : alive) {
			System.out.println("C " + cell.getX() + " " + cell.getY() + " " + cell.getZ());
		}
    }
}

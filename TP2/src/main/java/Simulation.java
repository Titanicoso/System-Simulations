import model.Cell;
import model.State;
import model.enums.Rules;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class Simulation {
	
	static boolean append = false;

    public static void simulate(Rules rule,  Options options) {

        List<Cell> modified = new ArrayList<>();

        State state = new State(options.getDim(), options.is3D());
        if (options.getInput() != null) {
        	readInput(state, modified, options);
        } else {
        	for (int i = 0; i < options.getN(); i++) {
        		int x = rand(0, options.getDim());
        		int y = rand(0, options.getDim());
        		int z = options.is3D() ? rand(0, options.getDim()) : 0;
        		Cell c = state.getCell(x, y, z);
        		if (!modified.contains(c))
        			modified.add(c);
        		else
        			i--;
        	}
        }
    	state.changeState(modified);
    	modified.clear();

        int i = 0;
        while (i < options.getStates()) {
            List<Cell> lastModified = state.getModified();

            for (Cell cell: lastModified) {
                List<Cell> neighbours = rule.getNeighbours(state, cell);

                boolean isChecked = state.isChecked(cell);

                if(!isChecked && rule.apply(state, cell, cell)) {
                    modified.add(cell);
                }

                if(!isChecked) {
                    state.setChecked(cell);
                }

                for (Cell neighbour : neighbours) {
                    isChecked = state.isChecked(neighbour);

                    if(!isChecked && rule.apply(state, neighbour, cell)) {
                        modified.add(neighbour);
                    }

                    if(!isChecked) {
                        state.setChecked(neighbour);
                    }
                }
            }
            printState(state);
            logState(state);
            state.changeState(modified);
            modified.clear();
            i++;
        }
    }
    
    private static void logState(State state) {
    	File file = new File("output.xyz");
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file, append);
			append = true;
		} catch (FileNotFoundException e) {
			return;
		}
		PrintStream ps = new PrintStream(fos);
		
		int dim = state.getDim();
		boolean is3D = state.is3D();
		//List<Cell> alive = new ArrayList<>();
		
//		for (int i = 0; i < dim; i++) {
//            for (int j = 0; j < dim; j++) {
//                if(is3D) {
//                    for (int k = 0; k < dim; k++) {
//                    	Cell cell = state.getCell(i, j, k);
//                    	if (cell.isAlive())
//                    		alive.add(cell);
//                    }
//                } else {
//                	Cell cell = state.getCell(i, j, 0);
//                	if (cell.isAlive())
//                		alive.add(cell);
//                }
//            }
//        }

		ps.println(state.getAliveCount());
		ps.println();
		for (Cell cell : state.getAlive()) {
			ps.println(cell.getX() + " " + cell.getY() + " " + cell.getZ());
		}
		
		ps.close();
    }
    
    private static void printState(State state) {	
		int dim = state.getDim();
		boolean is3D = state.is3D();
		
		//List<Cell> alive = new ArrayList<>();
		
//		for (int i = 0; i < dim; i++) {
//            for (int j = 0; j < dim; j++) {
//                if(is3D) {
//                    for (int k = 0; k < dim; k++) {
//                    	Cell cell = state.getCell(i, j, k);
//                    	if (cell.isAlive())
//                    		alive.add(cell);
//                    }
//                } else {
//                	Cell cell = state.getCell(i, j, 0);
//                	if (cell.isAlive())
//                		alive.add(cell);
//                }
//            }
//        }
		
		System.out.println(state.getAliveCount());
		for (Cell cell : state.getAlive()) {
			System.out.println(cell.getX() + " " + cell.getY() + " " + cell.getZ());
		}
    }
    
	private static void readInput(final State state, final List<Cell> modified, final Options options) {
		File input = options.getInput();
		try {
			BufferedReader br = new BufferedReader(new FileReader(input));
			String st;
			while ((st = br.readLine()) != null) {
				Scanner scanner = new Scanner(st);
				scanner.useLocale(Locale.US);
				if (options.is3D())
					modified.add(state.getCell(scanner.nextInt(), scanner.nextInt(), scanner.nextInt()));
				else
					modified.add(state.getCell(scanner.nextInt(), scanner.nextInt(), 0));
				scanner.close();
			}
			br.close();
		} catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
		}
	}
	
	private static int rand(int min, int max) {
		return ThreadLocalRandom.current().nextInt(min, max);
	}
}

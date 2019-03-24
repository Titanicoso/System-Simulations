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
        	int domainX = rand(0, 2 * options.getDim()/3);
			int domainY = rand(0, 2 * options.getDim()/3);
			int domainZ = rand(0, 2 * options.getDim()/3);

        	for (int i = 0; i < options.getN(); i++) {
        		int x = rand(domainX, domainX + options.getDim()/3);
        		int y = rand(domainY, domainY + options.getDim()/3);
        		int z = options.is3D() ? rand(domainZ, domainZ + options.getDim()/3) : 0;
        		Cell c = state.getCell(x, y, z);
        		if (!modified.contains(c))
        			modified.add(c);
        		else
        			i--;
        	}
        }
    	state.changeState(modified);
    	modified.clear();
		state.setCenter();

        int i = 0;
        while (i < options.getStates()) {
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

		ps.println(state.getAliveCount());
		ps.println("R: " + state.getRadius());
		for (Cell cell : state.getAlive()) {
			ps.println(cell.getX() + " " + cell.getY() + " " + cell.getZ());
		}
		
		ps.close();
    }
    
    private static void printState(State state) {
		System.out.println(state.getAliveCount());
		System.out.println("R: " + state.getRadius());
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

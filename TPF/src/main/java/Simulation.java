import model.Cell;
import model.State;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class Simulation {
	
	static boolean append = false;

    public static void simulate(Options options) {

    	State s = new State(options.getLength(), options.getHeight(), options.getMaxVelocity(), options.getProbability());
		makeAutitosAppear(s, options);

        int i = 0;
        while (i < options.getStates() && s.getCarCount() > 0) {
            List<Cell> exited = s.calculateNextStep();
            logState(s);
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

		ps.println(state.getCarCount());
		ps.println();
		Arrays.stream(state.getCells())
				.flatMap(Arrays::stream)
				.filter(Objects::nonNull)
				.map(cell -> cell.getX() + " " + cell.getY() + " " + cell.getVelocity())
				.forEach(ps::println);
		
		ps.close();
    }

	private static void makeAutitosAppear(State s, Options options) {
    	Cell[][] cells = s.getCells();
    	List<Cell> autitos = s.getCars();

		boolean overlapped;

		while(autitos.size() < options.getN()) {
			int x = rand(0, 20);
			int y = /*rand(0, 1);*/0;

			Cell autito = new Cell(x, y, 1);

			overlapped = autitos.stream().anyMatch(a -> a.equals(autito));
			if (!overlapped) {
				autitos.add(autito);
				cells[x][y] = autito;
			}
		}

		s.setCars(autitos);
		s.setCells(cells);
	}

	private static int rand(int min, int max) {
		return ThreadLocalRandom.current().nextInt(min, max);
	}
}

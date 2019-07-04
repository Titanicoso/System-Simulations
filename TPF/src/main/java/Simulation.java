import model.Cell;
import model.State;

import java.io.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Simulation {
	
	static boolean append = false;
	static boolean flowAppend = false;
	static boolean dataAppend = false;

    public static void simulate(Options options) {

    	State s = new State(options.getLength(), options.getHeight(), options.getMaxVelocity(),
				options.getSlowDownProbability(), options.getLaneChangeProbability());
		readObstacles(s, options);
		makeAutitosAppear(s, options);

		logState(s);
        int i = 0;
        while (i < options.getStates() && s.getCarCount() > 0) {
            List<Cell> exited = s.calculateNextStep();
            logState(s);
            logFlow(exited, i);
            logData(s, i);
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

		ps.println(state.getCarCount() + state.getObstacleCount());
		ps.println();
		Arrays.stream(state.getCells())
				.flatMap(Arrays::stream)
				.filter(Objects::nonNull)
				.map(cell -> cell.getId() + " " + cell.getX() + " " + cell.getY() + " " + cell.getVelocity())
				.forEach(ps::println);
		
		ps.close();
    }

    private static double calculateMeanVelocity(State state) {
    	return state.getCars().stream()
				.mapToDouble(Cell::getVelocity)
				.average().orElse(0);
	}

	private static void logData(State state, int time) {
		File file = new File("data.data");
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file, dataAppend);
			dataAppend = true;
		} catch (FileNotFoundException e) {
			return;
		}
		PrintStream ps = new PrintStream(fos);

		ps.println(time + " " + calculateMeanVelocity(state));

		ps.close();
	}

	private static void logFlow(List<Cell> exited, int time) {
		File file = new File("flow.data");
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file, flowAppend);
			flowAppend = true;
		} catch (FileNotFoundException e) {
			return;
		}
		PrintStream ps = new PrintStream(fos);

		exited.forEach(car -> ps.println(time));

		ps.close();
	}

	private static void readObstacles(final State state, final Options options) {
		File file = options.getInput();
		if(file == null)
			return;
		Scanner input;
		try {
			input = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return ;
		}

		Cell[][] cells = state.getCells();

		int x = 0;
		int y = 0;
		int obstacles = 0;

		while(input.hasNextLine()) {
			x = 0;
			String line = input.nextLine();
			char[] currentLine = line.toCharArray();
			for(char current: currentLine ) {
				if(current != '\n') {
					if (current == 'X') {
						obstacles--;
						cells[x][y] = new Cell(obstacles, x, y, -1);
					}
					x++;
				}
			}
			y++;
		}

		state.setObstacleCount(-obstacles);
		input.close();
	}

	private static void makeAutitosAppear(State s, Options options) {
    	Cell[][] cells = s.getCells();
    	List<Cell> autitos = s.getCars();

		boolean overlapped;

		int i = 0;
		while(autitos.size() < options.getN()) {
			int x = rand(0, 20);
			int y = rand(0, options.getHeight());
			int velocity = rand(1, options.getMaxVelocity());

			Cell autito = new Cell(i, x, y, velocity);

			if (cells[x][y] == null) {
				autitos.add(autito);
				cells[x][y] = autito;
				i++;
			}
		}

		s.setCars(autitos);
		s.setCells(cells);
	}

	private static int rand(int min, int max) {
		return ThreadLocalRandom.current().nextInt(min, max);
	}
}

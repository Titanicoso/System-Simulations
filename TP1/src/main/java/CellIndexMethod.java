import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CellIndexMethod {

    private static Map<Integer, List<Particle>> createGrid(final Area area, final int m) {
        final Map<Integer, List<Particle>> grid = new HashMap<>();

        for(int i = 0; i < m * m; i++) {
            grid.put(i, new ArrayList<>());
        }

        final double cellLength = area.getLength() / m;


        for (final Particle particle: area.getParticles()) {
            final int cellX = (int) Math.floor(particle.getX() / cellLength);
            final int cellY = (int) Math.floor(particle.getY() / cellLength);

            grid.get(cellX + cellY * m).add(particle);
        }
        return grid;
    }

    public static Map<Integer, List<Particle>> findNeighbours(final Area area, final int m) {
        final Map<Integer, List<Particle>> grid = createGrid(area, m);

        final Map<Integer, List<Particle>> neighbours = new HashMap<>();
        for(int i = 0; i < area.getParticles().length; i++) {
            neighbours.put(i, new ArrayList<>());
        }

        for (int i = 0; i < m * m; i++) {
            final List<Particle> particles = grid.get(i);

            if(particles.size() != 0) {

                findCellNeighbours(neighbours, particles, particles, area);

                int neighbour = getNeighbour(i, 0, 1, m, area.isPeriodic());
                if (neighbour != -1) {
                    findCellNeighbours(neighbours, particles, grid.get(neighbour), area);
                }

                neighbour = getNeighbour(i, 1, 0, m, area.isPeriodic());
                if (neighbour != -1) {
                    findCellNeighbours(neighbours, particles, grid.get(neighbour), area);
                }

                neighbour = getNeighbour(i, 1, 1, m, area.isPeriodic());
                if (neighbour != -1) {
                    findCellNeighbours(neighbours, particles, grid.get(neighbour), area);
                }

                neighbour = getNeighbour(i, -1, 1, m, area.isPeriodic());
                if (neighbour != -1) {
                    findCellNeighbours(neighbours, particles, grid.get(neighbour), area);
                }
            }
        }
        return neighbours;
    }

    private static int getNeighbour(final int cell, final int up, final int right,
                                    final int m, final boolean periodic) {

        int neighbourX = (cell % m) + right;
        int neighbourY = (cell / m) - up;

        if((neighbourX < 0 || neighbourX >= m ||
                neighbourY < 0 || neighbourY >= m) && !periodic)
            return -1;

        return Math.floorMod(neighbourX, m) + Math.floorMod(neighbourY, m) * m;
    }

    private static void findCellNeighbours(final Map<Integer, List<Particle>> neighbours, final List<Particle> cell1,
                                           final List<Particle> cell2, final Area area) {
    	
        for (final Particle particle1 : cell1) {
            for (final Particle particle2 : cell2) {
                if (particle1.getId() != particle2.getId()) {
                    if (particle1.distance(particle2, area) < area.getInteractionRatio()) {
                        if(!neighbours.get(particle1.getId()).contains(particle2))
                            neighbours.get(particle1.getId()).add(particle2);
                        if(!neighbours.get(particle2.getId()).contains(particle1))
                            neighbours.get(particle2.getId()).add(particle1);
                    }
                }
            }
        }
    }

    public static int findMaximumM(final Area area) {
    	double maxRatio = 0.0;
    	for (final Particle particle : area.getParticles()) {
    		if (particle.getRatio() > maxRatio)
    			maxRatio = particle.getRatio();
    	}
    	return (int) Math.floor(area.getLength() / (area.getInteractionRatio() + maxRatio * 2));
    }
    
    public static int findOptimalM(final Area area) {
    	return 0;
    }
}

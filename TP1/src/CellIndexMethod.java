import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CellIndexMethod {

    private static Map<Integer, List<Particle>> createGrid(final Area area, final int m) {
        final Map<Integer, List<Particle>> grid = new HashMap<>();

        for(int i = 0; i < m * m; i++) {
            grid.put(i, new ArrayList<Particle>());
        }

        final double cellLength = area.getLength() / m;


        for (final Particle particle: area.getParticles()) {
            final int cellX = (int) Math.floor(particle.getX() / cellLength);
            final int cellY = (int) Math.floor(particle.getY() / cellLength);

            grid.get(cellX + cellY * m).add(particle);
        }
        return grid;
    }

    public static void findNeighbours(final Area area, final boolean cont, final int m) {
        final Map<Integer, List<Particle>> grid = createGrid(area, m);

        final Map<Integer, List<Particle>> neighbours = new HashMap<>();
        for(int i = 0; i < m * m; i++) {
            neighbours.put(i, new ArrayList<Particle>());
        }

        for (int i = 0; i < m * m; i++) {
            final List<Particle> particles = grid.get(i);

            int neighbour = getUpNeighbour(i, m, cont);
            if(neighbour != -1) {
                findCellNeighbours(neighbours, particles, grid.get(neighbour), area, cont);
            }

            neighbour = getRightNeighbour(i, m, cont);
            if(neighbour != -1) {
                findCellNeighbours(neighbours, particles, grid.get(neighbour), area, cont);
            }

            cell = i - m;
            if(cell < 0) {
                if(cont)
                    findCellNeighbours(neighbours, particles, grid.get(m * m - cell), area, cont);
            } else
                findCellNeighbours(neighbours, particles, grid.get(cell), area, cont);

            cell = i + m + 1;
            if(cell % m == 0 || cell >= m * m) {
                if(cont)
                    findCellNeighbours(neighbours, particles, grid.get(cell - m), area, cont);
            } else
                findCellNeighbours(neighbours, particles, grid.get(cell), area, cont);

            cell = i - m;
            if(cell >= m * m) {
                if(cont)
                    findCellNeighbours(neighbours, particles, grid.get(m * m - cell), area, cont);
            } else
                findCellNeighbours(neighbours, particles, grid.get(cell), area, cont);

        }


        +1, -m, -m +1, +m+1
    }

    private static int getUpNeighbour(final int cell, final int m, final boolean cont) {

        if(cell - m < 0 && !cont)
            return -1;

        final int neighbourX = (cell - m) % m;
        final int neighbourY = ((cell - m) / m - 1) % m;
        return neighbourX + neighbourY * m;
    }

    private static int getUpRightNeighbour(final int cell, final int m, final boolean cont) {

        if((cell - m < 0 || ((cell + 1) % m) == 0) && !cont)
            return -1;

        final int neighbourX = (cell - m + 1) % m;
        final int neighbourY = ((cell - m + 1) / m - 1) % m;
        return neighbourX + neighbourY * m;
    }

    private static int getRightNeighbour(final int cell, final int m, final boolean cont) {
        if(((cell + 1) % m) == 0 && !cont)
            return -1;

        final int neighbourX = (cell + 1) % m;
        final int neighbourY = ((cell + 1) / m - 1) % m;
        return neighbourX + neighbourY * m;
    }

    private static int getDownRightNeighbour(final int cell, final int m, final boolean cont) {
        final int neighbourX = neighbour % m;
        final int neighbourY = (neighbour / m - 1) % m;
        return neighbourX + neighbourY * m;
    }

    private static void findCellNeighbours(final Map<Integer, List<Particle>> neighbours, final List<Particle> cell1,
                                           final List<Particle> cell2, final Area area, final boolean cont) {
        for (final Particle particle1 : cell1) {
            for (final Particle particle2 : cell2) {
                if (particle1.getId() != particle2.getId()) {
                    if (particle1.distance(particle2, area, cont) < area.getInteractionRatio()) {
                        neighbours.get(particle1.getId()).add(particle2);
                        neighbours.get(particle2.getId()).add(particle1);
                    }
                }
            }
        }
    }


}

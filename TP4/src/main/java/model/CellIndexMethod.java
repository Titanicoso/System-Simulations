package model;
import java.util.*;

public class CellIndexMethod {

    private final double interactionRadius;
    private Map<Integer, List<Particle>> grid;
    private final int m;
    private final double cellLength;

    public CellIndexMethod(double interactionRadius, Area area) {
        this.interactionRadius = interactionRadius;
        this.m = findMaximumM(area);
        this.cellLength = area.getLength() / m;
    }

    private void populateGrid(final Area area) {

        grid = new HashMap<>();

        for (final Particle particle: area.getParticles()) {
            final int cellX = (int) Math.floor(particle.getX() / cellLength);
            final int cellY = (int) Math.floor(particle.getY() / cellLength);

            final int cell = cellX + cellY * 2 * m;

            if(!grid.containsKey(cell)) {
                grid.put(cell, new ArrayList<>());
            }
            grid.get(cell).add(particle);
        }
    }

//    public void updateGrid(final Particle previous, final Particle predicted) {
//        int cellX = (int) Math.floor(previous.getX() / cellLength);
//        int cellY = (int) Math.floor(previous.getY() / cellLength);
//
//        int predictedCellX = (int) Math.floor(predicted.getX() / cellLength);
//        int predictedCellY = (int) Math.floor(predicted.getY() / cellLength);
//
//        if(cellX != predictedCellX || predictedCellY != cellY) {
//            grid.get(cellX + cellY * 2 * m).remove(previous);
//            grid.get(cellX + cellY * 2 * m).add(predicted);
//        }
//    }

    public Map<Integer, List<Particle>> findNeighbours(final Area area) {
        populateGrid(area);

        final Map<Integer, List<Particle>> neighbours = new HashMap<>();

        for (Map.Entry<Integer, List<Particle>> entry: grid.entrySet()){
            List<Particle> particles = entry.getValue();
            int i = entry.getKey();

            sameCellNeighbours(neighbours, particles, area);

            int neighbour = getNeighbour(i, 0, 1);
            if (neighbour != -1) {
                findCellNeighbours(neighbours, particles, grid.get(neighbour), area);
            }

            neighbour = getNeighbour(i, 1, 0);
            if (neighbour != -1) {
                findCellNeighbours(neighbours, particles, grid.get(neighbour), area);
            }

            neighbour = getNeighbour(i, 1, 1);
            if (neighbour != -1) {
                findCellNeighbours(neighbours, particles, grid.get(neighbour), area);
            }

            neighbour = getNeighbour(i, -1, 1);
            if (neighbour != -1) {
                findCellNeighbours(neighbours, particles, grid.get(neighbour), area);
            }

        }
        return neighbours;
    }

    private int getNeighbour(final int cell, final int up, final int right) {

        int neighbourX = (cell % (2 * m)) + right;
        int neighbourY = (cell / (2 * m)) - up;

        if((neighbourX < 0 || neighbourX >= 2 * m ||
                neighbourY < 0 || neighbourY >= m))
            return -1;

        return neighbourX + neighbourY * 2 * m;
    }

    private void findCellNeighbours(final Map<Integer, List<Particle>> neighbours, final List<Particle> cell1,
                                           final List<Particle> cell2, final Area area) {

        if(cell1 == null || cell2 == null)
            return;

        for (final Particle particle1 : cell1) {
            for (final Particle particle2 : cell2) {
                if (particle1.getId() != particle2.getId()) {
                    if (particle1.distance(particle2) <= interactionRadius && area.forceInteraction(particle1, particle2)) {
                        if(!neighbours.containsKey(particle1.getId())) {
                            neighbours.put(particle1.getId(), new ArrayList<>());
                        }

                        neighbours.get(particle1.getId()).add(particle2);
                    }
                }
            }
        }
    }

    private void sameCellNeighbours(final Map<Integer, List<Particle>> neighbours, final List<Particle> cell, final Area area) {

        Particle particle1;
        Particle particle2;
        for (int i = 0; i < cell.size(); i++) {
            particle1 = cell.get(i);
            for (int j = i + 1; j < cell.size(); j++) {
                particle2 = cell.get(j);
                if (particle1.distance(particle2) <= interactionRadius && area.forceInteraction(particle1, particle2)) {
                    if(!neighbours.containsKey(particle1.getId())) {
                        neighbours.put(particle1.getId(), new ArrayList<>());
                    }

                    neighbours.get(particle1.getId()).add(particle2);
                }
            }
        }
    }

    private void findParticleNeighbours(final List<Particle> neighbours, Particle particle,
                                        final List<Particle> cell, final Area area) {

        if(cell == null)
            return;

        for (final Particle particle2 : cell) {
            if (particle.getId() != particle2.getId()) {
                if (particle.distance(particle2) <= interactionRadius && area.forceInteraction(particle, particle2)) {
                    neighbours.add(particle2);
                }
            }
        }
    }

    public List<Particle> predictParticleNeighbours(final Particle particle, final Area area) {

        final int cellX = (int) Math.floor(particle.getX() / cellLength);
        final int cellY = (int) Math.floor(particle.getY() / cellLength);
        final int cellI = cellX + cellY * 2 * m;

        if(cellI < 0 || cellI > grid.size())
            return Collections.emptyList();

        List<Particle> cell = grid.get(cellI);

        List<Particle> neighbours = new ArrayList<>();

        findParticleNeighbours(neighbours,  particle, cell, area);

        int neighbour = getNeighbour(cellI, 0, 1);
        if (neighbour != -1) {
            findParticleNeighbours(neighbours, particle, grid.get(neighbour), area);
        }

        neighbour = getNeighbour(cellI, 1, 0);
        if (neighbour != -1) {
            findParticleNeighbours(neighbours, particle, grid.get(neighbour), area);
        }

        neighbour = getNeighbour(cellI, 1, 1);
        if (neighbour != -1) {
            findParticleNeighbours(neighbours, particle, grid.get(neighbour), area);
        }

        neighbour = getNeighbour(cellI, -1, 1);
        if (neighbour != -1) {
            findParticleNeighbours(neighbours, particle, grid.get(neighbour), area);
        }

        neighbour = getNeighbour(cellI, -1, 0);
        if (neighbour != -1) {
            findParticleNeighbours(neighbours, particle, grid.get(neighbour), area);
        }

        neighbour = getNeighbour(cellI, -1, -1);
        if (neighbour != -1) {
            findParticleNeighbours(neighbours, particle, grid.get(neighbour), area);
        }

        neighbour = getNeighbour(cellI, 0, -1);
        if (neighbour != -1) {
            findParticleNeighbours(neighbours, particle, grid.get(neighbour), area);
        }

        neighbour = getNeighbour(cellI, 1, -1);
        if (neighbour != -1) {
            findParticleNeighbours(neighbours, particle, grid.get(neighbour), area);
        }

        return neighbours;
    }

    private int findMaximumM(final Area area) {
        int m = (int) Math.floor(area.getLength() / interactionRadius);
        return m == 0 ? 1 : m;
    }

}


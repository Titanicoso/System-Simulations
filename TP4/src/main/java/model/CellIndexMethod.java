package model;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CellIndexMethod {

    private final double interactionRadius;
    private List<List<Particle>> grid;
    private final int m;
    private final double cellLength;

    public CellIndexMethod(double interactionRadius, Area area) {
        this.interactionRadius = interactionRadius;
        this.m = findMaximumM(area);
        grid = new ArrayList<>();
        for(int i = 0; i < 2 * m * m; i++) {
            grid.add(new ArrayList<>());
        }
        this.cellLength = area.getLength() / m;
        populateGrid(area);
    }

    private void populateGrid(final Area area) {

        for (final Particle particle: area.getParticles()) {
            final int cellX = (int) Math.floor(particle.getX() / cellLength);
            final int cellY = (int) Math.floor(particle.getY() / cellLength);

            grid.get(cellX + cellY * 2 * m).add(particle);
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
        for(int i = 0; i < area.getParticles().size(); i++) {
            neighbours.put(i, new ArrayList<>());
        }

        for (int i = 0; i < 2 * m * m; i++) {
            final List<Particle> particles = grid.get(i);

            if(particles.size() != 0) {

                findCellNeighbours(neighbours, particles, particles, area);

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

        for (final Particle particle1 : cell1) {
            for (final Particle particle2 : cell2) {
                if (particle1.getId() != particle2.getId()) {
                    if (particle1.distance(particle2) <= interactionRadius && area.forceInteraction(particle1, particle2)) {
                        if(!neighbours.get(particle1.getId()).contains(particle2))
                            neighbours.get(particle1.getId()).add(particle2);
                        if(!neighbours.get(particle2.getId()).contains(particle1))
                            neighbours.get(particle2.getId()).add(particle1);
                    }
                }
            }
        }
    }

    private void findParticleNeighbours(final List<Particle> neighbours, Particle particle,
                                        final List<Particle> cell, final Area area) {

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


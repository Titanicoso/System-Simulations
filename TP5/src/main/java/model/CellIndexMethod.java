package model;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;


//TODO: Considerar altura y ancho diferentes
public class CellIndexMethod {

    private Map<Integer, List<Particle>> grid;
    private final int mX;
    private final int mY;
    private final double cellLength;

    public CellIndexMethod(Area area, Double maxRadius) {
        this.cellLength = maxRadius * 2;
        this.mX = findMaximumMX(area, maxRadius);
        this.mY = findMaximumMY(area, maxRadius);
    }

    private void populateGrid(final Area area) {

        grid = new HashMap<>();
        List<Integer> outParticles = new ArrayList<>();

        for (final Particle particle: area.getParticles()) {
            if(particle.getY() >= 0) {
                final int cellX = (int) Math.floor(particle.getX() / cellLength);
                final int cellY = (int) Math.floor(particle.getY() / cellLength);

                final int cell = cellX + cellY * mY;

                if (!grid.containsKey(cell)) {
                    grid.put(cell, new ArrayList<>());
                }
                grid.get(cell).add(particle);
                if (particle.getY() < area.getHeight() - 1.0 / 10) {
                    area.getOutParticles().remove(Integer.valueOf(particle.getId()));
                }
            } else {
                outParticles.add(particle.getId());
            }
        }

        int i = 0;
        while (i < outParticles.size()) {
            int id1 = outParticles.get(i);
            Particle p1 = area.getParticles().get(id1);
            p1.setVelocity(new Pair(0, 0));
            boolean overlapped = false;
            double x = rand(p1.getRadius(), area.getLength() - p1.getRadius());
            double y = rand(area.getHeight() - 1.0 / 10 + p1.getRadius(), area.getHeight() - p1.getRadius());
            p1.setPosition(new Pair(x, y));
            for (int j = 0; j < area.getOutParticles().size(); j++) {
                int id2 = area.getOutParticles().get(j);
                Particle p2 = area.getParticles().get(id2);
                if (p1.isOverlapped(p2)) {
                    overlapped = true;
                    break;
                }
            }
            if (!overlapped) {
                area.getOutParticles().add(p1.getId());
                i++;
            }
        }
    }

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

        int neighbourX = (cell % mY) + right;
        int neighbourY = (cell / mY) - up;

        if((neighbourX < 0 || neighbourX >=  mY ||
                neighbourY < 0 || neighbourY >= mY))
            return -1;

        return neighbourX + neighbourY * mY;
    }

    private void findCellNeighbours(final Map<Integer, List<Particle>> neighbours, final List<Particle> cell1,
                                           final List<Particle> cell2, final Area area) {

        if(cell1 == null || cell2 == null)
            return;

        for (final Particle particle1 : cell1) {
            for (final Particle particle2 : cell2) {
                if (particle1.getId() != particle2.getId()) {
                    if (particle1.isOverlapped(particle2)) {
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
                if (particle1.isOverlapped(particle2)) {
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
                if (particle.isOverlapped(particle2)) {
                    neighbours.add(particle2);
                }
            }
        }
    }

    public List<Particle> predictParticleNeighbours(final Particle particle, final Area area) {

        final int cellX = (int) Math.floor(particle.getX() / cellLength);
        final int cellY = (int) Math.floor(particle.getY() / cellLength);
        final int cellI = cellX + cellY * mY;

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

    private int findMaximumMX(final Area area, final Double maxRadius) {
         int m = (int) Math.floor(area.getLength() / (maxRadius * 2));
        return m == 0 ? 1 : m;
    }

    private int findMaximumMY(final Area area, final Double maxRadius) {
        int m = (int) Math.floor(area.getHeight() / (maxRadius * 2));
        return m == 0 ? 1 : m;
    }

    private static double rand(double min, double max) {
        return ThreadLocalRandom.current().nextDouble(min, max);
    }

}


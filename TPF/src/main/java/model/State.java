package model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class State {

    private List<Cell> cars;
    private Cell[][] cells;
    private int obstacleCount;
    private int length;
    private int height;
    private int maxVelocity;
    private double slowDownProbability;
    private double laneChangeProbability;

    public State(int length, int height, int maxVelocity, double slowDownProbability, double laneChangeProbability) {
        this.length = length;
        this.height = height;
        cells = new Cell[length][height];
        this.maxVelocity = maxVelocity;
        this.slowDownProbability = slowDownProbability;
        this.laneChangeProbability = laneChangeProbability;
        cars = new ArrayList<>();
    }

    private Cell getForwardNeighbour(Cell cell, int up) {

        int x = cell.getX();
        int y = cell.getY() + up;

        for (int i = x; i <= x + maxVelocity && i < length; i++) {
            if(cells[i][y] != null && (up != 0 || i != x)) {
                return new Cell(cells[i][y]);
            }
        }

        return null;
    }

    private Cell getBackwardNeighbour(Cell cell, int up) {

        int x = cell.getX();
        int y = cell.getY() + up;

        for (int i = x; i > 0 ; i--) {
            if(cells[i][y] != null && (up != 0 || i != x)) {
                return new Cell(cells[i][y]);
            }
        }

        return null;
    }

    public List<Cell> calculateNextStep() {
        List<Cell> modified = calculateModified();

        cars.stream().forEach(car -> {
            final int x = car.getX();
            final int y = car.getY();
            cells[x][y] = null;
        });

        modified.stream()
                .filter(car -> car.getX() < length)
                .forEach(car -> {
                    int x = car.getX();
                    int y = car.getY();

                    cells[x][y] = car;
                });

        final List<Cell> outOfBounds = modified.stream()
                .filter(car -> car.getX() >= length)
                .collect(Collectors.toList());

        modified.removeAll(outOfBounds);

        cars = modified;

        return outOfBounds;
    }

    private List<Cell> calculateModified() {

        final List<Cell> modified = new ArrayList<>();

        cars.stream().forEach(car -> {
            final Cell neighbour = getForwardNeighbour(car, 0);
            laneChange(car);
            Cell next = new Cell(car);
            next.updateVelocity(neighbour, maxVelocity, slowDownProbability);
            next.updatePosition();
            modified.add(next);
        });

        return modified;
    }

    private void laneChange(final Cell cell) {

        final int initialX = cell.getX();
        final int initialY = cell.getY();

        final Cell sameLaneNeighbour = getForwardNeighbour(cell, 0);
        final int currentGap = cell.getDistance(sameLaneNeighbour);
        final boolean incentive = currentGap != -1 && cell.getHopeVelocity(maxVelocity) > currentGap;

        if(!incentive)
            return ;

        for (int i = -1; i <= 1; i += 2) {
            if(initialY + i > 0 && initialY + i < height) {
                final Cell forwardOtherNeighbour = getForwardNeighbour(cell, i);
                final Cell backwardOtherNeighbour = getBackwardNeighbour(cell, i);
                final boolean willGainSpeed = forwardOtherNeighbour == null ||
                        cell.getDistance(forwardOtherNeighbour) > currentGap;
                final boolean willNotStopOthers = backwardOtherNeighbour == null ||
                        cell.getDistance(backwardOtherNeighbour) > backwardOtherNeighbour.getVelocity();
                final boolean change = ThreadLocalRandom.current().nextDouble() <= laneChangeProbability
                        && willGainSpeed && willNotStopOthers;

                if(change) {
                    cell.setY(initialY + i);
                    cells[initialX][initialY] = null;
                    cells[initialX][cell.getY()] = cell;
                    break;
                }
            }
        }
    }

    public int getCarCount() {
        return cars.size();
    }

    public int getObstacleCount() {
        return obstacleCount;
    }

    public void setObstacleCount(int obstacleCount) {
        this.obstacleCount = obstacleCount;
    }

    public List<Cell> getCars() {
        return cars;
    }

    public void setCars(List<Cell> cars) {
        this.cars = cars;
    }

    public Cell[][] getCells() {
        return cells;
    }

    public void setCells(Cell[][] cells) {
        this.cells = cells;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}

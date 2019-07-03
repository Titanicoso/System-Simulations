package model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class State {

    private List<Cell> cars;
    private Cell[][] cells;
    private int length;
    private int height;
    private int maxVelocity;
    private double probability;

    public State(int length, int height, int maxVelocity, double probability) {
        this.length = length;
        this.height = height;
        cells = new Cell[length][height];
        this.maxVelocity = maxVelocity;
        this.probability = probability;
        cars = new ArrayList<>();
    }

    private Cell getForwardNeighbour(Cell cell, int down) {

        int x = cell.getX();
        int y = cell.getY() + down;

        for (int i = x; i < x + maxVelocity && i < length; i++) {
            if(cells[i][y] != null && (down != 0 || i != x)) {
                return new Cell(cells[i][y]);
            }
        }

        return null;
    }

    public List<Cell> calculateNextStep() {
        List<Cell> modified = calculateModified();

        cars.stream().forEach(car -> {
            int x = car.getX();
            int y = car.getY();
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

        List<Cell> modified = new ArrayList<>();

        cars.stream().forEach(car -> {
            Cell neighbour = getForwardNeighbour(car, 0);
            Cell next = new Cell(car);
            next.updateVelocity(neighbour, maxVelocity, probability);
            next.updatePosition();
            modified.add(next);
        });

        return modified;
    }

    private void laneChange() {

    }

    public int getCarCount() {
        return cars.size();
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

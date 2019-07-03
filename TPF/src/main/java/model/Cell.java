package model;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Cell {

    private int x;
    private int y;
    private int velocity;


    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Cell(int x, int y, int velocity) {
        this.x = x;
        this.y = y;
        this.velocity = velocity;
    }

    public Cell(Cell other) {
        this.x = other.getX();
        this.y = other.getY();
        this.velocity = other.velocity;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getVelocity() {
        return velocity;
    }

    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }

    public void updateVelocity(Cell neighbour, int maxVelocity, double probability) {
        int newVelocity = Math.min(maxVelocity, velocity + 1);
        if(neighbour != null) {
            newVelocity = Math.min(newVelocity, getDistance(neighbour));
        }
        if(ThreadLocalRandom.current().nextDouble() <= probability) {
            newVelocity = Math.max(newVelocity - 1, 0);
        }
        this.velocity = newVelocity;
    }

    public void updatePosition() {
        this.x += velocity;
    }

    public int getDistance(Cell other) {
        return Math.abs(other.getX() - x - 1);
    }

    public boolean isObstacle() {
        return velocity == -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return x == cell.x &&
                y == cell.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

}

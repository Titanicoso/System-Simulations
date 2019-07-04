package model;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class Cell {

    private int id;
    private int x;
    private int y;
    private int velocity;


    public Cell(int id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public Cell(int id, int x, int y, int velocity) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.velocity = velocity;
    }

    public Cell(Cell other) {
        this.id = other.getId();
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void updateVelocity(Cell neighbour, int maxVelocity, double probability) {
        int newVelocity = getHopeVelocity(maxVelocity);
        if(neighbour != null) {
            newVelocity = Math.min(newVelocity, getDistance(neighbour));
        }
        if(ThreadLocalRandom.current().nextDouble() <= probability) {
            newVelocity = Math.max(newVelocity - 1, 0);
        }
        this.velocity = newVelocity;
    }

    public int getHopeVelocity(int maxVelocity) {
        return Math.min(maxVelocity, velocity + 1);
    }

    public void updatePosition() {
        this.x += velocity;
    }

    public int getDistance(Cell other) {
        return other == null ? -1 : Math.abs(other.getX() - x) - 1;
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

package sample.models;

public class Center {
    private int xCoordinate;
    private int yCoordinate;
    private boolean isActive;

    public int getxCoordinate() {
        return xCoordinate;
    }

    public void setxCoordinate(int xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public int getyCoordinate() {
        return yCoordinate;
    }

    public void setyCoordinate(int yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Center(int xCoordinate, int yCoordinate, boolean isActive) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.isActive = isActive;
    }

    public Center(int xCoordinate, int yCoordinate) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;

    }

    @Override
    public String toString() {
        return String.valueOf(isActive);

    }
}

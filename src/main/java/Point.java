import java.util.Arrays;
import java.util.Objects;

public class Point {

    private final double x;
    private final double y;
    private final int[] attributes;
    private final double[] params;
    private int neighbourIndex;
    private double distance;

    public Point(double x, double y, int[] attributes, double[] params) {
        this.x = x;
        this.y = y;
        this.attributes = attributes;
        this.params = params;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int[] getAttributes() {
        return attributes;
    }

    public double[] getParams() {
        return params;
    }

    public int getNeighbourIndex() {
        return neighbourIndex;
    }

    public Point setNeighbourIndex(int neighbourIndex) {
        this.neighbourIndex = neighbourIndex;
        return this;
    }

    public double getDistance() {
        return distance;
    }

    public Point setDistance(double distance) {
        this.distance = distance;
        return this;
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                ", attributes=" + Arrays.toString(attributes) +
                ", params=" + Arrays.toString(params) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return Double.compare(point.x, x) == 0 &&
                Double.compare(point.y, y) == 0 &&
                Arrays.equals(attributes, point.attributes) &&
                Arrays.equals(params, point.params);
    }

    @Override
    public int hashCode() {

        int result = Objects.hash(x, y);
        result = 31 * result + Arrays.hashCode(attributes);
        result = 31 * result + Arrays.hashCode(params);
        return result;
    }
}
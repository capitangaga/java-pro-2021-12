package ru.kirillgolovko.cw.common.model;

/**
 * @author kirillgolovko
 */
public class Point {
    private final double x;
    private final double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point() {
        this(0, 0);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Point minus(Point point) {
        return new Point(x - point.getX(), y - point.getY());
    }

    public Point plus(Point point) {
        return new Point(x + point.getX(), y + point.getY());
    }

    public double dot(Point point) {
        return x * point.x + y * point.y;
    }

    public Point mult(double a) {
        return new Point(x * a, y * a);
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}

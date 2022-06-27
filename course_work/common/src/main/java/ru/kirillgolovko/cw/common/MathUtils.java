package ru.kirillgolovko.cw.common;

import ru.kirillgolovko.cw.common.model.game.Point;

public class MathUtils {
    private static double EPS = Double.MIN_NORMAL;

    /*
        See http://e-maxx.ru/algo/circle_line_intersection
     */
    public static Point findCircleAndLineCrossing(Point circleCenter, double r, Point lp1, Point lp2) {
        // Сместим все в ск с центром в центре окружности
        lp1 = lp1.minus(circleCenter);
        lp2 = lp2.minus(circleCenter);

        LineEq lineEq = getLineEqFrom2Points(lp1, lp2);

        double a2b2 = Math.pow(lineEq.a(), 2) + Math.pow(lineEq.b(), 2);

        double x0 = -lineEq.a() * lineEq.c() / a2b2;
        double y0 = -lineEq.b() * lineEq.c() / a2b2;

        Point finalPoint;

        if (Math.pow(lineEq.c(), 2) > Math.pow(r, 2) * a2b2 + EPS) {
            return null;
        } else if (Math.abs(Math.pow(lineEq.c(), 2) - Math.pow(r, 2) * a2b2) < EPS) {
            finalPoint = new Point(x0, y0);
        } else {
            double d = Math.pow(r, 2) - Math.pow(lineEq.c(), 2) / a2b2;
            double mult = Math.sqrt(d / a2b2);
            Point point1 = new Point(x0 + lineEq.b * mult, y0 - lineEq.a * mult);
            Point point2 = new Point(x0 - lineEq.b * mult, y0 + lineEq.a * mult);
            if (isPointOk(point1, circleCenter)) {
                finalPoint = point1;
            } else {
                finalPoint = point2;
            }
        }

        return finalPoint.plus(circleCenter);
    }

    public static boolean isPointOk(Point testPoint, Point circleCenter) {
        double x = testPoint.plus(circleCenter).getX();
        return x >= 0 && x <=1;
    }

    public static LineEq getLineEqFrom2Points(Point a, Point b) {
        double x = b.getY() - a.getY();
        double y = a.getX() - b.getX();
        double c = a.getY() * b.getX() - a.getX() * b.getY();
        return new LineEq(x, y, c);
    }

    public static Point mirrorVec(Point vec, Point mirrorA, Point mirrorB) {
        Point norm = new Point(mirrorA.getY() - mirrorB.getY(), mirrorB.getX() - mirrorA.getX());
        return vec.minus(norm.mult(2 * vec.dot(norm) / norm.dot(norm))).mult(-1);
    }

    public static boolean pointInCircle(Point center, double radius, Point point) {
        return Math.pow(point.getX() - center.getX(), 2) + Math.pow(point.getY() - center.getY(), 2)
                <= Math.pow(radius, 2);
    }

    public record LineEq(double a, double b, double c) {}
}

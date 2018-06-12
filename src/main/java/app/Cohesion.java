package app;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;


class Cohesion {
    private List<Point[]> pairs;
    private double valueOfCohesion;

    Cohesion(LinkedList<Point[]> allPairs, double valueOfCohesion) {
        this.pairs = allPairs;
        this.valueOfCohesion = valueOfCohesion;
    }

    LinkedList<Point[]> neighbours(int numberOfNeighbours) {
        LinkedList<Point[]> cohesionNeighbours = new LinkedList<>();
        boolean consist;
        for (Point[] pair : pairs) {
            consist = checkNeighbors(pair[0], pair[1], numberOfNeighbours);
            if (consist) {
                cohesionNeighbours.add(pair);
            }
        }

        return cohesionNeighbours;
    }

    private boolean checkNeighbors(Point point1, Point point2, int numberOfNeighbours) {
        ArrayList<Point> points1 = new ArrayList<>();
        ArrayList<Point> points2 = new ArrayList<>();
        int coh = 0;
        for (Point[] pair : pairs) {
            pair[0].setDistance(euclidesDistance(point1, pair[0]));
            if (pair[0].getDistance() != 0) {
                points1.add(pair[0]);
            }
            pair[1].setDistance(euclidesDistance(point2, pair[1]));
            if (pair[1].getDistance() != 0) {
                points2.add(pair[1]);
            }
        }
        points1.sort(new distanceComparator());
        points2.sort(new distanceComparator());
        for (int i = 0; i < numberOfNeighbours; i++) {
            int key = points1.get(i).getNeighbourIndex();
            Point p = pairs.get(key)[1];
            for (int j = 0; j < numberOfNeighbours; j++) {
                if (points2.get(j) == p) {
                    coh++;
                }
            }
        }
        return (double) coh / (double) numberOfNeighbours > valueOfCohesion;
    }

    private double euclidesDistance(Point p1, Point p2) {
        return Math.sqrt(Math.pow((p1.getX() - p2.getX()), 2) + Math.pow((p1.getY() - p2.getY()), 2));
    }

    private static class distanceComparator implements Comparator<Point> {
        @Override
        public int compare(Point p1, Point p2) {
            return Double.compare(p1.getDistance(), p2.getDistance());
        }
    }
}

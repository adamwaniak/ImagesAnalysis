package app;

import java.util.LinkedList;

class FindNeighboursAlgorithm {
    private final Point[] imageI;
    private final Point[] imageJ;

    FindNeighboursAlgorithm(Point[] imageI, Point[] imageJ) {
        this.imageI = imageI;
        this.imageJ = imageJ;
    }

    LinkedList<Point[]> pairPoints() {
        pairPoints(imageI, imageJ);
        pairPoints(imageJ, imageI);

        LinkedList<Point[]> pairs = new LinkedList<>();

        int minLength = imageI.length;
        if (minLength > imageJ.length) {
            minLength = imageJ.length;
        }

        for (int i = 0; i < minLength; i++) {
            int index = imageI[i].getNeighbourIndex();
            if (i == imageJ[index].getNeighbourIndex()) {
                imageI[i].setNeighbourIndex(pairs.size());
                imageJ[index].setNeighbourIndex(pairs.size());
                pairs.add(new Point[]{imageI[i], imageJ[index]});

            }
        }

        return pairs;
    }

    private void pairPoints(Point[] imageI, Point[] imageJ) {
        double bestDistance;
        int bestNeighbour;
        double distance;
        for (Point anImageI : imageI) {
            bestDistance = Double.MAX_VALUE;
            bestNeighbour = 0;
            for (int j = 0; j < imageJ.length; j++) {
                distance = pointsAttributeDistance(anImageI, imageJ[j]);
                if (distance < bestDistance) {
                    bestDistance = distance;
                    bestNeighbour = j;
                }
            }
            anImageI.setNeighbourIndex(bestNeighbour);
            anImageI.setDistance(bestDistance);
        }
    }

    private double pointsAttributeDistance(Point p1, Point p2) {
        double value = 0;

        for (int i = 0; i < p1.getAttributes().length; i++) {
            value += (p1.getAttributes()[i] - p2.getAttributes()[i]) * (p1.getAttributes()[i] - p2.getAttributes()[i]);
        }
        return Math.sqrt(value);
    }
}

package app.ransac;

import app.Point;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.LinkedList;


public class Ransac {

    private AffinicTransformation algorithm;

    public Ransac() {
        this.algorithm = new AffinicTransformation();
    }

    public RealMatrix run(Point[] imageOnePoints, Point[] imageTwoPoints, LinkedList<Point[]> pairs, int maxerror, int iteration) {

        RealMatrix bestModel = null;
        int bestscore = 0;
        RealMatrix model = null;
        int score = 0;

        for (int i = 0; i < iteration; i++) {
            model = null;
            while (model == null) {
                model = algorithm.calculateModel(pairs);
            }
            score = 0;
            for (int k = 0; k < imageTwoPoints.length; k++) {
                Point p2 = getTransformedPoint(imageTwoPoints[k], model);
                int error = modelError(p2, imageTwoPoints[k]);
                if (error < maxerror) {
                    score += 1;
                }
            }
            if (score > bestscore) {
                bestscore = score;
                bestModel = model;
            }
        }
        return bestModel;
    }

    private int modelError(Point point, Point point1) {
        return (int) Math.sqrt(euclidesDistance(point, point1));
    }

    private double euclidesDistance(Point p1, Point p2) {
        return Math.sqrt(Math.pow((p1.getX() - p2.getX()), 2) + Math.pow((p1.getY() - p2.getY()), 2));
    }

    private Point getTransformedPoint(Point point, RealMatrix bestModel) {
        RealMatrix pointMatrix = new Array2DRowRealMatrix(new double[]{point.getX(), point.getY(), 1});
        RealMatrix res = bestModel.multiply(pointMatrix);
        return new Point(res.getEntry(0, 0), res.getEntry(1, 0), null, null);

    }

    public LinkedList<Point[]> getSelectedPairs(LinkedList<Point[]> allPairs, RealMatrix model, int maxError) {
        LinkedList<Point[]> selectedPairs = new LinkedList<>();
        while (allPairs.size() > 0) {
            Point[] pPairs = allPairs.poll();
            Point p2 = getTransformedPoint(pPairs[1], model);
            if (modelError(pPairs[1], p2) < maxError) {
                selectedPairs.add(pPairs);
            }
        }
        return selectedPairs;
    }
}

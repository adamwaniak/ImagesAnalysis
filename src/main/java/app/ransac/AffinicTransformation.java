package app.ransac;


import app.Point;
import org.apache.commons.math3.linear.*;

import java.util.*;
import java.util.List;

public class AffinicTransformation {

    public RealMatrix calculateModel(LinkedList<Point[]> pairs) {
        List<Point[]> pointsForAffinicTransformation = new ArrayList<>();
        Random random = new Random();

        Point[] p;
        while (pointsForAffinicTransformation.size() < 3) {
            p = pairs.get(random.nextInt(pairs.size()));
            if (!pointsForAffinicTransformation.contains(p))
                pointsForAffinicTransformation.add(p);
        }

        Point p1 = pointsForAffinicTransformation.get(0)[0];
        Point n1 = pointsForAffinicTransformation.get(0)[1];

        Point p2 = pointsForAffinicTransformation.get(1)[0];
        Point n2 = pointsForAffinicTransformation.get(1)[1];

        Point p3 = pointsForAffinicTransformation.get(2)[0];
        Point n3 = pointsForAffinicTransformation.get(2)[1];

        double[][] transformationPart1 = {
                {p1.getX(), p1.getY(), 1, 0, 0, 0},
                {p2.getX(), p2.getY(), 1, 0, 0, 0},
                {p3.getX(), p3.getY(), 1, 0, 0, 0},
                {0, 0, 0, p1.getX(), p1.getY(), 1},
                {0, 0, 0, p2.getX(), p2.getY(), 1},
                {0, 0, 0, p3.getX(), p3.getY(), 1}
        };

        RealMatrix transformMatrixPart1 = new Array2DRowRealMatrix(transformationPart1);
        transformMatrixPart1 = new LUDecomposition(transformMatrixPart1).getSolver().getInverse();

        double[][] transoformationPart2 = {{n1.getX()}, {n2.getX()}, {n3.getX()}, {n1.getY()}, {n2.getY()}, {n3.getY()}};
        RealMatrix transformMatrixPart2 = new Array2DRowRealMatrix(transoformationPart2);

        RealMatrix resMatrix = transformMatrixPart1.multiply(transformMatrixPart2);
        double[][] transformMatrixArray = {
                {resMatrix.getEntry(0, 0), resMatrix.getEntry(1, 0), resMatrix.getEntry(2, 0)},
                {resMatrix.getEntry(3, 0), resMatrix.getEntry(4, 0), resMatrix.getEntry(5, 0)},
                {0, 0, 1}
        };

        RealMatrix transformMatrix = new Array2DRowRealMatrix(transformMatrixArray);
        return transformMatrix;
    }


    private void printMatrix(RealMatrix realMatrix){
        StringBuilder sb = new StringBuilder();
        for(int i=0;  i<realMatrix.getColumnDimension(); i++){
            sb.append("| ");
            for(int j=0; j<realMatrix.getRowDimension(); j++){
                sb.append(realMatrix.getEntry(j, i)+" ");
                if(j==realMatrix.getRowDimension()-1){
                    sb.append("| \n");
                }
            }
        }
        System.out.println(sb.toString());
    }
}

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;

class SiftFileReader {

    Point[] read(String filePath) {
        Point[] points = null;
        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            Scanner scanner = new Scanner(br);
            int attributesAmount = scanner.nextInt();
            int pointsAmount = scanner.nextInt();
            points = new Point[pointsAmount];

            for (int i = 0; i < pointsAmount; i++) {
                double x = scanner.nextDouble();
                double y = scanner.nextDouble();
                double[] params = new double[3];
                for (int j = 0; j < 3; j++) {
                    params[j] = scanner.nextDouble();
                }
                int[] attributes = new int[attributesAmount];
                for (int j = 0; j < attributesAmount; j++) {
                    attributes[j] = scanner.nextInt();
                }
                points[i] = new Point(x, y, attributes, params);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return points;
    }

}

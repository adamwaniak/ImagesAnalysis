import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;

public class FindNeighboursAlgorithmTest {

    @Test
    public void test1() {
        SiftFileReader siftFileReader = new SiftFileReader();
        Point[] image1 = siftFileReader.read("src/res/1/DSC08085.png.haraff.sift");
        Point[] image2 = siftFileReader.read("src/res/1/DSC08087.png.haraff.sift");

        FindNeighboursAlgorithm findNeighboursAlgorithm = new FindNeighboursAlgorithm(image1,image2);
        LinkedList<Point[]> pairs = findNeighboursAlgorithm.pairPoints();
        for (Point[] pair: pairs){
            System.out.println(Arrays.toString(pair));
        }
        System.out.println("");
    }
}

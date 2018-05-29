import org.junit.Test;

import java.util.Arrays;

public class SiftFileReaderTest {



    @Test
    public void test1() {
        SiftFileReader siftFileReader = new SiftFileReader();

        Point[] points = siftFileReader.read("src/res/1/DSC08085.png.haraff.sift");
        System.out.println(Arrays.toString(points));
    }
}

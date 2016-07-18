
import java.awt.*;

import static org.junit.Assert.*;

/**
 * Created by Lieven on 18-7-2016.
 */
public class ColorSplitterTest {


    @org.junit.Test
    public void testCombineToRGBA() throws Exception {
        int num = ColorSplitter.combineToRGBA(new int[]{2,5,10,1});
        int[] answer = ColorSplitter.splitToColors(num);
        assertEquals(answer[0], 2);
        assertEquals(answer[1], 5);
        assertEquals(answer[2], 10);
        assertEquals(answer[3], 1);
    }
}
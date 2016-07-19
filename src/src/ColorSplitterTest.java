
import java.awt.*;

import static org.junit.Assert.*;

/**
 * Created by Lieven on 18-7-2016.
 */
public class ColorSplitterTest {


    @org.junit.Test
    public void testCombineToRGBA() throws Exception {
        int num = ColorSplitter.combineToRGBA(new int[]{255,255,255,255});
        int[] answer = ColorSplitter.splitToColors(num);
        assertEquals(answer[0], 255);
        assertEquals(answer[1], 255);
        assertEquals(answer[2], 255);
        assertEquals(answer[3], 255);
    }


    @org.junit.Test
    public void testsplitToRgba() throws Exception {
        int[] subcolors = ColorSplitter.splitToColors(-11514552);
        int answer = ColorSplitter.combineToRGBA(subcolors);
        assertEquals(answer, -11514552);
    }
}
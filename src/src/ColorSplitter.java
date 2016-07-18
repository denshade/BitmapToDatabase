/**
 * Created by Lieven on 18-7-2016.
 */
public class ColorSplitter
{
    public static int[] splitToColors(int rgbColor)
    {
        int r = (rgbColor)&0xFF;
        int g = (rgbColor>>8)&0xFF;
        int b = (rgbColor>>16)&0xFF;
        int a = (rgbColor>>24)&0xFF;
        return new int[] {r,g,b,a};
    }

    public static int combineToRGBA(int[] colors)
    {
        int r = colors[0];
        int g = colors[1];
        int b = colors[2];
        int a = colors[3];
        return r + (g << 8) + (b << 16) + (a << 24);
    }
}

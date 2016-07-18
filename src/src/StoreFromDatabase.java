

import javax.imageio.ImageIO;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.sql.*;

/**
 * Created by Lieven on 18-7-2016.
 */
public class StoreFromDatabase
{
    public static void main(String[] args)
    {
        if (args.length != 1)
        {
            System.err.println("Usage: StoreFromDatabase <filename>");
            System.exit(1);
        }
        String filename = args[0];
        File file = new File(filename);
        if (file.canRead())
        {
            System.err.println("File already exists: " + file);
            System.exit(2);
        }
        int id = lookupFile(filename);
        try {
            loadImageFromDatabase(id, filename);
        } catch (IOException e) {
            System.err.println("Can't read file " + file);
            System.exit(2);
        }
    }

    private static int lookupFile(String filename) {
        return 0;
    }

    private static void loadImageFromDatabase(int id, final String filename) throws IOException {
        String url = "jdbc:mysql://localhost:3306/imagedb";
        String username = "java";
        String password = "password";
        System.out.println("Connecting database...");

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement maxDim = connection.prepareStatement("SELECT max(x), max(y) FROM imagedata WHERE imageid = ?");
            maxDim.setInt(1, id);
            ResultSet set = maxDim.executeQuery();
            set.next();
            int maxX = set.getInt(1) + 1;
            int maxY = set.getInt(2) + 1;
            int[] matrix = new int[maxX * maxY];
            PreparedStatement colorLoop = connection.prepareStatement("SELECT x, y, r, g, b, a FROM imagedata WHERE imageid = ?");
            colorLoop.setInt(1, id);
            ResultSet colorset = colorLoop.executeQuery();
            while (colorset.next())
            {
                int counter = 1;
                int x = colorset.getInt(counter++);
                int y = colorset.getInt(counter++);
                int r = colorset.getInt(counter++);
                int g = colorset.getInt(counter++);
                int b = colorset.getInt(counter++);
                int a = colorset.getInt(counter++);
                matrix[x + y * maxX] = ColorSplitter.combineToRGBA(new int[]{r,g,b,a});
            }


            DataBufferInt buffer = new DataBufferInt(matrix, matrix.length);

            int[] bandMasks = {0xFF0000, 0xFF00, 0xFF, 0xFF000000}; // ARGB (yes, ARGB, as the masks are R, G, B, A always) order
            WritableRaster raster = Raster.createPackedRaster(buffer, maxX, maxY, maxX, bandMasks, null);

            System.out.println("raster: " + raster);

            ColorModel cm = ColorModel.getRGBdefault();
            BufferedImage image = new BufferedImage(cm, raster, cm.isAlphaPremultiplied(), null);
            ImageIO.write(image, "jpg", new File(filename));

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}

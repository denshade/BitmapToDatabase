

import javax.imageio.ImageIO;
import java.awt.*;
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
            System.err.println("Cancelled: Output File already exists: " + file);
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
        return 1;
    }

    private static void loadImageFromDatabase(int id, final String filename) throws IOException {
        String url = "jdbc:mysql://localhost:3306/imagedb";
        String username = "java";
        String password = "password";
        System.out.println("Connecting database...");

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            Dimension dimension = getImageDimension(id, connection);
            ResultSet colorset = getColorResultSet(id, connection);
            BufferedImage image = new BufferedImage((int)dimension.getWidth(), (int)dimension.getHeight(), BufferedImage.TYPE_INT_RGB);

            while (colorset.next())
            {
                int counter = 1;
                int x = colorset.getInt(counter++);
                int y = colorset.getInt(counter++);
                int r = colorset.getInt(counter++);
                int g = colorset.getInt(counter++);
                int b = colorset.getInt(counter++);
                int a = colorset.getInt(counter++);
                image.setRGB(x, y, ColorSplitter.combineToRGBA(new int[]{r,g,b,a}));
            }

            ImageIO.write(image, "jpg", new File(filename));
            System.out.println("Stored image");
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    private static ResultSet getColorResultSet(int id, Connection connection) throws SQLException {
        PreparedStatement colorLoop = connection.prepareStatement("SELECT x, y, r, g, b, a FROM imagedata WHERE imageid = ?");
        colorLoop.setInt(1, id);
        return colorLoop.executeQuery();
    }

    private static Dimension getImageDimension(int id, Connection connection) throws SQLException {
        PreparedStatement maxDim = connection.prepareStatement("SELECT max(x), max(y) FROM imagedata WHERE imageid = ?");
        maxDim.setInt(1, id);
        ResultSet set = maxDim.executeQuery();
        set.next();
        int maxX = set.getInt(1) + 1;
        int maxY = set.getInt(2) + 1;
        return new Dimension(maxX, maxY);
    }
}

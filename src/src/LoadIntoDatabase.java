


import java.sql.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Lieven on 17-7-2016.
 */
public class LoadIntoDatabase
{
    public static void main(String[] args)
    {
        if (args.length != 1)
        {
            System.err.println("Usage: LoadIntoDatabase <filename>");
            System.exit(1);
        }
        String filename = args[0];
        File file = new File(filename);
        if (!file.canRead())
        {
            System.err.println("Can't read file " + file);
            System.exit(2);
        }
        int id = registerImage(filename);
        try {
            BufferedImage img = ImageIO.read(new File(filename));
            storeImageToDb(img, id);
        } catch (IOException e) {
            System.err.println("Can't read file " + file);
            System.exit(2);
        }
    }

    private static int registerImage(String filename) {
        //TODO
        return 2;
    }

    private static void storeImageToDb(BufferedImage image, int id) {
        String url = "jdbc:mysql://localhost:3306/imagedb";
        String username = "java";
        String password = "password";

        System.out.println("Connecting database...");

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            System.out.println("Database connected!");

        int width = image.getWidth();
        int height = image.getHeight();
        PreparedStatement statement = connection.prepareStatement("INSERT INTO imagedata(imageid, x, y, r, g, b, a) values(?,?,?,?,?,?, ?)");
            statement.setInt(1, id);
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                statement.setInt(2, col);
                statement.setInt(3, row);

                int color = image.getRGB(col, row);
                image.getRaster();
                int[] colorArray = ColorSplitter.splitToColors(color);
                int r = colorArray[0];
                int g = colorArray[1];
                int b = colorArray[2];
                int a = colorArray[3];
                statement.setInt(4, r);
                statement.setInt(5, g);
                statement.setInt(6, b);
                statement.setInt(7, a);
                statement.execute();
            }
        }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }


        System.out.println("Stored image");
    }

}

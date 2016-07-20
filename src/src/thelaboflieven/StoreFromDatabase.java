package thelaboflieven;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.sql.*;

/**
 * Created by Lieven on 18-7-2016.
 */
public class StoreFromDatabase {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: thelaboflieven.StoreFromDatabase <filename>");
            System.exit(1);
        }
        String filename = args[0];
        File file = new File(filename);
        if (file.canRead()) {
            System.err.println("Cancelled: Output File already exists: " + file);
            System.exit(2);
        }
        int id = lookupFile(filename);
        try {
            loadImageFromDatabase(id, filename);
        } catch (IOException e) {
            System.err.println("Can't write file " + file);
            System.exit(2);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static int lookupFile(String filename) {
        return 1;
    }

    private static void loadImageFromDatabase(int id, final String filename) throws IOException, SQLException {
        DatabaseConnector connector = new DatabaseConnector();


        Dimension dimension = connector.getImageDimension(id);
        ResultSet colorset = connector.getColorResultSet(id);
        BufferedImage image = new BufferedImage((int) dimension.getWidth(), (int) dimension.getHeight(), BufferedImage.TYPE_INT_RGB);

        while (colorset.next()) {
            int counter = 1;
            int x = colorset.getInt(counter++);
            int y = colorset.getInt(counter++);
            int r = colorset.getInt(counter++);
            int g = colorset.getInt(counter++);
            int b = colorset.getInt(counter++);
            int a = colorset.getInt(counter++);
            image.setRGB(x, y, ColorSplitter.combineToRGBA(new int[]{r, g, b, a}));
        }

        ImageIO.write(image, "jpg", new File(filename));
        System.out.println("Stored image");

    }

}

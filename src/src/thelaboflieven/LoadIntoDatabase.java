package thelaboflieven;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Lieven on 17-7-2016.
 */
public class LoadIntoDatabase
{
    public static void main(String[] args) throws Exception {
        if (args.length != 2)
        {
            System.err.println("Usage: thelaboflieven.LoadIntoDatabase <filename> <destinationImageId>");
            System.exit(1);
        }
        String filename = args[0];
        int sourceImageId = Integer.parseInt(args[1]);
        File file = new File(filename);
        if (!file.canRead())
        {
            System.err.println("Can't read file " + file);
            System.exit(2);
        }
        try {
            BufferedImage img = ImageIO.read(new File(filename));
            if (img == null) {
                throw new Exception("The image cannot be loaded: " + filename);
            }
            storeImageToDb(img, sourceImageId);
        } catch (IOException e) {
            System.err.println("Can't read file " + file);
            System.exit(2);
        }
    }

    private static void storeImageToDb(BufferedImage image, int id) throws SQLException {
        DatabaseConnector connector = new DatabaseConnector();

        int width = image.getWidth();
        int height = image.getHeight();
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {

                int color = image.getRGB(col, row);
                int[] colorArray = ColorSplitter.splitToColors(color);
                int r = colorArray[0];
                int g = colorArray[1];
                int b = colorArray[2];
                int a = colorArray[3];
                connector.addPixel(id, col, row, r, g, b, a);
            }
        }


        System.out.println("Stored image");
    }

}

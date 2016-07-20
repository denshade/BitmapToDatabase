package thelaboflieven;

import java.awt.*;
import java.sql.*;

/**
 * Created by Lieven on 20-7-2016.
 */
public class DatabaseConnector
{
    private Connection connection;
    private PreparedStatement addPixelStatement;
    private PreparedStatement maxDim;

    public DatabaseConnector() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/imagedb";
        String username = "java";
        String password = "password";

        System.out.println("Connecting database...");

        connection = DriverManager.getConnection(url, username, password);
        addPixelStatement = connection.prepareStatement("INSERT INTO imagedata(imageid, x, y, r, g, b, a) values(?,?,?,?,?,?, ?)");
        maxDim = connection.prepareStatement("SELECT max(x), max(y) FROM imagedata WHERE imageid = ?");
    }

    public void addPixel(int imageid, int x, int y, int r, int g, int b, int a) throws SQLException {
        addPixelStatement.setInt(1, imageid);
        addPixelStatement.setInt(2, x);
        addPixelStatement.setInt(3, y);
        addPixelStatement.setInt(4, r);
        addPixelStatement.setInt(5, g);
        addPixelStatement.setInt(6, b);
        addPixelStatement.setInt(7, a);
        addPixelStatement.execute();
    }

    public Dimension getImageDimension(int id) throws SQLException {

        maxDim.setInt(1, id);
        ResultSet set = maxDim.executeQuery();
        if (set.next())
        {
            int maxX = set.getInt(1) + 1;
            int maxY = set.getInt(2) + 1;
            return new Dimension(maxX, maxY);
        }
        return null;
    }

    public ResultSet getColorResultSet(int id) throws SQLException {
        PreparedStatement colorLoop = connection.prepareStatement("SELECT x, y, r, g, b, a FROM imagedata WHERE imageid = ?");
        colorLoop.setInt(1, id);
        return colorLoop.executeQuery();
    }

}

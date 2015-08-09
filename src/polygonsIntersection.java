import math.geom2d.polygon.Polygon2D;
import math.geom2d.polygon.Polygons2D;
import math.geom2d.polygon.SimplePolygon2D;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by erna on 09.08.15.
 */


public class polygonsIntersection {

    /**
     * Returns the whole file as a string
     * @param fileName path to the file as a string
     * @return file content as a string
     * @throws IOException
     */
    public static String readFile(String fileName) throws IOException
    {
        Path path = Paths.get(fileName);
        byte[] bytes = Files.readAllBytes(path);
        return new String(bytes);
    }

    /**
     * reads the polygon coordinates, converts them to 2d polygons
     * and returns the two polygons as an polygon2d-array
     * @param data string with the content of the geojson-file
     * @return polygons
     */

    public static SimplePolygon2D[] getPolygons(String data)
    {
        JSONObject obj = (JSONObject) JSONValue.parse(data);
        JSONArray jsonArr = (JSONArray) obj.get("features");

        JSONObject polygon1 = (JSONObject) JSONValue.parse(jsonArr.get(0).toString());

        JSONObject polygon2 = (JSONObject) JSONValue.parse(jsonArr.get(1).toString());

        JSONArray coordinates1 = (JSONArray)((JSONObject)polygon1.get("geometry")).get("coordinates");

        JSONArray coordinates2 = (JSONArray)((JSONObject)polygon2.get("geometry")).get("coordinates");

        SimplePolygon2D polygons[] = new SimplePolygon2D[2];

        JSONArray temparr = (JSONArray)coordinates1.get(0);
        JSONArray tempcoord;
        double x1[] = new double[temparr.size()];
        double y1[] = new double[temparr.size()];

        for(int i=0;i<temparr.size();i++)
        {
            tempcoord = (JSONArray)temparr.get(i);
            x1[i] = ((Number)tempcoord.get(0)).doubleValue();
            y1[i] = ((Number)tempcoord.get(1)).doubleValue();
        }
        polygons[0] = new SimplePolygon2D(x1,y1);

        temparr = (JSONArray)coordinates2.get(0);
        double x2[] = new double[temparr.size()];
        double y2[] = new double[temparr.size()];
        for(int i=0;i<temparr.size();i++)
        {
            tempcoord = (JSONArray)temparr.get(i);
            x2[i] = ((Number)tempcoord.get(0)).doubleValue();
            y2[i] = ((Number)tempcoord.get(1)).doubleValue();
        }
        polygons[1] = new SimplePolygon2D(x2, y2);
        return polygons;
    }

    public static void main (String args[])
    {
        String data;
        try {
            if(args.length>0) {
                data = readFile("GeoJsonShapes/" + args[0]);

                SimplePolygon2D polygons[] = getPolygons(data);

                Polygon2D poly = Polygons2D.intersection(polygons[0],polygons[1]);
                System.out.println("Fläche: "+poly.area());
                for(int i=0;i<poly.vertexNumber();i++)
                {
                    System.out.println(poly.vertex(i).getX()+", "+poly.vertex(i).getY());
                }
            }
            else
            {
                System.out.println("Geben Sie bitte nach dem Programmnamen einen Dateinamen ein");
            }
        }
        catch (IOException e)
        {
            System.out.println("Eine Datei mit diesem Namen existiert nicht");
        }
    }
}

import cz.kitnarf.geom.Polygon2D;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;


import java.awt.geom.Area;
import java.awt.geom.Point2D;
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

    public static Polygon2D[] getPolygons(String data)
    {
        JSONObject obj = (JSONObject) JSONValue.parse(data);
        JSONArray jsonArr = (JSONArray) obj.get("features");

        JSONObject polygon1 = (JSONObject) JSONValue.parse(jsonArr.get(0).toString());

        JSONObject polygon2 = (JSONObject) JSONValue.parse(jsonArr.get(1).toString());

        JSONArray coordinates1 = (JSONArray)((JSONObject)polygon1.get("geometry")).get("coordinates");

        JSONArray coordinates2 = (JSONArray)((JSONObject)polygon2.get("geometry")).get("coordinates");

        Polygon2D polygons[] = new Polygon2D[2];

        JSONArray temparr = (JSONArray)coordinates1.get(0);
        JSONArray tempcoord;
        Double x;
        Double y;
        polygons[0] = new Polygon2D();
        for(int i=0;i<temparr.size();i++)
        {
            tempcoord = (JSONArray)temparr.get(i);
            x = ((Number)tempcoord.get(0)).doubleValue();
            y = ((Number)tempcoord.get(1)).doubleValue();
            polygons[0].addPoint(x, y);
        }

        temparr = (JSONArray)coordinates2.get(0);
        polygons[1] = new Polygon2D();
        for(int i=0;i<temparr.size();i++)
        {
            tempcoord = (JSONArray)temparr.get(i);
            x = ((Number)tempcoord.get(0)).doubleValue();
            y = ((Number)tempcoord.get(1)).doubleValue();
            polygons[1].addPoint(x, y);
        }
        return polygons;
    }

    public static void main (String args[])
    {
        String data;
        try {
            if(args.length>0) {
                data = readFile("GeoJsonShapes/" + args[0]);

                Polygon2D polygons[] = getPolygons(data);

                if (polygons[0].contains(polygons[1])) {
                    System.out.println("Polygon 2 ist in Polygon 1 enthalten");
                } else if (polygons[1].contains(polygons[0])) {
                    System.out.println("Polygon 1 ist in Polygon 2 enthalten");
                } else {
                    Area a = new Area(polygons[0]);
                    Area b = new Area(polygons[1]);
                    b.intersect(a);
                    Polygon2D result = new Polygon2D(b);
                    if (result.getPointCount() == 0) {
                        System.out.println("Diese Polynome schneiden sich nicht");
                    } else {
                        Point2D points[] = result.getPoints();
                        System.out.println("Koordinaten des Ergebnisspolygons");
                        for (int i = 0; i < result.getPointCount(); i++) {
                            System.out.println(points[i].getX() + ", " + points[i].getY());
                        }
                    }
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

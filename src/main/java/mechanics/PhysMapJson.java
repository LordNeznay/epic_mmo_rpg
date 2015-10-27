package mechanics;

import com.sun.javafx.geom.Vec2d;
import org.json.simple.JSONObject;

import java.io.File;
import java.util.Scanner;

/**
 * Created by Андрей on 27.10.2015.
 */
public class PhysMapJson implements PhysMap {
    public String getArea(Vec2d coord){
        Scanner file = null;
        try {
            file = new Scanner(new File("public_html/res/tilemap.json"));
        } catch(Exception e){
            System.out.print(e.toString());
        }
        String s = "";
        while(file.hasNext())
        {
            s+=file.nextLine();
        }

        return s;
    }
}

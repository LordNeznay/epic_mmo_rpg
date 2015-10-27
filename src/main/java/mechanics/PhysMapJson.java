package mechanics;

import com.sun.javafx.geom.Vec2d;
import org.json.simple.JSONObject;

/**
 * Created by Андрей on 27.10.2015.
 */
public class PhysMapJson implements PhysMap {
    public String getArea(Vec2d coord){
        JSONObject jsonStart = new JSONObject();
        jsonStart.put("karta", "Kusok kartu");
        return jsonStart.toString();
    }
}

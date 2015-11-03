package mechanics;

import com.sun.javafx.geom.Vec2d;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import utils.MapReader;

/**
 * Created by Андрей on 27.10.2015.
 */
public class PhysMapJson implements PhysMap {
    private static final int VIEW_WIDTH_2 = 8;
    private static final int VIEW_HEIGHT_2 = 5;
    private int mapWidth;
    private int mapHeight;
    private int tileWidth;
    private int tileHeight;
    private boolean[][] passabilityLayer;
    private int[][] backgroundLayer;
    private int[][] frontgroundLayer;
    private String tilesetsInfo = "";
    private String objectLayer = "";

    public PhysMapJson(){
        JSONObject map = MapReader.readMap("public_html/res/tilemap.json");
        assert map != null;
        try{
            mapWidth = Integer.valueOf(map.get("width").toString());
            mapHeight = Integer.valueOf(map.get("height").toString());
            tileWidth = Integer.valueOf(map.get("tilewidth").toString());
            tileHeight = Integer.valueOf(map.get("tileheight").toString());
        }catch (NumberFormatException e) {
            System.err.println("Cannot parse game map!");
        }

        passabilityLayer = new boolean[mapWidth][mapHeight];
        backgroundLayer = new int[mapWidth][mapHeight];
        frontgroundLayer = new int[mapWidth][mapHeight];

        int impassableGid = getGidIsNotPassability((JSONArray)map.get("tilesets"));

        getLayers((JSONArray)map.get("layers"), impassableGid);
    }

    private void getLayers(JSONArray layers, int impassableGid){
        for(Object layer : layers){
            String layerName = ((JSONObject)layer).get("name").toString();
            if(layerName.equals("Objects")){
                objectLayer = ((JSONObject)layer).get("objects").toString();
                continue;
            }
            JSONArray layerData = (JSONArray)((JSONObject) layer).get("data");
            int x = 0;
            int y = 0;
            for(Object gid : layerData){
                switch (layerName) {
                    case "Background":
                        backgroundLayer[x][y] = ((Long) gid).intValue();
                        break;
                    case "Frontground":
                        frontgroundLayer[x][y] = ((Long) gid).intValue();
                        break;
                    case "Passability":
                        passabilityLayer[x][y] = (!(((Long) gid).intValue() == 0 || ((Long) gid).intValue() == impassableGid));
                        break;
                    default: break;
                }
                ++x;
                if(x == mapWidth){
                    x = 0;
                    ++y;
                }
            }
        }
    }

    private int getGidIsNotPassability(JSONArray tilesets){
        StringBuilder tilesetsBuilder = new StringBuilder();
        tilesetsBuilder.append('[');

        int isNotPassability = 0;
        for(Object tileset : tilesets){
            if(!((JSONObject) tileset).get("name").toString().equals("passability")){
                tilesetsBuilder.append(((JSONObject)tileset).toJSONString());
                tilesetsBuilder.append(", ");
                continue;
            }

            int tilesetWidth = 0;
            int tilesetHeight = 0;
            try{
                tilesetWidth = Integer.valueOf(((JSONObject) tileset).get("imagewidth").toString());
                tilesetHeight = Integer.valueOf(((JSONObject) tileset).get("imageheight").toString());
            }catch (NumberFormatException e) {
                System.err.println("Cannot parse game map!");
            }

            int amount = tilesetWidth/tileWidth * tilesetHeight/tileHeight;
            JSONObject tileproperties = (JSONObject) ((JSONObject) tileset).get("tileproperties");
            for(int i =0; i<amount; ++i){
                String isPassabilityString = "";
                try {
                    isPassabilityString = ((JSONObject)tileproperties.get(String.valueOf(i))).get("isPassability").toString();
                } catch (RuntimeException e){
                    e.printStackTrace();
                }
                if(isPassabilityString.equals("false")){
                    try {
                        isNotPassability = Integer.valueOf(((JSONObject) tileset).get("firstgid").toString());
                        isNotPassability += i;
                    } catch (NumberFormatException e){
                        isNotPassability = 0;
                        System.err.println("Cannot parse game map!");
                    }

                }

            }
        }

        tilesetsBuilder.append("{ \"firstgid\":-1, \"image\":\"0.png\", \"imageheight\":64,\"imagewidth\":64, \"name\":\"zeroTile\", \"tileheight\":64, \"tilewidth\":64}");
        tilesetsBuilder.append(']');
        tilesetsInfo = tilesetsBuilder.toString();
        return  isNotPassability;
    }

    @Override
    public String getArea(Vec2d coord){

        StringBuilder resultString = new StringBuilder();
        resultString.append("{\"height\":");
        resultString.append(VIEW_HEIGHT_2*2 +1);
        resultString.append(", \"width\":");
        resultString.append(VIEW_WIDTH_2*2 +1);
        resultString.append(", \"layers\":[{\"data\":[");

        for(int j = (int)coord.y - VIEW_HEIGHT_2; j <= coord.y + VIEW_HEIGHT_2; ++j){
            for(int i = (int)coord.x - VIEW_WIDTH_2; i <= coord.x + VIEW_WIDTH_2; ++i){
                if(isPositionCorrect(j, i)) {
                    resultString.append(backgroundLayer[i][j] == 0 ? -1 : backgroundLayer[i][j]);
                } else {
                    resultString.append(-1);
                }
                resultString.append(", ");
            }
        }
        resultString.append(0);
        resultString.append("], \"name\": \"Background\"},{\"data\":[");

        for(int j = (int)coord.y - VIEW_HEIGHT_2; j <= coord.y + VIEW_HEIGHT_2; ++j){
            for(int i = (int)coord.x - VIEW_WIDTH_2; i <= coord.x + VIEW_WIDTH_2; ++i){
                if(isPositionCorrect(j, i)) {
                    resultString.append(frontgroundLayer[i][j]);
                } else {
                    resultString.append(0);
                }
                resultString.append(", ");
            }
        }

        resultString.append(0);
        resultString.append("], \"name\": \"Frontground\"}], \"tileheight\":");
        resultString.append(tileHeight);
        resultString.append(", \"tilewidth\":");
        resultString.append(tileWidth);
        resultString.append(", \"tilesets\":");
        resultString.append(tilesetsInfo);
        resultString.append('}');
        return resultString.toString();
    }

    private boolean isPositionCorrect(int j, int i) {
        return i >= 0 && i < mapWidth && j >= 0 && j < mapHeight;
    }

    public boolean isPassability(Vec2d cell){
        boolean result = false;
        if(isPositionCorrect((int)cell.x, (int)cell.y) ) {
            result = passabilityLayer[(int) cell.x][(int) cell.y];
        }
        return result;
    }

    public Vec2d getSize(){
        return new Vec2d(mapWidth, mapHeight);
    }

    public String getObjectLayer() {
        return objectLayer;
    }

}

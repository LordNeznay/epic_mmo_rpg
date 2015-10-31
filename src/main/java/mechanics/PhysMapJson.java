package mechanics;

import com.sun.javafx.geom.Vec2d;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import utils.MapReader;

import java.io.File;
import java.util.Scanner;

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

    public PhysMapJson(){
        JSONObject map = MapReader.ReadMap("public_html/res/tilemap.json");
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

        //tilesetsInfo = map.get("tilesets").toString();
        getLayers((JSONArray)map.get("layers"), impassableGid);
    }

    private void getLayers(JSONArray layers, int impassableGid){
        for(Object layer : layers){
            JSONArray layerData = (JSONArray)((JSONObject) layer).get("data");
            String layerName = ((JSONObject)layer).get("name").toString();
            int x = 0;
            int y = 0;
            for(Object gid : layerData){
                gid = (Long)gid;
                if(layerName.equals("Background")){
                    backgroundLayer[x][y] = ((Long) gid).intValue();
                } else if(layerName.equals("Frontground")){
                    frontgroundLayer[x][y] = ((Long) gid).intValue();
                } else if(layerName.equals("Passability")){
                    passabilityLayer[x][y] = (((Long) gid).intValue()==0 || ((Long) gid).intValue()==impassableGid) ? false : true;
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
        int isNotPassability = 0;
        StringBuilder tilesetsBuilder = new StringBuilder();
        tilesetsBuilder.append("[");

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
                } catch (Exception e){

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
        tilesetsBuilder.append("]");
        tilesetsInfo = tilesetsBuilder.toString();
        return  isNotPassability;
    }

    public String getArea(Vec2d coord){

        StringBuilder resultString = new StringBuilder();
        resultString.append("{\"height\":");
        resultString.append(VIEW_HEIGHT_2*2 +1);
        resultString.append(", \"width\":");
        resultString.append(VIEW_WIDTH_2*2 +1);
        resultString.append(", \"layers\":[{\"data\":[");

        for(int j = (int)coord.y - VIEW_HEIGHT_2; j <= coord.y + VIEW_HEIGHT_2; ++j){
            for(int i = (int)coord.x - VIEW_WIDTH_2; i <= coord.x + VIEW_WIDTH_2; ++i){
                if(i >= 0 && i < mapWidth && j >= 0 && j < mapHeight ) {
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
                if(i >= 0 && i < mapWidth && j >= 0 && j < mapHeight ) {
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
        resultString.append("}");
        return resultString.toString();
    }
}

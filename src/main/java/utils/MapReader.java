package utils;

import  javax.xml.parsers.DocumentBuilderFactory;
import  javax.xml.parsers.DocumentBuilder;

import mechanics.GameMap;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import  org.w3c.dom.Document;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by uschsh on 26.10.15.
 */
public class MapReader {
    public static JSONObject ReadMap(String fileName) {
        Scanner file = null;
        try {
            file = new Scanner(new File(fileName));
        } catch(Exception e){
            System.out.print(e.toString());
        }
        String data = "";
        while(file.hasNext())
        {
            data+=file.nextLine();
        }

        JSONObject gameMap = null;
        JSONParser jsonPaser = new JSONParser();
        try {
            Object obj = jsonPaser.parse(data);
            gameMap = (JSONObject)obj;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return gameMap;
    }
}

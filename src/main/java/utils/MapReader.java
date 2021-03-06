package utils;

import org.jetbrains.annotations.Nullable;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by uschsh on 26.10.15.
 */
public class MapReader {
    @Nullable
    public static JSONObject readMap(String fileName) {
        StringBuilder data = new StringBuilder();
        try (Scanner file = new Scanner(new File(fileName))) {
            while (file.hasNext()) {
                data.append(file.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            System.out.print(e.toString());
        }

        JSONObject gameMap = null;
        JSONParser jsonPaser = new JSONParser();
        try {
            Object obj = jsonPaser.parse(data.toString());
            gameMap = (JSONObject)obj;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return gameMap;
    }
}

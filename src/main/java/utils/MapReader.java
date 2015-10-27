package utils;

import  javax.xml.parsers.DocumentBuilderFactory;
import  javax.xml.parsers.DocumentBuilder;

import mechanics.GameMap;
import  org.w3c.dom.Document;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by uschsh on 26.10.15.
 */
public class MapReader {
    private String fileName;

    public MapReader(String fileName) {
        this.fileName = fileName;
    }

    public GameMap ReadMap() {
        GameMap gameMap = new GameMap();

        File fXmlFile = new File(fileName);

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
        } catch (Exception e) {

        }

        return gameMap;
    }
}

package utils;

import java.util.Random;

/**
 * Created by Андрей on 01.11.2015.
 */
public class Helper {
    public static int randomInt(int min, int max){
        return new Random(System.currentTimeMillis()).nextInt(max - min + 1) + min;
    }
}

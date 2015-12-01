package utils;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;

/**
 * Created by Андрей on 01.12.2015.
 */
public class ReflectionHelper {
    public static void setFieldValue(Object object, String fieldName, Object value) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(object, value);
            field.setAccessible(false);
        } catch (SecurityException | NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    public static Object getFieldValue(Object object, String fieldName){
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            Object result = field.get(object);
            field.setAccessible(false);
            return result;
        } catch (SecurityException | NoSuchFieldException | IllegalAccessError | IllegalAccessException e){
            e.printStackTrace();
            return null;
        }
    }
}

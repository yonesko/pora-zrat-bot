package util;

import java.lang.reflect.Field;


/**
 * @author Gleb Danichev
 */
public class ReflectionUtils {

    public static Field getDeclaredField(Class clazz, String name) {
        try {
            return clazz.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}

package util;

import java.util.List;
import java.util.Random;

/**
 * @author Gleb Danichev
 */
public class RandomUtils {

    private final static Random R = new Random();

    public static <T> T element(List<T> list) {
        return list.get(R.nextInt(list.size()));
    }
}

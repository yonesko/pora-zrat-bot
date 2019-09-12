import java.util.List;
import java.util.Random;

/**
 * @author Gleb Danichev
 */
class RandomUtils {

    private final static Random R = new Random();

    static <T> T element(List<T> list) {
        return list.get(R.nextInt(list.size()));
    }
}

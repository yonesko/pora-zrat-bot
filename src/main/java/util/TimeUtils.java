package util;

import java.time.DayOfWeek;
import java.time.LocalDate;

/**
 * @author Gleb Danichev
 */
public class TimeUtils {

    public static boolean isWeekend() {
        DayOfWeek today = LocalDate.now().getDayOfWeek();
        return today == DayOfWeek.SATURDAY || today == DayOfWeek.SUNDAY;
    }
}

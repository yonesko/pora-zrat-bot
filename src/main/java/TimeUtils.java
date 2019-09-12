import java.time.DayOfWeek;
import java.time.LocalDate;

/**
 * @author Gleb Danichev
 */
class TimeUtils {

    static boolean isWeekend() {
        DayOfWeek today = LocalDate.now().getDayOfWeek();
        return today == DayOfWeek.SATURDAY || today == DayOfWeek.SUNDAY;
    }
}

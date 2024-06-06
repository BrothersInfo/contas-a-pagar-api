package desafio.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Utils {

    public static LocalDate getLocalDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date != null ? LocalDate.parse(date, formatter) : null;
    }

}

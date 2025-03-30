package qa.guru.niffler.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateFormatter {
    public static String formatDate(Date date) {
        SimpleDateFormat outputFormatter = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
        return outputFormatter.format(date);
    }
}

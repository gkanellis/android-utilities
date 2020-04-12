package gr.gkanellis.utilities;

import android.text.TextUtils;
import android.widget.TextView;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public final class StringUtils {

    private StringUtils() {
    }

    public static boolean isEmpty(TextView view) {
        if (view == null) {
            return true;
        }
        return isEmpty(view.getText());
    }

    public static boolean isEmpty(CharSequence c) {
        String trimmedValue = getTrimmedValue(c);
        return TextUtils.isEmpty(trimmedValue);
    }

    @Nullable
    public static String getTrimmedValue(TextView view) {
        if (view == null) {
            return null;
        }
        return getTrimmedValue(view.getText());
    }

    @Nullable
    public static String getTrimmedValue(CharSequence c) {
        if (c == null) {
            return null;
        }
        return c.toString().trim();
    }

    @NonNull
    public static List<String> split(String value, String regex) {
        try {
            String[] split = value.split(regex);
            return Arrays.asList(split);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    @Nullable
    public static String toCSV(@Nullable List<String> list) {
        if (list == null) {
            return null;
        }
        StringBuilder csv = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            csv.append(list.get(i));
            if (i != list.size() - 1) {
                csv.append(",");
            }
        }
        return csv.toString();
    }

    @Nullable
    public static String[] fromCSV(@Nullable String input) {
        if (input == null) {
            return null;
        }
        input = input.trim().replaceAll(" ", "");
        return input.split(",");
    }

    public static String formatDecimal(double value, @IntRange(from = 0) int points) {
        return String.format(Locale.ENGLISH, "%." + points + "f", value);
    }

}

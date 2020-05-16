package gr.gkanellis.utilities;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

public final class Activities {

    private Activities() {
    }

    public static <T> T requireArgument(Activity activity, String arg) {
        return requireArgument(activity, arg, "Required argument " + arg + " could not be found.");
    }

    @SuppressWarnings("unchecked")
    public static <T> T requireArgument(Activity activity, String arg, String message) {
        Bundle extras = activity.getIntent().getExtras();
        if (extras != null && extras.containsKey(arg)) {
            return (T) extras.get(arg);
        }
        throw new NullPointerException(message);
    }

    @Nullable
    public static <T> T optionalArgument(Activity activity, String arg) {
        return optionalArgument(activity, arg, null);
    }

    @SuppressWarnings("unchecked")
    public static <T> T optionalArgument(Activity activity, String arg, T defValue) {
        Bundle extras = activity.getIntent().getExtras();
        if (extras != null && extras.containsKey(arg)) {
            return (T) extras.get(arg);
        }
        return defValue;
    }

}

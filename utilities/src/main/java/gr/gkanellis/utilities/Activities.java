package gr.gkanellis.utilities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public final class Activities {

    private Activities() {
    }

    public static void transitionToActivity(Activity fromActivity, Intent intent, int enterAnim,
                                            int exitAnimation, long delay) {
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            fromActivity.startActivity(intent);
            fromActivity.overridePendingTransition(enterAnim, exitAnimation);
        }, delay);
    }

    @SuppressWarnings("unchecked")
    public static <T> T requireArgument(Activity activity, String arg) {
        Bundle extras = activity.getIntent().getExtras();
        if (extras != null && extras.containsKey(arg)) {
            return (T) extras.get(arg);
        }
        throw new NullPointerException("Required argument " + arg + " could not be found.");
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

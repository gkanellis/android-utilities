package gr.gkanellis.utilities;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

public final class Fragments {

    private Fragments() {
    }

    @SuppressWarnings("unchecked")
    public static <T> T requireArgument(Fragment fragment, String arg) {
        if (fragment.getArguments() != null && fragment.getArguments().containsKey(arg)) {
            return (T) fragment.getArguments().get(arg);
        }
        throw new NullPointerException("Required argument " + arg + " could not be found.");
    }

    @SuppressWarnings("unchecked")
    public static <T> T optionalArgument(Fragment fragment, String arg, T defValue) {
        if (fragment.getArguments() != null && fragment.getArguments().containsKey(arg)) {
            return (T) fragment.getArguments().get(arg);
        }
        return defValue;
    }

    public static void putArgument(Fragment fragment, String key, int value) {
        Bundle arguments = fragment.getArguments();
        if (arguments == null) {
            arguments = new Bundle();
        }
        arguments.putInt(key, value);
        fragment.setArguments(arguments);
    }

    public static void putArgument(Fragment fragment, String key, boolean value) {
        Bundle arguments = fragment.getArguments();
        if (arguments == null) {
            arguments = new Bundle();
        }
        arguments.putBoolean(key, value);
        fragment.setArguments(arguments);
    }

}

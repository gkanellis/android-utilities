package gr.gkanellis.utilities;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.util.DisplayMetrics;

import androidx.annotation.AnyRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import java8.util.stream.IntStreams;

public final class GenericUtils {

	private GenericUtils() {
	}

	public static int dpToPx(@NonNull Context context, double dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}

	public static Uri getUriFromResource(@NonNull Context context, @AnyRes int resId) {
		Resources res = context.getResources();
		return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
				"://" + res.getResourcePackageName(resId)
				+ '/' + res.getResourceTypeName(resId)
				+ '/' + res.getResourceEntryName(resId));
	}

	public static int getNavigationBarHeight(@NonNull Context context) {
		Resources resources = context.getResources();
		int resourceId = resources.getIdentifier("navigation_bar_height",
				"dimen", "android");
		if (resourceId > 0) {
			return resources.getDimensionPixelSize(resourceId);
		}
		return 0;
	}

	public static int getStatusBarHeight(@NonNull Context context) {
		Resources resources = context.getResources();
		int resourceId = resources.getIdentifier("status_bar_height",
				"dimen", "android");
		if (resourceId > 0) {
			return resources.getDimensionPixelSize(resourceId);
		}
		return 0;
	}

	public static DisplayMetrics getScreenMetrics(@NonNull Context context) {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		((Activity) context).getWindowManager()
				.getDefaultDisplay()
				.getMetrics(displayMetrics);
		return displayMetrics;
	}

	public static int getScreenWidth(@NonNull Context context) {
		return getScreenMetrics(context).widthPixels;
	}

	public static int getScreenHeight(@NonNull Context context) {
		return getScreenMetrics(context).heightPixels;
	}

	public static boolean permissionGranted(@NonNull String requiredPermission,
											@NonNull String[] permissions,
											@NonNull int[] grantResults) {
		if (permissions.length != grantResults.length) {
			return false;
		}
		for (int i = 0; i < permissions.length; i++) {
			if (permissions[i].equals(requiredPermission)) {
				return grantResults[i] == PackageManager.PERMISSION_GRANTED;
			}
		}
		return false;
	}

	public static boolean permissionsGranted(@NonNull int[] grantResults) {
		return IntStreams.of(grantResults)
				.allMatch(result -> result == PackageManager.PERMISSION_GRANTED);
	}

	public static boolean hasPermissions(@NonNull Activity activity, @NonNull String... perms) {
		for (String perm : perms) {
			if (ContextCompat.checkSelfPermission(activity, perm) != PackageManager.PERMISSION_GRANTED) {
				return false;
			}
		}
		return true;
	}

}

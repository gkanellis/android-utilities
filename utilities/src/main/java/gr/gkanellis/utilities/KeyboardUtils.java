package gr.gkanellis.utilities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;

import gr.gkanellis.utilities.helper.AbstarctActivityLifecycleCallbacks;

public final class KeyboardUtils {

	public interface VisibilityEventListener {
		void onVisibilityChanged(boolean isOpen);
	}

	private static final double KEYBOARD_MIN_HEIGHT_RATIO = 0.15;

	private KeyboardUtils() {
	}

	public static void hideKeyboard(@NonNull Activity activity) {
		View focusedView = activity.getCurrentFocus();
		if (focusedView != null) {
			InputMethodManager inputMethodManager =
					(InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
			assert inputMethodManager != null;
			inputMethodManager.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
		}
	}

	public static void showKeyboard(@NonNull View view) {
		InputMethodManager inputMethodManager = (InputMethodManager)
				view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		view.requestFocus();
		assert inputMethodManager != null;
		inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_FORCED);
	}

	public static boolean isKeyboardVisible(Activity activity ) {
		Rect rect = new Rect();

		View activityRoot = getActivityRoot(activity);
		activityRoot.getWindowVisibleDisplayFrame(rect);

		int screenHeight = activityRoot.getRootView().getHeight();
		int heightDiff = screenHeight - rect.height();

		return heightDiff > screenHeight * KEYBOARD_MIN_HEIGHT_RATIO;
	}

	public static void registerEventListener(@NonNull Activity activity,
											 @NonNull VisibilityEventListener listener) {

		View activityRoot = getActivityRoot(activity);
		ViewTreeObserver.OnGlobalLayoutListener layoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
			Rect rect = new Rect();
			boolean wasOpened = false;
			@Override
			public void onGlobalLayout() {
				activityRoot.getWindowVisibleDisplayFrame(rect);
				int screenHeight = activityRoot.getRootView().getHeight();
				int heightDiff = screenHeight - rect.height();
				boolean isOpen = heightDiff > screenHeight * KEYBOARD_MIN_HEIGHT_RATIO;
				if (isOpen == wasOpened) {
					return;
				}
				wasOpened = isOpen;
				listener.onVisibilityChanged(isOpen);
			}
		};
		activityRoot.getViewTreeObserver().addOnGlobalLayoutListener(layoutListener);
		activity.getApplication().registerActivityLifecycleCallbacks(new AbstarctActivityLifecycleCallbacks() {
			@Override
			public void onActivityDestroyed(@NonNull Activity activity) {
				activityRoot.getViewTreeObserver().removeOnGlobalLayoutListener(layoutListener);
			}
		});
	}

	private static View getActivityRoot(@NonNull Activity activity) {
		return ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
	}

}

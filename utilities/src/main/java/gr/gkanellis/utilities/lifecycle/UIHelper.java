package gr.gkanellis.utilities.lifecycle;

import android.graphics.drawable.Drawable;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;

public interface UIHelper {

	@ColorInt
	int getColorCompat(@ColorRes int color);

	Drawable getDrawableCompat(@DrawableRes int drawable);

	int getDimenCompat(@DimenRes int dimen);

	void setStatusBarColor(@ColorInt int color);

	void setStatusBarColorRes(@ColorRes int colorRes);

	void setNavigationBarColor(@ColorInt int color);

	void setNavigationBarColorRes(@ColorRes int colorRes);

}

package gr.gkanellis.utilities.lifecycle;

import android.graphics.drawable.Drawable;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.Px;
import androidx.lifecycle.LifecycleOwner;

public interface ContextHelper<T extends LifecycleOwner> {

    @Px
    int dpToPx(double dp);

	@ColorInt
	int getColorCompat(@ColorRes int color);

	Drawable getDrawableCompat(@DrawableRes int drawable);

    @Px
    int getDimenCompat(@DimenRes int dimen);

	void setStatusBarColor(@ColorInt int color);

	void setStatusBarColorRes(@ColorRes int colorRes);

	void setNavigationBarColor(@ColorInt int color);

	void setNavigationBarColorRes(@ColorRes int colorRes);

    <R> R requireArgument(T t, String arg);

    <R> R optionalArgument(T t, String arg, R defValue);

}

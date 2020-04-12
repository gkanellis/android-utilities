package gr.gkanellis.utilities.lifecycle;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.annotation.CallSuper;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.franmontiel.localechanger.LocaleChanger;

import gr.gkanellis.utilities.Activities;
import gr.gkanellis.utilities.GenericUtils;

public abstract class BaseActivity extends AppCompatActivity implements ContextHelper<FragmentActivity> {

	@Override
	@CallSuper
	protected final void attachBaseContext(Context newBase) {
		newBase = LocaleChanger.configureBaseContext(newBase);
		super.attachBaseContext(newBase);
	}

	@Override
	public int getColorCompat(int color) {
		return ContextCompat.getColor(this, color);
	}

	@Override
	public Drawable getDrawableCompat(int drawable) {
		return ContextCompat.getDrawable(this, drawable);
	}

	@Override
	public int getDimenCompat(int dimen) {
		return getResources().getDimensionPixelSize(dimen);
	}

	@Override
	public void setStatusBarColor(int color) {
		getWindow().setStatusBarColor(color);
	}

	@Override
	public void setStatusBarColorRes(int colorRes) {
		getWindow().setStatusBarColor(getColorCompat(colorRes));
	}

	@Override
	public void setNavigationBarColor(int color) {
		getWindow().setNavigationBarColor(color);
	}

	@Override
	public void setNavigationBarColorRes(int colorRes) {
		getWindow().setNavigationBarColor(getColorCompat(colorRes));
	}

    @Override
    public <R> R requireArgument(FragmentActivity fragmentActivity, String arg) {
        return Activities.requireArgument(fragmentActivity, arg);
    }

    @Override
    public <R> R optionalArgument(FragmentActivity fragmentActivity, String arg, R defValue) {
        return Activities.optionalArgument(fragmentActivity, arg, defValue);
    }

    @Override
    public int dpToPx(double dp) {
        return GenericUtils.dpToPx(this, dp);
    }
}

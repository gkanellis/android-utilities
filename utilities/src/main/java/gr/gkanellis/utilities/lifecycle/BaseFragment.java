package gr.gkanellis.utilities.lifecycle;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment implements UIHelper {

	protected Context mContext;

	@Override
	public void onAttach(@NonNull Context context) {
		super.onAttach(context);
		this.mContext = context;
	}

	@Override
	public int getColorCompat(int color) {
		return ContextCompat.getColor(mContext, color);
	}

	@Override
	public Drawable getDrawableCompat(int drawable) {
		return ContextCompat.getDrawable(mContext, drawable);
	}

	@Override
	public int getDimenCompat(int dimen) {
		return mContext.getResources().getDimensionPixelSize(dimen);
	}

	@Override
	public void setStatusBarColor(int color) {
		((Activity) mContext).getWindow().setStatusBarColor(color);
	}

	@Override
	public void setStatusBarColorRes(int colorRes) {
		((Activity) mContext).getWindow().setStatusBarColor(getColorCompat(colorRes));
	}

	@Override
	public void setNavigationBarColor(int color) {
		((Activity) mContext).getWindow().setNavigationBarColor(color);
	}

	@Override
	public void setNavigationBarColorRes(int colorRes) {
		((Activity) mContext).getWindow().setNavigationBarColor(getColorCompat(colorRes));
	}
}

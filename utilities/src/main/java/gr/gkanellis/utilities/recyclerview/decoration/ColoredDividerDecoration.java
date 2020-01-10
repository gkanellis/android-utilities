package gr.gkanellis.utilities.recyclerview.decoration;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;

import gr.gkanellis.utilities.GenericUtils;

public final class ColoredDividerDecoration extends DividerItemDecoration {

	public static final int LIGHT_WHITE = Color.parseColor("#1Affffff");

    private Context mContext;

	private int mOrientation;
	private int mColor = LIGHT_WHITE;
	private int mSize;

    public ColoredDividerDecoration(Context context, int orientation) {
        super(context, orientation);
        this.mContext = context;
		this.mOrientation = orientation;
		this.mSize = GenericUtils.dpToPx(context, 1);
    }

    public ColoredDividerDecoration setColorRes(@ColorRes int colorRes) {
        return setColor(ContextCompat.getColor(mContext, colorRes));
    }

    public ColoredDividerDecoration setColor(@ColorInt int color) {
        mColor = color;
        createAndSetDrawable();
        return this;
    }

	public ColoredDividerDecoration setSize(int width) {
		mSize = width;
		createAndSetDrawable();
		return this;
	}

	@Override
	public void setDrawable(@NonNull Drawable drawable) {
		throw new UnsupportedOperationException();
	}

	public static ColoredDividerDecoration vertical(Context context) {
		return vertical(context, LIGHT_WHITE);
	}

	public static ColoredDividerDecoration vertical(Context context, @ColorInt int color) {
		return new ColoredDividerDecoration(context, VERTICAL)
				.setColor(color);
	}

	public static ColoredDividerDecoration horizontal(Context context) {
		return new ColoredDividerDecoration(context, HORIZONTAL)
				.setColor(LIGHT_WHITE);
	}

	public static ColoredDividerDecoration horizontal(Context context, @ColorInt int color) {
		return new ColoredDividerDecoration(context, HORIZONTAL)
				.setColor(color);
	}

	private void createAndSetDrawable() {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setColor(mColor);

		if (mOrientation == HORIZONTAL) {
			drawable.setSize(mSize, 0);
		} else {
			drawable.setSize(0, mSize);
		}

        super.setDrawable(drawable);
    }

}

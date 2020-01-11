package gr.gkanellis.utilities.recyclerview.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Px;
import androidx.recyclerview.widget.RecyclerView;

import gr.gkanellis.utilities.GenericUtils;

public class ColoredDividerDecoration extends RecyclerView.ItemDecoration {

	public static final int LIGHT_BLACK = Color.parseColor("#1A000000");

	public static final int LIGHT_WHITE = Color.parseColor("#1Affffff");
	private final Rect mBounds = new Rect();

	private int mOrientation;
	private int mSize;
	private int mColor;
	private boolean mDrawAtLastItem;

	private Drawable mDivider;

	public ColoredDividerDecoration(@NonNull Context context, @Orientation int orientation,
									boolean drawAtLastItem) {
		this(orientation, GenericUtils.dpToPx(context, 1), LIGHT_BLACK, drawAtLastItem);
	}

	public ColoredDividerDecoration(@Orientation int orientation, @Px int size,
									@ColorInt int color, boolean drawAtLastItem) {
		this.mOrientation = orientation;
		this.mSize = size;
		this.mColor = color;
		this.mDrawAtLastItem = drawAtLastItem;
		createAndSetDrawable();
	}

	public static ColoredDividerDecoration vertical(@NonNull Context context,
													boolean drawAtLastItem) {
		return vertical(context, LIGHT_BLACK, drawAtLastItem);
	}

	public static ColoredDividerDecoration vertical(@NonNull Context context, @ColorInt int color,
													boolean drawAtLastItem) {
		return new ColoredDividerDecoration(context, LinearLayout.VERTICAL, drawAtLastItem)
				.setColor(color);
	}

	public static ColoredDividerDecoration horizontal(@NonNull Context context,
													  boolean drawAtLastItem) {
		return horizontal(context, LIGHT_BLACK, drawAtLastItem);
	}

	public static ColoredDividerDecoration horizontal(@NonNull Context context, @ColorInt int color,
													  boolean drawAtLastItem) {
		return new ColoredDividerDecoration(context, LinearLayout.HORIZONTAL, drawAtLastItem)
				.setColor(color);
	}

	private void createAndSetDrawable() {
		GradientDrawable drawable = new GradientDrawable();
		drawable.setShape(GradientDrawable.RECTANGLE);
		drawable.setColor(mColor);
		if (mOrientation == LinearLayout.HORIZONTAL) {
			drawable.setSize(mSize, 0);
		} else {
			drawable.setSize(0, mSize);
		}
		mDivider = drawable;
	}

	public ColoredDividerDecoration setDrawAtLastItem(boolean drawAtLastItem) {
		this.mDrawAtLastItem = drawAtLastItem;
		return this;
	}

	public ColoredDividerDecoration setColor(int color) {
		this.mColor = color;
		createAndSetDrawable();
		return this;
	}

	public ColoredDividerDecoration setSize(int size) {
		this.mSize = size;
		createAndSetDrawable();
		return this;
	}

	@Override
	public void onDraw(@NonNull Canvas c, RecyclerView parent, @NonNull RecyclerView.State state) {
		if (parent.getLayoutManager() == null || mDivider == null) {
			return;
		}
		if (mOrientation == LinearLayout.VERTICAL) {
			drawVertical(c, parent);
		} else {
			drawHorizontal(c, parent);
		}
	}

	private void drawVertical(Canvas canvas, RecyclerView parent) {
		canvas.save();
		final int left;
		final int right;
		if (parent.getClipToPadding()) {
			left = parent.getPaddingLeft();
			right = parent.getWidth() - parent.getPaddingRight();
			canvas.clipRect(left, parent.getPaddingTop(), right,
					parent.getHeight() - parent.getPaddingBottom());
		} else {
			left = 0;
			right = parent.getWidth();
		}

		final int childCount = mDrawAtLastItem ?
				parent.getChildCount() :
				Math.max(parent.getChildCount() - 1, 0);
		for (int i = 0; i < childCount; i++) {
			final View child = parent.getChildAt(i);
			parent.getDecoratedBoundsWithMargins(child, mBounds);
			final int bottom = mBounds.bottom + Math.round(child.getTranslationY());
			final int top = bottom - mDivider.getIntrinsicHeight();
			mDivider.setBounds(left, top, right, bottom);
			mDivider.draw(canvas);
		}
		canvas.restore();
	}

	private void drawHorizontal(Canvas canvas, RecyclerView parent) {
		canvas.save();
		final int top;
		final int bottom;
		if (parent.getClipToPadding()) {
			top = parent.getPaddingTop();
			bottom = parent.getHeight() - parent.getPaddingBottom();
			canvas.clipRect(parent.getPaddingLeft(), top,
					parent.getWidth() - parent.getPaddingRight(), bottom);
		} else {
			top = 0;
			bottom = parent.getHeight();
		}

		final int childCount = mDrawAtLastItem ?
				parent.getChildCount() :
				Math.max(parent.getChildCount() - 1, 0);
		for (int i = 0; i < childCount; i++) {
			final View child = parent.getChildAt(i);
			parent.getLayoutManager().getDecoratedBoundsWithMargins(child, mBounds);
			final int right = mBounds.right + Math.round(child.getTranslationX());
			final int left = right - mDivider.getIntrinsicWidth();
			mDivider.setBounds(left, top, right, bottom);
			mDivider.draw(canvas);
		}
		canvas.restore();
	}

	@Override
	public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
							   @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
		if (mDivider == null) {
			outRect.set(0, 0, 0, 0);
			return;
		}
		if (mOrientation == LinearLayout.VERTICAL) {
			outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
		} else {
			outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
		}
	}

	@IntDef({LinearLayout.HORIZONTAL, LinearLayout.VERTICAL})
	private @interface Orientation {
	}
}
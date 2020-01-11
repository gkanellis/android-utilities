package gr.gkanellis.utilities.recyclerview.decoration;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static gr.gkanellis.utilities.GenericUtils.dpToPx;

public final class SpacesItemDecoration extends RecyclerView.ItemDecoration {

	private int mLeft;
	private int mTop;
	private int mRight;
	private int mBottom;
	private boolean mRetainTopSpace;

	public SpacesItemDecoration(int left, int top, int right, int bottom, boolean retainTopSpace) {
		this.mLeft = left;
		this.mTop = top;
		this.mRight = right;
		this.mBottom = bottom;
		this.mRetainTopSpace = retainTopSpace;
	}

	public static SpacesItemDecoration all(int left, int top, int right, int bottom) {
		return new SpacesItemDecoration(left, top, right, bottom, false);
	}

	public static SpacesItemDecoration allDp(Context context, int left, int top, int right, int bottom) {
		return new SpacesItemDecoration(
				dpToPx(context, left),
				dpToPx(context, top),
				dpToPx(context, right),
				dpToPx(context, bottom),
				false);
	}

	public static SpacesItemDecoration left(int left) {
		return all(left, 0, 0, 0);
	}

	public static SpacesItemDecoration right(int right) {
		return all(0, 0, right, 0);
	}

	public static SpacesItemDecoration top(int top) {
		return all(0, top, 0, 0);
	}

	public static SpacesItemDecoration bottom(int bottom) {
		return all(0, 0, 0, bottom);
	}

	public SpacesItemDecoration setRetainTopSpace(boolean retainTopSpace) {
		this.mRetainTopSpace = retainTopSpace;
		return this;
	}

	@Override
	public void getItemOffsets(Rect outRect, @NonNull View view, @NonNull RecyclerView parent,
							   @NonNull RecyclerView.State state) {
		outRect.left = mLeft;
		outRect.right = mRight;
		outRect.bottom = mBottom;
		if (mRetainTopSpace) {
			if (parent.getChildLayoutPosition(view) == 0) {
				outRect.top = mTop;
			} else {
				outRect.top = 0;
			}
		} else {
			outRect.top = mTop;
		}
	}
}
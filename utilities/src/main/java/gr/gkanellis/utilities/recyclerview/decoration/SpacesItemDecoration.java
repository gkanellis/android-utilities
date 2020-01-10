package gr.gkanellis.utilities.recyclerview.decoration;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import gr.gkanellis.utilities.GenericUtils;

public final class SpacesItemDecoration extends RecyclerView.ItemDecoration {

	private int left, top, right, bottom;
	private boolean mRetainTopSpace;

	public SpacesItemDecoration(@NonNull Context context, int spaceDp) {
		this(GenericUtils.dpToPx(context, spaceDp));
	}

	public SpacesItemDecoration(int space) {
		this(space, space, space, space, true);
	}

	public SpacesItemDecoration(int left, int top, int right, int bottom) {
		this(left, top, right, bottom, true);
	}

	public SpacesItemDecoration(int left, int top, int right, int bottom, boolean retainTopSpace) {
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		this.mRetainTopSpace = retainTopSpace;
	}

	@Override
	public void getItemOffsets(Rect outRect, @NonNull View view, @NonNull RecyclerView parent,
							   @NonNull RecyclerView.State state) {
		outRect.left = left;
		outRect.right = right;
		outRect.bottom = bottom;
		if (mRetainTopSpace) {
			if (parent.getChildLayoutPosition(view) == 0) {
				outRect.top = top;
			} else {
				outRect.top = 0;
			}
		} else {
			outRect.top = top;
		}
	}
}
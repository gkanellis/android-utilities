package gr.gkanellis.utilities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class Animations {

	public static final long ANIMATION_DURATION_SHORT = 300;
	public static final long ANIMATION_DURATION_LONG = 1000;

	public static void visibility(@NonNull View view, @Visibility int visibility) {
		visibility(view, visibility, ANIMATION_DURATION_SHORT);
	}

	public static final long EXPAND_COLLAPSE_ANIMATION_DURATION = 300;

	private Animations() {
	}

	public static void visibility(@NonNull View view, @Visibility int visibility,
								  long duration) {
		if (visibility == View.VISIBLE) {
			view.setVisibility(View.VISIBLE);
			view.setAlpha(0.0f);
			view.animate()
					.alpha(1.0f)
					.setDuration(duration)
					.setListener(null);
		} else {
			view.setAlpha(1.0f);
			view.animate()
					.alpha(0.0f)
					.setDuration(duration)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							view.setVisibility(visibility);
						}
					});
		}
	}

	@IntDef({View.VISIBLE, View.INVISIBLE, View.GONE})
	@Retention(RetentionPolicy.SOURCE)
	public @interface Visibility {
	}

	public static void expand(final View v) {
		v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		final int targetHeight = v.getMeasuredHeight();

		v.getLayoutParams().height = 0;
		v.setVisibility(View.VISIBLE);
		Animation animation = new Animation() {
			@Override
			protected void applyTransformation(float interpolatedTime, Transformation t) {
				v.getLayoutParams().height = interpolatedTime == 1
						? ViewGroup.LayoutParams.WRAP_CONTENT
						: (int) (targetHeight * interpolatedTime);
				v.requestLayout();
			}

			@Override
			public boolean willChangeBounds() {
				return true;
			}
		};

		animation.setDuration(EXPAND_COLLAPSE_ANIMATION_DURATION);
		v.startAnimation(animation);
	}

	public static void collapse(final View v) {
		final int initialHeight = v.getMeasuredHeight();
		Animation animation = new Animation() {
			@Override
			protected void applyTransformation(float interpolatedTime, Transformation t) {
				if (interpolatedTime == 1) {
					v.setVisibility(View.GONE);
				} else {
					v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
					v.requestLayout();
				}
			}

			@Override
			public boolean willChangeBounds() {
				return true;
			}
		};
		animation.setDuration(EXPAND_COLLAPSE_ANIMATION_DURATION);
		v.startAnimation(animation);
	}

	public static void rotateAroundCenter(@NonNull View view, float decStart, float decEnd, long duration,
										  @Nullable Animation.AnimationListener listener) {
		RotateAnimation anim = new RotateAnimation(decStart, decEnd,
				Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		anim.setInterpolator(new AccelerateDecelerateInterpolator());
		anim.setDuration(duration);
		anim.setAnimationListener(listener);
		anim.setFillAfter(true);

		view.startAnimation(anim);
	}

}

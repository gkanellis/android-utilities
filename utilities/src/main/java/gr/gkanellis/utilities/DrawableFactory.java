package gr.gkanellis.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ScaleDrawable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Px;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import java.util.List;

public final class DrawableFactory {

    private DrawableFactory() {
    }

    public static void tintMenu(@NonNull Menu menu, @ColorInt int color) {
        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            Drawable icon = menuItem.getIcon();
            if (icon != null) {
                menuItem.setIcon(tint(icon, color));
            }
        }
    }

    public static Drawable tint(@Nullable Drawable drawable, @ColorInt int color) {
        if (drawable == null) {
            return null;
        }
        Drawable wrapDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(wrapDrawable, color);
        return wrapDrawable;
    }

    public static Drawable resizeDp(@NonNull Context context, @Nullable Drawable drawable,
                                    int widthDp, int heightDp) {
        int height = GenericUtils.dpToPx(context, widthDp);
        int width = GenericUtils.dpToPx(context, heightDp);
        return resize(context, drawable, width, height);
    }

    public static Drawable resize(@NonNull Context context, @Nullable Drawable image,
                                  int width, int height) {
        if (!(image instanceof BitmapDrawable)) {
            return image;
        }
        Bitmap bitmap = ((BitmapDrawable) image).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(bitmap, width, height, false);
        return new BitmapDrawable(context.getResources(), bitmapResized);
    }

    @Nullable
    public static Drawable scaleDp(@NonNull Context context, @Nullable Drawable drawable,
                                   int widthDp, int heightDp) {
        int height = GenericUtils.dpToPx(context, widthDp);
        int width = GenericUtils.dpToPx(context, heightDp);
        return scale(drawable, width, height);
    }

    @Nullable
    public static Drawable scale(@Nullable Drawable drawable, @Px int width, @Px int height) {
        if (drawable == null) {
            return null;
        }
        drawable.setBounds(0, 0, (int) (drawable.getIntrinsicWidth() * 0.5),
                (int) (drawable.getIntrinsicHeight() * 0.5));
        return new ScaleDrawable(drawable, 0, width, height);
    }

    @NonNull
    public static Bitmap mergeMultiple(@NonNull List<Bitmap> parts) {
        Bitmap result = Bitmap.createBitmap(
                parts.get(0).getWidth() * 2,
                parts.get(0).getHeight() * 2,
                Bitmap.Config.ARGB_8888
        );
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        for (int i = 0; i < Math.min(parts.size(), 4); i++) {
            Bitmap bitmap = parts.get(i);
            canvas.drawBitmap(bitmap,
                    bitmap.getWidth() * (i % 2),
                    bitmap.getHeight() * (i >> 2), paint);
        }
        return result;
    }

    @NonNull
    public static Builder produce(@NonNull Context context) {
        return new Builder(context);
    }

    public static class Builder {

        private Context mContext;
        private GradientDrawable mDrawable;

        public Builder(@NonNull Context context) {
            this.mContext = context;
            this.mDrawable = new GradientDrawable();
        }

        public Builder withColorGradient(int[] colors) {
            return withColorGradient(colors, GradientDrawable.LINEAR_GRADIENT,
                    GradientDrawable.Orientation.TOP_BOTTOM);
        }

        public Builder withColorGradient(int[] colors, int gradientType,
                                         @NonNull GradientDrawable.Orientation orientation) {
            mDrawable.setColors(colors);
            mDrawable.setGradientType(gradientType);
            mDrawable.setOrientation(orientation);
            return this;
        }

        @NonNull
        public Builder withColorRes(@ColorRes int colorRes) {
            mDrawable.setColor(ContextCompat.getColor(mContext, colorRes));
            return this;
        }

        @NonNull
        public Builder withColor(@ColorInt int argb) {
            mDrawable.setColor(argb);
            return this;
        }

        @NonNull
        public Builder withShape(int shape) {
            mDrawable.setShape(shape);
            return this;
        }

        @NonNull
        public Builder withSize(int width, int height) {
            mDrawable.setSize(width, height);
            return this;
        }

        @NonNull
        public Builder withStroke(int width, @ColorInt int color) {
            return withStroke(width, color, 0, 0);
        }

        @NonNull
        public Builder withStroke(int width, @ColorInt int color, float dashWidth, float dashGap) {
            mDrawable.setStroke(width, color, dashWidth, dashGap);
            return this;
        }

        @NonNull
        public Builder withStrokeRes(int width, @ColorRes int colorRes) {
            return withStroke(width, ContextCompat.getColor(mContext, colorRes), 0, 0);
        }

        @NonNull
        public Builder withRadius(float radius) {
            mDrawable.setCornerRadius(radius);
            return this;
        }

        public void into(View view) {
            if (view instanceof ImageView) {
                ((ImageView) view).setImageDrawable(mDrawable);
            } else {
                view.setBackground(mDrawable);
            }
        }

        @NonNull
        public Drawable build() {
            return mDrawable;
        }

    }

}

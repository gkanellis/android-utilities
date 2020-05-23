package gr.gkanellis.utilities.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatSeekBar;

public class RangeAwareSeekBar extends AppCompatSeekBar {

    public interface OnRangeValueChangeListener {
        void onRangeValueChanged(int newValue, int min, int max);
    }

    private int mMinValue;
    private int mMaxValue;
    private int mStepValue;
    private OnRangeValueChangeListener mValueChangeListener;

    public RangeAwareSeekBar(Context context) {
        super(context);
    }

    public RangeAwareSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RangeAwareSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setValueChangeListener(OnRangeValueChangeListener listener) {
        this.mValueChangeListener = listener;
    }

    public void setRange(int min, int max, int step) {
        mMinValue = min;
        mMaxValue = max;
        mStepValue = step;
        setMax((mMaxValue - mMinValue) / mStepValue);
        setValueOnRange(mMinValue);
    }

    public int getValueOnRange() {
        return mMinValue + (getProgress() * mStepValue);
    }

    public void setValueOnRange(int value) {
        if (value < mMinValue || value > mMaxValue) {
            throw new IllegalArgumentException("Value out of range: " + value
                    + "(min:" + mMinValue + ", max: " + mMaxValue + ")");
        }
        int newValue = (value - mMinValue) / mStepValue;
        setProgress(newValue);
        if (mValueChangeListener != null) {
            mValueChangeListener.onRangeValueChanged(newValue, mMinValue, mMaxValue);
        }
    }
}

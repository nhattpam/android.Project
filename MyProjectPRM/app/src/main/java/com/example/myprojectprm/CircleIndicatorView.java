package com.example.myprojectprm;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class CircleIndicatorView extends LinearLayout {
    private static final int INDICATOR_RADIUS_DP = 8;
    private static final int INDICATOR_SPACING_DP = 8;
    private static final int INDICATOR_COLOR_SELECTED = Color.RED;
    private static final int INDICATOR_COLOR_UNSELECTED = Color.GRAY;

    private int indicatorSize;
    private int indicatorSpacing;
    private int selectedIndicatorColor;
    private int unselectedIndicatorColor;

    private int itemCount;
    private int selectedItemPosition;

    private Paint paint;

    public CircleIndicatorView(Context context) {
        super(context);
        init();
    }

    public CircleIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleIndicatorView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setOrientation(HORIZONTAL);

        indicatorSize = dpToPx(INDICATOR_RADIUS_DP);
        indicatorSpacing = dpToPx(INDICATOR_SPACING_DP);
        selectedIndicatorColor = INDICATOR_COLOR_SELECTED;
        unselectedIndicatorColor = INDICATOR_COLOR_UNSELECTED;

        paint = new Paint();
        paint.setAntiAlias(true);
    }

    public void setIndicators(int itemCount) {
        this.itemCount = itemCount;
        this.selectedItemPosition = 0;
        createIndicators();
    }

    private void createIndicators() {
        removeAllViews();

        for (int i = 0; i < itemCount; i++) {
            View indicator = new View(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(indicatorSize, indicatorSize);
            params.setMargins(indicatorSpacing, 0, indicatorSpacing, 0);
            indicator.setLayoutParams(params);

            if (i == selectedItemPosition) {
                indicator.setBackgroundColor(selectedIndicatorColor);
            } else {
                indicator.setBackgroundColor(unselectedIndicatorColor);
            }

            addView(indicator);
        }
    }

    public void selectIndicator(int position) {
        selectedItemPosition = position;
        updateIndicators();
    }

    private void updateIndicators() {
        for (int i = 0; i < getChildCount(); i++) {
            View indicator = getChildAt(i);

            if (i == selectedItemPosition) {
                indicator.setBackgroundColor(selectedIndicatorColor);
            } else {
                indicator.setBackgroundColor(unselectedIndicatorColor);
            }
        }
    }

    private int dpToPx(int dp) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}

package com.example.myprojectprm;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class CircleIndicatorView extends LinearLayout {
    private static final int INDICATOR_RADIUS_DP = 8;
    private static final int INDICATOR_SPACING_DP = 8;
    private static final int INDICATOR_COLOR_SELECTED = 0xFFFF9900;
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
            Drawable indicator = createIndicatorDrawable(i == selectedItemPosition);

            View indicatorView = new View(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(indicatorSize, indicatorSize);
            params.setMargins(indicatorSpacing, 0, indicatorSpacing, 0);
            indicatorView.setLayoutParams(params);
            indicatorView.setBackground(indicator);

            addView(indicatorView);
        }
    }

    private Drawable createIndicatorDrawable(boolean isSelected) {
        Drawable drawable = new Drawable() {
            @Override
            public void draw(Canvas canvas) {
                int radius = indicatorSize / 2;
                int centerX = canvas.getWidth() / 2;
                int centerY = canvas.getHeight() / 2;

                paint.setColor(isSelected ? selectedIndicatorColor : unselectedIndicatorColor);
                canvas.drawCircle(centerX, centerY, radius, paint);
            }

            @Override
            public void setAlpha(int alpha) {
                // Not used
            }

            @Override
            public void setColorFilter(ColorFilter colorFilter) {
                // Not used
            }

            @Override
            public int getOpacity() {
                return PixelFormat.TRANSLUCENT;
            }
        };

        return drawable;
    }

    public void selectIndicator(int position) {
        selectedItemPosition = position;
        updateIndicators();
    }

    private void updateIndicators() {
        for (int i = 0; i < getChildCount(); i++) {
            Drawable indicator = createIndicatorDrawable(i == selectedItemPosition);
            View indicatorView = getChildAt(i);
            indicatorView.setBackground(indicator);
        }
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}

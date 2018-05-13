package com.major.extra.decoration;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * TODO
 * Created by MEI on 2017/9/28.
 */

public class LineItemDecoration extends RecyclerView.ItemDecoration {

    private Context mContext;
    private final Paint mPaint;
    private final Resources mRes;

    public LineItemDecoration(Context context, int color) {
        mContext = context;
        mRes = mContext.getResources();
        mPaint = new Paint();
        mPaint.setColor(color);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        int count = parent.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = parent.getChildAt(i);
            c.drawLine(parent.getPaddingLeft(), view.getBottom(),
                    parent.getMeasuredWidth() - parent.getPaddingRight(), view.getBottom(), mPaint);
        }
        c.drawLine(parent.getPaddingLeft(), 0, parent.getMeasuredWidth() - parent.getPaddingRight(), 0, mPaint);

        c.drawLine(parent.getPaddingLeft(), parent.getMeasuredHeight() - 1,
                parent.getMeasuredWidth() - parent.getPaddingRight(), parent.getMeasuredHeight() - 1, mPaint);
    }
}

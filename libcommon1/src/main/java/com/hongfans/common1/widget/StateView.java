package com.hongfans.common1.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hongfans.common1.BaseApp;
import com.hongfans.common1.R;
import com.hongfans.common1.util.DensityUtil;

/**
 * TODO
 * Created by MEI on 2017/9/28.
 */

public class StateView extends RelativeLayout {

    public static final int IMAGE_BORDER = 140;
    public static final String COLOR_STRING = "#3fc9fc";
    public static final int TEXT_SIZE = 14;

    private LinearLayout mLoadingLl;
    private RelativeLayout mErrorLl;
    private LinearLayout mEmptyRl;
    private View mContentV;
    private TextView mErrorTv;

    public StateView(Context context) {
        this(context, null);
    }

    public StateView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLoadingView(context);
        initErrorView(context);
        initEmptyView(context);
    }

    private void initLoadingView(Context ctx) {
        mLoadingLl = new LinearLayout(ctx);
        mLoadingLl.setOrientation(LinearLayout.VERTICAL);


        ImageView iv = new ImageView(ctx);
        iv.setBackgroundResource(R.drawable.ic_loading_logo);
        int border = DensityUtil.dp2px(BaseApp.getInstance(), IMAGE_BORDER);
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(border, border);
        params1.gravity = Gravity.CENTER_HORIZONTAL;
        iv.setLayoutParams(params1);
        mLoadingLl.addView(iv);

        LinearLayout ll = new LinearLayout(ctx);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setGravity(CENTER_VERTICAL);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        ll.setLayoutParams(layoutParams);

        ProgressBar pb = new ProgressBar(ctx);
        pb.setIndeterminateDrawable(getResources().getDrawable(R.drawable.loading_pb));
        int width = DensityUtil.dp2px(BaseApp.getInstance(), 30);
        LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(width, width);
        params3.gravity = Gravity.CENTER_VERTICAL;
        params3.rightMargin = DensityUtil.dp2px(BaseApp.getInstance(), 8);
        pb.setLayoutParams(params3);
        ll.addView(pb);

        TextView tv = new TextView(ctx);
        tv.setTextColor(Color.parseColor(COLOR_STRING));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, TEXT_SIZE);
        tv.setText("正在加载数据...");
        tv.setLines(1);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params2.gravity = Gravity.CENTER_VERTICAL | Gravity.LEFT;
        tv.setLayoutParams(params2);
        ll.addView(tv);

        LayoutParams params4 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params4.addRule(RelativeLayout.CENTER_IN_PARENT);
        ll.setLayoutParams(params4);

        mLoadingLl.addView(ll);

        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        mLoadingLl.setLayoutParams(params);

        addView(mLoadingLl);
    }

    private void initErrorView(Context ctx) {
        mErrorLl = new RelativeLayout(ctx);
        LayoutParams params2 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        params2.addRule(RelativeLayout.CENTER_IN_PARENT);
        mErrorLl.setLayoutParams(params2);

        LinearLayout ll = new LinearLayout(ctx);
        LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        ll.setLayoutParams(params3);
        ll.setOrientation(LinearLayout.VERTICAL);

        ImageView iv = new ImageView(ctx);
        iv.setBackgroundResource(R.drawable.ic_error);
        int border = DensityUtil.dp2px(BaseApp.getInstance(), IMAGE_BORDER);
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(border, border);
        params1.gravity = Gravity.CENTER_HORIZONTAL;
        iv.setLayoutParams(params1);
        ll.addView(iv);

        mErrorTv = new TextView(ctx);
        LinearLayout.LayoutParams params5 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params5.gravity = Gravity.CENTER_HORIZONTAL;
        mErrorTv.setLayoutParams(params5);
        mErrorTv.setLines(1);
        mErrorTv.setText("网络异常，请检查网络后点击重试");
        mErrorTv.setTextColor(Color.parseColor(COLOR_STRING));
        mErrorTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, TEXT_SIZE);
        mErrorTv.setGravity(Gravity.CENTER);
        ll.addView(mErrorTv);

        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        ll.setLayoutParams(params);

        mErrorLl.addView(ll);

        addView(mErrorLl);
    }

    private void initEmptyView(Context ctx) {
        mEmptyRl = new LinearLayout(ctx);
        mEmptyRl.setOrientation(LinearLayout.VERTICAL);

        ImageView iv = new ImageView(ctx);
        iv.setBackgroundResource(R.drawable.ic_empty);
        int border = DensityUtil.dp2px(BaseApp.getInstance(), IMAGE_BORDER);
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(border, border);
        iv.setLayoutParams(params1);
        mEmptyRl.addView(iv);

        TextView tv = new TextView(ctx);
        tv.setText("没有更多的数据了");
        tv.setTextColor(Color.parseColor(COLOR_STRING));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, TEXT_SIZE);
        tv.setGravity(Gravity.CENTER);
        mEmptyRl.addView(tv);

        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        mEmptyRl.setLayoutParams(params);

        addView(mEmptyRl);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() == 4) {
            mContentV = getChildAt(3);
        } else {
            throw new RuntimeException("必须为　StateView 添加一个子布局");
        }

        reset();
    }

    public void showLoading() {
        reset();
        mLoadingLl.setVisibility(VISIBLE);
    }

    public void showError(String msg) {
        reset();
        mErrorLl.setVisibility(VISIBLE);
        mErrorTv.setText(msg);
    }

    public void showEmpty() {
        reset();
        mEmptyRl.setVisibility(VISIBLE);
    }

    public void showContent() {
        reset();
        mContentV.setVisibility(VISIBLE);
    }

    public void setReload(OnClickListener listener) {
        mErrorLl.setOnClickListener(listener);
    }

    private void reset() {
        mContentV.setVisibility(GONE);
        mLoadingLl.setVisibility(GONE);
        mErrorLl.setVisibility(GONE);
        mEmptyRl.setVisibility(GONE);
    }
}

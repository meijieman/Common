package com.hongfans.common1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 * Created by MEI on 2017/4/26.
 */
public abstract class CommonAdapter<T, H extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<H> {

    protected List<T> mData = new ArrayList<>();
    protected Context mContext; // 需要在 onCreateViewHolder

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setDatas(List<T> data) {
        if (data != null) {
            mData.clear();
            mData.addAll(data);
            notifyDataSetChanged();
        }
    }

    public void addDatas(int pos, List<T> data) {
        if (data != null) {
            mData.addAll(pos, data);
            notifyDataSetChanged();
        }
    }

    public void addDatas(List<T> data) {
        if (data != null) {
            mData.addAll(data);
            notifyItemRangeInserted(mData.size() - 1, data.size());
        }
    }

    public void clear() {
        mData.clear();
        notifyDataSetChanged();
    }

    public int getSize() {
        return mData.size();
    }

    public List<T> getDatas() {
        return mData;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
            // FIXME: 2018/1/16
//            ButterKnife.bind(this, itemView);
        }
    }

    protected OnItemClickListener<T> mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public interface OnItemClickListener<T> {
        void onItemClick(T t, int position);
    }
}

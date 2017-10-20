package com.khoantt91.hiqanimationtest.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ThienKhoa on 5/8/17.
 */

public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<BaseRecyclerAdapter.BaseViewHolder> {

    //region Properties
    private List<T> objectList = Collections.emptyList();
    private Activity activity;
    private OnAdapterListener listener;
    //endregion

    //region Constructor
    public BaseRecyclerAdapter(Activity activity, OnAdapterListener listener) {
        super();
        this.activity = activity;
        this.listener = listener;
    }
    //endregion

    //region Getter - Setter
    public void setOnAdapterListener(OnAdapterListener listener) {
        this.listener = listener;
    }

    public OnAdapterListener getListener() {
        return listener;
    }
    //endregion

    //region Abstract methods
    protected abstract View createView(Context context, ViewGroup viewGroup, int viewType);

    protected abstract void bindView(T item, int position, BaseViewHolder baseViewHolder);

    public BaseRecyclerAdapter(Activity activity) {
        this(activity, null);
    }
    //endregion

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new BaseViewHolder(createView(activity, viewGroup, viewType));
    }

    @Override
    public void onBindViewHolder(BaseViewHolder baseViewHolder, int position) {
        bindView(getItem(position), position, baseViewHolder);
    }

    @Override
    public int getItemCount() {
        return objectList.size();
    }

    public T getItem(int index) {
        return ((objectList != null && index < objectList.size()) ? objectList.get(index) : null);
    }

    public Context getActivity() {
        return activity;
    }

    public void setList(List<T> list) {
        objectList = list;
    }

    public List<T> getList() {
        return objectList;
    }

    //region BaseViewHolder
    public static class BaseViewHolder extends RecyclerView.ViewHolder {
        private Map<Integer, View> mMapView;

        public BaseViewHolder(View view) {
            super(view);
            mMapView = new HashMap<>();
            mMapView.put(0, view);
        }

        public void initViewList(int[] idList) {
            for (int id : idList)
                initViewById(id);
        }

        public void initViewById(int id) {
            View view = (getView() != null ? getView().findViewById(id) : null);

            if (view != null)
                mMapView.put(id, view);
        }

        public View getView() {
            return getView(0);
        }

        public View getView(int id) {
            if (mMapView.containsKey(id))
                return mMapView.get(id);
            else
                initViewById(id);

            return mMapView.get(id);
        }
    }
    //endregion
}

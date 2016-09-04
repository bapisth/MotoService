package com.urja.motoservice.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.urja.motoservice.R;

/**
 * Created by BAPI1 on 9/3/2016.
 */
class ItemViewHolder extends RecyclerView.ViewHolder {

    private final View rootView;
    private final ImageView imgItem;
    private final TextView tvItem;
    private final TextView tvSubItem;

    public ItemViewHolder(View view) {
        super(view);

        rootView = view;
        imgItem = (ImageView) view.findViewById(R.id.imgItem);
        tvItem = (TextView) view.findViewById(R.id.tvItem);
        tvSubItem = (TextView) view.findViewById(R.id.tvSubItem);
    }
}

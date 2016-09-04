package com.urja.motoservice.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.urja.motoservice.R;
import com.urja.motoservice.database.ServiceRequest;

import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

/**
 * Created by BAPI1 on 9/3/2016.
 */
public class ExpandableVehicleSection extends StatelessSection {
    String title;
    List<ServiceRequest> list;
    boolean expanded = true;
    Context mContext;

    public ExpandableVehicleSection(String title, List<ServiceRequest> list, Context context) {
        super(R.layout.modify_choosen_service_header, R.layout.modify_choosen_service_item);

        this.title = title;
        this.list = list;
        this.mContext = context;
    }

    @Override
    public int getContentItemsTotal() {
        return expanded? list.size() : 0;
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ItemViewHolder itemHolder = (ItemViewHolder) holder;

        String name = list.get(position).getDesc();
        String category = list.get(position).getGroupname();

        itemHolder.tvItem.setText(name);
        itemHolder.tvSubItem.setText(category);
        itemHolder.imgItem.setImageResource(R.drawable.ic_movie_black_48dp);

        itemHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Toast.makeText(mContext, String.format("Clicked on position #%s of Section %s",
                        sectionAdapter.getSectionPosition(itemHolder.getAdapterPosition()), title),
                        Toast.LENGTH_SHORT).show();*/
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        final HeaderViewHolder headerHolder = (HeaderViewHolder) holder;

        headerHolder.tvTitle.setText(title);

        headerHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expanded = !expanded;
                headerHolder.imgArrow.setImageResource(
                        expanded ? R.drawable.ic_keyboard_arrow_up_black_18dp : R.drawable.ic_keyboard_arrow_down_black_18dp
                );
                //sectionAdapter.notifyDataSetChanged();
            }
        });
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {

        private final View rootView;
        private final TextView tvTitle;
        private final ImageView imgArrow;

        public HeaderViewHolder(View view) {
            super(view);

            rootView = view;
            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            imgArrow = (ImageView) view.findViewById(R.id.imgArrow);
        }
    }

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
}

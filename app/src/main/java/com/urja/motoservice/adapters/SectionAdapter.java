package com.urja.motoservice.adapters;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.urja.motoservice.R;
import com.urja.motoservice.database.ServiceRequest;
import com.urja.motoservice.model.ServiceTypeSection;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.logging.Handler;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class SectionAdapter extends BaseSectionQuickAdapter<ServiceTypeSection> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param sectionHeadResId The section head layout id for each item
     * @param layoutResId The layout resource id of each item.
     * @param data        A new list is created out of this one to avoid mutable list
     */
    private static final String CAR_URL = "https://fs01.androidpit.info/a/63/0e/android-l-wallpapers-630ea6-h900.jpg";
    public SectionAdapter( int layoutResId, int sectionHeadResId, List data) {
        super( layoutResId, sectionHeadResId, data);
    }

    @Override
    protected void convertHead(BaseViewHolder helper,ServiceTypeSection item) {
        ServiceRequest serviceRequest = (ServiceRequest) item.t;
        helper.setText(R.id.header, item.header);
        helper.setVisible(R.id.more,item.isMore());
        View  view = helper.getView(R.id.item_section_content);
        TextView vehicleViewHolder = helper.getView(R.id.vehicle_group_holder);
        if (serviceRequest!=null)
            vehicleViewHolder.setText(serviceRequest.getVehiclegroup());

        helper.setOnClickListener(R.id.more,new OnItemChildClickListener());
    }


    @Override
    protected void convert(BaseViewHolder helper, ServiceTypeSection item) {
        helper.setOnClickListener(R.id.close_btn, new OnItemChildClickListener());
        ServiceRequest serviceRequest = (ServiceRequest) item.t;
        int[] androidColors = mContext.getResources().getIntArray(R.array.androidcolors);
        int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];
        CardView  cardView = (CardView) helper.getView(R.id.item_section_content);
        cardView.setCardBackgroundColor(randomAndroidColor);

        //helper.setBackgroundColor(R.id.item_section_content, randomAndroidColor);
        helper.setText(R.id.item_code, serviceRequest.getCode());
        helper.setText(R.id.tv, serviceRequest.getDesc());
    }

}

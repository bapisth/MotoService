package com.urja.motoservice.adapters;

import android.view.View;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.urja.motoservice.R;
import com.urja.motoservice.database.ServiceRequest;
import com.urja.motoservice.model.ServiceTypeSection;

import java.util.List;

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
    public SectionAdapter( int layoutResId, int sectionHeadResId, List data) {
        super( layoutResId, sectionHeadResId, data);
    }

    @Override
    protected void convertHead(BaseViewHolder helper,final ServiceTypeSection item) {
        helper.setText(R.id.header, item.header);
        helper.setVisible(R.id.more,item.isMore());
        helper.setOnClickListener(R.id.more,new OnItemChildClickListener());
    }


    @Override
    protected void convert(BaseViewHolder helper, ServiceTypeSection item) {
        helper.setOnClickListener(R.id.close_btn, new OnItemChildClickListener());
        ServiceRequest serviceRequest = (ServiceRequest) item.t;
        //helper.setImageUrl(R.id.iv, video.getImg());
        helper.setText(R.id.item_code, serviceRequest.getCode());
        helper.setText(R.id.tv, serviceRequest.getDesc());
    }

}

package com.urja.motoservice.model;

import com.chad.library.adapter.base.entity.SectionEntity;
import com.urja.motoservice.database.ServiceRequest;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class ServiceTypeSection extends SectionEntity<ServiceRequest> {
    private boolean isMore;
    public ServiceTypeSection(boolean isHeader, String header, boolean isMroe) {
        super(isHeader, header);
        this.isMore = isMroe;
    }

    public ServiceTypeSection(ServiceRequest t) {
        super(t);
    }

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean mroe) {
        isMore = mroe;
    }
}

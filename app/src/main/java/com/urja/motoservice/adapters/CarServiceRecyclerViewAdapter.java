package com.urja.motoservice.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.urja.motoservice.R;
import com.urja.motoservice.database.CarServicePrice;
import com.urja.motoservice.model.ServiceEventModel;
import com.urja.motoservice.utils.AppConstants;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by BAPI1 on 19/08/16.
 */
public class CarServiceRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = CarServiceRecyclerViewAdapter.class.getSimpleName();

    List<CarServicePrice> contents;

    static final int TYPE_HEADER = 0;
    static final int TYPE_CELL = 1;
    private Context mContext;
    private final String CAR_TYPE;
    private final String RUPEES = "Rs. ";

    public CarServiceRecyclerViewAdapter(List<CarServicePrice> contents, Context context, String carType) {
        this.CAR_TYPE = carType;
        this.contents = contents;
        this.mContext = context;
        for(CarServicePrice servicePrice : contents)
            servicePrice.setChecked(false);
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return TYPE_HEADER;
            default:
                return TYPE_CELL;
        }
    }

    @Override
    public int getItemCount() {
        return contents.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        RecyclerView.ViewHolder viewHolder = null;

        switch (viewType) {
            case TYPE_HEADER: {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_card_big, parent, false);
                viewHolder = new HeaderViewHolder(view);
                return viewHolder;
            }
            case TYPE_CELL: {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_card_small, parent, false);
                viewHolder = new ContentViewHolder(view);
                return viewHolder;
            }
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_HEADER:
                HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
                headerViewHolder.mlargeDesc.setText(mContext.getString(R.string.tab_wash_desc));
                headerViewHolder.mlargeDesc.setMovementMethod(new ScrollingMovementMethod());
                break;
            case TYPE_CELL:
                final ContentViewHolder contentViewHolder = (ContentViewHolder) holder;
                final CarServicePrice servicePrice = contents.get(position);
                if (contents != null && servicePrice != null) {
                    contentViewHolder.mCode.setText(servicePrice.getServiceCode());
                    contentViewHolder.mCheckBox.setText(servicePrice.getServiceDesc());

                    if (CAR_TYPE.equalsIgnoreCase(AppConstants.ValidVehicle.CAR_TYPE_SMALL)){
                        contentViewHolder.mItemPrice.setText(RUPEES+servicePrice.getPriceSmall());
                    }else if (CAR_TYPE.equalsIgnoreCase(AppConstants.ValidVehicle.CAR_TYPE_MEDIUM)){
                        contentViewHolder.mItemPrice.setText(RUPEES+servicePrice.getPriceMedium());
                    }else if (CAR_TYPE.equalsIgnoreCase(AppConstants.ValidVehicle.CAR_TYPE_LARGE)){
                        contentViewHolder.mItemPrice.setText(RUPEES+servicePrice.getPriceLarge());
                    }

                }
                //viewHolder.chkSelected.setChecked(stList.get(position).isSelected());
                contentViewHolder.mCheckBox.setChecked(servicePrice.isChecked());
                contentViewHolder.mCheckBox.setTag(servicePrice);

                contentViewHolder.mCheckBox.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;
                        CarServicePrice csp = (CarServicePrice) cb.getTag();
                        csp.setChecked(cb.isChecked());
                        servicePrice.setChecked(cb.isChecked());
                        EventBus.getDefault().post(new ServiceEventModel(cb.isChecked(), csp));
                    }
                });
                break;
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder{
        private final TextView mlargeDesc;
        public HeaderViewHolder(View itemView) {
            super(itemView);
            mlargeDesc = (TextView) itemView.findViewById(R.id.washDetailDesription);
        }
    }

    public class ContentViewHolder extends RecyclerView.ViewHolder{
        private final TextView mCode;
        private final TextView mItemPrice;
        private final CheckBox mCheckBox;
        public ContentViewHolder(View view) {
            super(view);
            mCode = (TextView) view.findViewById(R.id.code);
            mItemPrice = (TextView) view.findViewById(R.id.item_price);
            mCheckBox = (CheckBox) view.findViewById(R.id.washDetailCheckBox);
        }
    }
}
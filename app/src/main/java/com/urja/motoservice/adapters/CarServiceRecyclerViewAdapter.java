package com.urja.motoservice.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.urja.motoservice.R;
import com.urja.motoservice.model.Accessories;
import com.urja.motoservice.model.CarCareDetailing;
import com.urja.motoservice.model.DentPaint;
import com.urja.motoservice.model.ServiceEventModel;
import com.urja.motoservice.model.ServiceRepair;
import com.urja.motoservice.model.TyreWheel;
import com.urja.motoservice.model.WashDetailing;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by BAPI1 on 19/08/16.
 */
public class CarServiceRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Object> contents;

    static final int TYPE_HEADER = 0;
    static final int TYPE_CELL = 1;
    private TextView mlargeDesc;
    private TextView mCode;
    private TextView mItemDesc;
    private CheckBox mCheckBox;
    private Context mContext;

    public CarServiceRecyclerViewAdapter(List<Object> contents, Context context) {
        this.contents = contents;
        this.mContext = context;
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

        switch (viewType) {
            case TYPE_HEADER: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_card_big, parent, false);
                mlargeDesc = (TextView) view.findViewById(R.id.washDetailDesription);
                mlargeDesc.setText(mContext.getString(R.string.tab_wash_desc));
                mlargeDesc.setMovementMethod(new ScrollingMovementMethod());
                return new RecyclerView.ViewHolder(view) {
                };
            }
            case TYPE_CELL: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_card_small, parent, false);
                mCode = (TextView) view.findViewById(R.id.code);
                mItemDesc = (TextView) view.findViewById(R.id.item_desc);
                mCheckBox = (CheckBox) view.findViewById(R.id.washDetailCheckBox);

                return new RecyclerView.ViewHolder(view) {
                };
            }
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_HEADER:
                break;
            case TYPE_CELL:
                final Object object = contents.get(position);
                if (contents != null && object instanceof CarCareDetailing) {
                    CarCareDetailing carCareDetailing = (CarCareDetailing) object;
                    mCode.setText(carCareDetailing.getCode());
                    mCheckBox.setText(carCareDetailing.getDesc());
                } /*else if (contents != null && object instanceof ServiceRepair) {
                    ServiceRepair serviceRepair = (ServiceRepair) object;
                    mCode.setText(serviceRepair.getCode());
                    mCheckBox.setText(serviceRepair.getDesc());
                } else if (contents != null && object instanceof TyreWheel) {
                    TyreWheel tyreWheel = (TyreWheel) object;
                    mCode.setText(tyreWheel.getCode());
                    mCheckBox.setText(tyreWheel.getDesc());
                } else if (contents != null && object instanceof DentPaint) {
                    DentPaint dentPaint = (DentPaint) object;
                    mCode.setText(dentPaint.getCode());
                    mCheckBox.setText(dentPaint.getDesc());
                } else if (contents != null && object instanceof Accessories) {
                    Accessories accessories = (Accessories) object;
                    mCode.setText(accessories.getCode());
                    mCheckBox.setText(accessories.getDesc());
                }*/

                mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        EventBus.getDefault().post(new ServiceEventModel(b, object));
                    }
                });
                break;
        }
    }
}
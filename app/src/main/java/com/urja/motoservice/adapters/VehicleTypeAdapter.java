package com.urja.motoservice.adapters;

/**
 * Created by BAPI1 on 8/16/2016.
 */


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.urja.motoservice.ChooseServiceActivity;
import com.urja.motoservice.R;
import com.urja.motoservice.model.Vehicle;
import com.urja.motoservice.utils.AppConstants;

import java.util.List;

public class VehicleTypeAdapter extends RecyclerView.Adapter<VehicleTypeAdapter.MyViewHolder> {

    private static final String TAG = VehicleTypeAdapter.class.getSimpleName();
    private Context mContext;
    private List<Vehicle> mVehicleList;
    private LayoutInflater mLayoutInflater;
    private WindowManager mWindowManager;
    private int mWidth = 240;
    private float mScale;
    private View mContentView;
    private PopupWindow mPopupWindow;
    private ViewGroup mViewGroup;
    private long mServiceID = -1;
    private String code = "";
    String carSize = "";

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count;
        public ImageView thumbnail, overflow;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            count = (TextView) view.findViewById(R.id.price);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            //overflow = (ImageView) view.findViewById(R.id.overflow);
        }
    }


    public VehicleTypeAdapter(Context mContext, List<Vehicle> vehicleList) {
        this.mContext = mContext;
        this.mVehicleList = vehicleList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mViewGroup = parent;
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_types_card, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Vehicle vehicle = mVehicleList.get(position);
        holder.title.setText(vehicle.getCarType());
        code = Long.toString(vehicle.getCode());
        switch (code){
            case "1": carSize = AppConstants.ValidVehicle.CAR_TYPE_SMALL;break;
            case "2": carSize = AppConstants.ValidVehicle.CAR_TYPE_MEDIUM;break;
            case "3": carSize = AppConstants.ValidVehicle.CAR_TYPE_LARGE;break;
        }

        holder.count.setText(carSize); //remove

        Log.e(TAG, "onBindViewHolder: " + vehicle.getDownloadPath());
        // loading album cover using Glide library
        Glide.with(mContext).load(vehicle.getDownloadPath()).into(holder.thumbnail);

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ChooseServiceActivity.class);
                //intent.putExtra(AppConstants.CAR_ID, mServiceID);
                intent.putExtra(AppConstants.CAR_ID, carSize);
                mContext.startActivity(intent);
            }
        });

        /*holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow, position);
                //showPopupWindow(holder.overflow);
            }
        });*/
    }

    private PopupWindow showPopupWindow(View view) {
        PopupWindow mDropdown = null;

        try {

            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics metrics = new DisplayMetrics();
            mWindowManager.getDefaultDisplay().getMetrics(metrics);
            mScale = metrics.scaledDensity;
            mContentView = mLayoutInflater.inflate(R.layout.popup_window, null);
            //If you want to add any listeners to your textviews, these are two //textviews.
            final TextView itema = (TextView) mContentView.findViewById(R.id.request_service);

            mContentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            mDropdown = new PopupWindow(mContentView, FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT, true);
            //Drawable background = mContext.getResources().getDrawable(android.R.drawable.editbox_dropdown_dark_frame);
            //mDropdown.setBackgroundDrawable(background);
            mDropdown.showAsDropDown(view, 5, 5);
            mDropdown.showAtLocation(view, Gravity.CENTER, 0, 0);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return mDropdown;

    }

    public void show(View anchor) {

        if (anchor == null) {
            View parent = ((Activity) mContext).getWindow().getDecorView();
            mPopupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
            return;
        }

        int xPos, yPos;
        int[] location = new int[2];
        anchor.getLocationOnScreen(location);

        Rect anchorRect = new Rect(location[0], location[1],
                location[0] + anchor.getWidth(),
                location[0] + anchor.getHeight());

        mContentView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mContentView.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        int rootHeight = mContentView.getMeasuredHeight();
        int screenHeight = mWindowManager.getDefaultDisplay().getHeight();

        // Set x-coordinate to display the popup menu
        xPos = anchorRect.centerX() - mPopupWindow.getWidth() / 2;

        int dyTop = anchorRect.top;
        int dyBottom = screenHeight + rootHeight;
        boolean onTop = dyTop > dyBottom;

        // Set y-coordinate to display the popup menu
        if (onTop) {
            yPos = anchorRect.top - rootHeight;
        } else {
            if (anchorRect.bottom > dyTop) {
                yPos = anchorRect.bottom - 20;
            } else {
                yPos = anchorRect.top - anchorRect.bottom + 50;
            }
        }

        mPopupWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, xPos, yPos);
    }


    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view, final int position) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_book, popup.getMenu());
        Vehicle vehicle = mVehicleList.get(position);
        mServiceID = mVehicleList.get(position).getCode();
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_request_service:
                    Intent intent = new Intent(mContext, ChooseServiceActivity.class);
                    intent.putExtra(AppConstants.CAR_ID, mServiceID);
                    mContext.startActivity(intent);
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        Log.e("XA", "getItemCount: " + mVehicleList.size());
        return mVehicleList.size();
    }
}

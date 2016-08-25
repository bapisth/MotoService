package com.urja.motoservice;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;
import com.urja.motoservice.database.DbHelper;
import com.urja.motoservice.database.ServiceRequest;
import com.urja.motoservice.database.dao.ServiceRequestDao;
import com.urja.motoservice.fragment.AccessoriesFragment;
import com.urja.motoservice.fragment.DentPaintFragment;
import com.urja.motoservice.fragment.ServiceRepairFragment;
import com.urja.motoservice.fragment.TyreWheelFragment;
import com.urja.motoservice.fragment.WashDetailingFragment;
import com.urja.motoservice.model.Accessories;
import com.urja.motoservice.model.DentPaint;
import com.urja.motoservice.model.ServiceEventModel;
import com.urja.motoservice.model.ServiceRepair;
import com.urja.motoservice.model.TyreWheel;
import com.urja.motoservice.model.WashDetailing;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class ChooseServiceActivity extends AppCompatActivity {


    private static final String TAG = ChooseServiceActivity.class.getSimpleName();
    public static final String WASH_DETAILING = "WashDetailing";
    public static final String TYRE_WHEEL = "TyreWheel";
    public static final String SERVICE_REPAIR = "ServiceRepair";
    public static final String ACCESSORIES = "Accessories";
    public static final String DENT_PAINT = "DentPaint";
    private MaterialViewPager mViewPager;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private List<WashDetailing> mWashDetailingList = new ArrayList<>();
    private List<TyreWheel> mTyreWheelList = new ArrayList<>();
    private List<ServiceRepair> mServiceRepairList = new ArrayList<>();
    private List<DentPaint> mDentPaintList = new ArrayList<>();
    private List<Accessories> mAccessoriesList = new ArrayList<>();
    private List<ServiceRequest> mServiceRequestList = new ArrayList<>();
    private ServiceRequest mServiceRequest = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_service);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mWashDetailingList.size()==0 && mTyreWheelList.size()==0 && mServiceRepairList.size()==0  && mDentPaintList.size()==0  && mAccessoriesList.size()==0 ){
                    Toast.makeText(ChooseServiceActivity.this, "Choose Any of the Service!!!", Toast.LENGTH_SHORT).show();
                    return;
                }

                ServiceRequestDao serviceRequestDao = DbHelper.getInstance(ChooseServiceActivity.this).getServiceRequestDao();
                for (WashDetailing washDetailing: mWashDetailingList){
                    mServiceRequest = new ServiceRequest();
                    mServiceRequest.setCode(washDetailing.getCode());
                    mServiceRequest.setDesc(washDetailing.getDesc());
                    mServiceRequest.setGroupname(WASH_DETAILING);

                    mServiceRequestList.add(mServiceRequest);
                }

                for (TyreWheel tyreWheel: mTyreWheelList){
                    mServiceRequest = new ServiceRequest();
                    mServiceRequest.setCode(tyreWheel.getCode());
                    mServiceRequest.setDesc(tyreWheel.getDesc());
                    mServiceRequest.setGroupname(TYRE_WHEEL);

                    mServiceRequestList.add(mServiceRequest);
                }

                for (ServiceRepair serviceRepair: mServiceRepairList){
                    mServiceRequest = new ServiceRequest();
                    mServiceRequest.setCode(serviceRepair.getCode());
                    mServiceRequest.setDesc(serviceRepair.getDesc());
                    mServiceRequest.setGroupname(SERVICE_REPAIR);

                    mServiceRequestList.add(mServiceRequest);
                }

                for (DentPaint dentPaint: mDentPaintList){
                    mServiceRequest = new ServiceRequest();
                    mServiceRequest.setCode(dentPaint.getCode());
                    mServiceRequest.setDesc(dentPaint.getDesc());
                    mServiceRequest.setGroupname(DENT_PAINT);

                    mServiceRequestList.add(mServiceRequest);
                }

                for (Accessories accessories: mAccessoriesList){
                    mServiceRequest = new ServiceRequest();
                    mServiceRequest.setCode(accessories.getCode());
                    mServiceRequest.setDesc(accessories.getDesc());
                    mServiceRequest.setGroupname(ACCESSORIES);

                    mServiceRequestList.add(mServiceRequest);
                }

                //Insert into service request table
                serviceRequestDao.insertInTx(mServiceRequestList);

                Intent intent  = new Intent(ChooseServiceActivity.this, ConfirmRequestActivity.class );
                startActivity(intent);

                //finish();
                //
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });


        mViewPager = (MaterialViewPager) findViewById(R.id.materialViewPager);


        toolbar = mViewPager.getToolbar();

        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }




        mViewPager.getViewPager().setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                Fragment fragment = null;
                Log.e(TAG, "getItem: position="+position );
                switch (position % 5) {
                    case 0:
                        fragment = WashDetailingFragment.newInstance();
                        break;
                    case 1:
                        fragment = ServiceRepairFragment.newInstance();
                        break;
                    case 2:
                        fragment = TyreWheelFragment.newInstance();
                        break;
                    case 3:
                        fragment = DentPaintFragment.newInstance();
                        break;
                    case 4:
                        fragment = AccessoriesFragment.newInstance();
                        break;
                }
                return fragment;
            }

            @Override
            public int getCount() {
                return 5;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position % 5) {
                    case 0:
                        return getString(R.string.tab_wash_detailing);
                    case 1:
                        return getString(R.string.tab_service_repairs);
                    case 2:
                        return getString(R.string.tab_tyre_wheel);
                    case 3:
                        return getString(R.string.tab_dent_paint);
                    case 4:
                        return getString(R.string.tab_accessories);
                }
                return "";
            }
        });

        mViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                switch (page) {
                    case 0:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.green,
                                "https://fs01.androidpit.info/a/63/0e/android-l-wallpapers-630ea6-h900.jpg");
                    case 1:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.blue,
                                "http://cdn1.tnwcdn.com/wp-content/blogs.dir/1/files/2014/06/wallpaper_51.jpg");
                    case 2:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.cyan,
                                "http://www.droid-life.com/wp-content/uploads/2014/10/lollipop-wallpapers10.jpg");
                    case 3:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.red,
                                "http://www.tothemobile.com/wp-content/uploads/2014/07/original.jpg");
                    case 4:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.red,
                                "http://www.tothemobile.com/wp-content/uploads/2014/07/original.jpg");
                }

                //execute others actions if needed (ex : modify your header logo)

                return null;
            }
        });

        mViewPager.getViewPager().setOffscreenPageLimit(mViewPager.getViewPager().getAdapter().getCount());
        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());

        View logo = findViewById(R.id.logo_white);
        if (logo != null) {
            logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.notifyHeaderChanged();
                    Toast.makeText(getApplicationContext(), "Yes, the title is clickable", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ServiceEventModel serviceEventModel) {
        Object event = serviceEventModel.getObject();
        boolean add = serviceEventModel.isAdd();
        if (event instanceof WashDetailing){
            WashDetailing washDetailing = (WashDetailing) event;
            if (add)
                mWashDetailingList.add(washDetailing);
            else
                mWashDetailingList.remove(washDetailing);
        }else if (event instanceof TyreWheel){
            TyreWheel tyreWheel = (TyreWheel) event;

            if (add)
                mTyreWheelList.add(tyreWheel);
            else
                mTyreWheelList.remove(tyreWheel);

        }else if (event instanceof ServiceRepair){
            ServiceRepair serviceRepair = (ServiceRepair) event;

            if (add)
                mServiceRepairList.add(serviceRepair);
            else
                mServiceRepairList.remove(serviceRepair);

        }else if (event instanceof DentPaint){
            DentPaint dentPaint = (DentPaint) event;

            if (add)
                mDentPaintList.add(dentPaint);
            else
                mDentPaintList.remove(dentPaint);

        }else if (event instanceof Accessories){
            Accessories accessories = (Accessories) event;

            if (add)
                mAccessoriesList.add(accessories);
            else
                mAccessoriesList.remove(accessories);
        }
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
}

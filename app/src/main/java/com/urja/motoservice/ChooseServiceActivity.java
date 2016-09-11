package com.urja.motoservice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;
import com.urja.motoservice.database.DbHelper;
import com.urja.motoservice.database.ServiceRequest;
import com.urja.motoservice.database.dao.ServiceRequestDao;
import com.urja.motoservice.fragment.AccessoriesFragment;
import com.urja.motoservice.fragment.Ask4CarNumberDialogFragment;
import com.urja.motoservice.fragment.DentPaintFragment;
import com.urja.motoservice.fragment.ServiceRepairFragment;
import com.urja.motoservice.fragment.TyreWheelFragment;
import com.urja.motoservice.fragment.WashDetailingFragment;
import com.urja.motoservice.model.Accessories;
import com.urja.motoservice.model.DentPaint;
import com.urja.motoservice.model.ServiceEventModel;
import com.urja.motoservice.model.ServiceRepair;
import com.urja.motoservice.model.TransactionComplete;
import com.urja.motoservice.model.TyreWheel;
import com.urja.motoservice.model.WashDetailing;
import com.urja.motoservice.utils.AppConstants;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ChooseServiceActivity extends AppCompatActivity implements Ask4CarNumberDialogFragment.Ask4CarNumberDialogListener {
    private static final String TAG = ChooseServiceActivity.class.getSimpleName();
    private MaterialViewPager mViewPager;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private Set<WashDetailing> mWashDetailingList = new HashSet<>();
    private Set<TyreWheel> mTyreWheelList = new HashSet<>();
    private Set<ServiceRepair> mServiceRepairList = new HashSet<>();
    private Set<DentPaint> mDentPaintList = new HashSet<>();
    private Set<Accessories> mAccessoriesList = new HashSet<>();
    private List<ServiceRequest> mServiceRequestList = new ArrayList<>();
    private Set<ServiceRequest> mServiceRequestListSet = new HashSet<>();
    private ServiceRequest mServiceRequest = null;

    private FloatingActionMenu menuLabelsRight;
    private com.github.clans.fab.FloatingActionButton mAddMore;
    private com.github.clans.fab.FloatingActionButton mNext;
    private List<FloatingActionMenu> menus = new ArrayList<>();
    private long mVehicleGroupId;
    private String mCarNumber = null;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_service);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        Ask4CarNumberDialogFragment ask4CarNumberDialogFragment = new Ask4CarNumberDialogFragment();
        ask4CarNumberDialogFragment.show(getSupportFragmentManager(), TAG);
        ask4CarNumberDialogFragment.setCancelable(false);

        menuLabelsRight = (FloatingActionMenu) findViewById(R.id.menu_labels_right);
        mAddMore = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.add_more);
        mNext = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.next);
        mAddMore.setOnClickListener(clickListener);
        mNext.setOnClickListener(clickListener);

        Intent intent = getIntent();
        Log.d(TAG, "onCreate: " + intent.getLongExtra(AppConstants.CAR_ID, -1));
        mVehicleGroupId = intent.getLongExtra(AppConstants.CAR_ID, -1);

        mViewPager = (MaterialViewPager) findViewById(R.id.materialViewPager);
        toolbar = mViewPager.getToolbar();

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            //actionBar.setDisplayHomeAsUpEnabled(true);
            //actionBar.setDisplayShowHomeEnabled(true);
            //actionBar.setDisplayShowTitleEnabled(true);
            //actionBar.setDisplayUseLogoEnabled(false);
            //actionBar.setHomeButtonEnabled(true);
        }

        mViewPager.getViewPager().setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                Fragment fragment = null;
                Log.e(TAG, "getItem: position=" + position);
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
                }
            });
        }
    }

    private void goToServiceCheckList() {
        if (mWashDetailingList.size() == 0 && mTyreWheelList.size() == 0 && mServiceRepairList.size() == 0 && mDentPaintList.size() == 0 && mAccessoriesList.size() == 0) {
            Toast.makeText(ChooseServiceActivity.this, "Choose Any of the Service!!!", Toast.LENGTH_SHORT).show();
            return;
        }

        ServiceRequestDao serviceRequestDao = addToServiceRequestList();

        //Check if the same service is available for the same Vehicle Number
        List<ServiceRequest> list = serviceRequestDao.queryBuilder().where(ServiceRequestDao.Properties.Carnumber.eq(mCarNumber)).build().list();
        int size = mServiceRequestList.size();
        for (int i = 0; i < list.size(); i++) { //Check for Every data in the database
            ServiceRequest serviceRequest = list.get(i);
            for (Iterator<ServiceRequest> iterator = mServiceRequestList.iterator(); iterator.hasNext(); ) {
                ServiceRequest srvReq = iterator.next();
                if (srvReq.getCode().equalsIgnoreCase(serviceRequest.getCode())) {//then remove the object which is already
                    // there for the same vehicle number
                    iterator.remove();
                }
            }
        }

        Object[] st = mServiceRequestList.toArray();
        for (Object s : st) {
            if (mServiceRequestList.indexOf(s) != mServiceRequestList.lastIndexOf(s)) {
                mServiceRequestList.remove(mServiceRequestList.lastIndexOf(s));
            }
        }

        //Insert into service request table
        serviceRequestDao.insertOrReplaceInTx(mServiceRequestList);

        mServiceRequestList = new ArrayList<>();
        //Intent intent  = new Intent(ChooseServiceActivity.this, ConfirmRequestActivity.class );
        Intent intent = new Intent(ChooseServiceActivity.this, ModifyChoosenServiceRquestActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivityForResult(intent, AppConstants.BACK_FROM_CHILD_MODIFY_CHOOSEN_SERVICE_ACTIVITY);

    }

    private ServiceRequestDao addToServiceRequestList() {
        ServiceRequestDao serviceRequestDao = DbHelper.getInstance(this).getServiceRequestDao();
        for (WashDetailing washDetailing : mWashDetailingList) {
            mServiceRequest = new ServiceRequest();
            mServiceRequest.setCode(washDetailing.getCode());
            mServiceRequest.setDesc(washDetailing.getDesc());
            mServiceRequest.setGroupname(AppConstants.WASH_DETAILING);
            mServiceRequest.setVehiclegroup(String.valueOf(mVehicleGroupId));
            mServiceRequest.setCarnumber(String.valueOf(mCarNumber));

            mServiceRequestList.add(mServiceRequest);
        }

        for (TyreWheel tyreWheel : mTyreWheelList) {
            mServiceRequest = new ServiceRequest();
            mServiceRequest.setCode(tyreWheel.getCode());
            mServiceRequest.setDesc(tyreWheel.getDesc());
            mServiceRequest.setGroupname(AppConstants.TYRE_WHEEL);
            mServiceRequest.setVehiclegroup(String.valueOf(mVehicleGroupId));
            mServiceRequest.setCarnumber(String.valueOf(mCarNumber));

            mServiceRequestList.add(mServiceRequest);
        }

        for (ServiceRepair serviceRepair : mServiceRepairList) {
            mServiceRequest = new ServiceRequest();
            mServiceRequest.setCode(serviceRepair.getCode());
            mServiceRequest.setDesc(serviceRepair.getDesc());
            mServiceRequest.setGroupname(AppConstants.SERVICE_REPAIR);
            mServiceRequest.setVehiclegroup(String.valueOf(mVehicleGroupId));
            mServiceRequest.setCarnumber(String.valueOf(mCarNumber));

            mServiceRequestList.add(mServiceRequest);
        }

        for (DentPaint dentPaint : mDentPaintList) {
            mServiceRequest = new ServiceRequest();
            mServiceRequest.setCode(dentPaint.getCode());
            mServiceRequest.setDesc(dentPaint.getDesc());
            mServiceRequest.setGroupname(AppConstants.DENT_PAINT);
            mServiceRequest.setVehiclegroup(String.valueOf(mVehicleGroupId));
            mServiceRequest.setCarnumber(String.valueOf(mCarNumber));

            mServiceRequestList.add(mServiceRequest);
        }

        for (Accessories accessories : mAccessoriesList) {
            mServiceRequest = new ServiceRequest();
            mServiceRequest.setCode(accessories.getCode());
            mServiceRequest.setDesc(accessories.getDesc());
            mServiceRequest.setGroupname(AppConstants.ACCESSORIES);
            mServiceRequest.setVehiclegroup(String.valueOf(mVehicleGroupId));
            mServiceRequest.setCarnumber(String.valueOf(mCarNumber));


            mServiceRequestList.add(mServiceRequest);
        }
        return serviceRequestDao;
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(TransactionComplete transactionComplete) {
        if (transactionComplete.isTransactionComplete()) //If the transaction is completed, then stop the Activity
            finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ServiceEventModel serviceEventModel) {
        Object event = serviceEventModel.getObject();
        boolean add = serviceEventModel.isAdd();
        if (event instanceof WashDetailing) {
            WashDetailing washDetailing = (WashDetailing) event;
            if (add)
                mWashDetailingList.add(washDetailing);
            else
                mWashDetailingList.remove(washDetailing);
        } else if (event instanceof TyreWheel) {
            TyreWheel tyreWheel = (TyreWheel) event;

            if (add)
                mTyreWheelList.add(tyreWheel);
            else
                mTyreWheelList.remove(tyreWheel);

        } else if (event instanceof ServiceRepair) {
            ServiceRepair serviceRepair = (ServiceRepair) event;

            if (add)
                mServiceRepairList.add(serviceRepair);
            else
                mServiceRepairList.remove(serviceRepair);

        } else if (event instanceof DentPaint) {
            DentPaint dentPaint = (DentPaint) event;

            if (add)
                mDentPaintList.add(dentPaint);
            else
                mDentPaintList.remove(dentPaint);

        } else if (event instanceof Accessories) {
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

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.add_more:
                    //Save the Older data if has some before goinh back
                    ServiceRequestDao serviceRequestDao = addToServiceRequestList();
                    serviceRequestDao.insertOrReplaceInTx(mServiceRequestList);
                    Intent intent = new Intent(ChooseServiceActivity.this, WelcomeDashboardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.next:
                    goToServiceCheckList();
                    break;
            }
        }
    };

    @Override
    public void onSubmit(String carNumber) {
        this.mCarNumber = carNumber;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

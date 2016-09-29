package com.urja.motoservice;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.github.clans.fab.FloatingActionMenu;
import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;
import com.urja.motoservice.database.CarServicePrice;
import com.urja.motoservice.database.DbHelper;
import com.urja.motoservice.database.ServiceRequest;
import com.urja.motoservice.database.dao.CarServicePriceDao;
import com.urja.motoservice.database.dao.ServiceRequestDao;
import com.urja.motoservice.fragment.AccessoriesFragment;
import com.urja.motoservice.fragment.Ask4CarNumberDialogFragment;
import com.urja.motoservice.fragment.DentPaintFragment;
import com.urja.motoservice.fragment.ServiceRepairFragment;
import com.urja.motoservice.fragment.TyreWheelFragment;
import com.urja.motoservice.fragment.CarCareDetailingFragment;
import com.urja.motoservice.model.Accessories;
import com.urja.motoservice.model.CarCareDetailing;
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
    //private Set<CarCareDetailing> carCareDetailings = new HashSet<>();
    private Set<CarServicePrice> carServicePriceList = new HashSet<>();

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
    private String mVehicleGroupId;
    private String mCarNumber = null;
    private String mCarType = null;
    private boolean backFromModifyService = false;

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

        if (!backFromModifyService){
            Ask4CarNumberDialogFragment ask4CarNumberDialogFragment = new Ask4CarNumberDialogFragment();
            ask4CarNumberDialogFragment.show(getSupportFragmentManager(), TAG);
            ask4CarNumberDialogFragment.setCancelable(false);
        }

        menuLabelsRight = (FloatingActionMenu) findViewById(R.id.menu_labels_right);
        mAddMore = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.add_more);
        mNext = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.next);
        mAddMore.setOnClickListener(clickListener);
        mNext.setOnClickListener(clickListener);

        Intent intent = getIntent();
        Log.d(TAG, "onCreate: " + intent.getStringExtra(AppConstants.CAR_ID));
        mVehicleGroupId = intent.getStringExtra(AppConstants.CAR_ID);
        //mVehicleGroupId = intent.getLongExtra(AppConstants.CAR_ID, -1);

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
                switch (position % 1) {
                    case 0:
                        fragment = CarCareDetailingFragment.newInstance(mVehicleGroupId);
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
                return 1;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position % 1) {
                    case 0:
                        return getString(R.string.tab_car_care_detailing);
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

                        HeaderDesign headerDesign = HeaderDesign.fromColorAndDrawable(getResources().getColor(R.color.colorPrimary), ContextCompat.getDrawable(ChooseServiceActivity.this, R.drawable.welcomescreen_bg));
                        return headerDesign;
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
        PagerSlidingTabStrip pagerTitleStrip = mViewPager.getPagerTitleStrip();
        pagerTitleStrip.setVisibility(View.INVISIBLE);
        //pagerTitleStrip.setViewPager(mViewPager.getViewPager());

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
        if (carServicePriceList.size() == 0) {
            Toast.makeText(ChooseServiceActivity.this, "Choose Any of the Service!!!", Toast.LENGTH_SHORT).show();
            return;
        }

        ServiceRequestDao serviceRequestDao = removeDuplicateService();

        //Insert into service request table
        serviceRequestDao.insertOrReplaceInTx(mServiceRequestList);

        mServiceRequestList = new ArrayList<>();
        //Intent intent  = new Intent(ChooseServiceActivity.this, ConfirmRequestActivity.class );
        Intent intent = new Intent(ChooseServiceActivity.this, ModifyChoosenServiceRquestActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivityForResult(intent, AppConstants.BACK_FROM_CHILD_MODIFY_CHOOSEN_SERVICE_ACTIVITY);

    }

    @NonNull
    private ServiceRequestDao removeDuplicateService() {
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
        return serviceRequestDao;
    }

    private ServiceRequestDao addToServiceRequestList() {
        ServiceRequestDao serviceRequestDao = DbHelper.getInstance(this).getServiceRequestDao();
        for (CarServicePrice carServicePrice : carServicePriceList) {
            mServiceRequest = new ServiceRequest();
            mServiceRequest.setCode(carServicePrice.getServiceCode());
            mServiceRequest.setDesc(carServicePrice.getServiceDesc());
            mServiceRequest.setGroupname(String.valueOf(mCarType)); //Adding CarType in GroupName
            switch (mVehicleGroupId){
                case AppConstants.ValidVehicle.CAR_TYPE_SMALL:
                    mServiceRequest.setVehiclegroup(String.valueOf(carServicePrice.getPriceSmall()));
                    break;
                case AppConstants.ValidVehicle.CAR_TYPE_MEDIUM:
                    mServiceRequest.setVehiclegroup(String.valueOf(carServicePrice.getPriceMedium()));
                    break;
                case AppConstants.ValidVehicle.CAR_TYPE_LARGE:
                    mServiceRequest.setVehiclegroup(String.valueOf(carServicePrice.getPriceLarge()));
                    break;

            }
            //Inplace of vehicleGroupid keep the Prices
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
        if (event instanceof CarServicePrice) {
            CarServicePrice carServicePrice = (CarServicePrice) event;
            if (add)
                carServicePriceList.add(carServicePrice);
            else
                carServicePriceList.remove(carServicePrice);
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
                    ServiceRequestDao serviceRequestDao = removeDuplicateService();
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
    public void onSubmit(String carNumber, String carType) {
        this.mCarNumber = carNumber;
        this.mCarType = carType;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        backFromModifyService = true;
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

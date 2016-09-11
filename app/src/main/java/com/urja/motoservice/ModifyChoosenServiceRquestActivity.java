package com.urja.motoservice;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import com.chad.library.adapter.base.BaseQuickAdapter;
import com.urja.motoservice.adapters.SectionAdapter;
import com.urja.motoservice.database.DbHelper;
import com.urja.motoservice.database.ServiceRequest;
import com.urja.motoservice.database.dao.ServiceRequestDao;
import com.urja.motoservice.model.ServiceTypeSection;
import com.urja.motoservice.model.TransactionComplete;
import com.urja.motoservice.utils.AppConstants;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.dao.query.DeleteQuery;
import de.greenrobot.dao.query.QueryBuilder;
import de.greenrobot.dao.query.WhereCondition;
import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ModifyChoosenServiceRquestActivity extends AppCompatActivity {

    static final boolean GRID_LAYOUT = false;
    private static final String TAG = ModifyChoosenServiceRquestActivity.class.getSimpleName();
    private List<ServiceRequest> mServiceRequestList = null;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private List<ServiceTypeSection> mContentItems = new ArrayList<>();
    private Toolbar mToolbar;
    private Button mButton;
    private String mCurrentUserId = null;
    private ServiceRequestDao mServiceRequestDao = null;
    //private BaseQuickAdapter mBaseQuickAdapter = null;
    private View mBaseQuickAdapterView = null;
    private int mItemSelectedPosition;
    private SectionAdapter mSectionAdapter = null;
    /*==================================================*/
    private SectionedRecyclerViewAdapter mGridCollapseSectionAdapter;
    private ExpandableVehicleSection expandableVehicleSection;
    private List<ServiceRequest> serviceListByCarNumber;
    private String mSectionTitle = "";

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
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

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeRecyclerView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get the Database Instance
        mServiceRequestDao = DbHelper.getInstance(ModifyChoosenServiceRquestActivity.this).getServiceRequestDao();

        setContentView(R.layout.activity_confirm_request);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //initCollapsingToolbar();

        mButton = (Button) findViewById(R.id.confirm);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //addTransactionToServer(ConfirmRequestActivity.this);
                List<ServiceRequest> requestList = mServiceRequestDao.loadAll();
                if (requestList.size() < 1) {
                    Toast.makeText(ModifyChoosenServiceRquestActivity.this, "You need to add atleast one service.", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(ModifyChoosenServiceRquestActivity.this, CustomerCarDetailsActivity.class);
                startActivity(intent);
            }
        });

        //Initialize Recycler View
        //Code for Grid Collapsing RecyclerView Layout
        mGridCollapseSectionAdapter = new SectionedRecyclerViewAdapter();
        //initializeRecyclerView();


    }

    private void initializeRecyclerView() {
        //mServiceRequestList = mServiceRequestDao.loadAll();
        QueryBuilder<ServiceRequest> serviceRequestQueryBuilder = mServiceRequestDao.queryBuilder();
        serviceRequestQueryBuilder.LOG_SQL = true;
        serviceRequestQueryBuilder.LOG_VALUES = true;

        mServiceRequestList = new ArrayList<>();
        mServiceRequestList = serviceRequestQueryBuilder.orderAsc(ServiceRequestDao.Properties.Carnumber).list();

        String tempCarNumber = "";

        //Find out the Section Header as per the vehicle number
        int count = 0;
        //List<ExpandableVehicleSection> expandableVehicleSections = new ArrayList<>();
        for (ServiceRequest serviceRequest : mServiceRequestList) {
            if (!tempCarNumber.equalsIgnoreCase(serviceRequest.getCarnumber())) {

                tempCarNumber = serviceRequest.getCarnumber();
                mSectionTitle = AppConstants.VEHICLE_GRID_TITLE + tempCarNumber;
                serviceListByCarNumber = new ArrayList<>();
                serviceListByCarNumber = getServiceListByCarNumber(tempCarNumber);

                expandableVehicleSection = new ExpandableVehicleSection(mSectionTitle, serviceListByCarNumber) {
                    private static final String TAG = "ModifyChoosenServiceRquestActivity";

                    @Override
                    protected void onItemClick(int sectionPosition, String vehicleNumber, String serviceCode) {
                        //Get the Object as per the position from the list
                        ServiceRequest request = mServiceRequestList.get(sectionPosition);
                        //If there is only one record as per the VehicleNumber than remove the section
                        String itemCode = String.valueOf(request.getCode());
                        deleteRecordFromDatabase(serviceCode, vehicleNumber, mSectionTitle);
                        mGridCollapseSectionAdapter.notifyDataSetChanged();
                    }
                };
                mGridCollapseSectionAdapter.addSection(mSectionTitle, expandableVehicleSection);
            }
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.requestedServiceRecyclerView);
        GridLayoutManager glm = new GridLayoutManager(this, 2);
        glm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (mGridCollapseSectionAdapter.getSectionItemViewType(position)) {
                    case SectionedRecyclerViewAdapter.VIEW_TYPE_HEADER:
                        return 2;
                    default:
                        return 1;
                }
            }
        });
        mRecyclerView.setLayoutManager(glm);
        mRecyclerView.setAdapter(mGridCollapseSectionAdapter);

        //read the saved data from the sqlite database
        //readServiceRequestData(mSectionAdapter);
    }

    private List<ServiceRequest> getServiceListByCarNumber(String tempCarNumber) {
        List<ServiceRequest> serviceRequestList = new ArrayList<>();
        QueryBuilder<ServiceRequest> requestQueryBuilder = mServiceRequestDao.queryBuilder();
        requestQueryBuilder.LOG_SQL = true;
        requestQueryBuilder.LOG_VALUES = true;
        serviceRequestList = requestQueryBuilder.where(ServiceRequestDao.Properties.Carnumber.eq(tempCarNumber)).list();
        return serviceRequestList;
    }

    private void deleteRecordFromDatabaseByVehicleGroup(String vehiclegroup) {
        ServiceRequestDao serviceRequestDao = DbHelper.getInstance(ModifyChoosenServiceRquestActivity.this).getDaoSession().getServiceRequestDao();
        DeleteQuery deleteQuery = serviceRequestDao.queryBuilder().where(new WhereCondition.StringCondition(String.valueOf("vehiclegroup='" + vehiclegroup + "'"))).buildDelete();
        deleteQuery.executeDeleteWithoutDetachingEntities();
    }

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        final CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    private void readServiceRequestData(SectionAdapter sectionAdapter) {
        mServiceRequestList = mServiceRequestDao.loadAll();
        mServiceRequestList = mServiceRequestDao.queryBuilder().orderAsc(ServiceRequestDao.Properties.Vehiclegroup).list();

        List<ServiceTypeSection> serviceTypeSections = null;
        ServiceTypeSection serviceTypeSection = null;
        mGridCollapseSectionAdapter.notifyDataSetChanged();
    }

    private String getSectionHeaderDescriptio(String groupname) {
        if (groupname == null)
            return "";
        switch (groupname) {
            case AppConstants.WASH_DETAILING:
                return "Wash Detailing";
            case AppConstants.TYRE_WHEEL:
                return "Tyre Wheel";
            case AppConstants.SERVICE_REPAIR:
                return "Service Repair";
            case AppConstants.DENT_PAINT:
                return "Dent Paint";
            case AppConstants.ACCESSORIES:
                return "Accessories";
            default:
                return "";
        }
    }

    public void deleteRecordFromDatabase(String itemCode, String vehicleNumber, String mSectionTitle) {
        ServiceRequestDao serviceRequestDao = DbHelper.getInstance(ModifyChoosenServiceRquestActivity.this).getDaoSession().getServiceRequestDao();
        QueryBuilder<ServiceRequest> queryBuilder = serviceRequestDao.queryBuilder();
        queryBuilder.LOG_SQL = true;
        queryBuilder.LOG_VALUES = true;
        //DeleteQuery deleteQuery  = queryBuilder.where(new WhereCondition.StringCondition(String.valueOf("code='"+itemCode+"'"))).buildDelete();
        List<ServiceRequest> serviceRequestList = queryBuilder.where(ServiceRequestDao.Properties.Code.eq(itemCode), ServiceRequestDao.Properties.Carnumber.eq(vehicleNumber)).list();

        //Get all the records of that same vehicle group
        QueryBuilder<ServiceRequest> queryBuilder2 = serviceRequestDao.queryBuilder();
        queryBuilder2.LOG_SQL=true;
        queryBuilder2.LOG_VALUES=true;

        List<ServiceRequest> countList = queryBuilder2.where(new WhereCondition.StringCondition(String.valueOf("carnumber='" + vehicleNumber + "'"))).list();
        Log.e(TAG, "deleteRecordFromDatabase: "+serviceRequestList.size() );
        serviceRequestDao.delete(serviceRequestList.get(0));//Assuming only one record will be fetced
        if (countList.size() == 1) {
            // i.e for that particular vehicle number there is only one vehicle
            // so we do not require the Section, hence remove the section
            Section section = null;
            LinkedHashMap<String, Section> sectionsMap = mGridCollapseSectionAdapter.getSectionsMap();
            for (Map.Entry<String, Section> entry : sectionsMap.entrySet()) {
                String key = entry.getKey();
                if (key.equalsIgnoreCase(AppConstants.VEHICLE_GRID_TITLE + vehicleNumber)) {
                    section = entry.getValue();
                    break;
                }
            }
            mGridCollapseSectionAdapter.removeSection(AppConstants.VEHICLE_GRID_TITLE + vehicleNumber);
        }

        //serviceRequestDao.deleteInTx(serviceRequestList);
    }

    abstract class ExpandableVehicleSection extends StatelessSection {
        String title;
        List<ServiceRequest> list = new ArrayList<>();
        boolean expanded = true;

        public ExpandableVehicleSection(String title, List<ServiceRequest> list) {
            super(R.layout.modify_choosen_service_header, R.layout.modify_choosen_service_item);

            this.title = title;
            this.list = list;
        }

        @Override
        public int getContentItemsTotal() {
            return expanded ? this.list.size() : 0;
        }

        @Override
        public RecyclerView.ViewHolder getItemViewHolder(View view) {
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindItemViewHolder(final RecyclerView.ViewHolder holder, int position) {
            final ItemViewHolder itemHolder = (ItemViewHolder) holder;

            final ServiceRequest serviceRequest = list.get(position);
            String name = serviceRequest.getDesc();
            String category = serviceRequest.getGroupname();

            itemHolder.tvItem.setText(name);
            itemHolder.tvSubItem.setText(category);
            //itemHolder.imgItem.setImageResource(R.drawable.ic_movie_black_48dp);

            itemHolder.imgItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String vehicleNumber = "";
                    String serviceCode = "";
                    int sectionPosition = mGridCollapseSectionAdapter.getSectionPosition(itemHolder.getAdapterPosition());
                    /*Toast.makeText(ModifyChoosenServiceRquestActivity.this, String.format("Clicked on position #%s of Section %s", sectionPosition, title),
                            Toast.LENGTH_SHORT).show();*/
                    vehicleNumber = title.split(AppConstants.VEHICLE_GRID_TITLE)[1];
                    serviceCode = serviceRequest.getCode();
                    onItemClick(sectionPosition, vehicleNumber, serviceCode);
                    list.remove(sectionPosition);
                }
            });
        }

        protected abstract void onItemClick(int sectionPosition, String vehicleNumber, String serviceCode);

        /*public void addItem(int position, String item) {
            list.add(position, item);
        }*/

        public void removeItem(int position) {
            this.list.remove(position);
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
                    mGridCollapseSectionAdapter.notifyDataSetChanged();
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
}

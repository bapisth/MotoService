package com.urja.motoservice;

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

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.urja.motoservice.adapters.SectionAdapter;
import com.urja.motoservice.database.DbHelper;
import com.urja.motoservice.database.ServiceRequest;
import com.urja.motoservice.database.dao.ServiceRequestDao;
import com.urja.motoservice.model.ServiceTypeSection;
import com.urja.motoservice.utils.AppConstants;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.DeleteQuery;
import de.greenrobot.dao.query.QueryBuilder;
import de.greenrobot.dao.query.WhereCondition;
import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

public class ModifyChoosenServiceRquestActivity extends AppCompatActivity implements DialogInterface.OnClickListener{

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
    private BaseQuickAdapter mBaseQuickAdapter = null;
    private View mBaseQuickAdapterView = null;
    private int mItemSelectedPosition ;
    private SectionAdapter mSectionAdapter = null;
    /*==================================================*/
    private SectionedRecyclerViewAdapter mGridCollapseSectionAdapter;
    private ExpandableVehicleSection expandableVehicleSection;
    private List<ServiceRequest> serviceListByCarNumber;




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
                Intent intent = new Intent(ModifyChoosenServiceRquestActivity.this, CustomerCarDetailsActivity.class);
                startActivity(intent);
            }
        });

        //Code for Grid Collapsing RecyclerView Layout
        mGridCollapseSectionAdapter = new SectionedRecyclerViewAdapter();

        mServiceRequestList = mServiceRequestDao.loadAll();
        QueryBuilder<ServiceRequest> serviceRequestQueryBuilder = mServiceRequestDao.queryBuilder();
        serviceRequestQueryBuilder.LOG_SQL =true;
        serviceRequestQueryBuilder.LOG_VALUES =true;

        mServiceRequestList = serviceRequestQueryBuilder.orderAsc(ServiceRequestDao.Properties.Carnumber).list();

        String tempCarNumber = "";

        //Find out the Section Header as per the vehicle number
        int count =0;
        //List<ExpandableVehicleSection> expandableVehicleSections = new ArrayList<>();
        for (ServiceRequest  serviceRequest : mServiceRequestList){
            if (!tempCarNumber.equalsIgnoreCase(serviceRequest.getCarnumber())){
                count++;
                Log.d(TAG, "onCreate: Group Count="+count);
                tempCarNumber=serviceRequest.getCarnumber();
                Log.e(TAG, "onCreate: tempCarNumber = ["+tempCarNumber+"]" );
                final String title = AppConstants.VEHICLE_GRID_TITLE + tempCarNumber;
                serviceListByCarNumber = new ArrayList<>();
                serviceListByCarNumber = getServiceListByCarNumber(tempCarNumber);

                expandableVehicleSection = new ExpandableVehicleSection(title, serviceListByCarNumber) {
                    private static final String TAG = "ModifyChoosenServiceRquestActivity";
                    @Override
                    protected void onItemClick(int sectionPosition, String vehicleNumber) {
                        //Get the Object as per the position from the list
                        ServiceRequest request = mServiceRequestList.get(sectionPosition);
                        deleteRecordFromDatabase(String.valueOf(request.getCode()), vehicleNumber);
                        mGridCollapseSectionAdapter.notifyDataSetChanged();
                    }
                };
                mGridCollapseSectionAdapter.addSection(expandableVehicleSection);
            }
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.requestedServiceRecyclerView);
        GridLayoutManager glm = new GridLayoutManager(this, 2);
        glm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch(mGridCollapseSectionAdapter.getSectionItemViewType(position)) {
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
        readServiceRequestData(mSectionAdapter);


    }

    private List<ServiceRequest> getServiceListByCarNumber(String tempCarNumber) {
        List<ServiceRequest> serviceRequestList = new ArrayList<>();
        QueryBuilder<ServiceRequest> requestQueryBuilder = mServiceRequestDao.queryBuilder();
        requestQueryBuilder.LOG_SQL=true;
        requestQueryBuilder.LOG_VALUES=true;
        serviceRequestList = requestQueryBuilder.where(ServiceRequestDao.Properties.Carnumber.eq(tempCarNumber)).list();
        return serviceRequestList;
    }

    private void deleteRecordFromDatabaseByVehicleGroup(String vehiclegroup) {
        ServiceRequestDao serviceRequestDao = DbHelper.getInstance(ModifyChoosenServiceRquestActivity.this).getDaoSession().getServiceRequestDao();
        DeleteQuery deleteQuery  = serviceRequestDao.queryBuilder().where(new WhereCondition.StringCondition(String.valueOf("vehiclegroup='"+vehiclegroup+"'"))).buildDelete();
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
        ServiceTypeSection  serviceTypeSection=null;
        mGridCollapseSectionAdapter.notifyDataSetChanged();
        /*
        String tempVehicleGroup="";

        if (mServiceRequestList != null){
            for (ServiceRequest serviceRequest :mServiceRequestList){
                if (!tempVehicleGroup.equalsIgnoreCase(serviceRequest.getVehiclegroup())){
                    tempVehicleGroup=serviceRequest.getVehiclegroup();
                    serviceTypeSection = new ServiceTypeSection(true, getSectionHeaderDescriptio(serviceRequest.getGroupname()), true);
                    mContentItems.add(serviceTypeSection);
                    continue;
                }
                serviceTypeSection = new ServiceTypeSection(serviceRequest);
                mContentItems.add(new ServiceTypeSection(serviceRequest));
            }
        }*/
    }

    private String getSectionHeaderDescriptio(String groupname) {
        if (groupname==null)
            return "";
        switch (groupname){
            case ChooseServiceActivity.WASH_DETAILING:
                return "Wash Detailing";
            case ChooseServiceActivity.TYRE_WHEEL:
                return "Tyre Wheel";
            case ChooseServiceActivity.SERVICE_REPAIR:
                return "Service Repair";
            case ChooseServiceActivity.DENT_PAINT:
                return "Dent Paint";
            case ChooseServiceActivity.ACCESSORIES:
                return "Accessories";
            default:
                return "";
        }
    }

    public void deleteRecordFromDatabase(String itemCode, String vehicleNumber){
        ServiceRequestDao serviceRequestDao = DbHelper.getInstance(ModifyChoosenServiceRquestActivity.this).getDaoSession().getServiceRequestDao();
        QueryBuilder<ServiceRequest> queryBuilder = serviceRequestDao.queryBuilder();
        queryBuilder.LOG_SQL = true;
        queryBuilder.LOG_VALUES = true;
        //DeleteQuery deleteQuery  = queryBuilder.where(new WhereCondition.StringCondition(String.valueOf("code='"+itemCode+"'"))).buildDelete();
        DeleteQuery deleteQuery  = queryBuilder.where(ServiceRequestDao.Properties.Code.eq(itemCode), ServiceRequestDao.Properties.Carnumber.eq(vehicleNumber)).buildDelete();
        deleteQuery.executeDeleteWithoutDetachingEntities();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int position) {
        Log.d(TAG, "onClick: You clicked yes.. data going to be deleted"+mItemSelectedPosition);

        switch (mBaseQuickAdapterView.getId()){
            case R.id.more:
                String vehiclegroup = mServiceRequestList.get(mItemSelectedPosition).getVehiclegroup();
                deleteRecordFromDatabaseByVehicleGroup(vehiclegroup);
                Log.e(TAG, "onClick: Size="+mServiceRequestList.size());
                ServiceRequest  serviceRequest = null;
                for(int i=0; i<mServiceRequestList.size(); i++) {
                    serviceRequest = mServiceRequestList.get(i);
                    if(serviceRequest.getVehiclegroup().equalsIgnoreCase(vehiclegroup)) {
                        mServiceRequestList.remove(i);
                        mBaseQuickAdapter.remove(i);
                    }
                }
                Log.e(TAG, "onClick: Size="+mServiceRequestList.size());
                break;
            case R.id.close_btn:
                mBaseQuickAdapter.remove(mItemSelectedPosition);
                mBaseQuickAdapter.notifyItemRemoved(mItemSelectedPosition);
                TextView itemCode = (TextView) mBaseQuickAdapterView.findViewById(R.id.item_code);
                String code ="";
                if (itemCode!=null && itemCode.getText()!=null)
                    code = itemCode.getText().toString();
                //deleteRecordFromDatabase(mServiceRequestList.get(mItemSelectedPosition).getCode(), vehicleNumber);
                break;
        }

        mBaseQuickAdapter.notifyDataSetChanged();
    }



    abstract class ExpandableVehicleSection extends StatelessSection {
        String title;
        List<ServiceRequest> list;
        boolean expanded = true;

        public ExpandableVehicleSection(String title, List<ServiceRequest> list) {
            super(R.layout.modify_choosen_service_header, R.layout.modify_choosen_service_item);

            this.title = title;
            this.list = list;
        }

        @Override
        public int getContentItemsTotal() {
            return expanded? this.list.size() : 0;
        }

        @Override
        public RecyclerView.ViewHolder getItemViewHolder(View view) {
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindItemViewHolder(final RecyclerView.ViewHolder holder, int position) {
            final ItemViewHolder itemHolder = (ItemViewHolder) holder;

            String name = list.get(position).getDesc();
            String category = list.get(position).getGroupname();

            itemHolder.tvItem.setText(name);
            itemHolder.tvSubItem.setText(category);
            //itemHolder.imgItem.setImageResource(R.drawable.ic_movie_black_48dp);

            itemHolder.imgItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String vehicleNumber = "";
                    int sectionPosition = mGridCollapseSectionAdapter.getSectionPosition(itemHolder.getAdapterPosition());
                    /*Toast.makeText(ModifyChoosenServiceRquestActivity.this, String.format("Clicked on position #%s of Section %s", sectionPosition, title),
                            Toast.LENGTH_SHORT).show();*/
                    vehicleNumber = title;
                    onItemClick(sectionPosition, vehicleNumber);
                    list.remove(sectionPosition);
                }
            });
        }

        protected abstract void onItemClick(int sectionPosition, String vehicleNumber);

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

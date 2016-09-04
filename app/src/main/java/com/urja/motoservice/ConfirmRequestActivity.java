package com.urja.motoservice;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.urja.motoservice.adapters.SectionAdapter;
import com.urja.motoservice.database.DbHelper;
import com.urja.motoservice.database.ServiceRequest;
import com.urja.motoservice.database.dao.DaoSession;
import com.urja.motoservice.database.dao.ServiceRequestDao;
import com.urja.motoservice.model.ServiceTypeSection;
import com.urja.motoservice.utils.DatabaseConstants;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.DeleteQuery;
import de.greenrobot.dao.query.WhereCondition;

public class ConfirmRequestActivity extends AppCompatActivity implements DialogInterface.OnClickListener{

    static final boolean GRID_LAYOUT = false;
    private static final String TAG = ConfirmRequestActivity.class.getSimpleName();
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




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                Intent intent = new Intent(ConfirmRequestActivity.this, CustomerCarDetailsActivity.class);
                startActivity(intent);
            }
        });



        mRecyclerView = (RecyclerView) findViewById(R.id.requestedServiceRecyclerView);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mSectionAdapter = new SectionAdapter(R.layout.item_section_content, R.layout.def_section_head, mContentItems);
        //sectionAdapter.setOnRecyclerViewItemClickListener(this);
        mSectionAdapter.setOnRecyclerViewItemChildClickListener(new BaseQuickAdapter.OnRecyclerViewItemChildClickListener() {
            @Override
            public void onItemChildClick(final BaseQuickAdapter adapter, final View view, final int position) {
                //Toast.makeText(ConfirmRequestActivity.this, "onItemChildClick"+position, Toast.LENGTH_LONG).show();
                mBaseQuickAdapter = adapter;
                mBaseQuickAdapterView = view;
                mItemSelectedPosition = position;
                AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmRequestActivity.this, R.style.AppTheme_AlertStyle);
                builder.setTitle("Confirm");
                builder.setMessage("Do you want to Delete?");

                switch (view.getId()){
                    case R.id.more:
                        if (adapter.getItemCount() <1) {
                            com.urja.motoservice.utils.AlertDialog.Alert(ConfirmRequestActivity.this, "", "Choose atleast one service!!");
                        }else{
                            builder.setPositiveButton("Yes",ConfirmRequestActivity.this);
                            builder.setNegativeButton("No", null);
                            builder.show();
                            /*deleteRecordFromDatabaseByVehicleGroup(mServiceRequestList.get(mItemSelectedPosition).getVehiclegroup());
                            adapter.notifyDataSetChanged();*/
                        }
                        break;
                    case R.id.close_btn:
                        if (adapter.getItemCount() >1){
                            builder.setPositiveButton("Yes",ConfirmRequestActivity.this);
                            builder.setNegativeButton("No", null);
                            builder.show();
                        }else{
                            com.urja.motoservice.utils.AlertDialog.Alert(ConfirmRequestActivity.this,"", "Choose atleast one service!!");
                        }
                        break;
                }

            }
        });

        mRecyclerView.setAdapter(mSectionAdapter);



        //read the saved data from the sqlite database
        readServiceRequestData(mSectionAdapter);


    }

    private void deleteRecordFromDatabaseByVehicleGroup(String vehiclegroup) {
        ServiceRequestDao serviceRequestDao = DbHelper.getInstance(ConfirmRequestActivity.this).getDaoSession().getServiceRequestDao();
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
        mServiceRequestDao = DbHelper.getInstance(ConfirmRequestActivity.this).getServiceRequestDao();
        mServiceRequestList = mServiceRequestDao.loadAll();
        mServiceRequestList = mServiceRequestDao.queryBuilder().orderAsc(ServiceRequestDao.Properties.Vehiclegroup).list();

        List<ServiceTypeSection> serviceTypeSections = null;
        ServiceTypeSection  serviceTypeSection=null;
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
        }
        sectionAdapter.notifyDataSetChanged();
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

    public void deleteRecordFromDatabase(String itemCode){
        ServiceRequestDao serviceRequestDao = DbHelper.getInstance(ConfirmRequestActivity.this).getDaoSession().getServiceRequestDao();
        DeleteQuery deleteQuery  = serviceRequestDao.queryBuilder().where(new WhereCondition.StringCondition(String.valueOf("code='"+itemCode+"'"))).buildDelete();
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
                deleteRecordFromDatabase(mServiceRequestList.get(mItemSelectedPosition).getCode());
                break;
        }

        mBaseQuickAdapter.notifyDataSetChanged();
    }
}

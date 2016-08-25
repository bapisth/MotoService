package com.urja.motoservice;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.urja.motoservice.database.dao.ServiceRequestDao;
import com.urja.motoservice.model.ServiceTypeSection;
import com.urja.motoservice.utils.DatabaseConstants;

import java.util.ArrayList;
import java.util.List;

public class ConfirmRequestActivity extends AppCompatActivity implements BaseQuickAdapter.OnRecyclerViewItemClickListener {

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


    private DatabaseReference mDatabaseRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference mTransactionRef = mDatabaseRootRef.child(DatabaseConstants.TABLE_TRANSACTION);

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
        SectionAdapter sectionAdapter = new SectionAdapter(R.layout.item_section_content, R.layout.def_section_head, mContentItems);
        sectionAdapter.setOnRecyclerViewItemClickListener(this);
        sectionAdapter.setOnRecyclerViewItemChildClickListener(new BaseQuickAdapter.OnRecyclerViewItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(ConfirmRequestActivity.this, "onItemChildClick", Toast.LENGTH_LONG).show();
            }
        });

        mRecyclerView.setAdapter(sectionAdapter);



        //read the saved data from the sqlite database
        readServiceRequestData(sectionAdapter);


    }

    private void addTransactionToServer(ConfirmRequestActivity confirmRequestActivity) {
        if (mCurrentUserId == null)
            mCurrentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            mTransactionRef.child(mCurrentUserId).push().setValue(mServiceRequestList).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(ConfirmRequestActivity.this, "Request Added to the server!!", Toast.LENGTH_SHORT).show();
                    mServiceRequestDao.deleteAll();
                    startActivity(new Intent(ConfirmRequestActivity.this, DashboardActivity.class));

                    finish();
                }
            });

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

        List<ServiceTypeSection> serviceTypeSections = null;
        ServiceTypeSection  serviceTypeSection=null;
        for (ServiceRequest serviceRequest :mServiceRequestList){
            Log.e(TAG, "onCreate: "+serviceRequest.getCode()+ ":" + serviceRequest.getDesc());
            serviceTypeSection = new ServiceTypeSection(true, getSectionHeaderDescriptio(serviceRequest.getGroupname()), true);
            mContentItems.add(new ServiceTypeSection(serviceRequest));
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


    @Override
    public void onItemClick(View view, int i) {

    }
}

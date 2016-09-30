package com.urja.motoservice.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.urja.motoservice.R;
import com.urja.motoservice.adapters.CarServiceRecyclerViewAdapter;
import com.urja.motoservice.database.CarServicePrice;
import com.urja.motoservice.database.DbHelper;
import com.urja.motoservice.database.dao.CarServicePriceDao;
import com.urja.motoservice.utils.DatabaseConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by florentchampigny on 24/04/15.
 */
public class CarCareDetailingFragment extends Fragment {

    private static final String TAG = CarCareDetailingFragment.class.getSimpleName();
    static final boolean GRID_LAYOUT = false;
    private static final int ITEM_COUNT = 100;
    private RecyclerView mRecyclerView;
    public RecyclerView.Adapter mAdapter;
    private List<CarServicePrice> mContentItems = new ArrayList<>();
    private DatabaseReference mDatabaseRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference mCarServiceWashTypesRef = mDatabaseRootRef.child(DatabaseConstants.TABLE_CAR_SERVICE + "/" + DatabaseConstants.CAR_CARE_DETAILING);
    private FirebaseAuth firebaseAuth;
    CarServicePriceDao carServicePriceDao = null;
    private static String CAR_TYPE = "";

    public static CarCareDetailingFragment newInstance(String carType) {
        CarCareDetailingFragment.CAR_TYPE = carType;
        return new CarCareDetailingFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        carServicePriceDao = DbHelper.getInstance(getActivity()).getCarServicePriceDao();
        mContentItems = carServicePriceDao.loadAll();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager;

        if (GRID_LAYOUT) {
            layoutManager = new GridLayoutManager(getActivity(), 2);
        } else {
            layoutManager = new LinearLayoutManager(getActivity());
        }
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        //Use this now
        mRecyclerView.addItemDecoration(new MaterialViewPagerHeaderDecorator());
        mAdapter = new CarServiceRecyclerViewAdapter(mContentItems, getActivity(), CAR_TYPE);

        //mAdapter = new RecyclerViewMaterialAdapter();
        mRecyclerView.setAdapter(mAdapter);


        //fetchServiceData(this);


    }

    private void fetchServiceData(CarCareDetailingFragment carCareDetailingFragment) {
        mContentItems = carServicePriceDao.loadAll();
        mAdapter.notifyDataSetChanged();
    }
}

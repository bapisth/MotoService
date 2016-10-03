package com.urja.motoservice.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.urja.motoservice.R;
import com.urja.motoservice.adapters.CarServiceRecyclerViewAdapter;
import com.urja.motoservice.model.TyreWheel;
import com.urja.motoservice.utils.DatabaseConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by florentchampigny on 24/04/15.
 */
public class TyreWheelFragment extends Fragment {

    static final boolean GRID_LAYOUT = false;
    private static final int ITEM_COUNT = 100;
    private static final String TAG = TyreWheelFragment.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private List<Object> mContentItems = new ArrayList<>();
    private DatabaseReference mDatabaseRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference mTyreWheelRef = mDatabaseRootRef.child(DatabaseConstants.TABLE_CAR_SERVICE + "/" + DatabaseConstants.TABLE_TYRE_WHEELS);

    public static TyreWheelFragment newInstance() {
        return new TyreWheelFragment();
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

        //mAdapter = new CarServiceRecyclerViewAdapter(mContentItems, getActivity(), "");
        mAdapter = new CarServiceRecyclerViewAdapter(null, getActivity(), "", "");

        //mAdapter = new RecyclerViewMaterialAdapter();
        mRecyclerView.setAdapter(mAdapter);

        //update the recyclerview after fetching data from the server
        fetchTyreWheelServiceData(this);
    }

    private void fetchTyreWheelServiceData(TyreWheelFragment tyreWheelFragment) {
        mTyreWheelRef.orderByKey().addValueEventListener(new ValueEventListener() {
            TyreWheel mTyreWheel = null;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "onDataChange: Key=" + snapshot.getKey());
                    Log.d(TAG, "onDataChange: value=" + snapshot.getValue());
                    mContentItems.add(new TyreWheel(snapshot.getKey(), snapshot.getValue().toString()));
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}

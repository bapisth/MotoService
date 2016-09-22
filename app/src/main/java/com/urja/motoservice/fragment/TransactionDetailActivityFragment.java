package com.urja.motoservice.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.urja.motoservice.R;
import com.urja.motoservice.adapters.MyOrderForServicesTransactionRecyclerViewAdapter;
import com.urja.motoservice.database.ServiceRequest;
import com.urja.motoservice.fragment.dummy.DummyContent;
import com.urja.motoservice.model.CarPickAddress;
import com.urja.motoservice.model.Transaction;
import com.urja.motoservice.model.Vehicle;
import com.urja.motoservice.utils.AppConstants;
import com.urja.motoservice.utils.CurrentLoggedInUser;
import com.urja.motoservice.utils.FirebaseRootReference;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class TransactionDetailActivityFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String TAG = TransactionDetailActivityFragment.class.getSimpleName();

    private int mColumnCount = 0;
    private OnListFragmentInteractionListener mListener;
    FirebaseRootReference firebaseRootReference = FirebaseRootReference.get_instance();
    private String currentLoggedInUser;
    private List<Transaction> mTransactionList = new ArrayList<>();
    private MyOrderForServicesTransactionRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private TextView emptyView;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TransactionDetailActivityFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static TransactionDetailActivityFragment newInstance(int columnCount) {
        TransactionDetailActivityFragment fragment = new TransactionDetailActivityFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentLoggedInUser = CurrentLoggedInUser.getCurrentFirebaseUser().getUid();

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }


        firebaseRootReference.getmTransactionDatabaseRef().child(currentLoggedInUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot snapshot : children) {
                    for (DataSnapshot dss:snapshot.getChildren()){
                        Transaction transaction = dss.getValue(Transaction.class);
                        transaction.setTransactionId(dss.getKey());
                        mTransactionList.add(transaction);
                    }

                }
                if (mTransactionList.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                }
                else {
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orderforservicestransaction_list, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        emptyView = (TextView) view.findViewById(R.id.empty_view);

        // Set the adapter
        if (recyclerView instanceof RecyclerView) {
            Context context = recyclerView.getContext();/*
            RecyclerView recyclerView = (RecyclerView) view;*/
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            Log.e(TAG, "onCreateView: mTransactionList.size() :"+mTransactionList.size() );
            adapter = new MyOrderForServicesTransactionRecyclerViewAdapter(mTransactionList, mListener);
            recyclerView.setAdapter(adapter);
        }


        return view;
    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(DummyContent.DummyItem item);
    }
}

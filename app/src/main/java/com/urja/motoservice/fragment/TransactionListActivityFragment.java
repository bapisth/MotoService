package com.urja.motoservice.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.urja.motoservice.model.Transaction;
import com.urja.motoservice.utils.CurrentLoggedInUser;
import com.urja.motoservice.utils.FirebaseRootReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class TransactionListActivityFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String TAG = TransactionListActivityFragment.class.getSimpleName();

    private int mColumnCount = 0;
    private OnListFragmentInteractionListener mListener;
    FirebaseRootReference firebaseRootReference = FirebaseRootReference.get_instance();
    private String currentLoggedInUser;
    private List<Transaction> mTransactionList = new ArrayList<>();
    private MyOrderForServicesTransactionRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private TextView emptyView;
    private static final String REQUEST_STATUS = "requestStatus";
    private static final String OPEN = "open";


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TransactionListActivityFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static TransactionListActivityFragment newInstance(int columnCount) {
        TransactionListActivityFragment fragment = new TransactionListActivityFragment();
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

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Downloading Transactions...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        firebaseRootReference.getmTransactionDatabaseRef().child(currentLoggedInUser).orderByChild(REQUEST_STATUS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot snapshot : children) {
                    int totalAmount = 0;
                    Transaction transaction = null;
                    int i= 0;
                    for (DataSnapshot dss:snapshot.getChildren()){
                        transaction = dss.getValue(Transaction.class);
                        transaction.setTransactionId(snapshot.getKey());
                        List<ServiceRequest> serviceRequestList = transaction.getServiceRequestList();
                        for (ServiceRequest serviceRequest :transaction.getServiceRequestList()){
                            totalAmount += Integer.valueOf(serviceRequest.getVehiclegroup());
                        }
                    }
                    transaction.setTotalAmount(String.valueOf(totalAmount));
                    mTransactionList.add(transaction);
                }
                if (mTransactionList.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                }
                else {
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                }
                /*Collections.sort(mTransactionList, new Comparator<Transaction>() {
                    @Override
                    public int compare(Transaction t1, Transaction t2) {
                        return t1.getRequestStatus().compareTo(t2.getRequestStatus());
                    }
                });*/
                Collections.sort(mTransactionList);
                progressDialog.dismiss();
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
            adapter = new MyOrderForServicesTransactionRecyclerViewAdapter(mTransactionList, mListener, getActivity());
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
        void onListFragmentInteraction(String transactionId);
    }
}

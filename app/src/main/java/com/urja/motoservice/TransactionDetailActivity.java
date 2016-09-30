package com.urja.motoservice;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.urja.motoservice.adapters.TransactionDetailRecyclerViewAdapter;
import com.urja.motoservice.model.Transaction;
import com.urja.motoservice.utils.AppConstants;
import com.urja.motoservice.utils.CurrentLoggedInUser;
import com.urja.motoservice.utils.FirebaseRootReference;

import java.util.ArrayList;
import java.util.List;

public class TransactionDetailActivity extends AppCompatActivity {

    private static final String TAG = TransactionDetailActivity.class.getSimpleName();

    private List<Transaction> mTransactionList = null;
    FirebaseRootReference firebaseRootReference = null;
    private String currentLoggedInUser;
    private String transactionId;
    private TransactionDetailRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private TextView emptyView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactionid_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseRootReference = FirebaseRootReference.get_instance();
        mTransactionList = new ArrayList<>();

        //Transaction Details
        currentLoggedInUser = CurrentLoggedInUser.getCurrentFirebaseUser().getUid();

        Intent intent = getIntent();
        transactionId = intent.getStringExtra(AppConstants.TRANSACTIOIN_ID);

        recyclerView = (RecyclerView) findViewById(R.id.transactionDetailList);
        emptyView = (TextView) findViewById(R.id.transaction_detail_empty_view);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Downloading Transactions...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        firebaseRootReference.getmTransactionDatabaseRef().child(currentLoggedInUser).child(transactionId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                Transaction transaction = null;
                for (DataSnapshot snapshot : children) {
                    transaction = snapshot.getValue(Transaction.class);
                    transaction.setTransactionId(snapshot.getKey());
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
                progressDialog.dismiss();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        // Set the adapter
        if (recyclerView instanceof RecyclerView) {
            Context context = recyclerView.getContext();
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            adapter = new TransactionDetailRecyclerViewAdapter(mTransactionList, this);
            recyclerView.setAdapter(adapter);
        }
    }

}

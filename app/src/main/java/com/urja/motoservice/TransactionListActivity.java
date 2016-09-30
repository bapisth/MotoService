package com.urja.motoservice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.urja.motoservice.fragment.TransactionListActivityFragment;
import com.urja.motoservice.utils.AppConstants;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class TransactionListActivity extends AppCompatActivity implements TransactionListActivityFragment.OnListFragmentInteractionListener {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setHomeButtonEnabled(true);
        supportActionBar.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void onListFragmentInteraction(String transactionId) {
        Intent intent = new Intent(TransactionListActivity.this, TransactionDetailActivity.class);
        intent.putExtra(AppConstants.TRANSACTIOIN_ID, transactionId);
        startActivity(intent);
    }
}

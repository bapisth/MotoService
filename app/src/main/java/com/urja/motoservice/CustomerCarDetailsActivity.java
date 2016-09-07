package com.urja.motoservice;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.urja.motoservice.fragment.Ask4CarNumberDialogFragment;
import com.urja.motoservice.model.TransactionComplete;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class CustomerCarDetailsActivity extends AppCompatActivity {
    public static final String TAG = CustomerCarDetailsActivity.class.getSimpleName();
    private String mCarNumber = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_car_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*Ask4CarNumberDialogFragment  ask4CarNumberDialogFragment = new Ask4CarNumberDialogFragment();
        ask4CarNumberDialogFragment.show(getSupportFragmentManager(), TAG);
        ask4CarNumberDialogFragment.setCancelable(false);*/
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(TransactionComplete transactionComplete) {
        Toast.makeText(CustomerCarDetailsActivity.this, "Transaction Complete Event CustomerCarDetailsActivity"+ transactionComplete.isTransactionComplete(), Toast.LENGTH_SHORT).show();
        if (transactionComplete.isTransactionComplete()) //If the transaction is completed, then stop the Activity
            finish();
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    /*@Override
    public void onSubmit(String carNumber) {
        this.mCarNumber =  carNumber;
    }*/
}
